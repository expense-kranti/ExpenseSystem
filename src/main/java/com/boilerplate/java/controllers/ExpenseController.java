package com.boilerplate.java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ExpenseApproveOrRejectEntity;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseReportEntity;
import com.boilerplate.java.entities.ExpenseStatusType;
import com.boilerplate.java.entities.FetchExpenseEntity;
import com.boilerplate.java.entities.UserRoleType;
import com.boilerplate.service.interfaces.IExpenseService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This controller has APIs for expense CRUD operations and other expense
 * related operations
 * 
 * @author ruchi
 *
 */
@Api(description = "This api has controllers for expense CRUD operations", value = "Expense API's", basePath = "/expense")
@Controller
public class ExpenseController extends BaseController {

	/**
	 * This is the autowired instance of IExpenseService
	 */
	@Autowired
	IExpenseService expenseService;

	/**
	 * This API is used to create a new expense in the system
	 * 
	 * @param expenseEntity
	 *            this is the Expense entity
	 * @return Saved expense entity
	 * @throws NotFoundException
	 *             Throw this exception if entity not found
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation rules
	 * @throws Exception
	 *             Throw this exception if any exception occurs wile
	 *             saving/updating data in MySQL
	 */
	@ApiOperation(value = "Creates a new expense entity in the system", notes = "The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, If user sends invalid data"),
			@ApiResponse(code = 404, message = "If entity does not exist") })
	@RequestMapping(value = "/expense", method = RequestMethod.POST)
	public @ResponseBody ExpenseEntity createExpense(@RequestBody ExpenseEntity expenseEntity)
			throws ValidationFailedException, BadRequestException, NotFoundException, Exception {
		// call the business layer
		return expenseService.createExpense(expenseEntity);
	}

	/**
	 * This API is used to update an existing expense in the system
	 * 
	 * @param expenseEntity
	 *            this is the Expense entity
	 * @return Saved expense entity
	 * @throws NotFoundException
	 *             Throw this exception if entity not found
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation rules
	 * @throws Exception
	 *             Throw this exception if any exception occurs wile
	 *             saving/updating data in MySQL
	 * 
	 */
	@ApiOperation(value = "Updates an expense entity in the system", notes = "The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, If user sends invalid data"),
			@ApiResponse(code = 404, message = "If entity does not exist") })
	@RequestMapping(value = "/expense", method = RequestMethod.PUT)
	public @ResponseBody ExpenseEntity updateExpense(@RequestBody ExpenseEntity expenseEntity)
			throws BadRequestException, ValidationFailedException, NotFoundException, Exception {
		// call the business layer
		return expenseService.updateExpense(expenseEntity);
	}

	/**
	 * This API is used to get expenses for a given user
	 * 
	 * @param userId
	 *            this is the user id for which expenses need o be fetched
	 * @return List of expenses
	 * @throws NotFoundException
	 *             Throw this exception if entity not found
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation rules
	 */
	@ApiOperation(value = "Gets expenses for a given user", notes = "The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, If user sends invalid data"),
			@ApiResponse(code = 404, message = "If entity does not exist") })
	@RequestMapping(value = "/getExpensesForEmployee", method = RequestMethod.POST)
	public @ResponseBody List<ExpenseEntity> getExpensesForEmployee(@RequestBody FetchExpenseEntity fetchExpenseEntity)
			throws ValidationFailedException, NotFoundException, BadRequestException {
		// call the business layer
		return expenseService.getExpenses(fetchExpenseEntity);
	}

	/**
	 * This API is used to get expenses for approver/Super approvers
	 * 
	 * @param approverId
	 *            This is the id of the approver
	 * @param role
	 *            This is the role of the approver
	 * @return List of expenses
	 * @throws NotFoundException
	 *             Throw this exception if entity not found
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation rules
	 */
	@ApiOperation(value = "Gets expenses for a given approver/super approver", notes = "The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, If user sends invalid data"),
			@ApiResponse(code = 404, message = "If entity does not exist") })
	@RequestMapping(value = "/getExpensesForApprover", method = RequestMethod.GET)
	public @ResponseBody List<ExpenseEntity> getExpensesForApprover(@RequestParam UserRoleType role)
			throws NotFoundException, ValidationFailedException, BadRequestException {
		// call the business layer
		return expenseService.getExpensesForApproval(role);
	}

	/**
	 * This API is used to approver expenses by approver/super approvers
	 * 
	 * @param expenseId
	 *            This is the id of the expense to be approved
	 * @return Updated expense entity
	 * @throws NotFoundException
	 *             Throw this exception if entity not found
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation rules
	 * @throws Exception
	 *             Throw this exception if any exception occurs wile
	 *             saving/updating data in MySQL
	 */
	@ApiOperation(value = "Approve an expense", notes = "The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, If user sends invalid data"),
			@ApiResponse(code = 404, message = "If entity does not exist") })
	@RequestMapping(value = "/approveForApprovers", method = RequestMethod.POST)
	public @ResponseBody ExpenseEntity approveExpenseForApprover(
			@RequestBody ExpenseApproveOrRejectEntity expenseApproveOrRejectEntity)
			throws ValidationFailedException, BadRequestException, NotFoundException, Exception {
		// call the business layer
		return expenseService.approveExpenseForApprover(expenseApproveOrRejectEntity);
	}

	/**
	 * This api is used to get list for expenses for finance
	 * 
	 * @return List of reports
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 */
	@ApiOperation(value = "Gets expenses for finance", notes = "The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, If user sends invalid data"),
			@ApiResponse(code = 404, message = "If entity does not exist") })
	@RequestMapping(value = "/getExpensesForFinance/{expenseStatus}", method = RequestMethod.GET)
	public @ResponseBody List<ExpenseReportEntity> getExpensesForFinance(@RequestParam ExpenseStatusType expenseStatus)
			throws BadRequestException {
		// call the business layer
		return expenseService.getExpensesForFinance(expenseStatus);
	}

	/**
	 * This API is used to approve/reject/move to ready for payment state by
	 * Finance
	 * 
	 * @param expenseReportEntity
	 *            This entity contains all the expenses to be updated
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation rules
	 * @throws Exception
	 *             Throw this exception if any exception occurs wile
	 *             saving/updating data in MySQL
	 */
	@ApiOperation(value = "Approve an expense", notes = "The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, If user sends invalid data"),
			@ApiResponse(code = 404, message = "If entity does not exist") })
	@RequestMapping(value = "/approveForFinance", method = RequestMethod.POST)
	public @ResponseBody void approveExpenseForFinance(@RequestBody ExpenseReportEntity expenseReportEntity)
			throws BadRequestException, ValidationFailedException, Exception {
		// call the business layer
		expenseService.approveExpenseForFinance(expenseReportEntity);
	}

}
