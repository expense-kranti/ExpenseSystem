package com.boilerplate.java.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.IncomeTaxEntity;
import com.boilerplate.service.interfaces.IIncomeTaxService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This controller has apis to calculate income tax data
 * 
 * @author urvij
 *
 */
@Api(description = "This controller has apis to calculate income tax data", value = "Income Tax Apis", basePath = "/incomeTax")
@Controller
public class IncomeTaxCalculatorController extends BaseController {

	/**
	 * This is the instance of IIncomeTaxService
	 */
	@Autowired
	IIncomeTaxService incomeTaxService;

	/**
	 * This method is used to calculate IncomeTax ctc and saves that data
	 * against a uuid
	 * 
	 * @param incomeTaxEntity
	 *            contains the input values required to calculate income tax
	 * @return the incomeTaxEntity with uuid against which the income tax data
	 * @throws ValidationFailedException
	 *             thrown when required inputs are not provided
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	@ApiOperation(value = "Calculates tax with predefined deductions on given cost to company")
	@ApiResponses({ @ApiResponse(code = 200, message = "Ok") })
	@RequestMapping(value = "/calculateSimpleIncomeTax", method = RequestMethod.POST)
	public @ResponseBody IncomeTaxEntity calculateSimpleTax(@RequestBody IncomeTaxEntity incomeTaxEntity)
			throws ValidationFailedException, JsonParseException, JsonMappingException, IOException {
		return incomeTaxService.calculateSimpleTax(incomeTaxEntity);
	}

	/**
	 * 
	 * @param incomeTaxEntity
	 * @return
	 * @throws ValidationFailedException
	 *             thrown when required input is not provided
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws NotFoundException
	 *             thrown when income tax data is not found against uuid
	 */
	@ApiOperation(value = "Gets the income tax data against a given uuid")
	@ApiResponses({ @ApiResponse(code = 200, message = "Ok") })
	@RequestMapping(value = "/incomeTaxDetails", method = RequestMethod.POST)
	public @ResponseBody IncomeTaxEntity getIncomeTaxData(@RequestBody IncomeTaxEntity incomeTaxEntity)
			throws ValidationFailedException, JsonParseException, JsonMappingException, IOException, NotFoundException {
		return incomeTaxService.getIncomeTaxData(incomeTaxEntity);
	}

	/**
	 * This method is used to calculate income tax with investments made in
	 * categories of rebate
	 * 
	 * @param incomeTaxEntity
	 *            contains investments made if any
	 * @return the incometax entity with estimated tax
	 * @throws ValidationFailedException
	 *             thrown when required input is not provided
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 * @throws NotFoundException
	 *             thrown when income tax data is not found against uuid
	 */
	@ApiOperation(value = "Calculates income tax with investments made")
	@ApiResponses({ @ApiResponse(code = 200, message = "Ok") })
	@RequestMapping(value = "/calculateIncomeTaxWithInvestments", method = RequestMethod.POST)
	public @ResponseBody IncomeTaxEntity calculateIncomeTaxWithInvestments(@RequestBody IncomeTaxEntity incomeTaxEntity)
			throws ValidationFailedException, JsonParseException, JsonMappingException, IOException, NotFoundException {
		return incomeTaxService.calculateTaxWithInvestments(incomeTaxEntity);
	}

	/**
	 * This method is used to save user details against whom income tax data was
	 * being calculated by matching uuid of income tax and send the email to
	 * user with income tax details
	 * 
	 * @param incomeTaxEntity
	 *            contains all user details to be saved
	 * @throws Exception
	 *             thrown when exception occurs in saving and sending email to
	 *             user emailId
	 */
	@ApiOperation(value = "Saves the user contact details against whom income tax data was being calculated by matching uuid of income tax and sends email to user emailId with income tax details")
	@ApiResponses({ @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/saveUserDetailsAndSendIncomeTaxDetailsInEmail", method = RequestMethod.POST)
	public @ResponseBody void saveIncomeTaxUserDetails(@RequestBody IncomeTaxEntity incomeTaxEntity) throws Exception {
		incomeTaxService.saveIncomeTaxUserDetailsAndEmail(incomeTaxEntity);
	}

}
