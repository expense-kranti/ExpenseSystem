package com.boilerplate.database.interfaces;

import java.util.Set;

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
	 */
	void saveActivity(BlogActivityEntity blogActivityEntity);

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

}
