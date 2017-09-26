package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class provide the question type information
 * 
 * @author shiva
 *
 */
public class QuestionTypeEntity extends BaseEntity {

	/**
	 * This is the question type name
	 */
	private String name;

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
	 * This method is used to get the name of question type
	 * 
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method is used to set the name of question type
	 * 
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
