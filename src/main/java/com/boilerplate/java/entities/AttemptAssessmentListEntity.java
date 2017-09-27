package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;

public class AttemptAssessmentListEntity extends BaseEntity implements Serializable {

	private BoilerplateList<AssessmentEntity> assessmentList = new BoilerplateList<>();

	/**
	 * @return the attemptAssessmentList
	 */
	public BoilerplateList<AssessmentEntity> getAttemptAssessmentList() {
		return assessmentList;
	}

	/**
	 * @param attemptAssessmentList the attemptAssessmentList to set
	 */
	public void setAttemptAssessmentList(BoilerplateList<AssessmentEntity> assessmentList) {
		this.assessmentList = assessmentList;
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

	private String userId;
}
