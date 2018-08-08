package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class defines the attributes of option entity to be used in module
 * questions ad scenarios
 * 
 * @author ruchi
 *
 */
public class ModuleOptionEntity extends BaseEntity {

	/**
	 * This is the id of the question to which this option belongs
	 */
	private String questionId;

	/**
	 * This is the option text
	 */
	private String text;

	/**
	 * These are the reward points
	 */
	private float rewardPoints;

	/**
	 * This is the credit score
	 */
	private float creditScore;

	/**
	 * This indicates whether this option is correct or not
	 */
	private boolean isCorrect;

	/**
	 * This method is used to get question id
	 * 
	 * @return
	 */
	public String getQuestionId() {
		return questionId;
	}

	/**
	 * This method is used to set question id
	 * 
	 * @return
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	/**
	 * This method is used to get text
	 * 
	 * @return
	 */
	public String getText() {
		return text;
	}

	/**
	 * This method is used to set text
	 * 
	 * @return
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * This method is used to get reward points
	 * 
	 * @return
	 */
	public float getRewardPoints() {
		return rewardPoints;
	}

	/**
	 * This method is used to set reward points
	 * 
	 * @return
	 */
	public void setRewardPoints(float rewardPoints) {
		this.rewardPoints = rewardPoints;
	}

	/**
	 * This method is used to get credit score
	 * 
	 * @return
	 */
	public float getCreditScore() {
		return creditScore;
	}

	/**
	 * This method is used to set credit score
	 * 
	 * @return
	 */
	public void setCreditScore(float creditScore) {
		this.creditScore = creditScore;
	}

	/**
	 * This method is used to get is correct
	 * 
	 * @return
	 */
	public boolean getIsCorrect() {
		return isCorrect;
	}

	/**
	 * This method is used to set is correct
	 * 
	 * @return
	 */
	public void setIsCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
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
