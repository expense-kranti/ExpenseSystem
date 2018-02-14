package com.boilerplate.asyncWork;

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

		// read all the Redis Keys and add them to Redis Sets
		addAllRedisKeysToRedisSet();

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
			if ((keySet != null) && !(keySet.isEmpty())) {
				for (String userIdWithRedisKeyName : keySet) {
					logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver", "addAllRedisKeysToRedisSet",
							"Inside if statement checking user Redis Set not null or empty",
							"about to fetch UserId from Redis Keys and Add into RedisSet, UserIdKeyName is : "
									+ userIdWithRedisKeyName);
					String[] userIdParts = userIdWithRedisKeyName.split(":");
					String userId = userIdParts[1] + ":" + userIdParts[2];

					String userReferId = this.redisSFUpdateHashAccess
							.hget(configurationManager.get("AKS_USER_UUID_HASH_BASE_TAG"), userId.toUpperCase());
					// add to Redis set
					userDataAccess.addInRedisSet(userIdParts[1] + ":" + userIdParts[2]);
					// get assessment for this user Id and add in assessment
					// redis set
					for (String userAssessmentKey : redisAssessment.getAllAssessmentKeysForUser(userId)) {
						addAssessmentAndAssessmentScoreKeyByProcessingRedisKey(userAssessmentKey);
					}
					// get BlogActivity for this user Id and add in
					// assessment
					// redis set
					for (String userBlogActivityKey : blogActivityDataAccess.getAllBlogUserKeys(userId)) {
						addBlogActivityKeyByProcessingRedisKey(userBlogActivityKey);
					}
					// get ReferredContact for this user Id and add in
					// assessment
					// redis set
					for (String userReferredContactKey : referral.getUserAllReferredContactsKeys(userReferId)) {
						addReferredRelatedDataKeyByProcessingRedisKey(userReferredContactKey);
					}
					// get MonthlyScore for this user Id and add in
					// assessment
					// redis set
					for (String userMonthlyScoreKey : redisAssessment.getAllMonthlyScoreKeys(userId)) {
						addMonthlyScoreKeyByProcessingRedisKey(userMonthlyScoreKey);
					}
					// get userFiles for this user Id and add in assessment
					// redis set
					for (String userFileKey : filePointer.getAllUserFileKeysForUser(userId)) {
						filePointer.addInRedisSet(userFileKey);
					}
				}

			}
		}

		logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver", "addAllRedisKeysToRedisSet",
				"Last statement Inside body of addAllRedisKeysToRedisSet method",
				"adding of Redis Keys into RedisSet done");

	}

	/**
	 * This method is used to prepare assessment keys to be used to be saved in
	 * Redis Set
	 * 
	 * @param assessmentIdWithRedisKeyName
	 *            the complete key containing Redis assessment key
	 */
	private void addAssessmentAndAssessmentScoreKeyByProcessingRedisKey(String assessmentIdWithRedisKeyName) {
		String[] assessmentKeyPart = assessmentIdWithRedisKeyName.split(":");
		// add to Redis set
		redisAssessment.addInRedisSetForUserAssessment(assessmentKeyPart[1] + ":" + assessmentKeyPart[2],
				assessmentKeyPart[3]);
		redisAssessment.addIdInRedisSetForAssessmentScore(
				assessmentKeyPart[1] + ":" + assessmentKeyPart[2] + "," + assessmentKeyPart[3]);

	}

	/**
	 * This method is used to prepare blog activity and save in Redis Set
	 * 
	 * @param blogActivityKey
	 *            containing the Redis blog key
	 */
	private void addBlogActivityKeyByProcessingRedisKey(String blogActivityKey) {
		String[] blogActivityKeyPart = blogActivityKey.split(":");
		blogActivityDataAccess.addInRedisSet(blogActivityKeyPart[1],
				blogActivityKeyPart[2] + ":" + blogActivityKeyPart[3]);

	}

	/**
	 * This method is used to get referred related key and processkey to get
	 * info
	 * 
	 * @param referalKey
	 *            containing the Redis key for referal key for user
	 */
	private void addReferredRelatedDataKeyByProcessingRedisKey(String referalKey) {
		String[] referalKeyPart = referalKey.split(":");
		referral.addInRedisSet(referalKeyPart[1], referalKeyPart[2], referalKeyPart[3].toUpperCase());
		referral.addInRedisSet(referalKeyPart[1], referalKeyPart[2], referalKeyPart[3].toUpperCase());

	}

	/**
	 * This method is used to get the monthly score key and prepare it to be
	 * used in making entry in Redis Set
	 * 
	 * @param monthlyScoreKey
	 *            containing the Redis key for referal keys
	 */
	private void addMonthlyScoreKeyByProcessingRedisKey(String monthlyScoreKey) {
		String[] monthlyScoreKeyPart = monthlyScoreKey.split(":");
		redisAssessment.addIdInRedisSetForAssessmentMonthlyScore(monthlyScoreKeyPart[3] + ":" + monthlyScoreKeyPart[4]
				+ "," + monthlyScoreKeyPart[1] + "," + monthlyScoreKeyPart[2]);
	}

}
