package com.boilerplate.database.interfaces;

import com.boilerplate.java.entities.ModuleEntity;

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

}
