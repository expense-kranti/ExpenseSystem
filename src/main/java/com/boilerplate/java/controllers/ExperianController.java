package com.boilerplate.java.controllers;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.service.interfaces.IExperianService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class encapsulates the api's for experian integration
 * @author gaurav.verma.icloud
 *
 */
@Api(value="API's for Experian integration"
	,basePath="/experian",description="API's for experian integration")
@Controller
public class ExperianController extends BaseController {

	@Autowired
	IExperianService experianBureauService;
	
	/**
	 * This stars the experian integration process
	 * @param reportInputEntity It contains initial inputs for fetching the report
	 * @throws NotFoundException 
	 * @throws BadRequestException thrown when input is not provided properly
	 */
	@ApiOperation(	value="Starts the experian session"
		 )
	@ApiResponses(value={
					@ApiResponse(code=200, message="Ok")
				,	@ApiResponse(code=400, message="Data not completly filled")
				})
	@RequestMapping(value = "/experian/startSingle", method = RequestMethod.POST)
	public @ResponseBody ReportInputEntity startSingle(@RequestBody ReportInputEntity reportInputEntity) throws Exception, ValidationFailedException,ConflictException, NotFoundException,IOException,PreconditionFailedException, BadRequestException,UnauthorizedException{
		return this.experianBureauService.startSingle(reportInputEntity);
	}

}
