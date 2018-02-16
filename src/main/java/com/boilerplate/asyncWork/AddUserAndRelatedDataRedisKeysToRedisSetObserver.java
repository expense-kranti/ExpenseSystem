package com.boilerplate.asyncWork;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IRedisScript;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.interfaces.ISFUpdateHash;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.jobs.MySQLQueueWriterJob;

/**
 * This class is used to fetch user and user related data Redis keys and add it
 * to Redis Sets
 * 
 * @author urvij
 *
 */
public class AddUserAndRelatedDataRedisKeysToRedisSetObserver implements IAsyncWorkObserver {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(AddUserAndRelatedDataRedisKeysToRedisSetObserver.class);

	/**
	 * The autowired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;

	/**
	 * This is the setter for user data access
	 * 
	 * @param userDataAccess
	 *            to set
	 */
	public void setUserDataAccess(IUser userDataAccess) {
		this.userDataAccess = userDataAccess;
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
	 * This is the instance of blog activity data access
	 */
	IBlogActivity blogActivityDataAccess;

	/**
	 * Sets the blog activity data access
	 * 
	 * @param blogActivityDataAccess
	 */
	public void setBlogActivityDataAccess(IBlogActivity blogActivityDataAccess) {
		this.blogActivityDataAccess = blogActivityDataAccess;
	}

	/**
	 * This is the instance of filePointer
	 */
	@Autowired
	IFilePointer filePointer;

	/**
	 * Sets the file pointer
	 * 
	 * @param filePointer
	 *            to set
	 */
	public void setFilePointer(IFilePointer filePointer) {
		this.filePointer = filePointer;
	}

	/**
	 * This is the instance of redissfupdatehashaccess
	 */
	@Autowired
	ISFUpdateHash redisSFUpdateHashAccess;

	/**
	 * This method set the instance of redisSFUpdateHashAccess
	 * 
	 * @param redisSFUpdateHashAccess
	 *            the redisSFUpdateHashAccess to set
	 */
	public void setRedisSFUpdateHashAccess(ISFUpdateHash redisSFUpdateHashAccess) {
		this.redisSFUpdateHashAccess = redisSFUpdateHashAccess;
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

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {

		logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver", "OBSERVE",
				" FIRST STATEMENT IN OBSERVE METHOD",
				" READING REDIS KEYS AND ADDING INTO REDIS SETS ******STARTED******* ");
		// read all the Redis Keys and add them to Redis Sets
		addAllRedisKeysToRedisSet();

		logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver", "OBSERVE",
				" LAST STATEMENT IN OBSERVE METHOD",
				" READING REDIS KEYS AND ADDING INTO REDIS SETS ******ENDED******* ");

	}

	/**
	 * This method is used to read all the Redis Keys which are in interest and
	 * add them to Redis Sets by preparing them according to the format the
	 * reader observers who process them requires
	 */
	private void addAllRedisKeysToRedisSet() {

		logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver", "addAllRedisKeysToRedisSet",
				"First statement Inside body of addAllRedisKeysToRedisSet method",
				"about to fetch Redis Keys and Add into RedisSet");

		if (Boolean.parseBoolean(configurationManager.get("ISBulkAddInRedisSetEnabled"))) {

			// add all user ids to Redis set
			Set<String> keySet = userDataAccess.getAllUserKeys();
			List<String> keysList = new ArrayList<>();
			keysList.addAll(keySet);
			logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver", "addAllRedisKeysToRedisSet",
					"After getting all User Keys from Redis ",
					"Before null Number of User Keys Fetched : " + keySet.size());
			if ((keysList.size() > 0)) {
				logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver", "addAllRedisKeysToRedisSet",
						"After getting all User Keys from Redis ",
						"After null Number of User Keys Fetched : " + keysList.size());
				int count = 1;
				try {
					for (int i = 0; i < keysList.size(); i++) {
						if (keysList.get(i) != null) {
							String userIdWithRedisKeyName = keysList.get(i);
							logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
									"addAllRedisKeysToRedisSet", "Set Key Counter",
									"Counter Value " + Integer.toString(count));
							try {
								logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
										"addAllRedisKeysToRedisSet",
										"Inside if statement checking user Redis Set size greater than 0",
										"about to fetch UserId from Redis Keys and Add into RedisSet, UserIdKeyName is : "
												+ userIdWithRedisKeyName);

								String[] userIdParts = userIdWithRedisKeyName.split(":");
								String userId = userIdParts[1] + ":" + userIdParts[2];

								String userReferId = this.redisSFUpdateHashAccess.hget(
										configurationManager.get("AKS_USER_UUID_HASH_BASE_TAG"), userId.toUpperCase());
								// add to Redis set
								userDataAccess.addInRedisSet(userId);
								// get assessment for this user Id and add in
								// assessment
								// redis set
								fetchEachAssesssmentKeyAndAddInRedisSet(userId);

								// get BlogActivity for this user Id and add in
								// assessment
								// redis set
								fetchEachBlogActivityAndAddInRedisSet(userId);

								// get ReferredContact for this user Id and add
								// in
								// assessment
								// redis set
								fetchEachReferredContactsKeyAndAddInRedisSet(userReferId);

								// get MonthlyScore for this user Id and add in
								// assessment
								// redis set
								fetchEachMonthlyScoreKeyAndAddInRedisSet(userId);

								// get userFiles for this user Id and add in
								// assessment
								// redis set
								fetchEachFileKeyAndAddInRedisSet(userId);
								count++;
							} catch (Exception ex) {
								logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
										"addAllRedisKeysToRedisSet",
										"Last statement Inside body of first for loop which is getting userids from redis keys and traversing to add into redis set",
										"exception messege : " + ex.getMessage() + ", exception cause : "
												+ ex.getCause().toString(),
										ex);

							}

						} else {
							logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
									"addAllRedisKeysToRedisSet", "key null", "");
						}
					}

				} catch (Exception e) {
					logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver", "addAllRedisKeysToRedisSet",
							"Set for loop erro", "Error while processing set for loop", e);

				}

			}

		}

		logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver", "addAllRedisKeysToRedisSet",
				"Last statement Inside body of addAllRedisKeysToRedisSet method",
				"adding of Redis Keys into RedisSet done");

	}

	/**
	 * This method fetches each user file key from UserFileKey and add into
	 * Redis Set
	 * 
	 * @param userReferId
	 *            against which file details to be fetched
	 */
	private void fetchEachFileKeyAndAddInRedisSet(String userId) {
		for (String userFileKey : filePointer.getAllUserFileKeysForUser(userId)) {
			try {
				filePointer.addInRedisSet(userFileKey);
			} catch (Exception ex) {
				logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
						"fetchEachFileKeyAndAddInRedisSet",
						"inside for loop which is fetching each file key and adding in redis set",
						"Exception message : " + ex.getMessage() + ", Exception cause : " + ex.getCause().toString(),
						ex);

			}
		}
	}

	/**
	 * This method fetches each user monthly score key from MonthlyScoreKey and
	 * add into Redis Set
	 * 
	 * @param userReferId
	 *            against which monthly score to be fetched
	 */
	private void fetchEachMonthlyScoreKeyAndAddInRedisSet(String userId) {
		for (String userMonthlyScoreKey : redisAssessment.getAllMonthlyScoreKeys(userId)) {
			try {
				addMonthlyScoreKeyByProcessingRedisKey(userMonthlyScoreKey);
			} catch (Exception ex) {
				logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
						"fetchEachMonthlyScoreKeyAndAddInRedisSet",
						"inside for loop which is fetching each monthly score key and adding in redis set",
						"Exception message : " + ex.getMessage() + ", Exception cause : " + ex.getCause().toString(),
						ex);

			}
		}
	}

	/**
	 * This method fetches each referredcontact key from AssessmentKeys and add
	 * into Redis Set
	 * 
	 * @param userReferId
	 *            against which referred contact to be fetched
	 */
	private void fetchEachReferredContactsKeyAndAddInRedisSet(String userReferId) {
		for (String userReferredContactKey : referral.getUserAllReferredContactsKeys(userReferId)) {
			try {
				addReferredRelatedDataKeyByProcessingRedisKey(userReferredContactKey);
			} catch (Exception ex) {
				logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
						"fetchEachReferredContactsKeyAndAddInRedisSet",
						"inside for loop which is fetching each referred contact key and adding in redis set",
						"Exception message : " + ex.getMessage() + ", Exception cause : " + ex.getCause().toString(),
						ex);

			}
		}
	}

	/**
	 * This method fetches each BlogActivity key from BlogActivityKeys and add
	 * into Redis Set
	 * 
	 * @param userReferId
	 *            against which blog activity to be fetched
	 */
	private void fetchEachBlogActivityAndAddInRedisSet(String userId) {
		for (String userBlogActivityKey : blogActivityDataAccess.getAllBlogUserKeys(userId)) {
			try {
				addBlogActivityKeyByProcessingRedisKey(userBlogActivityKey);
			} catch (Exception ex) {
				logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
						"fetchEachBlogActivityAndAddInRedisSet",
						"inside for loop which is fetching each blog acitvity key and adding in redis set",
						"Exception message : " + ex.getMessage() + ", Exception cause : " + ex.getCause().toString(),
						ex);

			}

		}
	}

	/**
	 * This method fetches each asessmnet key from AssessmentKeys and add into
	 * Redis Set
	 * 
	 * @param userId
	 *            against which assessment keys to be fetched
	 */
	private void fetchEachAssesssmentKeyAndAddInRedisSet(String userId) {
		for (String userAssessmentKey : redisAssessment.getAllAssessmentKeysForUser(userId)) {
			try {
				addAssessmentAndAssessmentScoreKeyByProcessingRedisKey(userAssessmentKey);
			} catch (Exception ex) {
				logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
						"fetchEachAssesssmentKeyAndAddInRedisSet",
						"inside for loop which is fetching each assessment key and adding in redis set",
						"Exception message : " + ex.getMessage() + ", Exception cause : " + ex.getCause().toString(),
						ex);

			}
		}

	}

	/**
	 * This method is used to prepare assessment keys to be used to be saved in
	 * Redis Set
	 * 
	 * @param assessmentIdWithRedisKeyName
	 *            the complete key containing Redis assessment key
	 */
	private void addAssessmentAndAssessmentScoreKeyByProcessingRedisKey(String assessmentIdWithRedisKeyName) {
		try {
			String[] assessmentKeyPart = assessmentIdWithRedisKeyName.split(":");
			// add to Redis set
			redisAssessment.addInRedisSetForUserAssessment(assessmentKeyPart[1] + ":" + assessmentKeyPart[2],
					assessmentKeyPart[3]);
			redisAssessment.addIdInRedisSetForAssessmentScore(
					assessmentKeyPart[1] + ":" + assessmentKeyPart[2] + "," + assessmentKeyPart[3]);
		} catch (Exception ex) {
			logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
					"addAssessmentAndAssessmentScoreKeyByProcessingRedisKey",
					"inside catch block of try block spliting assessment redis key and adding it to assessment Redis set and Assessment score redis set",
					"Exception message : " + ex.getMessage() + ", Exception cause : " + ex.getCause().toString(), ex);

		}

	}

	/**
	 * This method is used to prepare blog activity and save in Redis Set
	 * 
	 * @param blogActivityKey
	 *            containing the Redis blog key
	 */
	private void addBlogActivityKeyByProcessingRedisKey(String blogActivityKey) {
		try {
			String[] blogActivityKeyPart = blogActivityKey.split(":");
			blogActivityDataAccess.addInRedisSet(blogActivityKeyPart[1],
					blogActivityKeyPart[2] + ":" + blogActivityKeyPart[3]);
		} catch (Exception ex) {

			logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
					"addBlogActivityKeyByProcessingRedisKey",
					"inside catch block of try block spliting blogactivity key and adding it to blog activity redis set",
					"Exception message : " + ex.getMessage() + ", Exception cause : " + ex.getCause().toString(), ex);

		}

	}

	/**
	 * This method is used to get referred related key and processkey to get
	 * info
	 * 
	 * @param referalKey
	 *            containing the Redis key for referal key for user
	 */
	private void addReferredRelatedDataKeyByProcessingRedisKey(String referalKey) {
		try {
			String[] referalKeyPart = referalKey.split(":");
			referral.addInRedisSet(referalKeyPart[1], referalKeyPart[2], referalKeyPart[3].toUpperCase());

		} catch (Exception ex) {
			logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
					"addReferredRelatedDataKeyByProcessingRedisKey",
					"inside catch block of try block spliting referal key and adding it to referral contact redis set and referred expired contact redis set",
					"Exception message : " + ex.getMessage() + ", Exception cause : " + ex.getCause().toString(), ex);

		}

	}

	/**
	 * This method is used to get the monthly score key and prepare it to be
	 * used in making entry in Redis Set
	 * 
	 * @param monthlyScoreKey
	 *            containing the Redis key for referal keys
	 */
	private void addMonthlyScoreKeyByProcessingRedisKey(String monthlyScoreKey) {
		try {
			String[] monthlyScoreKeyPart = monthlyScoreKey.split(":");
			redisAssessment.addIdInRedisSetForAssessmentMonthlyScore(monthlyScoreKeyPart[3] + ":"
					+ monthlyScoreKeyPart[4] + "," + monthlyScoreKeyPart[1] + "," + monthlyScoreKeyPart[2]);

		} catch (Exception ex) {
			logger.logException("AddUserAndRelatedDataRedisKeysToRedisSetObserver",
					"addMonthlyScoreKeyByProcessingRedisKey",
					"inside catch block of try block spliting monthly score redis key and adding it to monthlyscore Redis set ",
					"Exception message : " + ex.getMessage() + ", Exception cause : " + ex.getCause().toString(), ex);

		}
	}

}
