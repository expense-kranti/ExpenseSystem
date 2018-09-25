package com.boilerplate.service.implemetations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRole;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.AssignApproverEntity;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.SaveRoleEntity;
import com.boilerplate.java.entities.UserRoleEntity;
import com.boilerplate.java.entities.UserRoleType;
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
	@Autowired
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
	 * This is the autowired instance of Irole
	 */
	@Autowired
	IRole mySqlRole;

	/**
	 * This method is used to set the autowired instance of IRole
	 * 
	 * @param mySqlRole
	 */
	public void setMySqlRole(IRole mySqlRole) {
		this.mySqlRole = mySqlRole;
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
		ExternalFacingUser user = mySqlUser.getUser(saveRoleEntity.getUserId());
		// check if user exists or not
		if (user == null || !user.getIsActive())
			throw new NotFoundException("ExternalFacingUser", "User not found or is inactive", null);
		// save roles in mysql
		mySqlRole.saveUserRoles(saveRoleEntity);

	}

	/**
	 * @see IUserRoleService.assignApprover
	 */
	@Override
	public void assignApprover(AssignApproverEntity assignApproverEntity) throws Exception {
		// validate the assignApproverEntity
		assignApproverEntity.validate();
		// check that approver user id exists
		ExternalFacingUser approver = mySqlUser.getUser(assignApproverEntity.getApproverUserId());
		if (approver == null)
			throw new NotFoundException("AssignApproverEntity", "Approver not found", null);
		// create a role list for user
		List<UserRoleType> roles = new ArrayList<>();
		// for each role entity fetched, add it in role list
		// for (UserRoleEntity userRoleEntity : approver.getRoles()) {
		// roles.add(userRoleEntity.getRole());
		// }
		// // check if approver id has approver access or not
		// if (!roles.contains(UserRoleType.Approver))
		// throw new BadRequestException("AssignApproverEntity", "User Id of
		// approver does not have sufficient rights",
		// null);
		List<ExternalFacingUser> users = new ArrayList<>();
		// fetch all the users in a list
		for (String userId : assignApproverEntity.getUsers()) {
			// check if user exists
			ExternalFacingUser user = mySqlUser.getUser(userId);
			if (user == null)
				throw new NotFoundException("ExternalFacingUser", "User not found while assigning approver", null);
			users.add(user);
		}

		// for each user in list, fetch the user and set approver and
		// super-approver and save
		for (ExternalFacingUser userEntity : users) {
			// set approver id
			userEntity.setApproverId(approver.getId());
			mySqlUser.updateUser(userEntity);

		}
	}

	@Override
	public void deleteRoles(SaveRoleEntity saveRoleEntity)
			throws ValidationFailedException, BadRequestException, NotFoundException {
		// validate entity
		saveRoleEntity.validate();
		ExternalFacingUser user = mySqlUser.getUser(saveRoleEntity.getUserId());
		// check if user exists or not
		if (user == null || !user.getIsActive())
			throw new NotFoundException("ExternalFacingUser", "User not found or is inactive", null);
		// check if user has some roles
		if (user.getRoles() == null || user.getRoles().size() == 0)
			throw new BadRequestException("ExternalFacingUser", "User does not have any roles to be deleted", null);
		List<UserRoleEntity> rolesToDelete = new ArrayList<>();
		// list of role ids of user
		List<String> roleIds = new ArrayList<>();
		for (UserRoleEntity userRoleEntity : user.getRoles()) {
			roleIds.add(userRoleEntity.getRoleId());
			if (saveRoleEntity.getRoleIds().contains(userRoleEntity.getRoleId()))
				rolesToDelete.add(userRoleEntity);
		}
		// check if role mapping to be deleted exists or not
		for (String roleId : saveRoleEntity.getRoleIds()) {
			if (!roleIds.contains(roleId))
				throw new BadRequestException("UserRoleEntity",
						"Role id :" + roleId + " does not exist for the given user", null);
		}
		// delete the roles
		mySqlRole.deleteRoles(rolesToDelete);
	}

}
