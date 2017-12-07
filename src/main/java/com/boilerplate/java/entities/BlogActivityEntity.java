package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
/**
 * This entity have information related to blog activity.
 * @author love
 *
 */
public class BlogActivityEntity extends BaseEntity implements Serializable {
	
	/**
	 * This is the user id
	 */
	private String userId;
	/**
	 * This is the blog activity
	 */
	private String activity;
	/**
	 * This is blog activity action
	 */
	private String action;
	
	/**
	 * This method get the user id
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}
	/**
	 * This method set the user id
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/**
	 * This method get the activity
	 * @return the activity
	 */
	public String getActivity() {
		return activity;
	}
	/**
	 * This method set the activity
	 * @param activity the activity to set
	 */
	public void setActivity(String activity) {
		this.activity = activity;
	}
	/**
	 * This method get the action
	 * @return the action
	 */
	public String getAction() {
		return action;
	}
	/**
	 * This method set the action
	 * @param action the action to set
	 */
	public void setAction(String action) {
		this.action = action;
	}
	/**
	 * @see BaseEntity.ValidationFailedException
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
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
