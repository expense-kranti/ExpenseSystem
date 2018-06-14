package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.java.entities.ExpressEntity;
import com.boilerplate.service.interfaces.IExpressService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(value = "API's for Express registration and report generation", basePath = "/express", description = "API's for express registration and report generation")
@Controller
public class ExpressController extends BaseController {

	/**
	 * This is an instance of expressService
	 */
	@Autowired
	IExpressService expressService;

	/**
	 * This api is used to validate the user and register the user on validation
	 * success and then gets the user details on basis of phone number and name
	 * 
	 * @param expressEntity
	 *            contains the phone number and name of user
	 * @return the express entity
	 * @throws PreconditionFailedException
	 *             thrown when no express attempt found for user for validating
	 *             input name with express attempt generated name
	 */
	@ApiOperation(value = "Validates input name and registers user if name validated")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 400, message = "Data not completely filled") })
	@RequestMapping(value = "/experian/express/validateUser", method = RequestMethod.POST)
	public @ResponseBody ExpressEntity validateName(@RequestBody ExpressEntity expressEntity)
			throws PreconditionFailedException {
		return expressService.validateAndRegisterUser(expressEntity);
	}
}
