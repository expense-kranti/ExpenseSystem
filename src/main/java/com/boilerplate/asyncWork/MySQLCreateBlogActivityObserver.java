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
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.BlogActivityEntity;
import com.boilerplate.java.entities.ReferralContactEntity;;

public class MySQLCreateBlogActivityObserver extends MySQLBaseDataAccessLayer implements IAsyncWorkObserver {

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

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {

		// get the userfile from Redis data store and save it in MySQL data base
		saveOrUpdateBlogAcivityInMySQL((String) asyncWorkItem.getPayload());
	}

	/**
	 * This method is used to save or update the Blog Activity
	 * 
	 * @param blogActivityEntity
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
		for(Object blogActivityEntity : blogActivityEntityList){
			mySqlBlogActivity.mySqlSaveBlogActivity((BlogActivityEntity) blogActivityEntity);
		}
		
		// delete the redis key to empty the queue		
		blogActivityDataAccess.deleteItemFromRedisBlogActivitySet(blogActivityKey);

	}

	/**
	 * This method is used to create Blog Activity entity from Map
	 * @param blogActivityMap
	 * @param blogActivityKey
	 * @return
	 */
	private BoilerplateList<BlogActivityEntity> getBlogActivityListFromMap(Map<String, String> blogActivityMap,
			String blogActivityKey) {
		BoilerplateList<BlogActivityEntity> blogActivityEntityList = new BoilerplateList<BlogActivityEntity>();
		// split string to get UserId and Activity type to set in blog Activity
		String blogString[] = blogActivityKey.split(":");
		// create blog Activity entity from each map
		for (Map.Entry<String, String> entry : blogActivityMap.entrySet()) {
			
			BlogActivityEntity blogActivityEntity = new BlogActivityEntity();
			blogActivityEntity.setUserId(blogString[1] + ":" + blogString[2]);
			blogActivityEntity.setActivityType(blogString[0]);
			blogActivityEntity.setActivity(entry.getKey());
			blogActivityEntity.setAction(entry.getValue());
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
					((BlogActivityEntity) blogActivityEntity).setId(requestData.getId());
					((BlogActivityEntity) blogActivityEntity).setCreationDate(requestData.getCreationDate());
				}
			} else {
				// Set creation time to current time
				((BlogActivityEntity) blogActivityEntity).setCreationDate(Date.valueOf(LocalDate.now()));
			}

		}

		return blogActivityEntityList;
	}

}
