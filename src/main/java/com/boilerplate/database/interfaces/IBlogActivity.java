package com.boilerplate.database.interfaces;

import java.util.Map;
import java.util.Set;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.BlogActivityEntity;

/**
 * This class provides the method to save user's blog activity in to database
 * 
 * @author urvij
 *
 */
public interface IBlogActivity {

	/**
	 * This method is used to save the user's blog activity into database
	 * 
	 * @param blogActivityEntity
	 *            This contains the user blog activity and respective action
	 * @throws Exception 
	 */
	void saveActivity(BlogActivityEntity blogActivityEntity) throws Exception;

	/**
	 * This method is used to delete all user blog activities whose userId is
	 * given
	 * 
	 * @param userId
	 *            the userId of the user
	 */
	void deleteActivity(String userId);

	/**
	 * This method is used to get all the blog user activity keys
	 * 
	 * @param userId
	 *            the userId of the user
	 * @return the set of the user blog keys
	 */
	Set<String> getAllBlogUserKeys(String userId);
	
	/*
	 * Save Blog Activity to MySQL Database
	 */
	public void mySqlSaveBlogActivity(BlogActivityEntity BlogActivityEntity) throws Exception;

	/**
	 * this method is used to add key in redis databases
	 * @param blogActivity
	 */
	void addInRedisSet(BlogActivityEntity blogActivity);

	/**
	 * This method is used to fetch items from redis set
	 * 
	 * @param key
	 *            the key against which the Set is to get
	 * @return the set of members/items
	 */
	public Set<String> fetchBlogActivityAndAddInQueue();
	
	/**
	 * This method is used to delete BlogActivity key from set
	 * @param blogActivity
	 */
	void deleteItemFromRedisBlogActivitySet(String blogActivity);
	
	/**
	 * this method is used 
	 * @param blogActivity
	 * @return
	 */
	public BlogActivityEntity getBlogActivity(String blogActivity);

	/**
	 * This method is used to get all hmset
	 * @param payload
	 * @return 
	 */
	public Map<String, String> getBlogActivityMap(String payload);

}
