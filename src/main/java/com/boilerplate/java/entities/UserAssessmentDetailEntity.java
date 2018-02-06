package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity contains the user assessment details data
 * 
 * @author urvij
 *
 */
public class UserAssessmentDetailEntity extends BaseEntity implements Serializable {

	/**
	 * This is the user id of the user in context
	 */
	private String userId;
	/**
	 * This is the assessmentId the user attempted
	 */
	private int assessmentId;
	/**
	 * This is the sectionId
	 */
	private int sectionId;
	/**
	 * This is the questionId the user attempted
	 */
	private int questionId;
	/**
	 * This is the answerId of the answer the user gave for the attempted
	 * question
	 */
	private int answerId;
	/**
	 * This is the score the user obtained during assessment attempt
	 */
	private String obtainedScore;
	/**
	 * This is the total correct answers given by user in attempted assessment
	 */
	private int totalCorrectAnswer;
	/**
	 * This is the assessment status
	 */
	private String status;

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
	 * Gets the assessmentId
	 * 
	 * @return the assessmentId
	 */
	public int getAssessmentId() {
		return assessmentId;
	}

	/**
	 * Sets the assessmentId
	 * 
	 * @param assessmentId
	 *            the assessmentId to set
	 */
	public void setAssessmentId(int assessmentId) {
		this.assessmentId = assessmentId;
	}

	/**
	 * @return the sectionId
	 */
	public int getSectionId() {
		return sectionId;
	}

	/**
	 * @param sectionId
	 *            the sectionId to set
	 */
	public void setSectionId(int sectionId) {
		this.sectionId = sectionId;
	}

	/**
	 * Gets the questionId
	 * 
	 * @return the questionId
	 */
	public int getQuestionId() {
		return questionId;
	}

	/**
	 * Sets the questionId
	 * 
	 * @param questionId
	 *            the questionId to set
	 */
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}

	/**
	 * Gets the answerId
	 * 
	 * @return the answerId
	 */
	public int getAnswerId() {
		return answerId;
	}

	/**
	 * Sets the answerId
	 * 
	 * @param answerId
	 *            the answerId to set
	 */
	public void setAnswerId(int answerId) {
		this.answerId = answerId;
	}

	/**
	 * Gets the obtained score
	 * 
	 * @return the obtainedScore
	 */
	public String getObtainedScore() {
		return obtainedScore;
	}

	/**
	 * Sets the obtained score
	 * 
	 * @param obtainedScore
	 *            the obtainedScore to set
	 */
	public void setObtainedScore(String obtainedScore) {
		this.obtainedScore = obtainedScore;
	}

	/**
	 * Gets total correct answer
	 * 
	 * @return the totalCorrectAnswer
	 */
	public int getTotalCorrectAnswer() {
		return totalCorrectAnswer;
	}

	/**
	 * Sets the correct answer
	 * 
	 * @param totalCorrectAnswer
	 *            the totalCorrectAnswer to set
	 */
	public void setTotalCorrectAnswer(int totalCorrectAnswer) {
		this.totalCorrectAnswer = totalCorrectAnswer;
	}

	/**
	 * Gets the status
	 * 
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets the status
	 * 
	 * @param status
	 *            the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
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
