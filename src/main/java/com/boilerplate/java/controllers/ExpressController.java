package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ExpressEntity;
import com.boilerplate.java.entities.ReportInputEntity;
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
	 * This method is used to get the list of names
	 * 
	 * @param expressEntity
	 *            This is the express entity
	 * @return the list of names
	 * @throws Exception
	 *             This is the parent exception
	 * @throws ValidationFailedException
	 *             this exception occurred when mobile number is null or empty
	 * @throws NotFoundException
	 *             when the user is not found in database against whom experian
	 * @throws BadRequestException
	 *             if proper required input is not provided
	 * @throws UnauthorizedException
	 * 
	 */
	@ApiOperation(value = "gets the list of names for a given mobile number from our api")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/express/namesForMobileNumber", method = RequestMethod.POST)
	public @ResponseBody ExpressEntity getNamesByMobileNumber(@RequestBody ExpressEntity expressEntity)
			throws Exception, ValidationFailedException, NotFoundException, BadRequestException {
		return expressService.getNamesByMobileNumber(expressEntity);
	}

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
	 * @throws ValidationFailedException
	 *             thrown when required input not found
	 */
	@ApiOperation(value = "Validates input name and registers user if name validated")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 400, message = "Data not completely filled") })
	@RequestMapping(value = "/express/validateName", method = RequestMethod.POST)
	public @ResponseBody void validateName(@RequestBody ExpressEntity expressEntity)
			throws PreconditionFailedException, ValidationFailedException {
		expressService.validateName(expressEntity);
	}

	@ApiOperation(value = "Gets the logged in user's details available using mobileNumber and valid name")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 401, message = "user not logged in") })
	@RequestMapping(value = "/express/userDetails", method = RequestMethod.GET)
	public @ResponseBody ReportInputEntity getUserDetails() throws UnauthorizedException {
		return expressService.getUserDetails();
	}

}
