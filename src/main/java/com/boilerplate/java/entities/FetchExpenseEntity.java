package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

public class FetchExpenseEntity extends BaseEntity {

	/**
	 * This is the start date for expense filtering
	 */
	private String startDate;

	/**
	 * this is the end date for expense filtering
	 */
	private String endDate;

	/**
	 * This is the type expenses to be fetched
	 */
	private ExpenseStatusType expenseType;

	/**
	 * This method is used get start date
	 * 
	 * @return
	 */
	public String getStartDate() {
		return startDate;
	}

	/**
	 * This method is used to set start date
	 * 
	 * @param startDate
	 */
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	/**
	 * This method is used to get end date
	 * 
	 * @return
	 */
	public String getEndDate() {
		return endDate;
	}

	/**
	 * This method is used to set end date
	 * 
	 * @param endDate
	 */
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	/**
	 * This method is used to get expense type
	 * 
	 * @return
	 */
	public ExpenseStatusType getExpenseType() {
		return expenseType;
	}

	/**
	 * This method is used to set expense type
	 * 
	 * @param expenseType
	 */
	public void setExpenseType(ExpenseStatusType expenseType) {
		this.expenseType = expenseType;
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

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// check if start date is given then end date is mandatory and vice
		// versa
		if (isNullOrEmpty(this.getStartDate())) {
			if (!isNullOrEmpty(this.getEndDate()))
				throw new ValidationFailedException("FetchExpenseEntity",
						"Either none or both the dates should be given", null);
		}
		if (isNullOrEmpty(this.getEndDate())) {
			if (!isNullOrEmpty(this.getStartDate()))
				throw new ValidationFailedException("FetchExpenseEntity",
						"Either none or both the dates should be given", null);
		}
		return false;
	}

}
