package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

public class UserRoleEntity extends BaseEntity {

	/**
	 * Default constructor
	 */
	public UserRoleEntity() {
		// do nothing constructor
	}

	/**
	 * Parameterized constructor
	 * 
	 * @param role
	 *            This is the role
	 * @param userId
	 *            This is the user id
	 */
	public UserRoleEntity(UserRoleType role, String userId) {
		super();
		this.role = role;
		this.userId = userId;
	}

	/**
	 * This is the role
	 */
	private UserRoleType role;

	/**
	 * this is the id of the user
	 */
	private String userId;

	/**
	 * This method is used to get role
	 * 
	 * @return
	 */
	public UserRoleType getRole() {
		return role;
	}

	/**
	 * This method is used to set role
	 * 
	 * @param role
	 */
	public void setRole(UserRoleType role) {
		this.role = role;
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
		if (this.getRole() == null)
			throw new ValidationFailedException("UserRoleEntity", "Role is null/empty", null);
		return false;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
