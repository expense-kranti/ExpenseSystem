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
	Submitted,

	/**
	 * This means the expense has been approved by the approver
	 */
	Approver_Approved,

	/**
	 * This means the expense has been rejected by the approver
	 */
	Approver_Rejected,

	/**
	 * This means the expense has been approved by the finance
	 */
	Finance_Approved,

	/**
	 * This means the expense has been rejected by the finance
	 */
	Finance_Rejected,

	/**
	 * This means that the expense is ready for payment by finance
	 */
	Ready_For_Payment,

	/**
	 * This means that the expense has been submitted again
	 */
	Re_Submitted
}
