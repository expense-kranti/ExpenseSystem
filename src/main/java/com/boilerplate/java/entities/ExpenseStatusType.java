package com.boilerplate.java.entities;

/**
 * This enum defines the types of states of an expense
 * 
 * @author ruchi
 *
 */
public enum ExpenseStatusType {

	/**
	 * This means that expense has been submitted by the user
	 */
	SUBMITTED,

	/**
	 * This means the expense has been approved by the approver
	 */
	APPROVER_APPROVED,

	/**
	 * This means the expense has been rejected by the approver
	 */
	APPROVER_REJECTED,

	/**
	 * This means the expense has been approved by the finance
	 */
	FINANCE_APPROVED,

	/**
	 * This means the expense has been rejected by the finance
	 */
	FINANCE_REJECTED,

	/**
	 * This means that the expense is ready for payment by finance
	 */
	READY_FOR_PAYMENT,

	/**
	 * This means that the expense has been submitted again
	 */
	RE_SUBMITTED
}
