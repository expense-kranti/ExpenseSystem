package com.boilerplate.java.entities;

import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

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
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param userName
	 * @param userId
	 * @param totalAmount
	 * @param expenses
	 * @param status
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
	private String userName;

	/**
	 * This is the id of the user
	 */
	private String userId;

	/**
	 * This is the total amount for all the expenses
	 */
	private float totalAmount;

	/**
	 * This is the list of all expenses
	 */
	private List<ExpenseEntity> expenses;

	/**
	 * This is the status of the report
	 */
	private ExpenseStatusType status;

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
