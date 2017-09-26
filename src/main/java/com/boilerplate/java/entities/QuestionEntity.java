package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This is provide the question related information
 * 
 * @author shiva
 *
 */
public class QuestionEntity extends BaseEntity implements Serializable {

	/**
	 * This is the question type
	 */
	private QuestionTypeEntity questionType;

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
}
