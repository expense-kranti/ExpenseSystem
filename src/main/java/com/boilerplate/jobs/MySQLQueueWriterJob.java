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
		subjectsForCreateUser.add("CreateOrUpdateUserInMySQL");
		// add subject for another task like for assessment, referral, blog,
		// file
	}

	/**
	 * This method is used to add elements in
	 * 
	 * @throws NotFoundException
	 *             thrown if user is not found in redis database
	 */
	public void addElementsInQueue() throws NotFoundException {
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			// fetch and process userIds from Redis Set for migrating Users from
			// Redis to MySQL
			this.fetchUserIdsAndAddInQueue();

			// fetch other ids like for blog activity, file api, assessments api
			// from Redis Set

		}

	}

	private void fetchUserIdsAndAddInQueue() throws NotFoundException {
		// fetch userIds Set from redis
		Set<String> elements = userDataAccess.fetchUserIdsFromRedisSet();
		// if set is empty then do nothing
		if (!elements.isEmpty()) {

			// add the userIds into queue from user set
			addTaskInQueue(elements, subjectsForCreateUser);
			// // add the assessmentIds into queue from assessment set
			// addTaskInQueue(elements,subjectsForassessment);
			// // add the userReferralIds into queue from referral set
			// addTaskInQueue(elements,subjectsForReferal);
			// // add the blogIds into queue from blog set
			// addTaskInQueue(elements,subjectsForBlog);
			// //add file

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
	public void addTaskInQueue(Set<String> dataSet, BoilerplateList<String> subjectsForPerformingTask)
			throws NotFoundException {
		// fetch user against each userId present in Set
		for (String dataId : dataSet) {
			try {
				// add the users into queue against each userId found in redis
				// database
				queueReaderJob.requestBackroundWorkItem(dataId, subjectsForPerformingTask, "MySQLQueueWriterJob",
						"addUsersInQueue", configurationManager.get("MYSQL_PUBLISH_QUEUE"));

			} catch (Exception ex) {
				// if queue fails then log the exception
				logger.logException("MySQLQueueWriterJob", "addUsersInQueue", "try-catch block of queue",
						ex.getMessage(), ex);
			}

		}
	}

}
