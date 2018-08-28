package com.boilerplate.service.interfaces;

import java.util.List;
import java.util.Map;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ExpenseApproveOrRejectEntity;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.FetchExpenseEntity;
import com.boilerplate.java.entities.UserRoleType;

/**
 * This class had methods for CRUD and other operations related to expense
 * entity
 * 
 * @author ruchi
 *
 */
public interface IExpenseService {

	/**
	 * This method is used to create a new expense entity in the system
	 * 
	 * @param expenseEntity
	 *            this is the new expense entity to be saved
	 * @return this is the saved expense entity
	 * @throws ValidationFailedException
	 *             Throw this exception if entity is invalid
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             entity
	 */
	public ExpenseEntity createExpense(ExpenseEntity expenseEntity)
			throws ValidationFailedException, BadRequestException, NotFoundException, Exception;

	/**
	 * this method is used to update an existing expense filed by user
	 * 
	 * @param expenseEntity
	 *            this is the updated expense entity
	 * @return Saved expense entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws ValidationFailedException
	 *             throw this exception if entity is invalid
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             entity
	 */
	public ExpenseEntity updateExpense(ExpenseEntity expenseEntity)
			throws BadRequestException, ValidationFailedException, NotFoundException, Exception;

	/**
	 * This method is used to get all expenses for a given user id according to
	 * the status of expenses and a date range
	 * 
	 * @param fetchExpenseEntity
	 *            This entity contain user id, date range and status for
	 *            filtration
	 * @return Expense list
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws ValidationFailedException
	 *             throw this exception if entity is invalid
	 */
	public List<ExpenseEntity> getExpenses(FetchExpenseEntity fetchExpenseEntity)
			throws ValidationFailedException, NotFoundException, BadRequestException;

	/**
	 * This method is used to get list of expenses filed under a given approver
	 * or super approver
	 * 
	 * @param approverId
	 *            This is the id of the approver
	 * @param role
	 *            this is the role of the approver
	 * @return List of expenses
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws ValidationFailedException
	 *             throw this exception if entity is invalid
	 */
	public List<ExpenseEntity> getExpensesForApproval(String approverId, UserRoleType role)
			throws NotFoundException, ValidationFailedException, BadRequestException;

	public ExpenseEntity approveExpense(ExpenseApproveOrRejectEntity expenseApproveOrRejectEntity)
			throws ValidationFailedException, BadRequestException, NotFoundException, Exception;

}
