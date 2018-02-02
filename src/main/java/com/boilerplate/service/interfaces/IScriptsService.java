package com.boilerplate.service.interfaces;

import java.io.IOException;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;

public interface IScriptsService {

	/**
	 * @throws UnauthorizedException
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * 
	 */
	public void publishUserAndAssessmentReport() throws UnauthorizedException, NotFoundException, BadRequestException;

	/**
	 * This method is used to set user is password change flag
	 * 
	 * @throws UnauthorizedException
	 *             throw this exception in case user is not authorized for this
	 *             operation
	 * @throws NotFoundException
	 *             throw this exception if no data found
	 * @throws BadRequestException
	 *             throw this exception in case of request is not valid
	 */
	public void setUserChangePasswordStatus() throws UnauthorizedException, NotFoundException, BadRequestException;

	/**
	 * This method is used used to add job for publishing AKS or ReferReport
	 * 
	 * @throws UnauthorizedException
	 *             throw this exception in case user is not authorized for this
	 *             operation
	 * @throws NotFoundException
	 *             throw this exception if no data found
	 * @throws BadRequestException
	 *             throw this exception in case of request is not valid
	 */
	public void publishUserAKSOrReferReport() throws UnauthorizedException, NotFoundException, BadRequestException;

	/**
	 * This method is used to fetch the new score from csv file and updates the
	 * user's score (by fetching phoneNumber from csv and getting user from data
	 * store using userId) accordingly either add into or subtract from users
	 * obtained score part of Total Score
	 * 
	 * @param fileId
	 *            the file id of csv file containing the score points to add or
	 *            subtract
	 * @throws UnauthorizedException
	 *             thrown when logged in user is not authorized to update score
	 * @throws BadRequestException
	 *             thrown when userId is not provided for getting user from data
	 *             store
	 * @throws NotFoundException
	 *             thrown when user is not found with given user Id in the data
	 *             store
	 * @throws IOException
	 *             thrown when some IOexception occurs while getting file
	 */
	public void fetchScorePointsFromFileAndUpdateUserTotalScore(String fileId)
			throws UnauthorizedException, NotFoundException, BadRequestException, IOException;

}
