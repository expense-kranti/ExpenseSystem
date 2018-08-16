package com.boilerplate.java.entities;

import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class defines the entity used for assigning and updating
 * approvers/super-approver to a list of users
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
	 * This is the user id of the super-approver
	 */
	private String superApprover;

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
	 * This method is used to get super appover
	 * 
	 * @return
	 */
	public String getSuperApprover() {
		return superApprover;
	}

	/**
	 * This method is used to et super approver
	 * 
	 * @param superApprover
	 */
	public void setSuperApprover(String superApprover) {
		this.superApprover = superApprover;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// check whether all the mandatory data is not null or empty
		if (isNullOrEmpty(this.approverUserId) || isNullOrEmpty(superApprover))
			throw new ValidationFailedException("AssignApproverEntity", "Approver or Super approver is null", null);
		// Check if list of users is not null or empty
		if (this.users.size() == 0)
			throw new ValidationFailedException("AssignApproverEntity", "List of users is empty", null);

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
