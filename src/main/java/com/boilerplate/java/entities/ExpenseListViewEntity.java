package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This entity has attributes for list of expenses with less attributes
 * 
 * @author ruchi
 *
 */
public class ExpenseListViewEntity extends BaseEntity {

	/**
	 * This is the default constructor
	 */
	public ExpenseListViewEntity() {
		super();
	}

	/**
	 * This is the parameterized costructor
	 * 
	 * @param userName
	 * @param title
	 * @param amount
	 */
	public ExpenseListViewEntity(String id, String userName, String title, float amount) {
		super();
		this.setId(id);
		this.userName = userName;
		this.title = title;
		this.amount = amount;
	}

	/**
	 * This is the name of the user
	 */
	@ApiModelProperty(value = "This is the name of the user who filed this expense", required = true, notes = "This is the name of the user who filed this expense")
	private String userName;

	/**
	 * This is the title of the expense
	 */
	@ApiModelProperty(value = "This is the title of the expense", required = true, notes = "This is the title of the expense")
	private String title;

	/**
	 * This is the amount
	 */
	@ApiModelProperty(value = "This is the amount of the expense", required = true, notes = "This is the amount of the expense")
	private float amount;

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
	 * This method is used to get title
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * This method is used to set title
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * This method is used to get amount
	 * 
	 * @return
	 */
	public float getAmount() {
		return amount;
	}

	/**
	 * This method is used to set amount
	 * 
	 * @param amount
	 */
	public void setAmount(float amount) {
		this.amount = amount;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return this;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return this;
	}

}
