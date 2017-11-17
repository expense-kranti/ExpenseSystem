package com.boilerplate.asyncWork;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IRedisScript;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentStatusPubishEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.service.interfaces.IAssessmentService;

/**
 * This class run in back ground to publish the user data
 * 
 * @author shiva
 *
 */
public class PublishUserDataObserver implements IAsyncWorkObserver {

	/**
	 * PublishUserDataObserver logger
	 */
	private static Logger logger = Logger.getInstance(PublishUserDataObserver.class);

	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader job
	 * 
	 * @param queueReaderJob
	 *            The queue reader jon
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the new instance of redis script
	 */
	IRedisScript redisScript;

	/**
	 * This is the new instance of assessment service
	 */
	IAssessmentService assessmentService;

	/**
	 * This method set the assessment service
	 * 
	 * @param assessmentService
	 *            the assessmentService to set
	 */
	public void setAssessmentService(IAssessmentService assessmentService) {
		this.assessmentService = assessmentService;
	}

	/**
	 * This is new instance of user data access
	 */
	IUser userDataAccess;

	/**
	 * This method is used to set the user data access
	 * 
	 * @param userDataAccess
	 *            the userDataAccess to set
	 */
	public void setUserDataAccess(IUser userDataAccess) {
		this.userDataAccess = userDataAccess;
	}

	/**
	 * This method set the redis Script
	 * 
	 * @param redisScript
	 *            the redisScript to set
	 */
	public void setRedisScript(IRedisScript redisScript) {
		this.redisScript = redisScript;
	}

	private BoilerplateList<String> subjects = null;

	/**
	 * This is the new instance of redis assessment
	 */
	@Autowired
	IRedisAssessment redisAssessment;

	/**
	 * This method set the redisAssessment
	 * 
	 * @param redisAssessment
	 *            the redisAssessment to set
	 */
	public void setRedisAssessment(IRedisAssessment redisAssessment) {
		this.redisAssessment = redisAssessment;
	}

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		Set<String> listOfUserKey = redisScript.getAllUserKeys();

		for (String userId : listOfUserKey) {
			try {
				ExternalFacingReturnedUser user = this.getUserDetails(userId.replace("USER:", ""));
				
				if (user != null) {
					this.publishUserToCRM(user);
					
					this.publishUserAssessmentDetails(userId.replace("USER:", ""));
				}
			} catch (Exception ex) {
				logger.logException("PublishUserDataObserver", "publishUserToCRM", "Publishing user assessment script date", "", ex);
				
			}
		}

	}

	private void publishUserToCRM(ExternalFacingReturnedUser user) {
		PublishEntity publishEntity = this.createPublishEntity
						("PublishUserDataObserver.publishUserToCRM",
				configurationManager.get("AKS_USER_Publish_Method"),
				configurationManager.get("AKS_USER_Publish_Subject"), user,
				configurationManager.get("AKS_USER_Publish_URL"),
				configurationManager.get("AKS_USER_Publish_Template"),
				configurationManager.get("AKS_USER_Dynamic_Publish_Url"));
		if (subjects == null) {
			subjects = new BoilerplateList<>();
			subjects.add(configurationManager.get("AKS_PUBLISH_SUBJECT"));
		}
		try {
			logger.logInfo("PublishUserDataObserver", "publishUserToCRM", "Publishing user",
					"publish user status" + user.getUserId());
			queueReaderJob.requestBackroundWorkItem(publishEntity,
					subjects, "PublishUserDataObserver",
					"publishToCRM", configurationManager.get("AKS_PUBLISH_QUEUE"));
		} catch (Exception exception) {
			logger.logError("PublishUserDataObserver", "publishUserToCRM", "queueReaderJob catch block",
					"Exception :" + exception);
		}
		
	}

	/**
	 * This method is used to get user assessment details
	 * 
	 * @param userId
	 *            this is the userId by which we can get the user assessment
	 *            status
	 */
	private void publishUserAssessmentDetails(String userId) {
		AttemptAssessmentListEntity attemptAssessment = redisAssessment.getAssessmentAttempt(userId);
		if(attemptAssessment!=null){
			BoilerplateList<AssessmentEntity> assessmentStatus = assessmentService.getUserAssessmentStatus(userId);
			// Create a new instance of assessment status publish entity
			AssessmentStatusPubishEntity assessmentPublishData = new AssessmentStatusPubishEntity();
			// Get user score details
			ScoreEntity userScoreDetails = redisAssessment.getTotalScore(userId);
			// Set the user id
			assessmentPublishData.setUserId(userId);
			// Set the total score of user
			assessmentPublishData.setTotalScore(userScoreDetails.getObtainedScore());
			// Set user Rank
			assessmentPublishData.setRank(userScoreDetails.getRank());
			// Set the assessment status
			assessmentPublishData.setAssessments(assessmentService.getUserAssessmentStatus(userId));
			// Publish user assessment status
			this.publishToCRM(assessmentPublishData);
		}
		
	}

	/**
	 * This method is used to get user details
	 * 
	 * @param userId
	 *            this is the userId by which we can get the user information
	 * @return the user details
	 * @throws NotFoundException
	 *             throw this exception if user not found in our data store
	 */
	private ExternalFacingReturnedUser getUserDetails(String userId) throws NotFoundException {
		ExternalFacingReturnedUser userDetails = userDataAccess.getUser(userId, null);
		return userDetails;
	}

	/**
	 * This method is used to publish the user assessment status details to CRM
	 * 
	 * @param assessmentStatusPubishEntity
	 *            This parameter contains the user assessment details like user
	 *            total score ,user rank and list of all assessment,each element
	 *            of list contain assessment name and its status,
	 */
	private void publishToCRM(AssessmentStatusPubishEntity assessmentStatusPubishEntity) {
		Boolean isPublishReport = Boolean.valueOf(configurationManager.get("Is_Publish_Report"));
		if (isPublishReport) {
			PublishEntity publishEntity = this.createPublishEntity("PublishUserDataObserver.publishToCRM",
					configurationManager.get("AKS_Assessment_Publish_Method"),
					configurationManager.get("AKS_Assessment_Publish_Subject"), assessmentStatusPubishEntity,
					configurationManager.get("AKS_Assessment_Publish_URL"),
					configurationManager.get("AKS_Assessment_Publish_Template"),
					configurationManager.get("AKS_Assessment_Dynamic_Publish_Url"));
			if (subjects == null) {
				subjects = new BoilerplateList<>();
				subjects.add(configurationManager.get("AKS_PUBLISH_SUBJECT"));
			}
			try {
				logger.logInfo("PublishUserDataObserver", "publishToCRM", "Publishing report",
						"publish assessment status" + assessmentStatusPubishEntity.getUserId());
				queueReaderJob.requestBackroundWorkItem(publishEntity, subjects, "CalculateTotalScoreObserver",
						"publishToCRM", configurationManager.get("AKS_PUBLISH_QUEUE"));
			} catch (Exception exception) {
				logger.logError("PublishUserDataObserver", "publishToCRM", "queueReaderJob catch block",
						"Exception :" + exception);
			}
		}
	}

	/**
	 * This method creates the publish entity.
	 * 
	 * @param method
	 *            the publish method
	 * @param publishMethod
	 *            the publish method
	 * @param publishSubject
	 *            the publish subject
	 * @param returnValue
	 *            the object
	 * @param url
	 *            the publish url
	 * @return the publish entity
	 */
	private PublishEntity createPublishEntity(String method, String publishMethod, String publishSubject,
			Object returnValue, String url, String publishTemplate, String isDynamicPublishURl) {
		PublishEntity publishEntity = new PublishEntity();
		publishEntity.setInput(new Object[0]);
		publishEntity.setMethod(method);
		publishEntity.setPublishMethod(publishMethod);
		publishEntity.setPublishSubject(publishSubject);
		publishEntity.setReturnValue(returnValue);
		publishEntity.setUrl(url);
		publishEntity.setDynamicPublishURl(Boolean.parseBoolean(isDynamicPublishURl));
		publishEntity.setPublishTemplate(publishTemplate);
		return publishEntity;
	}
}
