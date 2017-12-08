package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity holds the user selected feature
 * @author urvij
 *
 */
public class FeedBackEntity extends BaseEntity implements Serializable {
	
	/**
	 * This is the user selected feature
	 */
	String userSelectedFeature;
	/**
	 * This is the user id of logged in user
	 */
	String userId;

	/**
	 * Gets the user id
	 * @return The user id of logged in user
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the user id
	 * @param userId The user id of logged in user 
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the user selected feature
	 * @return The user selected feature
	 */
	public String getUserSelectedFeature() {
		return userSelectedFeature;
	}

	/**
	 * Sets the user selected feature
	 * @param userSelectedFeature The user selected feature
	 */
	public void setUserSelectedFeature(String userSelectedFeature) {
		this.userSelectedFeature = userSelectedFeature;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
