package com.boilerplate.java.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.CurrencyConversionEntity;
import com.boilerplate.service.interfaces.ICurrencyConversionService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has method used for currency conversion
 * 
 * @author urvij
 *
 */
@Api(description = "This api has controllers for Currency conversion", value = "Currency Conversion APIs", basePath = "/convertCurrency")
@Controller
public class CurrencyConversionController extends BaseController {

	/**
	 * This is the autowired instance of the CurrencyConversionService
	 */
	@Autowired
	ICurrencyConversionService currencyConversionService;

	/**
	 * This method is used to convert an amount in one currency to another
	 * 
	 * @param currencyConversionEntity
	 *            The currency conversion entity contains all inputs required
	 *            for currency conversion like amount of currency, its currency
	 *            code and currency code to which it is to be converted
	 * @return The currency conversion entity It contains the converted currency
	 * @throws ValidationFailedException
	 *             Thrown when one of the currency codes is null/empty
	 * @throws IOException
	 *             Thrown If there is an error in creating the http request
	 */
	@ApiOperation(value = "Converts given currency to required currency when their currency codes are provided")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			})
	@RequestMapping(value = "/convertCurrency", method = RequestMethod.POST)
	public @ResponseBody CurrencyConversionEntity findExchangeRateAndConvertCurrency(
			@RequestBody CurrencyConversionEntity currencyConversionEntity)
			throws ValidationFailedException, IOException {
		//call the business layer
		return currencyConversionService.findExchangeRateAndConvertCurrency(currencyConversionEntity);
	}
}
