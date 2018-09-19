package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.FileDetailsEntity;
import com.boilerplate.java.entities.FileMappingEntity;
import com.boilerplate.java.entities.UserRoleEntity;
import com.boilerplate.java.entities.UserRoleType;

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
	public void saveFileMapping(List<FileMappingEntity> fileMappings) throws Exception {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			// for each file mapping
			for (FileMappingEntity fileMapping : fileMappings) {
				// save file mapping in MySQL
				session.saveOrUpdate(fileMapping);
			}
			// commit the transaction
			transaction.commit();
		} catch (Exception ex) {
			logger.logException("MySQLFile", "create", "try-catch block", ex.getMessage(), ex);
			session.getTransaction().rollback();
			throw ex;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * @see IFilePointer.getFileMapping
	 */
	@Override
	public FileMappingEntity getFileMapping(String fileId) throws BadRequestException {
		// Get the SQL query from configurations to get file mapping
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_FILE_MAPPING");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("FileId", fileId);
		// This variable is used to hold the query response
		List<FileMappingEntity> mappings = new ArrayList<>();
		try {
			// Execute query
			mappings = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLFile", "getFileMapping", "exceptionGetFileMapping",
					"While trying to get file mapping data, This is the file id~ " + fileId + "This is the query"
							+ hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLFile", "While trying to get file mapping data ~ " + ex.toString(), ex);
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
			logger.logException("MySQLFile", "getFileMappingByExpenseId", "exceptionGetFileMappingByExpenseId",
					"While trying to get file mapping data, This is the expenseId~ " + expenseId + "This is the query"
							+ hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLFile", "While trying to get file mapping data ~ " + ex.toString(), ex);
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
			logger.logException("MySQLFile", "getFileMappingByExpenseId", "exceptionGetFileMappingByAttachmentId",
					"While trying to get file mapping data, This is the attachmentId~ " + attachmentId
							+ "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLFile", "While trying to get file mapping data ~ " + ex.toString(), ex);
		}
		return mappings.get(0);
	}

	/**
	 * @see IFilePointer.saveFileDetails
	 */
	@Override
	public void saveFileDetails(FileDetailsEntity fileDetailsEntity) throws Exception {
		try {
			super.create(fileDetailsEntity);
		} catch (Exception ex) {
			logger.logException("MySQLFile", "saveFileDetails", "exeptionSaveFileDetails",
					"Exeption occurred while saving details of file uploaded", ex);
			throw ex;
		}
	}

	/**
	 * @see IFilePointer.getFileDetailsByAttachmentId
	 */
	@Override
	public FileDetailsEntity getFileDetailsByAttachmentId(String attachmentId) throws BadRequestException {
		// Get the SQL query from configurations to get file details by
		// attachment id
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_FILE_DETAILS_BY_ATTACHMENT_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("AttachmentId", attachmentId);
		// This variable is used to hold the query response
		List<FileDetailsEntity> fileDetails = new ArrayList<>();
		try {
			// Execute query
			fileDetails = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLFile", "getFileDetailsByAttachmentId", "exceptionGetFileDetailsByAttachmentId",
					"While trying to get file details data, This is the attachmentId~ " + attachmentId
							+ "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLFile", "While trying to get file details data ~ " + ex.toString(), ex);
		}
		if (fileDetails.size() == 0)
			return null;
		return fileDetails.get(0);

	}

}
