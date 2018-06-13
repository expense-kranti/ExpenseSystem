package com.boilerplate.database.interfaces;

import com.boilerplate.java.entities.Handbook;

/**
 * This interface have methods to perform crud operations on handbook
 * 
 * @author urvij
 *
 */
public interface IHandbook {

	/**
	 * This method is used to save the handbook details of a user with given
	 * user id
	 * 
	 * @param handbook
	 *            contains the handbook details to save with userId of the user
	 * @throws Exception
	 *             thrown when any exception occurs in saving hand book
	 */
	public void saveHandbook(Handbook handbook) throws Exception;
}
