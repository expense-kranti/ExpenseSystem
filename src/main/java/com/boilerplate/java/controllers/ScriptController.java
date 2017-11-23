package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.service.interfaces.IScriptsService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Controller
public class ScriptController extends BaseController {

	/**
	 * This is the instance of scriptsService
	 */
	@Autowired
	IScriptsService scriptService;

	@ApiOperation(value = "Publish the user and it's assessment to CRM", notes = "Get all the user keys and publish to crm")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok") })
	@RequestMapping(value = "scripts/publishUserReport", method = RequestMethod.POST)
	public @ResponseBody void publishUserAndAssessmentReport()
			throws UnauthorizedException, NotFoundException, BadRequestException {
		// call the business layer
		this.scriptService.publishUserAndAssessmentReport();
	}

	@ApiOperation(value = "Set all existing user password change status to 'true'", notes = "Set all existing user password change status to 'true'")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok") })
	@RequestMapping(value = "scripts/setchangePasswordStatus", method = RequestMethod.POST)
	public @ResponseBody void setUserChangePasswordStatus()
			throws UnauthorizedException, NotFoundException, BadRequestException {
		// call the business layer
		this.scriptService.setUserChangePasswordStatus();
	}
}
