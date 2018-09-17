package com.boilerplate.java.entities;

import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class defines the entity used for assigning and updating approvers to a
 * list of users
 * 
 * @author ruchi
 *
 */
public class AssignApproverEntity extends BaseEntity {

	/**
	 * This is the user id of the approver
	 */
	private String approverUserId;

	/**
	 * This is the list of users
	 */
	private List<String> users;

	/**
	 * This method is used get approver user id
	 * 
	 * @return
	 */
	public String getApproverUserId() {
		return approverUserId;
	}

	/**
	 * This method is used to set approver user id
	 * 
	 * @param approverUserId
	 */
	public void setApproverUserId(String approverUserId) {
		this.approverUserId = approverUserId;
	}

	/**
	 * This method is used to get list of users
	 * 
	 * @return
	 */
	public List<String> getUsers() {
		return users;
	}

	/**
	 * This method is used to set users
	 * 
	 * @param users
	 */
	public void setUsers(List<String> users) {
		this.users = users;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// check whether all the mandatory data is not null or empty
		if (isNullOrEmpty(this.getApproverUserId()))
			throw new ValidationFailedException("AssignApproverEntity", "Approver's id is manadatory", null);
		// Check if list of users is not null or empty
		if (this.users.size() == 0)
			throw new ValidationFailedException("AssignApproverEntity", "List of users is empty", null);

		return true;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}

}
