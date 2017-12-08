package com.boilerplate.database.interfaces;

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

}
