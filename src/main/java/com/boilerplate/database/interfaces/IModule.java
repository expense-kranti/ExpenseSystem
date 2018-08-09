package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.ModuleEntity;
import com.boilerplate.java.entities.ModuleQuizEntity;

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
	 */
	public ModuleEntity saveModule(ModuleEntity module);

	public ModuleEntity getModule(String id) throws BadRequestException;

	/**
	 * This method is used to save module quiz entity
	 * 
	 * @param moduleQuiz
	 *            the module quiz entity
	 * @return the save module quiz entity
	 * @throws Exception
	 *             thrown when any exception occurs while saving module quiz
	 */
	public ModuleQuizEntity saveModuleQuiz(ModuleQuizEntity moduleQuiz) throws Exception;

}
