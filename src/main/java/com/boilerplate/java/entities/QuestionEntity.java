package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This is provide the question related information
 * @author shiva
 *
 */
public class QuestionEntity extends BaseEntity{
	
	/**
	 * This is the question type
	 */
	private QuestionTypeEntity type;
	
	/**
	 * This is the question type id
	 */
	private String questionTypeId;
	
	/**
	 * This method is used to get the question type
	 * @return the type
	 */
	public QuestionTypeEntity getType() {
		return type;
	}

	/**
	 * This method is used to set the question type
	 * @param type the type to set
	 */
	public void setType(QuestionTypeEntity type) {
		this.type = type;
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
	 * This method is used to get the question type id
	 * @return the questionTypeId
	 */
	public String getQuestionTypeId() {
		return questionTypeId;
	}

	/**
	 * This method is used to set the question type id
	 * @param questionTypeId the questionTypeId to set
	 */
	public void setQuestionTypeId(String questionTypeId) {
		this.questionTypeId = questionTypeId;
	}
}
