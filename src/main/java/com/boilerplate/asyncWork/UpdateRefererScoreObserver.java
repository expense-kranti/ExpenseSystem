package com.boilerplate.asyncWork;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.java.entities.UpdateReferralContactDetailsEntity;
import com.boilerplate.java.entities.UserReferalMediumType;

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
	 * This is the subject list for publish
	 */
	private BoilerplateList<String> subjects = null;
	
	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		// Get externalFacingUser from asyncWorkItem
		ExternalFacingUser externalFacingUser = (ExternalFacingUser) asyncWorkItem.getPayload();
		
		String referBYUser = referral.getReferUser(externalFacingUser.getUserReferId());
		if (referBYUser != null) {
			// Create the entity
			ReferalEntity referalEntity = new ReferalEntity(externalFacingUser.getCampaignType(),
					referBYUser,externalFacingUser.getUserReferId());
			// check counter for maxm user
			String signUpCount = referral.getSignUpCount(referalEntity);
			if(signUpCount==null && Integer.parseInt(signUpCount)<
					 Integer.parseInt(configurationManager.get("MAX_ALLOW_USER_SCORE")) ){
				// Update referring user score
				this.updateReferringUserScore(referalEntity);
				// Is sign up user get the refer score
				if (Boolean.valueOf(configurationManager.get("IS_SIGN_UP_USER_GET_REFER_SCORE"))) {
					// Update sign up user score
					this.updateSignUpUserScore(referalEntity, externalFacingUser);
				}
				// if null counter value then set othervise increase
				this.checkAndSetSignUpCounter(signUpCount,referalEntity);
			}
			
			UpdateReferralContactDetailsEntity updateReferralContactDetailsEntity = new UpdateReferralContactDetailsEntity(
					"", referalEntity.getUserReferId(),
					this.getSignUpUserReferScore(referalEntity.getReferralMediumType()),
					this.getReferScore(referalEntity.getReferralMediumType()), LocalDate.now().toString(),
					externalFacingUser.getUserId());
			// Publish report
			this.publishToCRM(updateReferralContactDetailsEntity);
		}

	}

	private void checkAndSetSignUpCounter(String signUpCount, ReferalEntity referalEntity) {
		if(signUpCount==null){
			referral.createSignUpCounter(referalEntity, "1");
		}else{
			referral.increaseReferSignUpCounter(referalEntity);
		}
		
	}

	/**
	 * This method is used to update Referring user score
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the refer by
	 *            user like refer medium type and user id of refer by user
	 */
	private void updateReferringUserScore(ReferalEntity referalEntity) {
		// Update user Total score
		this.updateUserTotalScore(referalEntity, this.getReferScore(referalEntity.getReferralMediumType()));
		// Update user monthly score
		this.updateUserMonthlyScore(referalEntity, this.getReferScore(referalEntity.getReferralMediumType()));
	}

	private void updateSignUpUserScore(ReferalEntity referalEntity, ExternalFacingUser externalFacingUser) {
		// Clone referral entity
		ReferalEntity signUpUserReferralDetails = (ReferalEntity) referalEntity.clone();
		// Set sign up user id
		signUpUserReferralDetails.setUserId(externalFacingUser.getUserId());
		// Update user Total score
		this.updateUserTotalScore(referalEntity, this.getSignUpUserReferScore(referalEntity.getReferralMediumType()));
		// Update user monthly score
		this.updateUserMonthlyScore(referalEntity, this.getSignUpUserReferScore(referalEntity.getReferralMediumType()));
	}

	/**
	 * This method is used to update user total score
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the refer by
	 *            user like refer medium type and user id of refer by user
	 */
	private void updateUserTotalScore(ReferalEntity referalEntity, String referScore) {
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
			redisAssessment.saveMonthlyScore(newScore);
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
		scoreEntity.setReferScore(String.valueOf(referScore));
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
		// set rank
		scoreEntity.setRank(calculateRank(Float.valueOf(referScore)));
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
	 * This method is used to publish the referral details to sales force
	 * 
	 * @param updateReferralContactDetailsEntity
	 *            this parameter contain information regarding the refer like
	 *            referral uuid,coming user id,referring user id,coming
	 *            time,coming user refer score and referring user refer score
	 */
	private void publishToCRM(UpdateReferralContactDetailsEntity updateReferralContactDetailsEntity) {
		// Get the status of is refer report publishing is enabled or not
		if (Boolean.valueOf(configurationManager.get("Is_REFERRAL_REPORT_PUBLISH_ENABLED"))) {
			// Create the publish entity
			PublishEntity publishEntity = this.createPublishEntity("PublishReferReportObserver.publishToCRM",
					configurationManager.get("AKS_REFER_PUBLISH_METHOD"),
					configurationManager.get("AKS_REFER_PUBLISH_SUBJECT"), updateReferralContactDetailsEntity,
					configurationManager.get("AKS_REFER_PUBLISH_URL"),
					configurationManager.get("AKS_REFER_PUBLISH_TEMPLATE"),
					configurationManager.get("AKS_REFER_DYNAMIC_PUBLISH_URL"));
			if (subjects == null) {
				subjects = new BoilerplateList<>();
				subjects.add(configurationManager.get("AKS_PUBLISH_SUBJECT"));
			}
			try {
				logger.logInfo("PublishReferReportObserver", "publishToCRM", "Publishing report",
						"publish referral report status" + updateReferralContactDetailsEntity.getComingUserId());
				queueReaderJob.requestBackroundWorkItem(publishEntity, subjects, "PublishReferReportObserver",
						"publishToCRM", configurationManager.get("AKS_PUBLISH_QUEUE"));
			} catch (Exception exception) {
				logger.logError("PublishReferReportObserver", "publishToCRM", "queueReaderJob catch block",
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
