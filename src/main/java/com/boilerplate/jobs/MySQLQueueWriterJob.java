package com.boilerplate.jobs;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.database.interfaces.IReferral;
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
	 * This is the userReferral key used to migrate redis data to mySql to 
	 * to save in redis.sadd()
	 */
	public static final String ReferalKeyForSet = "REFERALCONTACTS_MYSQL";
	
	/**
	 * This is the user file key used to migrate redis data to mySql to 
	 * to save in redis.sadd()
	 */
	private static final String UserFileKeyForSet = "USERFILE_MYSQL:";
	
	/**
	 * This is the userReferral key used to migrate redis data to mySql to 
	 * to save in redis.sadd()
	 */
	public static final String BlogUserKeyForSet = "BLOGUSER_MYSQL";

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
	
	@Autowired
	//private IBlogActivity blogDataAcess;
	
	private IFilePointer filePointer;
	
	private IBlogActivity blogActivityDataAccess;
	
	private IReferral referral;

	public void setReferral(IReferral referral) {
		this.referral = referral;
	}

	/**
	 * Sets the blogActivityDataAccess
	 * @param blogActivityDataAccess
	 */
	public void setBlogActivityDataAccess(IBlogActivity blogActivityDataAccess) {
		this.blogActivityDataAccess = blogActivityDataAccess;
	}

	/**
	 * Sets the filePointer
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
		
	BoilerplateList<String> subjectsForCreateUser = new BoilerplateList();
	BoilerplateList<String> subjectsForCreateBlogActivity = new BoilerplateList<>();
	BoilerplateList<String> subjectsForCreateUserFile = new BoilerplateList<>();
	BoilerplateList<String> subjectsForCreateReferalContact = new BoilerplateList<>();

	public void initialize() {
		// add subject for task like for assessment, referral, blog, file
		subjectsForCreateUser.add("CreateOrUpdateUserInMySQL");
		subjectsForCreateUserFile.add("CreateUserFileInMySQL");
		subjectsForCreateBlogActivity.add("CreateBlogActivityInMySQL");
		subjectsForCreateReferalContact.add("CreateReferalReferlContact");
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

			// fetch and process BlogActivity from Redis set for migrating user
			// from
			// redis to MySQL
			this.fetchBlogActivityAndAddInQueue();

			// fetch and process User uploaded File from Redis set for migrating
			// user from
			// redis to MySQL
			this.fetchUserFileAndAddInQueue();
			
			// fetch and process Referal Contcts from Redis set for migrating
			// Referal from
			// redis to MySQL
			this.fetchReferalContactAndAddInQueue();
		}

	}

	private void fetchReferalContactAndAddInQueue() throws NotFoundException {
		// TODO Auto-generated method stub
		// fetch the Referal from redis
		Set<String> elements = referral.fetchUserReferIdsFromRedisSet();
		if (!elements.isEmpty()) {
			// add the BlogUsers into queue from BlogActivity Set
			addTaskInQueue(elements, subjectsForCreateReferalContact);
		}

	}

	private void fetchUserFileAndAddInQueue() throws NotFoundException {
		// fetch the BlogActivity from redis
		Set<String> elements = filePointer.fetchUserFileAndAddInQueue();
		if (!elements.isEmpty()) {
			// add the BlogUsers into queue from BlogActivity Set
			addTaskInQueue(elements, subjectsForCreateUserFile);
		}

	}

	private void fetchBlogActivityAndAddInQueue() throws NotFoundException {
		// fetch the BlogActivity from redis
		Set<String> elements = blogActivityDataAccess.fetchBlogActivityAndAddInQueue();
		if (!elements.isEmpty()) {
			// add the BlogUsers into queue from BlogActivity Set
			addTaskInQueue(elements, subjectsForCreateBlogActivity);
		}
		
	}

	private void fetchUserIdsAndAddInQueue() throws NotFoundException {
		// fetch userIds Set from redis
		Set<String> elements = userDataAccess.fetchUserIdsFromRedisSet();
		// if set is empty then do nothing
		if (!elements.isEmpty()) {

			// add the userIds into queue from user set
			addTaskInQueue(elements, subjectsForCreateUser);			
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
						"addTaskInQueue", configurationManager.get("MYSQL_PUBLISH_QUEUE"));

			} catch (Exception ex) {
				// if queue fails then log the exception
				logger.logException("MySQLQueueWriterJob", "addTaskInQueue", "try-catch block of queue",
						ex.getMessage(), ex);
			}

		}
	}

}
