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
import com.boilerplate.java.entities.SubModuleEntity;

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
	public ModuleEntity saveModule(ModuleEntity module) throws Exception {
		try {
			// Save module in MySQl database
			return super.create(module);
		} catch (Exception ex) {
			logger.logException("MySQLModule", "saveModule", "exceptionSaveModule",
					"While trying to save module data into data base" + "This is the module name ~ " + module.getName(),
					ex);
			throw ex;
		}
	}

	/**
	 * @see IModule.updateModule
	 */
	@Override
	public ModuleEntity updateModule(ModuleEntity module) {
		try {
			// Save module in MySQl database
			return super.update(module);
		} catch (Exception ex) {
			logger.logException("MySQLModule", "updateModule", "exceptionUpdateModule",
					"While trying to update module data into data base" + "This is the module name ~ "
							+ module.getName(),
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
		// Put id in query parameter
		queryParameterMap.put("ModuleId", id);
		// This variable is used to hold the query response
		List<ModuleEntity> modules = new ArrayList<>();
		try {
			// Execute query
			modules = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLModule", "getModule", "exceptionGetModule",
					"While trying to get module data, This is the module id ~ " + id + "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLModule",
					"While trying to get module on the basis of id ~ " + ex.toString(), ex);
		}
		if (modules.size() == 0)
			return null;
		return modules.get(0);
	}

	/**
	 * @see IModule.saveSubModule
	 */
	@Override
	public SubModuleEntity saveSubModule(SubModuleEntity subModule) throws Exception {
		try {
			// Save sub module in MySQl database
			return super.create(subModule);
		} catch (Exception ex) {
			logger.logException("MySQLModule", "saveSubModule", "exceptionSaveSubModule",
					"While trying to save sub module into data base" + "This is the name ~ " + subModule.getName(), ex);
			throw ex;
		}
	}

	/**
	 * @see IModule.getSubModule
	 */
	@Override
	public SubModuleEntity getSubModule(String id) throws BadRequestException {
		// Get the SQL query from configurations to get sub module by id
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_SUB_MODULE_BY_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("SubModuleId", id);
		// This variable is used to hold the query response
		List<SubModuleEntity> subModules = new ArrayList<>();
		try {
			// Execute query
			subModules = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLModule", "getSubModule", "exceptionGetSubModule",
					"While trying to get sub module, This is the module id ~ " + id + "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLModule",
					"While trying to get sub module on the basis of id ~ " + ex.toString(), ex);
		}
		if (subModules.size() == 0)
			return null;
		return subModules.get(0);
	}

	/**
	 * @see IModule.getSubModule
	 */
	@Override
	public SubModuleEntity updateSubModule(SubModuleEntity subModule) {
		try {
			// update sub module in MySQl database
			return super.update(subModule);
		} catch (Exception ex) {
			logger.logException("MySQLModule", "updateSubModule", "exceptionUpdateSubModule",
					"While trying to update sub module data into data base" + "This is the sub module name ~ "
							+ subModule.getName(),
					ex);
			throw ex;
		}
	}

}
