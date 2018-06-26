package com.boilerplate.java.controllers;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.boilerplate.java.entities.GenericListEncapsulationEntity;
import com.boilerplate.java.entities.KycDocumentsInformation;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.service.interfaces.IBureauIntegrationService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
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

	/**
	 * This api is used to fetch next item from experian report generation
	 * activity session
	 * 
	 * @param experianQuestionAnswer
	 *            contains the question and answer details
	 * @return
	 * @throws Exception
	 * @throws ConflictException
	 *             thrown when user aready present
	 * @throws NotFoundException
	 *             thrown when user not found
	 * @throws PreconditionFailedException
	 *             thrown when any expected response is not got from experian
	 *             system
	 * @throws IOException
	 *             thrown when any IOException occurs in making call to experian
	 *             system
	 * @throws UpdateFailedException
	 * @throws BadRequestException
	 * @throws JAXBException
	 * @throws ParserConfigurationException
	 * @throws FactoryConfigurationError
	 * @throws SAXException
	 */
	@ApiOperation(value = "Fetches next question")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 400, message = "Data not completly filled") })
	@RequestMapping(value = "/experian/question", method = RequestMethod.POST)
	public @ResponseBody ReportInputEntity answerQuestionAndMoveToNext(
			@RequestBody ExperianQuestionAnswer experianQuestionAnswer) throws Exception, ConflictException,
			NotFoundException, PreconditionFailedException, IOException, UpdateFailedException, BadRequestException,
			JAXBException, ParserConfigurationException, FactoryConfigurationError, SAXException {
		return this.experianBureauService.fetchNextItem(experianQuestionAnswer.getQuestionId(),
				experianQuestionAnswer.getOptionSet1Answer(), experianQuestionAnswer.getOptionSet2Answer());
	}

	/**
	 * This api is used to send email to experian for verifing the user kyc
	 * details and get the report
	 * 
	 * @param kycDocumentsInformation
	 *            contains the information about kyc details like aadhar
	 *            details, passport etc
	 * @throws Exception
	 *             thrown when any exception occurs in process of sending email
	 *             to experian
	 * @throws NotFoundException
	 *             thrown If the user is not found
	 * @throws PreconditionFailedException
	 *             thrown when any expected condition doesnot occur like
	 *             expected response from experian etc.
	 * @throws BadRequestException
	 *             thrown when user id not found
	 */
	@ApiOperation(value = "Sends an email to experian")
	@RequestMapping(value = "/experian/email", method = RequestMethod.POST)
	public @ResponseBody void sendExperianEmail(
			@RequestBody GenericListEncapsulationEntity<KycDocumentsInformation> kycDocumentsInformation)
			throws Exception, NotFoundException, PreconditionFailedException, BadRequestException {
		this.experianBureauService.sendEmail(kycDocumentsInformation);
	}

	/**
	 * This api is used to process/parse uploaded experian report file offline
	 * when file id of file is given
	 * 
	 * @param fileId
	 *            the id of the uploaded file
	 * @throws JAXBException
	 * @throws UnauthorizedException
	 *             thrown when user is not authorized to use this api
	 * @throws PreconditionFailedException
	 *             thrown when any expected condition is not met
	 * @throws IOException
	 *             thrown when an IOException occurs while processing file like
	 *             getting file from s3
	 * @throws NotFoundException
	 *             thrown when no file details found for given file id
	 * @throws BadRequestException
	 *             thrown when userId is not found
	 * @throws ConflictException
	 *             thrown when user already exists
	 * @throws ParserConfigurationException
	 *             thrown when any parsing exception occurs
	 * @throws SAXException
	 */
	@ApiOperation(value = "processes an uploaded report")
	@RequestMapping(value = "/experian/processreport/{fileId}", method = RequestMethod.POST)
	public @ResponseBody void processOfflineReport(
			@ApiParam(value = "This is the id of the file", required = true, name = "fileId", allowMultiple = false) @PathVariable String fileId)
			throws JAXBException, UnauthorizedException, PreconditionFailedException, IOException, NotFoundException,
			BadRequestException, ConflictException, ParserConfigurationException, SAXException {
		this.experianBureauService.processOfflineReport(fileId);
	}

}
