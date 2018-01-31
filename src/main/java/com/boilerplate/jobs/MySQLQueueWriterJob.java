package com.boilerplate.jobs;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.database.redis.implementation.BaseRedisDataAccessLayer;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingUser;

/**
 * This class is used to fetch elements from redis set and add them in to a
 * queue for later processing
 * 
 * @author urvij
 *
 */
public class MySQLQueueWriterJob extends BaseRedisDataAccessLayer {

	public static final String UserKeyForSet = "USER_MYSQL";

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(MySQLQueueWriterJob.class);

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
	 * This is an instance of the queue job
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

	BoilerplateList<String> subjectsForCreateUser = new BoilerplateList();

	public void initialize() {
		subjectsForCreateUser.add("CreateUserInMySQL");
	}

	/**
	 * This method is used to add elements in
	 * 
	 * @throws NotFoundException
	 *             thrown if user is not found in redis database
	 */
	public void addElementsInQueue() throws NotFoundException {
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			// fetch userIds Set from redis
			Set<String> elements = userDataAccess.fetchUserIdsFromRedisSet();
			// if set is empty then do nothing
			if (elements.isEmpty()) {

			} else {
				// add the users into queue against each userId found in redis
				// database
				addUsersInQueue(elements);
			}

		}

	}

	/**
	 * This method is used to add users in to queue
	 * 
	 * @param userIdSet
	 *            the user ids against which users are to be fetched and added
	 *            into queue
	 * @throws NotFoundException
	 *             thrown if user is not found in redis database
	 */
	public void addUsersInQueue(Set<String> userIdSet) throws NotFoundException {
		// fetch user against each userId in Set
		for (String userId : userIdSet) {
			try {
				// add the users into queue against each userId found in redis
				// database
				queueReaderJob.requestBackroundWorkItem(
						(ExternalFacingUser) userDataAccess.getUser(normalizeUserId(userId), null),
						subjectsForCreateUser, "MySQLQueueWriterJob", "addUsersInQueue",
						configurationManager.get("MYSQL_PUBLISH_QUEUE"));

			} catch (Exception ex) {
				// if queue fails then log the exception
				ex.printStackTrace();
				logger.logException("MySQLQueueWriterJob", "addUsersInQueue", "try-catch block of queue",
						ex.getMessage(), ex);
			}

		}
	}

	/**
	 * This method is used to normalize the user Id
	 * 
	 * @param userId
	 *            the userId to normalize
	 * @return the normalized user Id
	 */
	private String normalizeUserId(String userId) {
		userId = userId.toUpperCase();
		// check if user id contains :
		if (userId.contains(":") == false) {
			// check if the user starts with DEFAULT:, if not then put in
			// Default: before it
			if (!userId
					.startsWith(this.configurationManager.get("DefaultAuthenticationProvider").toUpperCase() + ":")) {
				userId = this.configurationManager.get("DefaultAuthenticationProvider") + ":" + userId;
			}
		}
		return userId;
	}

}
