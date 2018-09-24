package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This class defines the attributes of roles
 * 
 * @author ruchi
 *
 */
public class RoleEntity extends BaseEntity {

	/**
	 * @param roleName
	 */
	public RoleEntity(String id, String roleName) {
		super();
		this.setId(id);
		this.roleName = roleName;
	}

	/**
	 * 
	 */
	public RoleEntity() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * This is the role name
	 */
	@ApiModelProperty(value = "This is the role name", notes = "This is the role name")
	private String roleName;

	/**
	 * This method is used to get role name
	 * 
	 * @return
	 */
	public String getRoleName() {
		return roleName;
	}

	/**
	 * This method is used o set role name
	 * 
	 * @param roleName
	 */
	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	/**
	 * @see Base.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see Base.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see Base.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

}
