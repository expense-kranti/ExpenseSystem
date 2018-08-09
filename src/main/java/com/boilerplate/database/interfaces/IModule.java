package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.ModuleEntity;
import com.boilerplate.java.entities.SubModuleEntity;

/**
 * This interface has classes for module CRUD operations in MySQL database
 * 
 * @author ruchi
 *
 */
public interface IModule {

	/**
	 * This method is used to save module entity in MySQL database
	 * 
	 * @param module
	 *            This is the new module entity
	 * @return this is the saved module entity
	 * @throws Exception
	 *             Throw if any exception occurs
	 */
	public ModuleEntity saveModule(ModuleEntity module) throws Exception;

	/**
	 * This method is used to update a module
	 * 
	 * @param module
	 *            This is the updated module entity to be saved
	 * @return Saved module entity
	 */
	public ModuleEntity updateModule(ModuleEntity module);

	/**
	 * This method is used to get module
	 * 
	 * @param id
	 *            This is the id of the module to be fetched
	 * @return Module entity if present
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 */
	public ModuleEntity getModule(String id) throws BadRequestException;

	/**
	 * This is method is used to save sub module
	 * 
	 * @param subModule
	 *            This is the sub module entity
	 * @return This is the saved sub module entity
	 * @throws Exception
	 *             Throw exception if exception occurs while saving data
	 */
	public SubModuleEntity saveSubModule(SubModuleEntity subModule) throws Exception;

	/**
	 * This method is used to get sub module
	 * 
	 * @param id
	 *            This is the id of the sub module to be fetched
	 * @return Sub Module entity if present
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 */
	public SubModuleEntity getSubModule(String id) throws BadRequestException;

	/**
	 * This method is used to update a sub module
	 * 
	 * @param subModule
	 *            This is the updated subModule entity to be saved
	 * @return Saved subModule entity
	 */
	public SubModuleEntity updateSubModule(SubModuleEntity module);

}
