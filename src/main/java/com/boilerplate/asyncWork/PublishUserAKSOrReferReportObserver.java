package com.boilerplate.asyncWork;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IRedisScript;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentStatusPubishEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.service.interfaces.IAssessmentService;

public class PublishUserAKSOrReferReportObserver implements IAsyncWorkObserver {

	private static Logger logger = Logger.getInstance(PublishUserAKSOrReferReportObserver.class);

	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader job
	 * 
	 * @param queueReaderJob
	 *            The queue reader job
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

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
	 * This method set the redis Script
	 * 
	 * @param redisScript
	 *            the redisScript to set
	 */
	public void setRedisScript(IRedisScript redisScript) {
		this.redisScript = redisScript;
	}

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

	private BoilerplateList<String> subjects = null;

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {		
		Set<String> listOfUserAttemptkeys = redisScript.getAllUserAttemptKeys();
		

		// Run for loop to process each user
		for (String userAttemptKey : listOfUserAttemptkeys) {
			try {
				String splitUserId [] = userAttemptKey.split(":");
				String userId = splitUserId[1]+":"+splitUserId[2];
				this.publishUserAssessmentDetails(userId);
			
			} catch (Exception ex) {
				logger.logException("PublishUserAKSOrReferReportObserver", "publishUserAssessmentDetails",
						"publishing user assessment data", "", ex);
			}
		}

	}

	/**
	 * This method is used to get user assessment details
	 * 
	 * @param userId
	 *            this is the userId by which we can get the user assessment
	 * 
	 */
	private void publishUserAssessmentDetails(String userId) {
		AttemptAssessmentListEntity attemptAssessment = redisAssessment.getAssessmentAttempt(userId);
		if (attemptAssessment != null) {
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

		}
	}

}
