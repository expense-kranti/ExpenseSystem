package com.boilerplate.java.controllers;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.xml.sax.SAXException;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ExperianQuestionAnswer;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.service.interfaces.IBureauIntegrationService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class encapsulates the api's for experian integration
 * 
 * @author gaurav.verma.icloud
 *
 */
@Api(value = "API's for Experian integration", basePath = "/experian", description = "API's for experian integration")
@Controller
public class ExperianController extends BaseController {

	/**
	 * This is an instance of experianBureauService
	 */
	@Autowired
	IBureauIntegrationService experianBureauService;

	/**
	 * This stars the experian integration process
	 * 
	 * @param reportInputEntity
	 *            it contains the data required to send request to server for
	 *            making experian integration
	 * @throws NotFoundException
	 *             If the user is not found
	 * @throws BadRequestException
	 *             If the userId is not provided (or not found in current
	 *             session)
	 */
	@ApiOperation(value = "Starts the experian session")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 400, message = "Data not completly filled") })
	@RequestMapping(value = "/experian/start", method = RequestMethod.POST)
	public @ResponseBody ReportInputEntity start(@RequestBody ReportInputEntity reportInputEntiity)
			throws Exception, ValidationFailedException, ConflictException, NotFoundException, IOException,
			PreconditionFailedException, BadRequestException, UnauthorizedException {
		return this.experianBureauService.start(reportInputEntiity);
	}
	
	@ApiOperation(	value="Fetches next question"
			 )
		@ApiResponses(value={
						@ApiResponse(code=200, message="Ok")
					,	@ApiResponse(code=400, message="Data not completly filled")
					})
		@RequestMapping(value = "/experian/question", method = RequestMethod.POST)
	public @ResponseBody ReportInputEntity answerQuestionAndMoveToNext(@RequestBody ExperianQuestionAnswer experianQuestionAnswer) throws Exception,ConflictException, NotFoundException, PreconditionFailedException, IOException, UpdateFailedException, BadRequestException,JAXBException, ParserConfigurationException, FactoryConfigurationError, SAXException{
		return this.experianBureauService.fetchNextItem(experianQuestionAnswer.getQuestionId()
				,experianQuestionAnswer.getOptionSet1Answer(),experianQuestionAnswer.getOptionSet2Answer());
	}

}
