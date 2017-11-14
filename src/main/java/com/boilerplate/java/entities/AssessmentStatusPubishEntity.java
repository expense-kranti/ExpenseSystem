package com.boilerplate.java.entities;

import com.boilerplate.java.collections.BoilerplateList;

/**
 * This class is used to provide the information regading the user assessments
 * for publishing.
 * 
 * @author shiva
 *
 */
public class AssessmentStatusPubishEntity {

	/**
	 * This is the user Id
	 */
	private String userId;

	/**
	 * This is the user total score
	 */
	private String totalScore;

	/**
	 * This is the user monthly score
	 */
	private String monthlyScore;

	/**
	 * This is the user rank
	 */
	private String Rank;

	/**
	 * This is the user monthly rank
	 */
	private String monthlyRank;

	/**
	 * This is the user assessments
	 */
	private BoilerplateList<AssessmentEntity> assessments;

	/**
	 * This method get the user id
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This method set the user id
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method get the total score
	 * 
	 * @return the totalScore
	 */
	public String getTotalScore() {
		return totalScore;
	}

	/**
	 * This method set the total score
	 * 
	 * @param totalScore
	 *            the totalScore to set
	 */
	public void setTotalScore(String totalScore) {
		this.totalScore = totalScore;
	}

	/**
	 * This method get the monthly score
	 * 
	 * @return the monthlyScore
	 */
	public String getMonthlyScore() {
		return monthlyScore;
	}

	/**
	 * This method set the monthly score
	 * 
	 * @param monthlyScore
	 *            the monthlyScore to set
	 */
	public void setMonthlyScore(String monthlyScore) {
		this.monthlyScore = monthlyScore;
	}

	/**
	 * This method get the user rank
	 * 
	 * @return the rank
	 */
	public String getRank() {
		return Rank;
	}

	/**
	 * This method set the user rank
	 * 
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(String rank) {
		Rank = rank;
	}

	/**
	 * This method get the user monthly rank
	 * 
	 * @return the monthlyRank
	 */
	public String getMonthlyRank() {
		return monthlyRank;
	}

	/**
	 * This method set the user monthly rank
	 * 
	 * @param monthlyRank
	 *            the monthlyRank to set
	 */
	public void setMonthlyRank(String monthlyRank) {
		this.monthlyRank = monthlyRank;
	}

	/**
	 * This method get the user assessments
	 * 
	 * @return the assessments
	 */
	public BoilerplateList<AssessmentEntity> getAssessments() {
		return assessments;
	}

	/**
	 * This method set the user assessments
	 * 
	 * @param assessments
	 *            the assessments to set
	 */
	public void setAssessments(BoilerplateList<AssessmentEntity> assessments) {
		this.assessments = assessments;
	}
}
