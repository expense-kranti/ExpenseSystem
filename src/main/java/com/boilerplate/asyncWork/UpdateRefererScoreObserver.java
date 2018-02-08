package com.boilerplate.asyncWork;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ReferredContactDetailEntity;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.java.entities.UserReferalMediumType;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This class update referrer user score
 * 
 * @author shiva
 *
 */
public class UpdateRefererScoreObserver implements IAsyncWorkObserver {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(SendEmailToReferredUserObserver.class);

	/**
	 * This is the new instance of queue reader job
	 */
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

	/**
	 * This is a new instance of Referral
	 */
	IReferral referral;

	/**
	 * This method is used to set the referral
	 * 
	 * @param referral
	 *            the referral to set
	 */
	public void setReferral(IReferral referral) {
		this.referral = referral;
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
	 * The autowired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;

	/**
	 * Sets the user data access
	 * 
	 * @param userDataAccess
	 *            the userDataAccess to set
	 */
	public void setUserDataAccess(IUser userDataAccess) {
		this.userDataAccess = userDataAccess;
	}

	/**
	 * This is the subject list for publish
	 */
	private BoilerplateList<String> subjects = null;

	/**
	 * This is the subject list for publish
	 */
	private BoilerplateList<String> subjectsForPublishReferralReport = null;

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		// Get externalFacingUser from asyncWorkItem
		ExternalFacingUser externalFacingUser = (ExternalFacingUser) asyncWorkItem.getPayload();
		String referBYUser = referral.getReferUser(externalFacingUser.getCampaignUUID());
		String signUpUserScore = "";
		String referUserScore = "";
		if (referBYUser != null) {
			// Create the entity
			ReferalEntity referalEntity = new ReferalEntity(externalFacingUser.getCampaignType(), referBYUser,
					externalFacingUser.getCampaignUUID());
			// check counter for maxm user
			String signUpCount = referral.getSignUpCount(referalEntity);
			if (signUpCount == null || Integer.parseInt(signUpCount) < Integer
					.parseInt(configurationManager.get("MAX_ALLOW_USER_SCORE"))) {
				// Update referring user score
				this.updateReferringUserScore(referalEntity);
				// Is sign up user get the refer score
				if (Boolean.valueOf(configurationManager.get("IS_SIGN_UP_USER_GET_REFER_SCORE"))) {
					// Update sign up user score
					this.updateSignUpUserScore(referalEntity, externalFacingUser);
					signUpUserScore = this.getSignUpUserReferScore(referalEntity.getReferralMediumType());
				}
				// if null counter value then set otherwise increase
				this.checkAndSetSignUpCounter(signUpCount, referalEntity);
				referUserScore = this.getReferScore(referalEntity.getReferralMediumType());
			}
			// Create referred contact details
			ReferredContactDetailEntity referredContactDetail = new ReferredContactDetailEntity(
					this.getContact(referalEntity.getReferralMediumType(), externalFacingUser), signUpUserScore,
					referUserScore, LocalDate.now().toString(), externalFacingUser.getUserId());
			// New referred contact list
			BoilerplateList<ReferredContactDetailEntity> referredContacts = new BoilerplateList<>();
			// Add details to list
			referredContacts.add(referredContactDetail);
			// Set this referred data
			referalEntity.setReferredContact(referredContacts);
			// Publish report
			this.publishReferralData(referalEntity);
			// Update contact detail
			this.updateReferredData(referalEntity, referredContactDetail);

			// if true then add key in redisset
			if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
				// add key in redis database to migrate data to MySQL
				referral.addInRedisSet(referalEntity, referredContactDetail);
			}
		}

	}

	private String getContact(UserReferalMediumType referralMediumType, ExternalFacingUser externalFacingUser) {
		String contact = "";
		// According to type trigger back ground job
		switch (referralMediumType) {
		case Email:
			if (!BaseEntity.isNullOrEmpty(externalFacingUser.getEmail())) {
				// Set contact with email
				contact = externalFacingUser.getEmail();
			} else {
				contact = externalFacingUser.getPhoneNumber();
			}
			break;
		case Phone_Number:
			// Set contact with phone number
			contact = externalFacingUser.getPhoneNumber();
			break;
		case Facebook:
			// Set contact with phone number
			contact = externalFacingUser.getPhoneNumber();
			break;
		case LinkedIn:
			// Set contact with phone number
			contact = externalFacingUser.getPhoneNumber();
			break;
		default:
			// Set contact with phone number
			contact = externalFacingUser.getPhoneNumber();
		}
		return contact;
	}

	/**
	 * This method is used to update referred contact data to database
	 * 
	 * @param referalEntity
	 *            this parameter contain the information regarding the referring
	 *            user id ,referring medium type and refer id
	 * @param referredContactDetail
	 *            this parameter contain information regarding referred contact
	 * @throws JsonParseException
	 *             throw this exception in case of we get error while trying to
	 *             get referred contact details from data store
	 * @throws JsonMappingException
	 *             throw this exception in case we fail to map json with map
	 *             type
	 * @throws IOException
	 *             throw this exception in case any we get any error while
	 *             trying to get data
	 */
	private void updateReferredData(ReferalEntity referalEntity, ReferredContactDetailEntity referredContactDetail)
			throws JsonParseException, JsonMappingException, IOException {
		if (referral.getUserReferredContacts(referalEntity, referredContactDetail.getContact()).size() > 0) {
			// Convert map to entity
			ReferredContactDetailEntity savedReferredContactDetailEntity = Base.fromMap(
					referral.getUserReferredContacts(referalEntity, referredContactDetail.getContact()),
					ReferredContactDetailEntity.class);
			// Set creation date
			referredContactDetail.setStringCreationDate(savedReferredContactDetailEntity.getStringCreationDate());
			// Set update date
			referredContactDetail.setStringUpdateDate(LocalDate.now().toString());
		} else {
			// Set creation date
			referredContactDetail.setStringCreationDate(LocalDate.now().toString());
			// Set update date
			referredContactDetail.setStringUpdateDate(LocalDate.now().toString());
		}
		// Save to Redis
		referral.saveUserReferContacts(referalEntity, referredContactDetail);
	}

	/**
	 * This method is used to check is sign up count exist if not then create
	 * and increase is by one
	 * 
	 * @param signUpCount
	 *            this parameter define the value of signUp count if it is null
	 *            means we dont have sign up counter for this referral type for
	 *            referring user
	 * @param referalEntity
	 *            this parameter contain the information regarding the referring
	 *            user id ,referring medium type and refer id
	 */
	private void checkAndSetSignUpCounter(String signUpCount, ReferalEntity referalEntity) {
		if (signUpCount == null) {
			referral.createSignUpCounter(referalEntity, "1");
		} else {
			referral.increaseReferSignUpCounter(referalEntity);
		}

	}

	/**
	 * This method is used to update Referring user score
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the refer by
	 *            user like refer medium type and user id of refer by user
	 * @throws NotFoundException
	 *             thrown when user not found
	 * @throws ConflictException
	 */
	private void updateReferringUserScore(ReferalEntity referalEntity) throws NotFoundException, ConflictException {
		// Update user Total score
		this.updateUserTotalScore(referalEntity, this.getReferScore(referalEntity.getReferralMediumType()));
		// Update user monthly score
		this.updateUserMonthlyScore(referalEntity, this.getReferScore(referalEntity.getReferralMediumType()));
	}

	/**
	 * This method is used to update sign up user score
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the refer by
	 *            user like refer medium type and user id of refer by user
	 * @param externalFacingUser
	 *            this parameter contain the information regarding the coming
	 *            user
	 * @throws NotFoundException
	 *             thrown when user not found
	 * @throws ConflictException
	 */
	private void updateSignUpUserScore(ReferalEntity referalEntity, ExternalFacingUser externalFacingUser)
			throws NotFoundException, ConflictException {
		// Clone referral entity
		ReferalEntity signUpUserReferralDetails = (ReferalEntity) referalEntity.clone();
		// Set sign up user id
		signUpUserReferralDetails.setUserId(externalFacingUser.getUserId());
		// Update user Total score
		this.updateUserTotalScore(signUpUserReferralDetails,
				this.getSignUpUserReferScore(referalEntity.getReferralMediumType()));
		// Update user monthly score
		this.updateUserMonthlyScore(signUpUserReferralDetails,
				this.getSignUpUserReferScore(referalEntity.getReferralMediumType()));
	}

	/**
	 * This method sets the external facing users total score and publish user
	 * 
	 * @param user
	 *            the user with updated total score
	 * @param totalScore
	 *            the total score
	 * @throws ConflictException
	 */
	private void updateUserScoreAndPublish(ExternalFacingReturnedUser user, String totalScore) {
		user.setTotalScore(totalScore);
		if (user.getTotalScore() != null && !(user.getTotalScore().isEmpty()))
			user.setTotalScoreInDouble(Double.parseDouble(user.getTotalScore()));
		userDataAccess.update(user);

		// add userid to the Redis set for updating total score of the
		// user(which
		// contains user assessment score and refer score)
		// if true then add in redis key set
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			userDataAccess.addInRedisSet(user);
		}
		publishUserToCRM(user);
	}

	/**
	 * This method is used to update user total score
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the refer by
	 *            user like refer medium type and user id of refer by user
	 * @throws NotFoundException
	 *             thrown if user not found
	 * @throws ConflictException
	 */
	private void updateUserTotalScore(ReferalEntity referalEntity, String referScore) throws NotFoundException {
		// Get total score
		ScoreEntity scoreEntity = redisAssessment.getTotalScore(referalEntity.getUserId());
		ScoreEntity newScore = null;
		// If score is not null
		if (scoreEntity != null) {
			newScore = this.updateScore(scoreEntity, referalEntity, referScore);
		} else {
			newScore = this.createNewScore(referalEntity, referScore);
		}
		if (newScore != null) {
			// Save total score
			redisAssessment.saveTotalScore(newScore);
			// create external facing returned user from the user id of referal
			// entity
			ExternalFacingReturnedUser user = userDataAccess.getUser(referalEntity.getUserId(), null);
			// set userTotal refer score in redis
			if (newScore.getReferScore() != null) {
				user.setTotalReferScore(Double.parseDouble(newScore.getReferScore()));
			}

			user.setRank(newScore.getRank());
			updateUserScoreAndPublish(user, String
					.valueOf(Float.valueOf(newScore.getReferScore()) + Float.valueOf(newScore.getObtainedScore())));

			// if true then add in redis key set
			if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
				userDataAccess.addInRedisSet(user);
			}

		}

	}

	/**
	 * This method is used to update user monthly score
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the refer by
	 *            user like refer medium type and user id of refer by user
	 */
	private void updateUserMonthlyScore(ReferalEntity referalEntity, String referScore) {
		// Get total score
		ScoreEntity scoreEntity = redisAssessment.getMonthlyScore(referalEntity.getUserId());
		ScoreEntity newScore = null;
		// If score is not null
		if (scoreEntity != null) {
			newScore = this.updateScore(scoreEntity, referalEntity, referScore);
		} else {
			newScore = this.createNewScore(referalEntity, referScore);
		}
		if (newScore != null) {
			// Save total score
			redisAssessment.saveMonthlyScore(newScore);

			// save monthly refer score
			// add userId in Redis set for saving monthly score in MYSQl
			// here we are assuming the now time will be same taken in Redis
			// if true add in redis key in set IsMySQLPublishQueueEnabled
			if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
				redisAssessment.addIdInRedisSetForAssessmentMonthlyScore(scoreEntity.getUserId() + ","
						+ LocalDateTime.now().getYear() + "," + LocalDateTime.now().getMonth());
			}

		}
	}

	/**
	 * This method is used to update the user total score
	 * 
	 * @param scoreEntity
	 *            this parameter contains the user monthly score,user obtained
	 *            score and max score
	 * @param referalEntity
	 *            this parameter contains the information regarding the refer by
	 *            user like refer medium type and user id of refer by user
	 */
	private ScoreEntity updateScore(ScoreEntity scoreEntity, ReferalEntity referalEntity, String referScore) {
		Float userReferScore = 0f;
		if (scoreEntity.getReferScore() != null) {
			// Calculate refer score
			userReferScore = Float.valueOf(scoreEntity.getReferScore()) + Float.valueOf(referScore);
		} else {
			// Calculate refer score
			userReferScore = Float.valueOf(referScore);
		}
		// Set refer score
		scoreEntity.setReferScore(String.valueOf(userReferScore));
		scoreEntity.setReferScoreInDouble(userReferScore);
		// set rank
		scoreEntity.setRank(calculateRank(Float.valueOf(scoreEntity.getObtainedScore()) + userReferScore));
		return scoreEntity;
	}

	/**
	 * This method is used to create a new user total score detail
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the refer by
	 *            user like refer medium type and user id of refer by user
	 */
	private ScoreEntity createNewScore(ReferalEntity referalEntity, String referScore) {
		// New instance of score entity
		ScoreEntity scoreEntity = new ScoreEntity();
		// Set user id
		scoreEntity.setUserId(referalEntity.getUserId());
		// Set refer score
		scoreEntity.setReferScore(referScore);
		// Set max score 0
		scoreEntity.setObtainedScore(String.valueOf(0f));
		// Set obtained score 0
		scoreEntity.setMaxScore(String.valueOf(0f));
		// set rank
		scoreEntity.setRank(calculateRank(Float.valueOf(referScore)));
		// set double referscore
		scoreEntity.setReferScoreInDouble(Double.parseDouble(referScore));
		// set Double obtained score
		scoreEntity.setObtainedScoreInDouble(0f);
		return scoreEntity;
	}

	/**
	 * This method is used to get the refer score according to refer type
	 * 
	 * @param referralMediumType
	 *            this parameter define the refer medium type
	 * @return the refer score
	 */
	private String getReferScore(UserReferalMediumType referralMediumType) {
		String referScore = null;
		// According to refer medium type update user score
		switch (referralMediumType) {
		case Email:
			// Get the refer score for email
			referScore = configurationManager.get("REFER_SCORE_FOR_EMAIL");
			break;
		case Phone_Number:
			// Get the refer score for phone
			referScore = configurationManager.get("REFER_SCORE_FOR_PHONE");
			break;
		case Facebook:
			// Get the refer score for faceBook
			referScore = configurationManager.get("REFER_SCORE_FOR_FACEBOOK");
			break;
		case LinkedIn:
			// Get the refer score for faceBook
			referScore = configurationManager.get("REFER_SCORE_FOR_LINKEDIN");
			break;
		default:
			break;
		}
		return referScore;
	}

	/**
	 * This method is used to get the sign up user refer score according to
	 * refer type
	 * 
	 * @param referralMediumType
	 *            this parameter define the refer medium type
	 * @return the refer score
	 */
	private String getSignUpUserReferScore(UserReferalMediumType referralMediumType) {
		String referScore = null;
		// According to refer medium type update user score
		switch (referralMediumType) {
		case Email:
			// Get the refer score for email
			referScore = configurationManager.get("SIGNUP_USER_REFER_SCORE_FOR_EMAIL");
			break;
		case Phone_Number:
			// Get the refer score for phone
			referScore = configurationManager.get("SIGNUP_USER_REFER_SCORE_FOR_PHONE");
			break;
		case Facebook:
			// Get the refer score for faceBook
			referScore = configurationManager.get("SIGNUP_USER_REFER_SCORE_FOR_FACEBOOK");
			break;
		case LinkedIn:
			// Get the refer score for faceBook
			referScore = configurationManager.get("SIGNUP_USER_REFER_SCORE_FOR_LINKEDIN");
			break;
		default:
			break;
		}
		return referScore;
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

	/**
	 * This method is used to publish the referral data to sales force
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the user
	 *            Reference like refer unique id ,referral link,referral
	 *            contacts ,referral medium type etc.
	 */
	private void publishReferralData(ReferalEntity referalEntity) {
		subjectsForPublishReferralReport = new BoilerplateList<>();
		subjectsForPublishReferralReport.add("PublishReferReport");
		try {
			// Trigger back ground job to send referral link through Email
			queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForPublishReferralReport, "ReferalEntity",
					"publishReferralData");
			throw new Exception();
		} catch (Exception ex) {
			logger.logException(
					"referralService", "publishReferralData", "try-Queue Reader", ex.toString()
							+ " ReferalEntity inserting in queue is: " + Base.toJSON(referalEntity) + " Queue Down",
					ex);
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

	private void publishUserToCRM(ExternalFacingReturnedUser user) {
		Boolean isPublishReport = Boolean.valueOf(configurationManager.get("Is_Publish_Report"));

		if (isPublishReport) {
			PublishEntity publishEntity = this.createPublishEntity("UpdateRefererScoreObserver.publishToCRM",
					configurationManager.get(""), configurationManager.get("UPDATE_AKS_USER_SUBJECT"), user,
					configurationManager.get(""), configurationManager.get(""), configurationManager.get(""));
			if (subjects == null) {
				subjects = new BoilerplateList<>();
				subjects.add(configurationManager.get("AKS_PUBLISH_SUBJECT"));
			}
			try {

				queueReaderJob.requestBackroundWorkItem(publishEntity, subjects, "UpdateR", "publishToCRM",
						configurationManager.get("AKS_PUBLISH_QUEUE"));
			} catch (Exception exception) {
				logger.logError("UpdateRefererScoreObserver", "publishToCRM", "queueReaderJob catch block",
						"Exception :" + exception);
			}
		}
	}

}
