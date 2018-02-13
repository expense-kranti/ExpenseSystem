package com.boilerplate.asyncWork;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentStatusPubishEntity;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.service.interfaces.IAssessmentService;

/**
 * This class calculate user total score and update the details into data store
 * 
 * @author shiva
 *
 */
public class CalculateTotalScoreObserver implements IAsyncWorkObserver {

	/**
	 * CalculateTotalScoreObserver logger
	 */
	private static Logger logger = Logger.getInstance(CalculateTotalScoreObserver.class);

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
	 * The autowired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;

	/**
	 * This is the setter for user data access
	 * 
	 * @param iUser
	 */
	public void setUserDataAccess(IUser iUser) {
		this.userDataAccess = iUser;
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
	 * This is the instance of the assessment service
	 */
	@Autowired
	IAssessmentService assessmentService;

	/**
	 * @param assessmentService
	 *            the assessmentService to set
	 */
	public void setAssessmentService(IAssessmentService assessmentService) {
		this.assessmentService = assessmentService;
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

	private BoilerplateList<String> subjects = null;

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		AssessmentEntity assessmentEntity = (AssessmentEntity) asyncWorkItem.getPayload();
		this.calculateTotalScore(assessmentEntity);
		this.calculateMonthlyScore(assessmentEntity);
		this.publishAssessmentStatus(assessmentEntity);
	}

	/**
	 * This method is used to publish the assessment status
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data ,assessment data
	 *            like assessment id, assessment section,assessment
	 *            questions,assessment score,obtained score etc.
	 * @throws NotFoundException
	 *             throws when user is not found
	 */
	private void publishAssessmentStatus(AssessmentEntity assessmentEntity)
			throws NotFoundException, ConflictException {
		// Create a new instance of assessment status publish entity
		AssessmentStatusPubishEntity assessmentPublishData = new AssessmentStatusPubishEntity();
		// Set the user id
		assessmentPublishData.setUserId(assessmentEntity.getUserId());
		ScoreEntity scoreEntity = redisAssessment.getTotalScore(assessmentEntity.getUserId());
		// // Set the total score of user
		// assessmentPublishData
		// .setTotalScore((redisAssessment.getTotalScore(assessmentEntity.getUserId())).getObtainedScore());
		// // Set the monthly score of user
		// assessmentPublishData
		// .setMonthlyScore((redisAssessment.getMonthlyScore(assessmentEntity.getUserId())).getObtainedScore());
		// // Set user Rank
		// assessmentPublishData.setRank(calculateRank(Float.parseFloat(assessmentPublishData.getTotalScore())));
		// // Set user monthly rank
		// assessmentPublishData.setMonthlyRank(calculateRank(Float.parseFloat(assessmentPublishData.getMonthlyScore())));
		// // Set the assessment status
		// assessmentPublishData.setAssessments(assessmentService.getUserAssessmentStatus(assessmentEntity.getUserId()));

		// Set the total score of user
		assessmentPublishData.setTotalScore(scoreEntity.getObtainedScore());
		// Set the monthly score of user
		assessmentPublishData
				.setMonthlyScore((redisAssessment.getMonthlyScore(assessmentEntity.getUserId())).getObtainedScore());
		// Set user Rank
		assessmentPublishData.setRank(calculateRank(Float.parseFloat(assessmentPublishData.getTotalScore())));
		// Set user monthly rank
		assessmentPublishData.setMonthlyRank(calculateRank(Float.parseFloat(assessmentPublishData.getMonthlyScore())));
		// Set the assessment status
		assessmentPublishData.setAssessments(assessmentService.getUserAssessmentStatus(assessmentEntity.getUserId()));

		// // save total score in user
		// this.saveUserTotalScore(scoreEntity);
		this.publishToCRM(assessmentPublishData);
	}

	/**
	 * This method save the user total score in user object
	 * 
	 * @param assessmentPublishData
	 *            This is the assessment publish data.
	 * @throws NotFoundException
	 *             throws when user is not found
	 */
	private void saveUserTotalScore(ScoreEntity scoreEntity) throws NotFoundException, ConflictException {
		// get user which is to be update total score is to save
		ExternalFacingReturnedUser user = userDataAccess.getUser(scoreEntity.getUserId(), null);

		user.setTotalScore(String
				.valueOf(Float.valueOf(scoreEntity.getObtainedScore()) + Float.valueOf(scoreEntity.getReferScore())));
		// if user total score in string is empty or null then set it to "0"
		// done for preventing NumberFormatException
		if (user.getTotalScore() == null || user.getTotalScore().isEmpty())
			user.setTotalScore("0");
		user.setTotalScoreInDouble(Double.parseDouble(user.getTotalScore()));
		user.setRank(scoreEntity.getRank());
		// save user total score
		userDataAccess.update(user);
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			// add userId in to Redis Set for updating user total score
			userDataAccess.addInRedisSet(user);
		}
		// publish user total score to crm
		this.publishUserToCRM(user);
	}

	/**
	 * This method is used to calculate the user total score
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data ,assessment data
	 *            like assessment id, assessment section,assessment
	 *            questions,assessment score,obtained score etc.
	 * @throws ConflictException
	 * @throws NotFoundException
	 */
	public void calculateTotalScore(AssessmentEntity assessmentEntity) throws NotFoundException, ConflictException {
		// Get total score
		ScoreEntity scoreEntity = redisAssessment.getTotalScore(assessmentEntity.getUserId());
		// If score is not null
		if (scoreEntity != null) {
			this.updateTotalScore(scoreEntity, assessmentEntity);
		} else {
			this.createNewTotalScore(assessmentEntity);
		}

	}

	/**
	 * This method is used to update the user total score
	 * 
	 * @param scoreEntity
	 *            this parameter contains the user monthly score,user obtained
	 *            score and max score
	 * @param assessmentEntity
	 *            this parameter contains the assessment data ,assessment data
	 *            like assessment id, assessment section,assessment
	 *            questions,assessment score,obtained score etc.
	 * @throws ConflictException
	 * @throws NotFoundException
	 */
	private void updateTotalScore(ScoreEntity scoreEntity, AssessmentEntity assessmentEntity)
			throws NotFoundException, ConflictException {
		// Calculate max score
		Float maxScore = Float.valueOf(scoreEntity.getMaxScore()) + Float.valueOf(assessmentEntity.getMaxScore());
		// Calculate total score
		Float obtainedScore = Float.valueOf(scoreEntity.getObtainedScore())
				+ Float.valueOf(assessmentEntity.getObtainedScore());
		// Set max score
		scoreEntity.setMaxScore(String.valueOf(maxScore));
		// Set the obtained score
		scoreEntity.setObtainedScore(String.valueOf(obtainedScore));
		// Set the obtained score in double for SUM from MySQL
		scoreEntity.setObtainedScoreInDouble(obtainedScore);

		// get rank according the sum of refer and obtained score
		if (scoreEntity.getReferScore() == null || scoreEntity.getReferScore().isEmpty()) {
			scoreEntity.setReferScore("0");
		}
		// set rank
		scoreEntity.setRank(calculateRank(obtainedScore + Float.valueOf(scoreEntity.getReferScore())));
		//////////////////////
		// Save total score
		redisAssessment.saveTotalScore(scoreEntity);
		// save total score in user in redis and in MySQL
		this.saveUserTotalScore(scoreEntity);

	}

	/**
	 * This method is used to create a new user total score detail
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data ,assessment data
	 *            like assessment id, assessment section,assessment
	 *            questions,assessment score,obtained score etc.
	 * @throws ConflictException
	 * @throws NotFoundException
	 */
	private void createNewTotalScore(AssessmentEntity assessmentEntity) throws NotFoundException, ConflictException {
		// New instance of score entity
		ScoreEntity scoreEntity = new ScoreEntity();
		// Set max score
		scoreEntity.setMaxScore(assessmentEntity.getMaxScore());
		// Set the obtained score
		scoreEntity.setObtainedScore(assessmentEntity.getObtainedScore());
		if (assessmentEntity.getObtainedScore() == null || assessmentEntity.getObtainedScore().isEmpty()) {
			scoreEntity.setObtainedScore("0");
		}
		// Set the obtained score in double for SUM in MySQL
		scoreEntity.setObtainedScoreInDouble(Double.parseDouble(scoreEntity.getObtainedScore()));
		// Set user id
		scoreEntity.setUserId(assessmentEntity.getUserId());
		// Set refer score 0
		scoreEntity.setReferScore(String.valueOf(0f));
		// set refer score in double for saving in MySQL
		scoreEntity.setReferScoreInDouble(0);
		// set rank
		scoreEntity.setRank(calculateRank(Float.parseFloat(assessmentEntity.getObtainedScore())));
		// Save total score
		redisAssessment.saveTotalScore(scoreEntity);
		// save total score in user in redis and in MySQL
		this.saveUserTotalScore(scoreEntity);
	}

	/**
	 * This method is used to calculate the user monthly score
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data ,assessment data
	 *            like assessment id, assessment section,assessment
	 *            questions,assessment score,obtained score etc.
	 */
	public void calculateMonthlyScore(AssessmentEntity assessmentEntity) {
		// Get total score
		ScoreEntity scoreEntity = redisAssessment.getMonthlyScore(assessmentEntity.getUserId());
		// If score is not null
		if (scoreEntity != null) {
			this.updateMonthlyScore(scoreEntity, assessmentEntity);
		} else {
			this.createNewMonthlyScore(assessmentEntity);
		}

	}

	/**
	 * This method is used to create a new user monthly score detail
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data ,assessment data
	 *            like assessment id, assessment section,assessment
	 *            questions,assessment score,obtained score etc.
	 */
	private void createNewMonthlyScore(AssessmentEntity assessmentEntity) {
		// New instance of score entity
		ScoreEntity scoreEntity = new ScoreEntity();
		// Set max score
		scoreEntity.setMaxScore(assessmentEntity.getMaxScore());
		// Set the obtained score
		scoreEntity.setObtainedScore(assessmentEntity.getObtainedScore());
		// check for preventing Numberformat exception
		if (assessmentEntity.getObtainedScore() == null || assessmentEntity.getObtainedScore().isEmpty()) {
			scoreEntity.setObtainedScore("0");
		}
		// Set the obtained score in double for SUM(like operations) in MySQL
		scoreEntity.setObtainedScoreInDouble(Double.parseDouble(scoreEntity.getObtainedScore()));

		// Set refer score 0
		scoreEntity.setReferScore(String.valueOf(0f));
		// Set refer score in double to save in MySQL for doing mathematical
		// operations
		scoreEntity.setReferScoreInDouble(0);
		// Set user id
		scoreEntity.setUserId(assessmentEntity.getUserId());
		// set rank
		scoreEntity.setRank(calculateRank(Float.parseFloat(assessmentEntity.getObtainedScore())));
		// Save monthly score
		redisAssessment.saveMonthlyScore(scoreEntity);

		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			// add userId in Redis set for saving monthly score in MYSQl
			// here we are assuming the now time will be same taken in Redis
			redisAssessment.addIdInRedisSetForAssessmentMonthlyScore(scoreEntity.getUserId() + ","
					+ LocalDateTime.now().getYear() + "," + LocalDateTime.now().getMonth());
		}
	}

	/**
	 * This method is used to update the user monthly score
	 * 
	 * @param scoreEntity
	 *            this parameter contains the user monthly score,user obtained
	 *            score and max score
	 * @param assessmentEntity
	 *            this parameter contains the assessment data ,assessment data
	 *            like assessment id, assessment section,assessment
	 *            questions,assessment score,obtained score etc.
	 */
	private void updateMonthlyScore(ScoreEntity scoreEntity, AssessmentEntity assessmentEntity) {
		// Calculate max score
		Float maxScore = Float.valueOf(scoreEntity.getMaxScore()) + Float.valueOf(assessmentEntity.getMaxScore());
		// Calculate total score
		Float obtainedScore = Float.valueOf(scoreEntity.getObtainedScore())
				+ Float.valueOf(assessmentEntity.getObtainedScore());
		// Set max score
		scoreEntity.setMaxScore(String.valueOf(maxScore));
		// If refer score is not null then get rank according the sum of refer
		// and obtained score
		if (scoreEntity.getReferScore() != null && !(scoreEntity.getReferScore().isEmpty())) {
			// set rank
			scoreEntity.setRank(calculateRank(obtainedScore + Float.valueOf(scoreEntity.getReferScore())));
		} else {
			// set rank
			scoreEntity.setRank(calculateRank(obtainedScore));
		}
		// Set the obtained score
		scoreEntity.setObtainedScore(String.valueOf(obtainedScore));
		// set obtained score in double for saving in MySQL for doing
		// mathematical operations over it
		scoreEntity.setObtainedScoreInDouble(obtainedScore);

		// Save monthly score
		redisAssessment.saveMonthlyScore(scoreEntity);

		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			// add userId in Redis set for saving monthly score in MYSQl
			// here we are assuming the now time will be same taken in Redis
			redisAssessment.addIdInRedisSetForAssessmentMonthlyScore(scoreEntity.getUserId() + ","
					+ LocalDateTime.now().getYear() + "," + LocalDateTime.now().getMonth());
		}
	}

	/**
	 * This method set the rank of the user.
	 * 
	 * @param score
	 *            This is the current user score.
	 * @return The rank of the user.
	 */
	private String calculateRank(Float score) {
		String rank = "";
		if (score > 0 && score < 500) {
			rank = configurationManager.get("Rank1");
		} else if (score >= 500 && score < 1000) {
			rank = configurationManager.get("Rank2");
		} else if (score >= 1000 && score < 5000) {
			rank = configurationManager.get("Rank3");
		} else if (score >= 5000 && score < 10000) {
			rank = configurationManager.get("Rank4");
		} else if (score >= 10000 && score < 15000) {
			rank = configurationManager.get("Rank5");
		} else if (score >= 15000 && score < 20000) {
			rank = configurationManager.get("Rank6");
		} else if (score >= 20000 && score < 25000) {
			rank = configurationManager.get("Rank7");
		} else if (score >= 25000 && score < 30000) {
			rank = configurationManager.get("Rank8");
		} else if (score >= 30000) {
			rank = configurationManager.get("Rank9");
		}
		return rank;
	}

	private void publishToCRM(AssessmentStatusPubishEntity assessmentStatusPubishEntity) {
		Boolean isPublishReport = Boolean.valueOf(configurationManager.get("Is_Publish_Report"));
		if (isPublishReport) {
			PublishEntity publishEntity = this.createPublishEntity("CalculateTotalScoreObserver.publishToCRM",
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
				logger.logInfo("CalculateTotalScoreObserver", "publishToCRM", "Publishing report",
						"publish assessment status" + assessmentStatusPubishEntity.getUserId());
				queueReaderJob.requestBackroundWorkItem(publishEntity, subjects, "CalculateTotalScoreObserver",
						"publishToCRM", configurationManager.get("AKS_PUBLISH_QUEUE"));
			} catch (Exception exception) {
				logger.logError("CalculateTotalScoreObserver", "publishToCRM", "queueReaderJob catch block",
						"Exception :" + exception);
			}
		}
	}

	private void publishUserToCRM(ExternalFacingReturnedUser user) {
		Boolean isPublishReport = Boolean.valueOf(configurationManager.get("Is_Publish_Report"));

		if (isPublishReport) {
			PublishEntity publishEntity = this.createPublishEntity("CalculateTotalScoreObserver.publishToCRM",
					configurationManager.get(""), configurationManager.get("UPDATE_AKS_USER_SUBJECT"), user,
					configurationManager.get(""), configurationManager.get(""), configurationManager.get(""));
			if (subjects == null) {
				subjects = new BoilerplateList<>();
				subjects.add(configurationManager.get("AKS_PUBLISH_SUBJECT"));
			}
			try {

				queueReaderJob.requestBackroundWorkItem(publishEntity, subjects, "CalculateTotalScoreObserver",
						"publishToCRM", configurationManager.get("AKS_PUBLISH_QUEUE"));
			} catch (Exception exception) {
				logger.logError("CreateReferUniqueIdObserver", "publishToCRM", "queueReaderJob catch block",
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