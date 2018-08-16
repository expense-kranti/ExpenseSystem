package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.java.entities.ExpenseEntity;
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
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             expense
	 */
	@ApiOperation(value = "Creates a new expense entity in the system", notes = "The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, User name or password is empty"),
			@ApiResponse(code = 404, message = "Is user does not exist") })
	@RequestMapping(value = "/expense", method = RequestMethod.POST)
	public @ResponseBody ExpenseEntity createUser(@RequestBody ExpenseEntity expenseEntity) throws Exception {
		// call the business layer
		return expenseService.createExpense(expenseEntity);
	}

	/**
	 * This API is used to update an existing expense in the system
	 * 
	 * @param expenseEntity
	 *            this is the Expense entity
	 * @return Saved expense entity
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             expense
	 */
	@ApiOperation(value = "Updates an expense entity in the system", notes = "The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, User name or password is empty"),
			@ApiResponse(code = 404, message = "Is user does not exist") })
	@RequestMapping(value = "/expense", method = RequestMethod.PUT)
	public @ResponseBody ExpenseEntity updateUser(@RequestBody ExpenseEntity expenseEntity) throws Exception {
		// call the business layer
		return expenseService.updateExpense(expenseEntity);
	}

}
