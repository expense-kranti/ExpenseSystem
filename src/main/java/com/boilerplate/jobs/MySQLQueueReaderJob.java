package com.boilerplate.jobs;

import javax.validation.ConstraintViolationException;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.AsyncWorkItem;
import com.boilerplate.asyncWork.IAsyncWorkObserver;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.database.mysql.implementations.MySQLUsers;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.java.entities.ExternalFacingUser;

/**
 * This class is used to pop or read queue and process each element
 * 
 * @author urvij
 *
 */
public class MySQLQueueReaderJob implements IAsyncWorkObserver {

	/**
	 * This is the instance of IUser
	 */
	IUser mySqlUser;

	/**
	 * This method set the mysqluser
	 * 
	 * @param mySqlUser
	 *            the mySqlUser to set
	 */
	public void setMySqlUser(IUser mySqlUser) {
		this.mySqlUser = mySqlUser;
	}

	/**
	 * The autowired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;

	/**
	 * Sets the user data access
	 * 
	 * @param userDataAccess
	 *            the userDataAccess to set
	 */
	public void setUserDataAccess(IUser userDataAccess) {
		this.userDataAccess = userDataAccess;
	}

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		saveOrUpdateUserInMySQL((ExternalFacingUser) asyncWorkItem.getPayload());
	}

	/**
	 * This method is used to save user in MySQLdatabase
	 * 
	 * @param externalFacingUser
	 *            the user to save
	 * @throws ConflictException
	 *             thrown if the user already exists in the database for the
	 *             given provider.
	 */
	private void saveOrUpdateUserInMySQL(ExternalFacingUser externalFacingUser) throws ConflictException {

		// try {
		// add user to the mysql database
		mySqlUser.create(externalFacingUser);
		// } catch (Exception cev) {
		// userDataAccess.deleteItemFromRedisUserIdSet(externalFacingUser.getUserId());
		// }
		// after getting work done by using userId delete that user id from set
		userDataAccess.deleteItemFromRedisUserIdSet(externalFacingUser.getUserId());
	}

}
