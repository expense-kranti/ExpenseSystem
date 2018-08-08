package com.boilerplate.database.mysql.implementations;

import com.boilerplate.database.interfaces.IModule;
import com.boilerplate.java.entities.ModuleEntity;

/**
 * This class implements IModule interface
 * 
 * @author ruchi
 *
 */
public class MySQLModule extends MySQLBaseDataAccessLayer implements IModule {

	/**
	 * @see IModule.saveModule
	 */
	@Override
	public ModuleEntity saveModule(ModuleEntity module) {
		// Save module in MySQl database
		return super.update(module);
	}

}
