package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ModuleEntity;
import com.boilerplate.java.entities.SubModuleEntity;

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
	 * @throws Exception
	 *             Throw if any exception ocurs
	 */
	public ModuleEntity createModule(ModuleEntity module)
			throws BadRequestException, ValidationFailedException, Exception;

	/**
	 * This method is used to update an existing module
	 * 
	 * @param module
	 *            This is the module entity to be updated
	 * @return This is the updated module entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation
	 * @throws NotFoundException
	 *             Throw this exception if module is not found or does not exist
	 */
	public ModuleEntity updateModule(ModuleEntity module)
			throws ValidationFailedException, BadRequestException, NotFoundException;

	/**
	 * This method is used to create a new subModule entity
	 * 
	 * @param subModule
	 *            This is the new sub module entity
	 * @return Saved module enityt
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation
	 * @throws Exception
	 *             Throw if any exception ocurs
	 */
	public SubModuleEntity createSubModule(SubModuleEntity subModule)
			throws BadRequestException, ValidationFailedException, Exception;

	/**
	 * This method is used to update an existing sub module
	 * 
	 * @param subModule
	 *            This is the sub module entity to be updated
	 * @return Updated sub module entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation
	 * @throws NotFoundException
	 *             Throw this exception if module is not found or does not exist
	 */
	public SubModuleEntity updateSubModule(SubModuleEntity subModule)
			throws ValidationFailedException, BadRequestException, NotFoundException;

}
