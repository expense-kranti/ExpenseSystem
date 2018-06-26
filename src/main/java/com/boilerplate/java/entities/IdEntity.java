package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

public class IdEntity extends BaseEntity implements Serializable{

	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}

	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

}
