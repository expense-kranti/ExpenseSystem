package com.boilerplate.asyncWork;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.database.mysql.implementations.MySQLBaseDataAccessLayer;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.BlogActivityEntity;
import com.boilerplate.java.entities.ReferralContactEntity;;

/**
 * This class is used to pop or read queue and process each element
 * 
 * @author Yash
 *
 */
public class MySQLCreateBlogActivityObserver extends MySQLBaseDataAccessLayer implements IAsyncWorkObserver {

	/**
	 * This is the instance of logger MySQLCreateOrUpdateUserObserver logger
	 */
	private static Logger logger = Logger.getInstance(MySQLCreateOrUpdateUserObserver.class);

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This method is used to set the configurationManager
	 * 
	 * @param configurationManager
	 *            the configurationManager to set
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This variable provide the key for SQL query for configurations this SQL
	 * query is used to get the user article
	 */
	private static String sqlQueryGetUserBlogActivity = "SQL_QUERY_GET_USER_BLOG_ACTIVITY";

	/**
	 * This is the instance of BlogActivity
	 */
	IBlogActivity mySqlBlogActivity;

	/**
	 * set myBlogActivity
	 * 
	 * @param mySqlBlogActivity
	 */
	public void setMySqlBlogActivity(IBlogActivity mySqlBlogActivity) {
		this.mySqlBlogActivity = mySqlBlogActivity;
	}

	@Autowired
	IBlogActivity blogActivityDataAccess;

	/**
	 * Set blogActivityDataAccess
	 * 
	 * @param blogActivityDataAccess
	 */
	public void setBlogActivityDataAccess(IBlogActivity blogActivityDataAccess) {
		this.blogActivityDataAccess = blogActivityDataAccess;
	}

	/**
	 * This method get the user from Redis data store using supplied
	 * BlogActivity and save it in MySQL Database
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		// get the userfile from Redis data store and save it in MySQL data base
		saveOrUpdateBlogAcivityInMySQL((String) asyncWorkItem.getPayload());
	}

	/**
	 * This method is used to save or update the Blog Activity
	 * 
	 * @param blogActivityKey
	 *            This is blog activity key saved in redis
	 * @throws Exception
	 */
	private void saveOrUpdateBlogAcivityInMySQL(String blogActivityKey) throws Exception {

		BoilerplateList<BlogActivityEntity> blogActivityEntityList = new BoilerplateList<BlogActivityEntity>();
		// getBlogActivityMap
		Map<String, String> blogActivityMap = blogActivityDataAccess.getBlogActivityMap(blogActivityKey);
		// get blog Activity from Map
		blogActivityEntityList = getBlogActivityListFromMap(blogActivityMap, blogActivityKey);

		// get the already save row and if exist update the row
		blogActivityEntityList = getAlreadySavedBlogActivity(blogActivityEntityList);

		// save the blog activity in the MySQL table
		for (Object blogActivityEntity : blogActivityEntityList) {
			try {
				// add blog activity to the mysql database
				mySqlBlogActivity.saveActivity((BlogActivityEntity) blogActivityEntity);
			} catch (Exception ex) {
				logger.logException("MySQLCreateBlogActivityObserver", "saveActivity",
						"try-catch block calling save blog activity method", "Exception Message is : " + ex.getMessage()
								+ " and Exception cause is : " + ex.getCause().toString(),
						ex);
				// delete the redis key to empty the queue
				blogActivityDataAccess.deleteItemFromRedisBlogActivitySet(blogActivityKey);
				throw ex;
			}
		}

		// delete the redis key to empty the queue
		blogActivityDataAccess.deleteItemFromRedisBlogActivitySet(blogActivityKey);

	}

	/**
	 * This method is used to create Blog Activity entity from Map
	 * 
	 * @param blogActivityMap
	 *            this is the Blog activity map<Activity , Action>
	 * @param blogActivityKey
	 *            this is the redis queue key get grom redis
	 * @return list of Blog Activity Entity
	 */
	private BoilerplateList<BlogActivityEntity> getBlogActivityListFromMap(Map<String, String> blogActivityMap,
			String blogActivityKey) {
		BoilerplateList<BlogActivityEntity> blogActivityEntityList = new BoilerplateList<BlogActivityEntity>();
		// split string to get UserId and Activity type to set in blog Activity
		String blogString[] = blogActivityKey.split(":");
		// create blog Activity entity from each map
		for (Map.Entry<String, String> entry : blogActivityMap.entrySet()) {
			// create new instance of blog activity
			BlogActivityEntity blogActivityEntity = new BlogActivityEntity();
			// set user id
			blogActivityEntity.setUserId(blogString[1] + ":" + blogString[2]);
			// set activity type
			blogActivityEntity.setActivityType(blogString[0]);
			// set activity
			blogActivityEntity.setActivity(entry.getKey());
			// aet action
			blogActivityEntity.setAction(entry.getValue());
			// add blog activity instance in the list
			blogActivityEntityList.add(blogActivityEntity);
		}
		return blogActivityEntityList;
	}

	/**
	 * This method id used to get already saved blog activity and if exist
	 * update the bloh activity entity Other wise return the entity
	 * 
	 * @param blogActivityEntityList
	 * @return
	 */
	private BoilerplateList<BlogActivityEntity> getAlreadySavedBlogActivity(
			BoilerplateList<BlogActivityEntity> blogActivityEntityList) {

		// Get the SQL query from configurations for get the list of assessment
		String sqlQuery = configurationManager.get(sqlQueryGetUserBlogActivity);

		for (Object blogActivityEntity : blogActivityEntityList) {
			Map<String, Object> queryParameterMap = new HashMap<String, Object>();
			// Put userid, activity, activityType in query parameter
			queryParameterMap.put("userId", ((BlogActivityEntity) blogActivityEntity).getUserId());
			queryParameterMap.put("activity", ((BlogActivityEntity) blogActivityEntity).getActivity());
			queryParameterMap.put("activityType", ((BlogActivityEntity) blogActivityEntity).getActivityType());

			// Get the user articles from the data base
			List<BlogActivityEntity> requestedDataList = super.executeSelect(sqlQuery, queryParameterMap);

			// if exist update the row
			if (requestedDataList.size() > 0) {
				for (BlogActivityEntity requestData : requestedDataList) {
					// set id to the updated blog activity
					((BlogActivityEntity) blogActivityEntity).setId(requestData.getId());
				}
			}
		}

		return blogActivityEntityList;
	}

}
