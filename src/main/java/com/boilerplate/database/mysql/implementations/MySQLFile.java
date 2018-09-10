package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.FileMappingEntity;

/**
 * this class implements IFilePointer
 * 
 * @author ruchi
 *
 */
public class MySQLFile extends MySQLBaseDataAccessLayer implements IFilePointer {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(MySQLFile.class);

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
	 * @see IFilePointer.saveFileMapping
	 */
	@Override
	public void saveFileMapping(FileMappingEntity fileMappingEntity) throws Exception {
		try {
			super.create(fileMappingEntity);
		} catch (ConstraintViolationException cex) {
			logger.logException("MySQLFile", "saveFileMapping", "exeptionSaveFileMapping",
					"ConstraintViolationException occurred while saving file mapping", cex);
			throw cex;
		} catch (Exception ex) {
			logger.logException("MySQLFile", "saveFileMapping", "exeptionSaveFileMapping",
					"Exeption occurred while saving file mapping", ex);
			throw ex;
		}

	}

	/**
	 * @see IFilePointer.getFileMapping
	 */
	@Override
	public FileMappingEntity getFileMapping(String attachmentId) throws BadRequestException {
		// Get the SQL query from configurations to get file mapping
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_FILE_MAPPING");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("AttachmentId", attachmentId);
		// This variable is used to hold the query response
		List<FileMappingEntity> mappings = new ArrayList<>();
		try {
			// Execute query
			mappings = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLExpense", "getFileMapping", "exceptionGetFileMapping",
					"While trying to get file mapping data, This is the attachmentId~ " + attachmentId
							+ "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLExpense", "While trying to get file mapping data ~ " + ex.toString(),
					ex);
		}
		if (mappings.size() == 0)
			return null;
		return mappings.get(0);

	}

	/**
	 * @see IFilePointer.updateFileMapping
	 */
	@Override
	public void updateFileMapping(FileMappingEntity fileMappingEntity) {
		try {
			super.update(fileMappingEntity);
		} catch (Exception ex) {
			logger.logException("MySQLFile", "updateFileMapping", "exeptionUpdateFileMapping",
					"Exeption occurred while updating file mapping", ex);
			throw ex;
		}

	}

	/**
	 * @see IFilePointer.getFileMappingByExpenseId
	 */
	@Override
	public List<FileMappingEntity> getFileMappingByExpenseId(String expenseId) throws BadRequestException {
		// Get the SQL query from configurations to get file mapping by expense
		// id
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_FILE_MAPPING_BY_EXPENSE_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("ExpenseId", expenseId);
		// This variable is used to hold the query response
		List<FileMappingEntity> mappings = new ArrayList<>();
		try {
			// Execute query
			mappings = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLExpense", "getFileMappingByExpenseId", "exceptionGetFileMappingByExpenseId",
					"While trying to get file mapping data, This is the expenseId~ " + expenseId + "This is the query"
							+ hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLExpense", "While trying to get file mapping data ~ " + ex.toString(),
					ex);
		}
		return mappings;
	}

	/**
	 * @see IFilePointer.getFileMappingByAttachmentId
	 */
	@Override
	public FileMappingEntity getFileMappingByAttachmentId(String attachmentId) throws BadRequestException {
		// Get the SQL query from configurations to get file mapping by expense
		// id
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_FILE_MAPPING_BY_ATTACHMENT_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("AttachmentId", attachmentId);
		// This variable is used to hold the query response
		List<FileMappingEntity> mappings = new ArrayList<>();
		try {
			// Execute query
			mappings = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLExpense", "getFileMappingByExpenseId", "exceptionGetFileMappingByAttachmentId",
					"While trying to get file mapping data, This is the attachmentId~ " + attachmentId
							+ "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLExpense", "While trying to get file mapping data ~ " + ex.toString(),
					ex);
		}
		return mappings.get(0);
	}

}
