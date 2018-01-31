package com.boilerplate.database.mysql.implementations;

import java.util.Set;

import javax.transaction.Transaction;
import javax.validation.ConstraintViolationException;

import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Role;
import com.boilerplate.sessions.Session;

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
	 * @see IUser.create
	 */
	@Override
	public ExternalFacingUser create(ExternalFacingUser user) throws ConflictException {
		org.hibernate.Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			org.hibernate.Transaction transaction = session.beginTransaction();
			session.saveOrUpdate(user);
			// commit the transaction
			transaction.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
			// } catch (ConstraintViolationException cve) {// error no
			// constraint
			// // violation exception class found
			// logger.logException("MySQLUsers", "create",
			// "ConstraintViolationException", cve.toString(), cve);
			// throw new ConflictException("User", cve.getCause().getMessage(),
			// null);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
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

}
