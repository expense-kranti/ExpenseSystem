package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.Handbook;

/**
 * This interface has methods that provide services for operation on user's
 * Handbook data like publishing
 * 
 * @author urvij
 *
 */
public interface IHandbookService {

	/**
	 * This method is used to publish handbook data of logged in user
	 * 
	 * @param handbook
	 *            the hand book entity containing handbook data
	 * @return the handbook entity containing logged in user's userId
	 * @throws ValidationFailedException
	 *             thrown if required input are not filled
	 * @throws Exception
	 *             thrown when any exception occurs
	 */
	public Handbook publishHandBook(Handbook handbook) throws ValidationFailedException, Exception;

}
