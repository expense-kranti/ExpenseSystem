package com.boilerplate.jobs;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.database.redis.implementation.BaseRedisDataAccessLayer;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingUser;

/**
 * This class is used to fetch elements from Redis set and add them in to a
 * queue for later processing
 * 
 * @author urvij
 *
 */
public class MySQLQueueWriterJob extends BaseRedisDataAccessLayer {

	/**
	 * These variables are used to recognized the type of key fetching to do
	 */
	public static String addUserInQueue = "User";
	public static String addUserAssessmentInQueue = "Assessment";
	public static String addUserIdForAssessmentObtainedScoreInQueue = "AssessmentObtainedScore";
	public static String addUserIdForMonthlyScoreInQueue = "UserMonthlyScore";

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
	 * These are the subjects list for fetching data from Redis set and add into
	 * queue
	 */
	BoilerplateList<String> subjectsForCreateUser = new BoilerplateList();
	BoilerplateList<String> subjectsForSaveUserAssessment = new BoilerplateList();
	BoilerplateList<String> subjectsForSaveUserAssessmentScore = new BoilerplateList();
	BoilerplateList<String> subjectsForSaveUserMonthlyScore = new BoilerplateList();

	public void initialize() {
		subjectsForCreateUser.add("CreateOrUpdateUserInMySQL");
		subjectsForSaveUserAssessment.add("SaveAssessmentInMySQL");
		// add the subject and observer entry in root context
		subjectsForSaveUserAssessmentScore.add("SaveUserAssessmentScoreInMySQL");
		subjectsForSaveUserMonthlyScore.add("SaveUserMonthlyScoreInMySQL");

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
			// fetch and process userIds from Redis Set and add into queue for
			// migrating Users from
			// Redis to MySQL
			this.fetchDataFromRedisSetAndAddInQueue(addUserInQueue);
			// fetch and process AssessmentIds from Redis Set and add into queue
			// for migrating Assessments from Redis to MySQL
			this.fetchDataFromRedisSetAndAddInQueue(addUserAssessmentInQueue);
			// fetch and process AssessmentIds from Redis Set and add into queue
			// for migrating Assessments from Redis to MySQL
			this.fetchDataFromRedisSetAndAddInQueue(addUserIdForAssessmentObtainedScoreInQueue);
			// fetch and process AssessmentIds from Redis Set and add into queue
			// for migrating Assessments from Redis to MySQL
			this.fetchDataFromRedisSetAndAddInQueue(addUserIdForMonthlyScoreInQueue);
			//
			// fetch other ids like for blog activity, file api, assessments api
			// from Redis Set

		}

	}

	/**
	 * This method is used to fetch the ids from the required Redis Set
	 * 
	 * @param key
	 *            the key for which the Redis set is to fetch
	 */
	public void fetchDataFromRedisSetAndAddInQueue(String key) {
		Set<String> elements = null;
		BoilerplateList<String> subject = null;
		switch (key) {
		case "User":
			// fetch userIds Set from redis
			elements = userDataAccess.fetchUserIdsFromRedisSet();
			// set the subject
			subject = subjectsForCreateUser;
			break;
		case "Assessment":
			// fetch AssessmentIds Set from Redis
			elements = redisAssessment.fetchAssessmentIdsFromRedisSet();
			/// set the subject
			subject = subjectsForSaveUserAssessment;
			break;

		case "AssessmentObtainedScore":
			// fetch AssessmentIds Set from Redis
			elements = redisAssessment.fetchIdsFromAssessmentScoreRedisSet();
			/// set the subject
			subject = subjectsForSaveUserAssessmentScore;
			break;

		case "UserMonthlyScore":
			// fetch AssessmentIds Set from Redis
			elements = redisAssessment.fetchIdsFromAssessmentMonthlyScoreRedisSet();
			/// set the subject
			subject = subjectsForSaveUserMonthlyScore;
			break;
		}

		// if set is empty then do nothing
		if (!elements.isEmpty() && !(elements == null)) {
			// add the userIds into queue from user set
			addTaskInQueue(elements, subject);
		}
	}

	/**
	 * This method is used to add the task in the queue from the Set of data
	 * 
	 * @param dataSet
	 *            comprises of data(usually ids) which is to be pushed in queue
	 * @param subjectsForPerformingTask
	 *            the subjects list related to particular data pushed to queue
	 */
	public void addTaskInQueue(Set<String> dataSet, BoilerplateList<String> subjectsForPerformingTask) {
		// fetch user against each userId present in Set
		for (String dataId : dataSet) {
			try {
				
				// add the data into queue redis
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
