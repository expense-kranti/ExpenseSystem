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
	
}
