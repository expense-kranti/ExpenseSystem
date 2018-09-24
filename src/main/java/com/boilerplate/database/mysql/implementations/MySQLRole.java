package com.boilerplate.database.mysql.implementations;

import java.util.List;

import com.boilerplate.database.interfaces.IRole;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.RoleEntity;

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

	@Override
	public RoleEntity getRoleById() {
		// TODO Auto-generated method stub
		return null;
	}

}
