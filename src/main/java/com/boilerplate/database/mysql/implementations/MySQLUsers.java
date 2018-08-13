package com.boilerplate.database.mysql.implementations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ModuleEntity;
import com.boilerplate.java.entities.Role;
import com.boilerplate.java.entities.UserEntity;

/**
 * This class is used to create user in MySQLdatabase
 * 
 * @author urvij
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
	 * @see IUser.create
	 */
	@Override
	public ExternalFacingUser create(ExternalFacingUser user) throws ConflictException {

		try {
			super.update(user);
		} catch (Exception ex) {
			logger.logException("MySQLUsers", "create", "try -catch block calling super.update method ",
					"UserId : " + user.getUserId() + " ~Cause : " + ex.getCause() + " ~Message : " + ex.getMessage(),
					ex);
			throw ex;
		}
		return user;

	}

	@Override
	public ExternalFacingReturnedUser getUser(String userId, BoilerplateMap<String, Role> roleIdMap)
			throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteUser(ExternalFacingUser user) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean userExists(String userId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ExternalFacingReturnedUser update(ExternalFacingReturnedUser user) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getReferUser(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addInRedisSet(ExternalFacingUser user) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> fetchUserIdsFromRedisSet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deleteItemFromRedisUserIdSet(String userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Set<String> getAllUserKeys() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addInRedisSet(String userId) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getUserIdByExperianRequestUniqueKey(String experianRequestUniqueKey) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveUserOTP(ExternalFacingReturnedUser user, String otp) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getUserOTP(String mobileNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	// expense system

	/**
	 * @see IUser.createUser
	 */
	@Override
	public UserEntity createUser(UserEntity userEntity) {
		try {
			// save user in MySQl database and return the entity
			return super.update(userEntity);
		} catch (Exception ex) {
			throw ex;
		}
	}

	/**
	 * @see IUser.getExistingUser
	 */
	@Override
	public UserEntity getExistingUser(String mobile, String emailId) throws BadRequestException {
		// Get the SQL query from configurations to get users
		String hSQLQuery = configurationManager.get("SQL_QUERY_FOR_GETTING_USERS_BY_MOBILE_OR_EMAIL_ID");
		// Make a new instance of BoilerplateMap ,used to define query
		// parameters
		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put id in query parameter
		queryParameterMap.put("Mobile", mobile);
		queryParameterMap.put("Email", emailId);
		// This variable is used to hold the query response
		List<UserEntity> users = new ArrayList<>();
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

}
