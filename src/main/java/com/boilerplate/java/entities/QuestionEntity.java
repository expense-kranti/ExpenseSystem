package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

public class QuestionEntity extends BaseEntity{
	
	/**
	 * This is the question type
	 */
	private String type;
	
	
	/**
	 * This method is used to get the question type
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * This method is used to set the question type
	 * @param type the type to set
	 */
	public void setType(String type) {
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
}
