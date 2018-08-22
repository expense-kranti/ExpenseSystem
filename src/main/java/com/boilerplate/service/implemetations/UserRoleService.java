package com.boilerplate.service.implemetations;

import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.AssignApproverEntity;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.SaveRoleEntity;
import com.boilerplate.service.interfaces.IUserRoleService;

/**
 * This class implements IUserRoleService
 * 
 * @author ruchi
 *
 */
public class UserRoleService implements IUserRoleService {

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
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(UserRoleService.class);

	/**
	 * @see IUserRoleService.assignRoles
	 */
	@Override
	public void assignRoles(SaveRoleEntity saveRoleEntity) throws Exception {
		// validate entity
		saveRoleEntity.validate();
		// check if user exists or not
		if (mySqlUser.getUser(saveRoleEntity.getUserId()) == null)
			throw new NotFoundException("SaveRoleEntity", "User not found with id:" + saveRoleEntity.getUserId(), null);
		// save roles in mysql
		mySqlUser.saveUserRoles(saveRoleEntity);

	}

	/**
	 * @see IUserRoleService.assignApprover
	 */
	@Override
	public void assignApprover(AssignApproverEntity assignApproverEntity) throws Exception {
		// validate the assignApproverEntity
		assignApproverEntity.validate();
		// check that approver user id exists
		ExternalFacingUser approver = mySqlUser.getUserById(assignApproverEntity.getApproverUserId());
		if (approver == null)
			throw new NotFoundException("AssignApproverEntity", "Approver not found", null);
		// check that super approver user id exists
		ExternalFacingUser superApprover = mySqlUser.getUserById(assignApproverEntity.getSuperApprover());
		if (superApprover == null)
			throw new NotFoundException("AssignApproverEntity", "Super Approver not found", null);

		// for each user in list, fetch the user and set approver and
		// super-approver and save
		for (String userId : assignApproverEntity.getUsers()) {
			try {
				// check if user exists
				ExternalFacingUser user = mySqlUser.getUserById(userId);
				if (user == null)
					throw new NotFoundException("ExternalFacingUser", "User not found while assigning approver", null);
				// set approver id
				user.setApproverId(approver.getId());
				// set super approver id
				user.setSuperApproverId(superApprover.getId());
				mySqlUser.updateUser(user);
			} catch (NotFoundException ex) {
				// Log exception for not found user
				logger.logException("UserService", "assignApprover", "exceptionAssignApprover",
						"User not found while assigning approver, this is the user id :" + userId, ex);
			}
		}
	}

}
