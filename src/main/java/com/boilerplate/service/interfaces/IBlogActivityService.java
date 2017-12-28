package com.boilerplate.service.interfaces;

import com.boilerplate.java.entities.BlogActivityEntity;

/**
 * This class provides the service to save user's blog activity
 * 
 * @author urvij
 *
 */
public interface IBlogActivityService {

	/**
	 * This method saves the activity in database by calling database layer
	 * method
	 * 
	 * @param blogActivityEntity
	 *            The blogActivityEntity containing the user's blog activity and
	 *            action
	 * @return
	 */
	public BlogActivityEntity saveActivity(BlogActivityEntity blogActivityEntity);

	/**
	 * This method is used to delete user's blog activity
	 * 
	 * @param userId
	 *            the user id of user whose blog activity is to be deleted
	 */
	void deleteUserBlogActivityService(String userId);

}
