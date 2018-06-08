package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class maintains the details about the signup total count of the referred
 * users by the logged user for all referral mediums
 * 
 * @author urvij
 *
 */
public class UserReferredSignedUpUsersCountEntity extends BaseEntity implements Serializable {

	/**
	 * This is the userId of the logged in user whose refer sign up counts are
	 * to get
	 */
	private String userId;
	/**
	 * This is the logged in user's referred users signup count
	 */
	private int referredUsersTotalSignUpCount;
	/**
	 * This is the users current month refereduser signup count
	 */
	private int referredUsersCurrentMonthSignUpCount;

	/**
	 * Gets the userId
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets the userId
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets the referredUsersTotalSignUpCount
	 * 
	 * @return the referredUsersTotalSignUpCount
	 */
	public int getReferredUsersTotalSignUpCount() {
		return referredUsersTotalSignUpCount;
	}

	/**
	 * Sets the referredUsersTotalSignUpCount
	 * 
	 * @param referredUsersTotalSignUpCount
	 *            the referredUsersTotalSignUpCount to set
	 */
	public void setReferredUsersTotalSignUpCount(int referredUsersTotalSignUpCount) {
		this.referredUsersTotalSignUpCount = referredUsersTotalSignUpCount;
	}

	/**
	 * Gets the referredUsersCurrentMonthSignUpCount
	 * 
	 * @return the referredUsersCurrentMonthSignUpCount
	 */
	public int getReferredUsersCurrentMonthSignUpCount() {
		return referredUsersCurrentMonthSignUpCount;
	}

	/**
	 * Sets referredUsersCurrentMonthSignUpCount
	 * 
	 * @param referredUsersCurrentMonthSignUpCount
	 *            the referredUsersCurrentMonthSignUpCount to set
	 */
	public void setReferredUsersCurrentMonthSignUpCount(int referredUsersCurrentMonthSignUpCount) {
		this.referredUsersCurrentMonthSignUpCount = referredUsersCurrentMonthSignUpCount;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
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
