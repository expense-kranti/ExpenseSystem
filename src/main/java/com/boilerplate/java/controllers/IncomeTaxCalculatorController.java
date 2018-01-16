package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.java.entities.IncomeTaxEntity;
import com.boilerplate.service.interfaces.IIncomeTaxService;
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

	// add method permissions
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
	 */
	@ApiOperation(value = "Calculates tax with predefined deductions on given cost to company")
	@ApiResponses({ @ApiResponse(code = 200, message = "Ok") })
	public @ResponseBody IncomeTaxEntity calculateSimpleTax(@RequestBody IncomeTaxEntity incomeTaxEntity) {
		return incomeTaxService.calculateSimpleTax(incomeTaxEntity);
	}
}
