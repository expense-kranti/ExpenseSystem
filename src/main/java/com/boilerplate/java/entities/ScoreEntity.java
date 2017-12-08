package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.Comparator;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class define the total score detail
 * 
 * @author shiva
 *
 */
public class ScoreEntity extends BaseEntity implements Serializable {

	/**
	 * This is the user id
	 */
	private String userId;

	/**
	 * This is the max score
	 */
	private String maxScore;

	/**
	 * This is the user total obtained score
	 */
	private String obtainedScore;

	/**
	 * This is the assessment id
	 */
	private String assessmentId;

	/**
	 * This is the user refer score
	 */
	private String referScore;

	/**
	 * This method is used to get the user id
	 * 
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This method is used to set the user id
	 * 
	 * @param userId
	 *            the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method is used to get the max score
	 * 
	 * @return the maxScore
	 */
	public String getMaxScore() {
		return maxScore;
	}

	/**
	 * This method is used to set the max score
	 * 
	 * @param maxScore
	 *            the maxScore to set
	 */
	public void setMaxScore(String maxScore) {
		this.maxScore = maxScore;
	}

	/**
	 * This method is used to get the user obtained score
	 * 
	 * @return the obtainedScore
	 */
	public String getObtainedScore() {
		return obtainedScore;
	}

	/**
	 * This method is used to set the user obtained score
	 * 
	 * @param obtainedScore
	 *            the obtainedScore to set
	 */
	public void setObtainedScore(String obtainedScore) {
		this.obtainedScore = obtainedScore;
	}

	/**
	 * This method is used to get the assessment id
	 * 
	 * @return the assessmentId
	 */
	public String getAssessmentId() {
		return assessmentId;
	}

	/**
	 * This method is used to set the assessment id
	 * 
	 * @param assessmentId
	 *            the assessmentId to set
	 */
	public void setAssessmentId(String assessmentId) {
		this.assessmentId = assessmentId;
	}

	/**
	 * @see BaseEntity.validate
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
		return null;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return null;
	}

	/**
	 * This method get the rank
	 * 
	 * @return the rank
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * This method set the rank
	 * 
	 * @param rank
	 *            the rank to set
	 */
	public void setRank(String rank) {
		this.rank = rank;
	}

	/**
	 * This method is used to get the refer score
	 * 
	 * @return the referScore
	 */
	public String getReferScore() {
		return referScore;
	}

	/**
	 * This method is used to set the refer score
	 * 
	 * @param referScore
	 *            the referScore to set
	 */
	public void setReferScore(String referScore) {
		this.referScore = referScore;
	}

	/**
	 * This is the rank of user
	 */
	private String rank;
	
	public static Comparator<ScoreEntity> COMPARATOR = new Comparator<ScoreEntity>() {
		// This is where the sorting happens.
		public int compare(ScoreEntity o1, ScoreEntity o2) {
			return (int) ((Float.valueOf(o1.getObtainedScore()) + Float.valueOf(o1.getReferScore()))
					- (Float.valueOf(o2.getObtainedScore()) + Float.valueOf(o2.getReferScore())));
		}
	};
}
