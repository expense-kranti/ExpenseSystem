package com.boilerplate.database.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.ExpenseEntity;

/**
 * This interface has methods for CRUD operations of expense entity in MySQL
 * database
 * 
 * @author ruchi
 *
 */
public interface IExpense {

	/**
	 * this method is used to save a new expense in the system
	 * 
	 * @param expenseEntity
	 *            this is the expense entity to be saved
	 * @return Saved expense entity
	 * @throws Exception
	 *             throw this exception if some exception occurs while saving
	 *             entity in database
	 */
	public ExpenseEntity createExpense(ExpenseEntity expenseEntity) throws Exception;

	/**
	 * This method is used to update expense
	 * 
	 * @param expenseEntity
	 *            this is the expense entity to be saved
	 * @return Saved expense entity
	 */
	public ExpenseEntity updateExpense(ExpenseEntity expenseEntity);

	/**
	 * This method is used to get an expense by id
	 * 
	 * @param id
	 *            This is the id of the expense
	 * @return This is the updated and saved expense entity
	 * @throws BadRequestException
	 *             TFShrow this exception if user sends a bad request
	 */
	public ExpenseEntity getExpense(String id) throws BadRequestException;

}
