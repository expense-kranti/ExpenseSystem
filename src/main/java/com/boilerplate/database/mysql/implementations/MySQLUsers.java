package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.SaveRoleEntity;
import com.boilerplate.java.entities.UserRoleEntity;
import com.boilerplate.java.entities.UserRoleType;

/**
 * This class is used to create user in MySQLdatabase
 * 
 * @author ruchi
 *
 */
public class MySQLUsers extends MySQLBaseDataAccessLayer implements IUser {
	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(MySQLUsers.class);

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
	 * @see IUser.createUser
	 */
	@Override
	public ExternalFacingUser createUser(ExternalFacingUser userEntity) throws Exception {
		try {
			// save user in MySQl database and return the entity
			return super.create(userEntity);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLUsers", "createUser", "exceptionCreateUser",
					"While trying to save user data, This is the id~ " + userEntity.getId(), ex);
			throw ex;
		}
	}

	/**
	 * @see IUser.getExistingUser
	 */
	@Override
	public ExternalFacingUser getExistingUser(String mobile, String emailId) throws BadRequestException {
		// Get the SQL query from configurations to get users
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_USERS_BY_MOBILE_OR_EMAIL_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("Mobile", mobile);
		queryParameterMap.put("Email", emailId);
		// This variable is used to hold the query response
		List<ExternalFacingUser> users = new ArrayList<>();
		try {
			// Execute query
			users = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLUsers", "getExistingUser", "exceptionGetExistingUser",
					"While trying to get user data, This is the mobile~ " + mobile + "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLUsers", "While trying to get user data ~ " + ex.toString(), ex);
		}
		if (users.size() == 0)
			return null;
		return users.get(0);
	}

	/**
	 * @see IUser.updateUser
	 */
	@Override
	public ExternalFacingUser updateUser(ExternalFacingUser userEntity) throws Exception {
		try {
			// save user in MySQl database and return the entity
			return super.update(userEntity);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLUsers", "updateUser", "exceptionUpdateUser",
					"While trying to update user data, This is the id~ " + userEntity.getId(), ex);
			throw ex;
		}
	}

	@Override
	public ExternalFacingUser getUserById(String userId) throws BadRequestException {
		// Get the SQL query from configurations to get users
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_USERS_BY_USER_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("UserId", userId);
		// This variable is used to hold the query response
		List<ExternalFacingUser> users = new ArrayList<>();
		try {
			// Execute query
			users = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLUsers", "getUserById", "exceptionGetUserById",
					"While trying to get user data, This is the user id~ " + userId + "This is the query" + hSQLQuery,
					ex);
			// Throw exception
			throw new BadRequestException("MySQLUsers", "While trying to get user data ~ " + ex.toString(), ex);
		}
		if (users.size() == 0)
			return null;
		return users.get(0);
	}

	/**
	 * @see IUser.getUser
	 */
	@Override
	public ExternalFacingUser getUser(String id) throws BadRequestException {
		// Get the SQL query from configurations to get users
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_USERS_BY_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("Id", id);
		// This variable is used to hold the query response
		List<ExternalFacingUser> users = new ArrayList<>();
		try {
			// Execute query
			users = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLUsers", "getUser", "exceptionGetUser",
					"While trying to get user data, This is the id~ " + id + "This is the query" + hSQLQuery, ex);
			// Throw exception
			throw new BadRequestException("MySQLUsers", "While trying to get user data ~ " + ex.toString(), ex);
		}
		if (users.size() == 0)
			return null;
		return users.get(0);
	}

	/**
	 * @see IUser.saveUserRoles
	 */
	@Override
	public void saveUserRoles(SaveRoleEntity userRoles) throws Exception {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			// for each roles
			for (UserRoleType role : userRoles.getRoles()) {
				// save role in MySQL
				UserRoleEntity userRole = new UserRoleEntity(role, userRoles.getUserId());
				session.saveOrUpdate(userRole);
			}
			// commit the transaction
			transaction.commit();
		} catch (Exception ex) {
			logger.logException("MySQLBaseDataAccessLayer", "create", "try-catch block", ex.getMessage(), ex);
			session.getTransaction().rollback();
			throw ex;
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * @see IUser.getUserRoles
	 */
	@Override
	public List<UserRoleEntity> getUserRoles(String userId) throws BadRequestException {
		// Get the SQL query from configurations to get user roles
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_USER_ROLES_BY_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("UserId", userId);
		// This variable is used to hold the query response
		List<UserRoleEntity> userRoles = new ArrayList<>();
		try {
			// Execute query
			userRoles = super.executeSelect(hSQLQuery, queryParameterMap);
		} catch (Exception ex) {
			// Log exception
			logger.logException("MySQLUsers", "getUserRoles", "exceptionGetUserRoles",
					"While trying to get user roles, This is the id~ " + userId + "This is the query" + hSQLQuery, ex);
			// Throw exception
			throw new BadRequestException("MySQLUsers", "While trying to get user roles ~ " + ex.toString(), ex);
		}
		if (userRoles.size() == 0)
			return null;
		return userRoles;

	}

}
