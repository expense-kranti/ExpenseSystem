package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class is provide the information reading the assessment question section
 * 
 * @author shiva
 *
 */
public class AssessmentQuestionSectionEntity extends BaseEntity implements Serializable {

	/**
	 * This is the section id
	 */
	private AssessmentSectionEntity section;

	/**
	 * This is the question id
	 */
	private QuestionEntity question;

	/**
	 * This is the order id
	 */
	private String orderId;

	/**
	 * This is the question score
	 */
	private String questionScore;

	/**
	 * This is shows is the user answer is correct or not
	 */
	private boolean isAnswerCorrect;

	/**
	 * This method is used to get the section id
	 * 
	 * @return the sectionId
	 */
	public AssessmentSectionEntity getSectionId() {
		return section;
	}

	/**
	 * This method is used to set the section id
	 * 
	 * @param sectionId
	 *            the sectionId to set
	 */
	public void setSectionId(AssessmentSectionEntity section) {
		this.section = section;
	}

	/**
	 * This method is used to get the question id
	 * 
	 * @return the questionId
	 */
	/*
	 * public QuestionEntity getQuestionId() { return question; }
	 * 
	 *//**
		 * This method is used to set the question id
		 * 
		 * @param questionId
		 *            the questionId to set
		 *//*
		 * public void setQuestionId(QuestionEntity question) { this.question =
		 * question; }
		 */

	/**
	 * This method is used to get the order id
	 * 
	 * @return the orderId
	 */
	public String getOrderId() {
		return orderId;
	}

	/**
	 * This method is used to set the order id
	 * 
	 * @param orderId
	 *            the orderId to set
	 */
	public void setOrderId(String orderId) {
		this.orderId = orderId;
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

	/**
	 * This method is used to get the question score
	 * 
	 * @return the questionScore
	 */
	public String getQuestionScore() {
		return questionScore;
	}

	/**
	 * This method is used to set the question score
	 * 
	 * @param questionScore
	 *            the questionScore to set
	 */
	public void setQuestionScore(String questionScore) {
		this.questionScore = questionScore;
	}

	/**
	 * @return the question
	 */
	public QuestionEntity getQuestion() {
		return question;
	}

	/**
	 * @param question
	 *            the question to set
	 */
	public void setQuestion(QuestionEntity question) {
		this.question = question;
	}

	/**
	 * This method is used to get isAnswerCorrect
	 * 
	 * @return the isAnswerCorrect
	 */
	public boolean getIsAnswerCorrect() {
		return isAnswerCorrect;
	}

	/**
	 * This method is used to set the isAnswerCorrect
	 * 
	 * @param isAnswerCorrect
	 *            the isAnswerCorrect to set
	 */
	public void setIsAnswerCorrect(boolean isAnswerCorrect) {
		this.isAnswerCorrect = isAnswerCorrect;
	}

}
