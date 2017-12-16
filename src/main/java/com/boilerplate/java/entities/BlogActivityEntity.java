package com.boilerplate.java.entities;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.Base;
/**
 * This entity have information related to blog activity.
 * @author love
 *
 */
public class BlogActivityEntity extends BaseEntity implements Serializable,ICRMPublishDynamicURl,ICRMPublishEntity {
	
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
	 * This is the activity type like share,like etc.
	 */
	private String activityType;
	
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
	 * Gets the activity type
	 * @return the activityType
	 */
	public String getActivityType() {
		return activityType;
	}
	/**
	 * Sets the activity type
	 * @param activityType the activityType to set
	 */
	public void setActivityType(String activityType) {
		this.activityType = activityType;
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
	@Override
	public String createPublishJSON(String template)
			throws UnauthorizedException {
		String retrunValue = template;
		retrunValue = retrunValue.replace("@action", this.getAction() == null ? "" : this.getAction());
		retrunValue = retrunValue.replace("@activity",
				this.getActivity() == null ? "" :this.getActivity());
		retrunValue = retrunValue.replace("@userId", this.getUserId() == null ? "" : this.getUserId());
		retrunValue = retrunValue.replace("@actType",
				this.getActivityType() == null ? "" : this.getActivityType());
		return retrunValue;
	}
	/**
	 * @see ICRMPublishDynamicURl.createPublishUrl
	 */
	@Override
	public String createPublishUrl(String url)
			throws UnsupportedEncodingException {
		// TODO Auto-generated method stub
		return null;
	}

}
