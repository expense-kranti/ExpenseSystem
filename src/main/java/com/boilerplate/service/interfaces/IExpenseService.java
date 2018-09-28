package com.boilerplate.service.interfaces;

import java.text.ParseException;
import java.util.List;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseReportEntity;
import com.boilerplate.java.entities.ExpenseReviewEntity;
import com.boilerplate.java.entities.ExpenseStatusType;
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
	 * @throws ParseException
	 *             Throw this exception if exception occurs while parsing date
	 */
	public List<ExpenseEntity> getExpensesForUser(FetchExpenseEntity fetchExpenseEntity)
			throws ValidationFailedException, NotFoundException, BadRequestException, ParseException;

	/**
	 * This method is used to get list of expenses filed under a given approver
	 * or super approver
	 * 
	 * @return List of expenses
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws ValidationFailedException
	 *             throw this exception if entity is invalid
	 */
	public List<ExpenseEntity> getExpensesForApproval()
			throws NotFoundException, ValidationFailedException, BadRequestException;

	/**
	 * This method is used to approve expenses by approver/super approver
	 * 
	 * @param expenseApproveOrRejectEntity
	 *            This entity has details for approval/rejection of an expense
	 * @return Expense Entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if user not found
	 * @throws ValidationFailedException
	 *             throw this exception if entity is invalid
	 */
	public ExpenseEntity approveExpenseForApprover(ExpenseReviewEntity expenseReviewEntity)
			throws ValidationFailedException, BadRequestException, NotFoundException, Exception;

	/**
	 * This method is used to get list of expenses for finance in report format
	 * 
	 * @return List of expenses
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 */
	public List<ExpenseEntity> getExpensesForFinance() throws BadRequestException, NotFoundException;

	/**
	 * This method is used to approve/rejects/move to ready for payment state
	 * for finance
	 * 
	 * @param reportEntity
	 *            This is the report entity containing all the expenses
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if entity not found
	 * @throws ValidationFailedException
	 *             throw this exception if entity is invalid
	 */
	public void approveExpenseForFinance(ExpenseReportEntity reportEntity)
			throws BadRequestException, ValidationFailedException, Exception;

	/**
	 * This method is used by finance to approve/reject a single expense
	 * 
	 * @param expenseReviewEntity
	 *            This entity determines the review action, expense id and
	 *            comments(if any)
	 * @throws ValidationFailedException
	 *             Throw this exception if request fails any validation
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             Throw this exception if expense is not found
	 */
	public void expenseReviewByFinance(ExpenseReviewEntity expenseReviewEntity)
			throws ValidationFailedException, BadRequestException, NotFoundException;

	/**
	 * This method is used to get expense reports for the given status
	 * 
	 * @param status
	 *            This is the status in string format
	 * @return List of reports for each the user
	 * @throws BadRequestException
	 *             Throw this exception if user sends bad request
	 * @throws NotFoundException
	 *             Throw this exception if no expenses are found
	 * @throws ValidationFailedException
	 *             throw this exception if user sends some invalid values
	 */
	public List<ExpenseReportEntity> getExpenseReportsForFinance(String status)
			throws BadRequestException, NotFoundException, ValidationFailedException;
}
