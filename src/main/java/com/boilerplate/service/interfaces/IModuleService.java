package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ModuleEntity;

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

}
