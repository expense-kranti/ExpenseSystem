package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.Handbook;
import com.boilerplate.service.interfaces.IHandbookService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This controller has apis to operate on user's handbook data like publishing
 * 
 * @author urvij
 *
 */
@Api(description = "This controller has apis to operate on user's handbook data", value = "Handbook Api", basePath = "/handbook")
@Controller
public class HandbookController extends BaseController {

	/**
	 * This is the IHandbookService instance
	 */
	@Autowired
	IHandbookService handbookService;

	/**
	 * Sets the handbookService
	 * 
	 * @param handbookService
	 *            the handbookService to set
	 */
	public void setHandbookService(IHandbookService handbookService) {
		this.handbookService = handbookService;
	}

	/**
	 * This method is used to publish handbook data of the logged in user
	 * 
	 * @param handbook
	 *            the handbook entity with data to be saved
	 * @return the handbook entity with userId of logged in user against whom
	 *         data is saved
	 * @throws ValidationFailedException
	 *             thrown when required input is not filled in request body
	 * @throws UnauthorizedException
	 *             thrown when user is not logged in
	 */
	@ApiOperation(value = "This api is used to publish user's hand book data to SalesForce")
	@ApiResponses({ @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/handbook", method = RequestMethod.POST)
	public @ResponseBody Handbook publishHandbook(@RequestBody Handbook handbook)
			throws ValidationFailedException, UnauthorizedException {
		return handbookService.publishHandBook(handbook);
	}

}
