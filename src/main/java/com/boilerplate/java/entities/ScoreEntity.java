package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

public class ScoreEntity extends BaseEntity implements Serializable {
	
	private String userId;
	
	private String maxScore;
	
	private String obtainedScore;
	
	private String assessmentId;

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the maxScore
	 */
	public String getMaxScore() {
		return maxScore;
	}

	/**
	 * @param maxScore the maxScore to set
	 */
	public void setMaxScore(String maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * @return the obtainedScore
	 */
	public String getObtainedScore() {
		return obtainedScore;
	}

	/**
	 * @param obtainedScore the obtainedScore to set
	 */
	public void setObtainedScore(String obtainedScore) {
		this.obtainedScore = obtainedScore;
	}

	/**
	 * @return the assessmentId
	 */
	public String getAssessmentId() {
		return assessmentId;
	}

	/**
	 * @param assessmentId the assessmentId to set
	 */
	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}

	@Override
	public BaseEntity transformToInternal() {
		return null;
	}

	@Override
	public BaseEntity transformToExternal() {
		return null;
	}
	
	

}
