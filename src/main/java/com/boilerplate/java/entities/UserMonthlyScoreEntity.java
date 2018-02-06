package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class contains the details about the monthly score of a user
 * 
 * @author urvij
 *
 */
public class UserMonthlyScoreEntity extends BaseEntity implements Serializable {

	/**
	 * This is the year of the monthly score
	 */
	private String year;
	/**
	 * This is the month of the monthly score
	 */
	private String month;
	/**
	 * This is the userId of the user whose monthly score it is
	 */
	private String userId;
	/**
	 * This is the monthly obtained score
	 */
	private String monthlyObtainedScore;
	/**
	 * This is the monthly refer score
	 */
	private String monthlyReferScore;
	/**
	 * This is the monthly rank
	 */
	private String monthlyRank;

	/**
	 * Gets year
	 * 
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * Sets year
	 * 
	 * @param year
	 *            the year to set
	 */
	public void setYear(String year) {
		this.year = year;
	}

	/**
	 * Gets month
	 * 
	 * @return the month
	 */
	public String getMonth() {
		return month;
	}

	/**
	 * Sets month
	 * 
	 * @param month
	 *            the month to set
	 */
	public void setMonth(String month) {
		this.month = month;
	}

	/**
	 * Gets userId
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * Sets userId
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * Gets monthlyObtainedScore
	 * 
	 * @return the monthlyObtainedScore
	 */
	public String getMonthlyObtainedScore() {
		return monthlyObtainedScore;
	}

	/**
	 * Sets monthlyObtainedScore
	 * 
	 * @param monthlyObtainedScore
	 *            the monthlyObtainedScore to set
	 */
	public void setMonthlyObtainedScore(String monthlyObtainedScore) {
		this.monthlyObtainedScore = monthlyObtainedScore;
	}

	/**
	 * Gets monthlyReferScore
	 * 
	 * @return the monthlyReferScore
	 */
	public String getMonthlyReferScore() {
		return monthlyReferScore;
	}

	/**
	 * Sets monthlyReferScore
	 * 
	 * @param monthlyReferScore
	 *            the monthlyReferScore to set
	 */
	public void setMonthlyReferScore(String monthlyReferScore) {
		this.monthlyReferScore = monthlyReferScore;
	}

	/**
	 * Gets monthlyRank
	 * 
	 * @return the monthlyRank
	 */
	public String getMonthlyRank() {
		return monthlyRank;
	}

	/**
	 * Sets monthlyRank
	 * 
	 * @param monthlyRank
	 *            the monthlyRank to set
	 */
	public void setMonthlyRank(String monthlyRank) {
		this.monthlyRank = monthlyRank;
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
