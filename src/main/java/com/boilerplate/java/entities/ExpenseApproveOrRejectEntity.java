package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

public class ExpenseApproveOrRejectEntity extends BaseEntity {

	/**
	 * This is the status
	 */
	private ExpenseStatusType status;

	/**
	 * These are the approver comments
	 */
	private String approverComments;

	/**
	 * This is the expense Id
	 */
	private String expenseId;

	/**
	 * This method is used to get status
	 * 
	 * @return
	 */
	public ExpenseStatusType getStatus() {
		return status;
	}

	/**
	 * This method is used to set status
	 * 
	 * @param status
	 */
	public void setStatus(ExpenseStatusType status) {
		this.status = status;
	}

	/**
	 * This method is used to get approver comments
	 * 
	 * @return
	 */
	public String getApproverComments() {
		return approverComments;
	}

	/**
	 * This method isused to set approver comments
	 * 
	 * @param approverComments
	 */
	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}

	/**
	 * This method is used to expense id
	 * 
	 * @return
	 */
	public String getExpenseId() {
		return expenseId;
	}

	/**
	 * This method used to set expense id
	 * 
	 * @param expenseId
	 */
	public void setExpenseId(String expenseId) {
		this.expenseId = expenseId;
	}

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
