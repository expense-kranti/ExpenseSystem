package com.boilerplate.jobs;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;

/**
 * This class is used to fetch elements from Redis set and add them in to a
 * queue for later processing
 * 
 * @author urvij
 *
 */
public class MySQLQueueWriterJob {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(MySQLQueueWriterJob.class);

	/**
	 * These variables are used to recognized the type of key fetching to do
	 * from Redis set
	 */
	public static String addUserInQueue = "User";
	public static String addUserAssessmentInQueue = "Assessment";
	public static String addUserIdForAssessmentObtainedScoreInQueue = "AssessmentObtainedScore";
	public static String addUserIdForMonthlyScoreInQueue = "UserMonthlyScore";
	public static String addUserReferIdInQueue = "UserReferral";
	public static String addIdForBlogActivityInQueue = "UserBlogActivity";
	public static String addIdForUserFilesInQueue = "UserFiles";

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

	private IFilePointer filePointer;

	/**
	 * Sets the filePointer
	 * 
	 * @param filePointer
	 */
	public void setFilePointer(IFilePointer filePointer) {
		this.filePointer = filePointer;
	}

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
	 * These are the subjects list for fetching data from Redis set and add into
	 * queue
	 */
	BoilerplateList<String> subjectsForCreateUser = new BoilerplateList();
	BoilerplateList<String> subjectsForSaveUserAssessment = new BoilerplateList();
	BoilerplateList<String> subjectsForSaveUserAssessmentScore = new BoilerplateList();
	BoilerplateList<String> subjectsForSaveUserMonthlyScore = new BoilerplateList();
	BoilerplateList<String> subjectsForCreateBlogActivity = new BoilerplateList<>();
	BoilerplateList<String> subjectsForCreateUserFile = new BoilerplateList<>();
	BoilerplateList<String> subjectsForCreateReferalContact = new BoilerplateList<>();

	public void initialize() {
		// subjects for task like for assessment, referral, blog, file
		subjectsForCreateUser.add("CreateOrUpdateUserInMySQL");
		subjectsForSaveUserAssessment.add("SaveAssessmentInMySQL");
		subjectsForSaveUserAssessmentScore.add("SaveUserAssessmentScoreInMySQL");
		subjectsForSaveUserMonthlyScore.add("SaveUserMonthlyScoreInMySQL");
		subjectsForCreateUserFile.add("CreateUserFileInMySQL");
		subjectsForCreateBlogActivity.add("CreateBlogActivityInMySQL");
		subjectsForCreateReferalContact.add("CreateUserReferralContact");
	}

	/**
	 * This method is used to add elements in
	 * 
	 * @throws NotFoundException
	 *             thrown if user is not found in redis database
	 */
	// This method is being called automatically through servlet context
	// configuration
	public void addRedisSetElementsInQueue() throws NotFoundException {

		logger.logInfo("MySQLQueueWriterJob", "addRedisSetElementsInQueue",
				" FIRST STATEMENT IN ADD REDIS SET ELEMENTS IN QUEUE",
				" READING REDIS SETS AND ADDING INTO QUEUE ******STARTED******* ");

		if (Boolean.parseBoolean(configurationManager.get("IsReadSetAndAddToMySQLPublishQueueEnabled"))) {
			// fetch and process userIds from Redis Set and add into queue for
			// migrating Users from Redis to MySQL
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
			// fetch and process Referal Contcts from Redis set for migrating
			// Referal from redis to MySQL
			this.fetchDataFromRedisSetAndAddInQueue(addUserReferIdInQueue);
			// fetch and process BlogActivity from Redis set for migrating user
			// from redis to MySQL
			this.fetchDataFromRedisSetAndAddInQueue(addIdForBlogActivityInQueue);
			// fetch and process User uploaded File from Redis set for migrating
			// user from redis to MySQL
			this.fetchDataFromRedisSetAndAddInQueue(addIdForUserFilesInQueue);
		}

		logger.logInfo("MySQLQueueWriterJob", "addRedisSetElementsInQueue",
				" LAST STATEMENT IN ADD REDIS SET ELEMENTS IN QUEUE",
				" READING REDIS SETS AND ADDING INTO QUEUE ******ENDED******* ");

	}

	/**
	 * This method is used to fetch the ids from the required Redis Set
	 * 
	 * @param key
	 *            the key for which the Redis set is to fetch
	 */
	private void fetchDataFromRedisSetAndAddInQueue(String key) {
		Set<String> elements = null;
		BoilerplateList<String> subject = null;
		switch (key) {

		case "User":
			// fetch userIds Set from redis
			// elements = userDataAccess.fetchUserIdsFromRedisSet();
			// set the subject
			subject = subjectsForCreateUser;
			break;

		case "UserFiles":
			// fetch userIds Set from redis
			elements = filePointer.fetchUserFileAndAddInQueue();
			// set the subject
			subject = subjectsForCreateUserFile;
			break;

		}
		// if set is empty then do nothing
		if (!elements.isEmpty() && !(elements == null)) {
			logger.logInfo("MySQLQueueWriterJob", "fetchDataFromRedisSetAndAddInQueue method",
					"Below switch block end inside if block",
					" About to call addTaskInQueue method for adding each id in Queue with number of elements are : "
							+ elements.size() + ", for subject : " + subject.get(0));
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
	private void addTaskInQueue(Set<String> dataSet, BoilerplateList<String> subjectsForPerformingTask) {

		int count = 0;

		// fetch user against each userId present in Set
		for (String dataId : dataSet) {
			logger.logInfo("MySQLQueueWriterJob", "addTaskInQueue method",
					"inside for loop which is fetching each id from Ids Redis Set to be added in queue",
					"about to add id in queue, id is : " + dataId);
			try {
				// add the data id into queue for processing
				queueReaderJob.requestBackroundWorkItem(dataId, subjectsForPerformingTask, "MySQLQueueWriterJob",
						"addTaskInQueue", configurationManager.get("MYSQL_PUBLISH_QUEUE"));
				// this variable is incremented when element is added in queue
				// successfully
				count += 1;
			} catch (Exception ex) {
				// if queue fails then log the exception
				logger.logException("MySQLQueueWriterJob", "addTaskInQueue", "try-catch block of queue",
						ex.getMessage(), ex);
			}

		}

		logger.logInfo("MySQLQueueWriterJob", "addTaskInQueue method",
				"outside for loop after adding elements from redis set to queue",
				"no. of elements added in queue, count varible is : " + count + ", for subject : "
						+ subjectsForPerformingTask.get(0));
	}

}
