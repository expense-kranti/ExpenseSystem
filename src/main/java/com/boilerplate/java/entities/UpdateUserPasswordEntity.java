package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@ApiModel(value = "A User", description = "This is a user entity for password update", parent = BaseEntity.class)
public class UpdateUserPasswordEntity {

	@ApiModelProperty(value = "This is the password of the user.", required = true)

	/**
	 * This is the password.
	 */
	private String password;

	/**
	 * This method gets the password
	 * 
	 * @return The password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * This method sets the password
	 * 
	 * @param password
	 *            The password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Converts an entity to the password entity
	 * 
	 * @return The entity
	 * @throws ValidationFailedException thrown when password is null/empty
	 */
	public UpdateUserEntity convertToUpdateUserEntity() throws ValidationFailedException {
		UpdateUserEntity updateUserEntity = new UpdateUserEntity();
		updateUserEntity.setPassword(this.getPassword());
		updateUserEntity.validate();
		return updateUserEntity;
	}
}
