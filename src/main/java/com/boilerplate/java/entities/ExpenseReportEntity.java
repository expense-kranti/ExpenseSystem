package com.boilerplate.java.entities;

import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This entity defines the report entity
 * 
 * @author ruchi
 *
 */
public class ExpenseReportEntity extends BaseEntity {

	/**
	 * This is the default constructor
	 */
	public ExpenseReportEntity() {
		// call the super constructor
		super();
	}

	/**
	 * This is the parameterized constructor
	 * 
	 * @param userName
	 *            This is the user name
	 * @param userId
	 *            This is the user id
	 * @param totalAmount
	 *            This is the total amount of all the expenses
	 * @param expenses
	 *            This is the list of expenses
	 * @param status
	 *            This is the status of the report
	 */
	public ExpenseReportEntity(String userName, String userId, float totalAmount, List<ExpenseEntity> expenses,
			ExpenseStatusType status) {
		super();
		this.userName = userName;
		this.userId = userId;
		this.totalAmount = totalAmount;
		this.expenses = expenses;
		this.status = status;
	}

	/**
	 * This is the name of the user to whom report belongs
	 */
	@ApiModelProperty(value = "This is the user name of the report", required = true, notes = "This is the user name of the report")
	private String userName;

	/**
	 * This is the id of the user
	 */
	@ApiModelProperty(value = "This is the id of the user", required = true, notes = "This is the id of the user")
	private String userId;

	/**
	 * This is the total amount for all the expenses
	 */
	@ApiModelProperty(value = "This is the total amount of all the expenses", required = true, notes = "This is the total amount of all the expenses")
	private float totalAmount;

	/**
	 * This is the list of all expenses
	 */
	@ApiModelProperty(value = "This is the list of expenses", required = true, notes = "This is the list of expenses")
	private List<ExpenseEntity> expenses;

	/**
	 * This is the status of the expense
	 */
	@ApiModelProperty(value = "This is the status of the expense", required = true, notes = "This is the status of the expense")
	@JsonIgnore
	private ExpenseStatusType status;

	/**
	 * This is the string for enum ExpenseStatusType
	 */
	@ApiModelProperty(value = "This is the string equivalent of the expense status type", required = true, notes = "This is the string equivalent of the expense status type")
	private String statusString;

	/**
	 * This is the approver comments
	 */
	@ApiModelProperty(value = "This is the approver comments", required = true, notes = "This is the approver comments")
	private String approverComments;

	/**
	 * This method is used to get user name
	 * 
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * This method is used to set user name
	 * 
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
	 * This method is used to set approver comments
	 * 
	 * @param approverComments
	 */
	public void setApproverComments(String approverComments) {
		this.approverComments = approverComments;
	}

	/**
	 * This method is used to get user id
	 * 
	 * @return
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * This method is used of set user id
	 * 
	 * @param userId
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * This method is used to get total amount
	 * 
	 * @return
	 */
	public float getTotalAmount() {
		return totalAmount;
	}

	/**
	 * This method is used to set total amount
	 * 
	 * @param totalAmount
	 */
	public void setTotalAmount(float totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * This method is used to get list of expenses
	 * 
	 * @return
	 */
	public List<ExpenseEntity> getExpenses() {
		return expenses;
	}

	/**
	 * This method is used to set list of expenses
	 * 
	 * @param expenses
	 */
	public void setExpenses(List<ExpenseEntity> expenses) {
		this.expenses = expenses;
	}

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
	 * This method is used to get status string
	 * 
	 * @return
	 */
	public String getStatusString() {
		return statusString;
	}

	/**
	 * This method is used to set status string
	 * 
	 * @param statusString
	 */
	public void setStatusString(String statusString) {
		this.statusString = statusString;
		this.status = ExpenseStatusType.convert(statusString);
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		if (status == null)
			throw new ValidationFailedException("ExpenseStatusType", "Invalid value for status string", null);
		if (status.equals(ExpenseStatusType.FINANCE_REJECTED) && this.approverComments == null)
			throw new ValidationFailedException("ExpenseReportEntity",
					"Cooments are mandatory when expense is being rejected", null);
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
