package com.boilerplate.asyncWork;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IRedisScript;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.framework.Logger;
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
			for (String userId : userDataAccess.getAllUserKeys()) {
				String[] userIdParts = userId.split(":");
				// add to Redis set
				userDataAccess.addInRedisSet(userIdParts[1] + ":" + userIdParts[2]);
			}

			// add all assessment keys to Redis set
			for (String assessmentKey : redisAssessment.getAllAssessmentKeys()) {
				String[] assessmentKeyPart = assessmentKey.split(":");
				// add to Redis set
				redisAssessment.addInRedisSetForUserAssessment(assessmentKeyPart[1] + ":" + assessmentKeyPart[2],
						assessmentKeyPart[3]);
			}

			// add all monthly score keys to Redis set
			for (String monthlyScoreKey : redisAssessment.getAllMonthlyScoreKeys()) {
				String[] monthlyScoreKeyPart = monthlyScoreKey.split(":");
				redisAssessment.addIdInRedisSetForAssessmentMonthlyScore(monthlyScoreKeyPart[3] + ":"
						+ monthlyScoreKeyPart[4] + "," + monthlyScoreKeyPart[1] + "," + monthlyScoreKeyPart[2]);
			}

			// add all referredcontact key to Redis Set
			for (String referredContactKey : referral.getAllReferredContactKeys()) {
				String[] referredContactKeyPart = referredContactKey.split(":");
				referral.addInRedisSet(referredContactKeyPart[1], referredContactKeyPart[2],
						referredContactKeyPart[3].toUpperCase());
			}

			// add all blogActivityKey to Redis Set
			for (String blogActivityKey : blogActivityDataAccess.getAllBlogActivityKeys()) {
				String[] blogActivityKeyPart = blogActivityKey.split(":");
				blogActivityDataAccess.addInRedisSet(blogActivityKeyPart[1],
						blogActivityKeyPart[2] + ":" + blogActivityKeyPart[3]);
			}

			// add all FileKey to Redis Set
			for (String fileKey : filePointer.getAllFileKeys()) {
				String[] fileKeyPart = fileKey.split(":");
				filePointer.addInRedisSet(fileKeyPart[1]);
			}
		}
		logger.logInfo("AddUserAndRelatedDataRedisKeysToRedisSetObserver", "addAllRedisKeysToRedisSet",
				"Last statement Inside body of addAllRedisKeysToRedisSet method",
				"adding of Redis Keys into RedisSet done");

	}

}
