package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

public class MCQQuestionOptionEntity extends BaseEntity{

	/**
	 * This is the question id
	 */
	private String questionId;
	
	/**
	 * This is the order id
	 */
	private String orderId;
	
	/**
	 * This is the text of option
	 */
	private String text;
	
	/**
	 * This is the flag for option shows is the option is correct or not
	 */
	private Boolean isCorrect;
	
	
	/**
	 * This method is used to get the question id
	 * @return the questionId
	 */
	public String getQuestionId() {
		return questionId;
	}

	/**
	 * This method is used to set the question id
	 * @param questionId the questionId to set
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	/**
	 * This method is used to get the order id
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * This method is used to set the order id
	 * @param orderId the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	/**
	 * This method is used to get the text
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * This method is used to set the text
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * This method is used to get the question is correct or not
	 * @return the isCorrect
	 */
	public Boolean getIsCorrect() {
		return isCorrect;
	}

	/**
	 * This method is used to set the question is correct or not
	 * @param isCorrect the isCorrect to set
	 */
	public void setIsCorrect(Boolean isCorrect) {
		this.isCorrect = isCorrect;
	}

	/**
	 * @see BaseEntity.ValidationFailedException
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
