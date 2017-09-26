package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class is provide the data regarding Multiple Choice Question
 * @author shiva
 *
 */
public class MultipleChoiceQuestionEntity extends BaseEntity{

	/**
	 * This is the question type text
	 */
	private String text;
	
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
