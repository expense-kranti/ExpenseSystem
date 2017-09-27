package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This is provide the question related information
 * 
 * @author shiva
 * @param <T>
 *
 */
public class QuestionEntity<T> extends BaseEntity implements Serializable {

	/**
	 * This is the question type
	 */
	private QuestionTypeEntity questionType;

	/**
	 * This is the question text
	 */
	private T questionData;

	/**
	 * This is the answer of question by user
	 */
	private String answer;

	/**
	 * This method is used to get the question data
	 * 
	 * @return the questionData
	 */
	public T getQuestionData() {
		return questionData;
	}

	/**
	 * This method is used to set the question text
	 * 
	 * @param questionData
	 *            the questionData to set
	 */
	public void setQuestionData(T questionData) {
		this.questionData = questionData;
	}

	/**
	 * This method is used to get the question type
	 * 
	 * @return the questionType
	 */
	public QuestionTypeEntity getQuestionType() {
		return questionType;
	}

	/**
	 * This method is used to set the question type
	 * 
	 * @param questionType
	 *            the questionType to set
	 */
	public void setQuestionType(QuestionTypeEntity questionType) {
		this.questionType = questionType;
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
	 * This method is used to get the answer
	 * 
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * This method is used to set the answer
	 * 
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}
}
