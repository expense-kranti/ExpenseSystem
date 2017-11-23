package com.boilerplate.service.interfaces;

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

}
