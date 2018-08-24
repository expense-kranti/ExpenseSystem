package com.boilerplate.service.implemetations;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.boilerplate.database.interfaces.IExpense;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseHistoryEntity;
import com.boilerplate.java.entities.ExpenseStatusType;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.FetchExpenseEntity;
import com.boilerplate.service.interfaces.IExpenseService;

/**
 * This class implements IExpenseService
 * 
 * @author ruchi
 *
 */
public class ExpenseService implements IExpenseService {

	/**
	 * This is the instance of IUser
	 */
	IUser mySqlUser;

	/**
	 * This method set the mysqluser
	 * 
	 * @param mySqlUser
	 *            the mySqlUser to set
	 */
	public void setMySqlUser(IUser mySqlUser) {
		this.mySqlUser = mySqlUser;
	}

	/**
	 * This is the autowired instance of IExpense
	 */
	IExpense mySqlExpense;

	/**
	 * This method is used to set autowired instance of IExpense
	 * 
	 * @param mySqlExpense
	 */
	public void setMySqlExpense(IExpense mySqlExpense) {
		this.mySqlExpense = mySqlExpense;
	}

	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(ExpenseService.class);

	/**
	 * @see IExpenseService.createExpense
	 */
	@Override
	public ExpenseEntity createExpense(ExpenseEntity expenseEntity) throws Exception {
		// check if expense entity is valid or not
		expenseEntity.validate();
		// entity should not have any id
		if (expenseEntity.getId() != null)
			throw new ValidationFailedException("ExpenseEntity", "Id should be null", null);
		// Check whether user id exists and is active
		ExternalFacingUser externalFacingUser = mySqlUser.getUser(expenseEntity.getUserId());
		if (externalFacingUser == null || !externalFacingUser.getIsActive())
			throw new NotFoundException("ExpenseEntity", "USer not found or is inactive", null);
		// set user id with id of the user
		expenseEntity.setUserId(externalFacingUser.getId());
		// set status of expense as submitted
		expenseEntity.setStatus(ExpenseStatusType.Submitted);
		// set creation date and update date
		expenseEntity.setCreationDate(new Date());
		expenseEntity.setUpdationDate(new Date());
		// save expense in database
		return mySqlExpense.createExpense(expenseEntity);
	}

	/**
	 * @see IExpenseService.updateExpense
	 */
	@Override
	public ExpenseEntity updateExpense(ExpenseEntity expenseEntity) throws Exception {
		// check if expense entity is valid or not
		expenseEntity.validate();
		// entity should not have any id
		if (expenseEntity.getId() == null)
			throw new ValidationFailedException("ExpenseEntity", "Id should not be null", null);
		// check whether expense entity exists or not
		ExpenseEntity previousExpense = mySqlExpense.getExpense(expenseEntity.getId());
		if (previousExpense == null)
			throw new NotFoundException("ExpenseEntity", "Expense entity not found", null);
		// Check whether user id exists and is active
		ExternalFacingUser externalFacingUser = mySqlUser.getUser(expenseEntity.getUserId());
		if (externalFacingUser == null || !externalFacingUser.getIsActive())
			throw new NotFoundException("ExpenseEntity", "USer not found or is inactive", null);
		// if expense status is rejected then change it to re-submitted
		if (expenseEntity.getStatus() != null) {
			if (expenseEntity.getStatus() == ExpenseStatusType.Approver_Rejected
					|| expenseEntity.getStatus() == ExpenseStatusType.Finance_Rejected)
				expenseEntity.setStatus(ExpenseStatusType.Re_Submitted);
		} else
			throw new BadRequestException("ExpenseEntity", "Status should be null", null);
		// set creation date and update date
		expenseEntity.setCreationDate(previousExpense.getCreationDate());
		expenseEntity.setUpdationDate(new Date());

		// create a new expense history entity using the data from expense
		// entity
		ExpenseHistoryEntity expenseHistoryEntity = new ExpenseHistoryEntity(previousExpense.getId(),
				previousExpense.getCreationDate(), previousExpense.getUpdationDate(), previousExpense.getAttachmentId(),
				previousExpense.getTitle(), previousExpense.getDescription(), previousExpense.getUserId(),
				previousExpense.getStatus());
		expenseHistoryEntity.setCreationDate(new Date());
		// save this history in mysql
		mySqlExpense.saveExpenseHistory(expenseHistoryEntity);

		// update expense
		return mySqlExpense.updateExpense(expenseEntity);
	}

	/**
	 * @see IExpenseService.getExpenses
	 * 
	 */
	@Override
	public List<ExpenseEntity> getExpenses(FetchExpenseEntity fetchExpenseEntity)
			throws ValidationFailedException, NotFoundException, BadRequestException {
		// validate entity
		fetchExpenseEntity.validate();
		// check if user exists
		if (mySqlUser.getUser(fetchExpenseEntity.getUserId()) == null)
			throw new NotFoundException("ExternalFacingUser", "User not found", null);
		// fetch list of expenses from database
		return mySqlExpense.getExpenses(fetchExpenseEntity);
	}

	@Override
	public List<Map<String, Object>> getExpensesForApproval(String approverId)
			throws NotFoundException, ValidationFailedException, BadRequestException {
		// check if approver id is not null or empty
		if (approverId == null)
			throw new ValidationFailedException("ExpenseEntity", "Approver id for fetching expenses is null or empty",
					null);
		// check if approver exists
		if (mySqlUser.getUser(approverId) == null)
			throw new NotFoundException("ExpenseEntity", "No user with gievn approver id found", null);
		// get list of expenses filed under the given approver
		return mySqlExpense.getExpensesForApprover(approverId);

	}

}
