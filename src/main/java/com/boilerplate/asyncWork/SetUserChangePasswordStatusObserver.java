package com.boilerplate.asyncWork;

import java.util.Arrays;
import java.util.Set;

import com.boilerplate.database.interfaces.IRedisScript;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateSet;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;

/**
 * This observer is used to set all user's change password status to true
 * 
 * @author shiva
 *
 */
public class SetUserChangePasswordStatusObserver implements IAsyncWorkObserver {

	/**
	 * PublishUserDataObserver logger
	 */
	private static Logger logger = Logger.getInstance(PublishUserDataObserver.class);

	/**
	 * This is the new instance of redis script
	 */
	IRedisScript redisScript;

	/**
	 * This method set the redis Script
	 * 
	 * @param redisScript
	 *            the redisScript to set
	 */
	public void setRedisScript(IRedisScript redisScript) {
		this.redisScript = redisScript;
	}

	/**
	 * This is new instance of user data access
	 */
	IUser userDataAccess;

	/**
	 * This method is used to set the user data access
	 * 
	 * @param userDataAccess
	 *            the userDataAccess to set
	 */
	public void setUserDataAccess(IUser userDataAccess) {
		this.userDataAccess = userDataAccess;
	}

	/**
	 * This is the list of default users
	 */
	private static final BoilerplateSet<String> defaultUsersSet = new BoilerplateSet<>(
			Arrays.asList("USER:AKS:BACKGROUND", "USER:AKS:ANNONYMOUS", "USER:AKS:ROLEASSIGNER", "USER:AKS:ADMIN"));

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		Set<String> listOfUserKey = redisScript.getAllUserKeys();
		// Run for loop to process each user
		for (String userId : listOfUserKey) {
			try {
				if (defaultUsersSet.contains(userId)) {
					continue;
				}
				// Get user details
				ExternalFacingReturnedUser user = userDataAccess.getUser(userId.replace("USER:", ""), null);
				// Check is user details not null
				if (user != null && !(user.getIsPasswordChanged())) {
					// Set user is password change status to true
					user.setIsPasswordChanged(true);
					// Update user
					userDataAccess.update(user);
				}
			} catch (Exception ex) {
				logger.logException("SetUserChangePasswordStatusObserver", "observe",
						"Update user is password change status to 'true'", "", ex);

			}
		}
	}
}
