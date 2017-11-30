package com.boilerplate.java.controllers;

import javax.xml.bind.ValidationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.java.entities.EmiDataEntity;
import com.boilerplate.service.interfaces.IEmiCalculatorService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has method to calculate EMI of loan taken and amortized schedule
 * of that loan for EMI payment
 * 
 * @author urvij
 *
 */
@Api(description = "This api has controller for calculating EMI and amortized schedule", value = "EMI Calculator APIs", basePath = "/calculateEmi")
@Controller
public class EmiCalculatorController extends BaseController{
	
	/**
	 * This is the autowired instance of IEmiCalculator
	 */
	@Autowired
	IEmiCalculatorService emiCalculatorService;
	


	/**
	 * Sets the emiCalculator service
	 * @param emiCalculatorService The emi calculator service
	 */
	public void setEmiCalculatorService(IEmiCalculatorService emiCalculatorService) {
		this.emiCalculatorService = emiCalculatorService;
	}



	/**
	 * This method is used to calculate the EMI and amortized schedule for given
	 * loan amount, loan period and loan interest rate
	 * 
	 * @param emiDataEntity
	 *            It contains(as input) the loan amount borrowed, loan period
	 *            for loan, interest rate(per annum) for loan taken
	 * @return The emiDataEntity It contains(as output) the amortized schedule
	 *         data(like interest, principal paid per month in each year), the
	 *         total interest payable,total payment to be done
	 * @throws ValidationException Thrown when one or more of the required fields are empty
	 */
	@ApiOperation(value = "Calculates the EMI and amortized schedule for the loan borrowed")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 400, message = "Bad request, some details are not provided") })
	@RequestMapping(value = "/calculateEmi", method = RequestMethod.POST)
	public @ResponseBody EmiDataEntity calculateEmi(@RequestBody EmiDataEntity emiDataEntity) throws ValidationException {
		return emiCalculatorService.emiCalculator(emiDataEntity);
	}

}
