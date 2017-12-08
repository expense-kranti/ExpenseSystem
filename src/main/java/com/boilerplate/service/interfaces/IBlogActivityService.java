package com.boilerplate.service.interfaces;

import com.boilerplate.java.entities.BlogActivityEntity;

/**
 * This class provides the service to save user's blog activity
 * @author urvij
 *
 */
public interface IBlogActivityService {

	/**
	 * This method saves the activity in database by calling database layer method
	 * @param blogActivityEntity The blogActivityEntity containing the user's blog activity and action
	 */
	public void saveActivity(BlogActivityEntity blogActivityEntity);

}
