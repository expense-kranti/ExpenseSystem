package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModelProperty;

public class UserRoleEntity extends BaseEntity {

	/**
	 * Default constructor
	 */
	public UserRoleEntity() {
		// call the super constructor
		super();
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param role
	 *            This is the role
	 * @param userId
	 *            This is the user id
	 */
	public UserRoleEntity(String roleId, String userId) {
		super();
		this.roleId = roleId;
		this.userId = userId;
	}

	/**
	 * This is the role
	 */
	@ApiModelProperty(value = "This is the role id", required = true, notes = "This is the role")
	private String roleId;

	/**
	 * this is the id of the user
	 */
	@ApiModelProperty(value = "This is the userId", required = true, notes = "This is the userId")
	private String userId;

	/**
	 * This method is used to get role
	 * 
	 * @return
	 */
	public String getRoleId() {
		return roleId;
	}

	/**
	 * This method is used to set role
	 * 
	 * @param role
	 */
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	/**
	 * this method is used to get user id
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This method is used to set user id
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// check if user id and role is not null or empty
		if (this.isNullOrEmpty(this.getUserId()))
			throw new ValidationFailedException("UserRoleEntity", "User id is null/empty", null);
		// check if role is null or empty
		if (this.isNullOrEmpty(this.getRoleId()))
			throw new ValidationFailedException("UserRoleEntity", "Role id is null/empty", null);
		return false;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

}
