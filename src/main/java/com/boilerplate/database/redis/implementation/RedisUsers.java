package com.boilerplate.database.redis.implementation;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisScript;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.IdEntity;
import com.boilerplate.java.entities.Role;
import com.boilerplate.service.interfaces.IUserService;

import redis.clients.jedis.Jedis;

/**
 * This class is a redis implementation of user
 * 
 * @author gaurav.verma.icloud
 *
 */
public class RedisUsers extends BaseRedisDataAccessLayer implements IUser {

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the key used to store user mapping against experian emails
	 */
	public String OrganizationProductUserMapping = "ORGANIZATIONPRODUCT:USER:";

	/**
	 * This is a user mapping for storing against user key
	 */
	private static final String UserKey = "USER_KEY:";

	private static final String User = "USER:";
	/**
	 * This is the Redis User Set key which will be used to migrate user
	 */
	private static final String UserKeyForSet = "AKS_USER_MYSQL";

	/**
	 * holds the key for experian request unique key users
	 */
	private static final String ExperianRequestUniqueKey = "EXPERIAN_REQUEST_UNIQUE_KEY:";

	/**
	 * holds the key for saving otp
	 */
	private static final String OTPKey = "AKS_OTP:";

	/**
	 * @see create
	 */
	@Override
	public ExternalFacingUser create(ExternalFacingUser externalFacingUser) throws ConflictException {

		ExternalFacingReturnedUser user = new ExternalFacingReturnedUser(externalFacingUser);
		if (user.getRoles() == null) {
			user.setRoles(new BoilerplateList<Role>());
		}
		if (user.getRoles().isEmpty()) {

		} else {
			user.setRoleName(user.getRoles().get(0).getRoleName());
		}

		if (super.get(User + user.getUserId()) != null) {
			new ConflictException("User", "User with given id already exists", null);
		}

		// save user
		user.setId(user.getUserId());
		super.set(User + user.getUserId(), user);

		return user;
	}

	/**
	 * @see getUser
	 */
	@Override
	public ExternalFacingReturnedUser getUser(String userId, BoilerplateMap<String, Role> roleIdMap)
			throws NotFoundException {
		ExternalFacingReturnedUser user = super.get(User + userId, ExternalFacingReturnedUser.class);
		if (user == null)
			throw new NotFoundException("User", "No records found with this mobile", null);
		if (user.getUserStatus() == 0) {
			throw new NotFoundException("User", "User with Id = " + userId + "not found", null);
		}
		return user;
	}

	/**
	 * @see getUser
	 */
	@Override
	public boolean userExists(String userId) {
		Jedis jedis = null;

		try {
			jedis = super.getConnection();
			return jedis.exists(User + userId);
		} finally {
			if (jedis != null) {
				jedis.close();
			}
		}
	}

	/**
	 * @see deleteUser
	 */
	@Override
	public void deleteUser(ExternalFacingUser user) {
		super.delete(User + user.getUserId());

	}

	private static final String PASSWORD_ENCRYPTED = "PASSWORD ENCRYPTED";

	@Override
	public ExternalFacingReturnedUser update(ExternalFacingReturnedUser user) {
		// save user
		if (user.getPassword().toUpperCase().equals(PASSWORD_ENCRYPTED)) {
			ExternalFacingReturnedUser userFromDatabase = super.get(User + user.getUserId(),
					ExternalFacingReturnedUser.class);
			user.setPassword(userFromDatabase.getPassword());
		}
		super.set(User + user.getUserId(), user);
		// save mapping against experian request email key
		if (user.getExperianRequestUniqueKey() != null) {
			IdEntity idEntity = new IdEntity();
			idEntity.setId(user.getUserId());
			super.set(ExperianRequestUniqueKey + user.getExperianRequestUniqueKey().toUpperCase(), idEntity);
		}

		// update user in MySQLdatabase
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			this.addInRedisSet(user);
		}

		// save mapping against experian request email key

		return user;

	}

	/**
	 * @see IUser.saveUserReferUUID
	 */
	@Override
	public String getReferUser(String uuid) {
		// Save user's id and refer UUID in hash map
		return super.hget(configurationManager.get("AKS_UUID_USER_HASH_BASE_TAG"), uuid);
	}

	/**
	 * @see IUser.addInSadd
	 */
	@Override
	public void addInRedisSet(ExternalFacingUser user) {
		super.sadd(UserKeyForSet, user.getUserId());
	}

	/**
	 * @see IUser.addInSadd
	 */
	@Override
	public void addInRedisSet(String userId) {
		super.sadd(UserKeyForSet, userId);
	}

	/**
	 * @see IUser.fetchUserIdsFromRedisSet
	 */
	@Override
	public Set<String> fetchUserIdsFromRedisSet() {
		return super.smembers(UserKeyForSet);
	}

	/**
	 * @see IUser.deleteRedisUserIdSet
	 */
	@Override
	public void deleteItemFromRedisUserIdSet(String userId) {
		super.srem(UserKeyForSet, userId);
	}

	/**
	 * @see IUser.getAllUserKeys
	 */
	@Override
	public Set<String> getAllUserKeys() {
		return super.keys(User + "AKS:" + "*");
	}

	/**
	 * @see IUser.getUserIdByExperianRequestUniqueKey
	 */
	@Override
	public String getUserIdByExperianRequestUniqueKey(String experianRequestUniqueKey) {
		IdEntity idEntity = super.get(ExperianRequestUniqueKey + experianRequestUniqueKey.toUpperCase(),
				IdEntity.class);
		if (idEntity == null) {
			return null;
		} else {
			return idEntity.getId();
		}
	}

	/**
	 * @see IUser.saveUserOTP
	 */
	@Override
	public void saveUserOTP(ExternalFacingReturnedUser user, String otp) {
		super.set(OTPKey + user.getUserId(), otp, 60);
	}

	/**
	 * @see IUser.getUserOTP
	 */
	@Override
	public String getUserOTP(String mobileNumber) {
		return super.get(OTPKey + mobileNumber);
	}
}
