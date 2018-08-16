package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.UserEntity;

/**
 * This has interfaces for user management
 * 
 * @author gaurav
 *
 */
public interface IUser {

	// /**
	// * This method creates a user in the database.
	// *
	// * @param user
	// * This is the user to be created.
	// * @throws ConflictException
	// * This is thrown if the user already exists in the database for
	// * the given provider.
	// * @return The created user.
	// */
	// public ExternalFacingUser create(ExternalFacingUser user) throws
	// ConflictException;
	//
	// /**
	// * This method is used to get a user from database given the userId
	// *
	// * @param userId
	// * The id of the user
	// * @param roleIdMap
	// * The map of user roles with there id's
	// * @return an instance of the user.
	// * @throws NotFoundException
	// * if the user doesnt exist
	// */
	// public ExternalFacingReturnedUser getUser(String userId,
	// BoilerplateMap<String, Role> roleIdMap)
	// throws NotFoundException;
	//
	// /**
	// * This method deletes the given user
	// *
	// * @param user
	// * The user to be deleted
	// */
	// public void deleteUser(ExternalFacingUser user);
	//
	// public boolean userExists(String userId);
	//
	// public ExternalFacingReturnedUser update(ExternalFacingReturnedUser
	// user);
	//
	// public String getReferUser(String uuid);
	//
	// /**
	// * This method is used to add user in Mysql db
	// *
	// * @param user
	// * the user to add
	// */
	// public void addInRedisSet(ExternalFacingUser user);
	//
	// /**
	// * This method is used to fetch items from Redis set
	// *
	// * @param key
	// * the key against which the Set is to get
	// * @return the set of members/items
	// */
	// public Set<String> fetchUserIdsFromRedisSet();
	//
	// /**
	// * This method is used to delete the userId from Redis set
	// */
	// public void deleteItemFromRedisUserIdSet(String userId);
	//
	// /**
	// * This method is used to get all user keys of Redis Database
	// *
	// * @return the set of user keys
	// */
	// public Set<String> getAllUserKeys();
	//
	// /**
	// * This method is used to add user in Mysql db
	// *
	// * @param userId
	// * the userId to add in Redis set
	// */
	// public void addInRedisSet(String userId);
	//
	// /**
	// * This method is used to get the user for given experian request unique
	// key
	// *
	// * @param experianRequestUniqueKey
	// * key for getting user
	// * @return
	// */
	// public String getUserIdByExperianRequestUniqueKey(String
	// experianRequestUniqueKey);
	//
	// /**
	// * This method is used to save user's OTP sent to his mobile number for 1
	// * minute
	// *
	// * @param user
	// * This is the user for which OTP has to be saved
	// * @param otp
	// * This is the OTP generated and sent to the user
	// */
	// public void saveUserOTP(ExternalFacingReturnedUser user, String otp);
	//
	// public String getUserOTP(String mobileNumber);
	//
	// // Expense system
	// /**
	// * This method is used to save a new user in the database
	// *
	// * @param userEntity
	// * This is the user entity to be saved
	// * @return Saved user entity
	// * @throws Exception
	// * Throw this exception if any exception occurs while saving
	// * user
	// */
	public ExternalFacingUser createUser(ExternalFacingUser userEntity) throws Exception;

	/**
	 * This method is used to fetch user on the basis of mobile number or user
	 * id
	 * 
	 * @param mobile
	 *            This is the mobile number
	 * @param emailId
	 *            This is the email id
	 * @return User entity if found
	 * @throws BadRequestException
	 *             Throw this exception if user sends bad request
	 */
	public ExternalFacingUser getExistingUser(String mobile, String emailId) throws BadRequestException;

	/**
	 * This method is used to update an existing user in the system
	 * 
	 * @param userEntity
	 *            This is the updated user enityt to be saved
	 * @return Saved user entity
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             user
	 */
	public ExternalFacingUser updateUser(ExternalFacingUser userEntity) throws Exception;

	public ExternalFacingUser getUserById(String userId) throws BadRequestException;

}
