package com.boilerplate.java.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.service.interfaces.IScriptsService;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
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

	@ApiOperation(value = "Publishes AKS Report or Refer Report of user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok") })
	@RequestMapping(value = "scripts/publishUserAKSOrReferReport", method = RequestMethod.POST)
	public @ResponseBody void publishUserAKSOrReferReport()
			throws UnauthorizedException, NotFoundException, BadRequestException {
		this.scriptService.publishUserAKSOrReferReport();
	}

	/**
	 * This method is used to update the User's Total Score fetched from a
	 * provided csv file that contains the user phone number whose total score
	 * is to update, the score points to update, the operation to do
	 * SUBTRACT/ADD
	 * 
	 * @param fileId
	 *            the fileId of the file to get user's userId, score points to
	 *            update, operation type to be done
	 * @throws UnauthorizedException
	 *             thrown when currently logged in user is not authorized to use
	 *             this api
	 * @throws NotFoundException
	 *             when user is not found with given userId
	 * @throws BadRequestException
	 *             thrown when userId is not supplied
	 * @throws IOException
	 *             thrown when IOException occurs while getting file from Amazon
	 *             S3 Client
	 */
	@ApiOperation(value = "Updates User's Total Score by making change only in Obtained Score part of Total Score")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok") })
	@RequestMapping(value = "scripts/changeUserTotalScore/{fileId}", method = RequestMethod.POST)
	public @ResponseBody void updateUserTotalScore(
			@ApiParam(value = "This is the file Id from which userIds and scores to update will be fetched", required = true, name = "fileId", allowMultiple = false) @PathVariable String fileId)
			throws UnauthorizedException, NotFoundException, BadRequestException, IOException {
		scriptService.fetchScorePointsFromFileAndUpdateUserTotalScore(fileId);
	}

	/**
	 * This method is used to publish user and user related data from Redis by
	 * starting a task
	 * 
	 * @throws UnauthorizedException
	 *             thrown when user is not authorized
	 * @throws NotFoundException
	 *             thrown when user is not found
	 * @throws BadRequestException
	 *             thrown when userId is not provided to check for authorization
	 * @throws IOException
	 */
	@ApiOperation(value = "Migrates User and User related data like Assessment data, Referral data, File data, BlogActivity data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok") })
	@RequestMapping(value = "scripts/publishUserAndUserRelatedDataToMySQL/{fileId}", method = RequestMethod.POST)
	public @ResponseBody void publishUserAndUserRelatedDataToMySQL(
			@ApiParam(value = "This is the file Id from which userIds will be get", required = true, name = "fileId", allowMultiple = false) @PathVariable String fileId)
			throws UnauthorizedException, NotFoundException, BadRequestException, IOException {
		scriptService.publishUserAndUserRelatedDataToMySQL(fileId);
	}
}
