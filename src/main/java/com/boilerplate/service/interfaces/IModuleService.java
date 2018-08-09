package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ModuleEntity;
import com.boilerplate.java.entities.ModuleQuizEntity;

/**
 * This interface has methods for module CRUD operations
 * 
 * @author ruchi
 *
 */
public interface IModuleService {

	/**
	 * This method is used to create a new module
	 * 
	 * @param module
	 *            This is the new module entity to be saved
	 * @return Saved module entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation
	 */
	public ModuleEntity createModule(ModuleEntity module) throws BadRequestException, ValidationFailedException;

	public ModuleEntity updateModule(ModuleEntity module)
			throws ValidationFailedException, BadRequestException, NotFoundException;

	/**
	 * This method is used to create a new module quiz
	 * 
	 * @param moduleQuiz
	 *            This is the new module quiz entity to be saved
	 * @return Saved module quiz entity
	 * @throws BadRequestException
	 *             thrown when required data is not input
	 * @throws ValidationFailedException
	 *             thrown when user required data is not input
	 * @throws Exception
	 *             thrown when any exception occurs in saving module quiz
	 */
	public ModuleQuizEntity createModuleQuiz(ModuleQuizEntity moduleQuiz)
			throws BadRequestException, ValidationFailedException, Exception;

}
