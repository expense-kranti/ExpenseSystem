package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class is used to contain data regarding user to manage a user like deleting a user etc.
 * @author urvij
 *
 */
public class ManageUserEntity extends BaseEntity implements Serializable{

	/**
	 * This is the user id of the user to be managed
	 */
	private String userId;
	
	/**
	 * Gets the userId
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the userId
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		if(this.isNullOrEmpty(this.getUserId()))
			throw new ValidationFailedException("ManageUserEntity", "UserId is null or empty", null);
		return true;
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
