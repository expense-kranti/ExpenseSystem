package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.java.entities.IncomeTaxEntity;
import com.boilerplate.service.implemetations.IncomeTaxService;
import com.boilerplate.service.interfaces.IIncomeTaxService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This controller has apis related to tax calculation
 * 
 * @author urvij
 *
 */
@Api(description = "This api has controllers related to tax calculation", value = "Income Tax calculation apis", basePath = "/taxCalculation")
@Controller
public class IncomeTaxCalculationController extends BaseController {

	// ADD DEPPENDENCY IN ROOTCONTEXTFILE
	// add method permissions
	/**
	 * This is the instance of IIncomeTaxService
	 */
	@Autowired
	IIncomeTaxService incomeTaxService;

	/**
	 * Sets the incomeTaxService
	 * 
	 * @param incomeTaxService
	 *            the incomeTaxService to set
	 */
	public void setIncomeTaxService(IIncomeTaxService incomeTaxService) {
		this.incomeTaxService = incomeTaxService;
	}

	/**
	 * This method is used to calculate IncomeTax ctc and saves that data
	 * against a uuid
	 * 
	 * @param incomeTaxEntity
	 *            contains the input values required to calculate income tax
	 * @return the incomeTaxEntity with uuid against which the income tax data
	 */
	@ApiOperation(value = "Calculates tax with predefined deductions on given cost to compay(ctc)")
	@ApiResponses({ @ApiResponse(code = 200, message = "Ok") })
	public @ResponseBody IncomeTaxEntity calculateSimpleTax(@RequestBody IncomeTaxEntity incomeTaxEntity) {
		return incomeTaxService.calculateSimpleTax(incomeTaxEntity);
	}
}
