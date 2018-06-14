package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity is used to represent the user express registration details
 * 
 * @author urvij
 *
 */
public class UserExpressEntity extends BaseEntity implements Serializable {

	/**
	 * This is the user fullname
	 */
	private String fullName;
	/**
	 * This is the list of user full name
	 */
	private List<String> fullNameList;
	/**
	 * This is the mobileNumber
	 */
	private String mobileNumber;

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
