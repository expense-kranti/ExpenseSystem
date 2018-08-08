package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IModule;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.ModuleEntity;

/**
 * This class implements IModule interface
 * 
 * @author ruchi
 *
 */
public class MySQLModule extends MySQLBaseDataAccessLayer implements IModule {

	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(MySQLModule.class);

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This method is used to set the configurationManager
	 * 
	 * @param configurationManager
	 *            the configurationManager to set
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @see IModule.saveModule
	 */
	@Override
	public ModuleEntity saveModule(ModuleEntity module) {
		try {
			// Save module in MySQl database
			return super.update(module);
		} catch (Exception ex) {
			logger.logException("MySQLModule", "saveModule", "try -catch block calling super.update method ",
					"While trying to save module data into data base" + "This is the module name ~ " + module.getName(),
					ex);
			throw ex;
		}
	}

	/**
	 * @see IModule.getModule
	 */
	@Override
	public ModuleEntity getModule(String id) throws BadRequestException {
		// Get the SQL query from configurations to get module by id
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_MODULE_BY_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put question in query parameter
		queryParameterMap.put("ModuleId", id);
		// This variable is used to hold the query response
		List<ModuleEntity> modules = new ArrayList<>();
		try {
			// Execute query
			modules = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLModule", "saveModule", "try -catch block calling super.update method ",
					"While trying to save module data into data base" + "This is the module id ~ " + id
							+ "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLModule",
					"While trying to get module on the basis of id ~ " + ex.toString(), ex);
		}
		if (modules.size() == 0)
			return null;
		return modules.get(0);
	}

}
