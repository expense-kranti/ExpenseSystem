package com.boilerplate.database.mysql.implementations;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.exception.ConstraintViolationException;

import com.boilerplate.database.interfaces.IRole;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.RoleEntity;
import com.boilerplate.java.entities.SaveRoleEntity;
import com.boilerplate.java.entities.UserRoleEntity;

/**
 * This class implements
 * 
 * @author ruchi
 *
 */
public class MySQLRole extends MySQLBaseDataAccessLayer implements IRole {

	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(MySQLRole.class);

	/**
	 * @see IRole.getRoleAllRoles
	 */
	@Override
	public List<RoleEntity> getAllRoles() {
		try {
			List<RoleEntity> allRoles = super.get(RoleEntity.class);
			return allRoles;
		} catch (Exception e) {
			// log the exception
			logger.logException("MySQLRole", "getRoleAllRoles", "exceptionGetRoleAllRoles",
					"Exception occurred while getting all te roles from database", e);
			throw e;
		}
	}

	/**
	 * @see IRole.saveUserRoles
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
			for (String role : userRoles.getRoleIds()) {
				// save role in MySQL
				UserRoleEntity userRole = new UserRoleEntity(role, userRoles.getUserId());
				session.saveOrUpdate(userRole);
			}
			// commit the transaction
			transaction.commit();
		} catch (ConstraintViolationException ex) {
			logger.logException("MySQLRole", "create", "try-catch block", ex.getMessage(), ex);
			session.getTransaction().rollback();
			throw new ValidationFailedException("UserRoleEntity",
					"ConstraintViolationException occurred. Might be due to the following reasons: Either th role ids are incorrect or user is trying assign duplicate roles, please check the list of role ids provided in the request",
					null);
		} catch (Exception e) {
			logger.logException("MySQLRole", "create", "try-catch block", e.getMessage(), e);
			throw new BadRequestException("MySQLUsers", "Some exception occurred while saving roles, please try again",
					null);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}

	}

	/**
	 * @see IRole.deleteRoles
	 */
	@Override
	public void deleteRoles(List<UserRoleEntity> userRoles) throws BadRequestException {
		Session session = null;
		try {
			// open a session
			session = HibernateUtility.getSessionFactory().openSession();
			// get all the configurations from the DB as a list
			Transaction transaction = session.beginTransaction();
			// for each roles
			for (UserRoleEntity role : userRoles) {
				// delete role from MySQL
				session.delete(role);
			}
			// commit the transaction
			transaction.commit();
		} catch (Exception e) {
			logger.logException("MySQLRole", "deleteRoles", "try-catch block", e.getMessage(), e);
			throw new BadRequestException("MySQLRole", "Some exception occurred while deleting roles, please try again",
					null);
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

}
