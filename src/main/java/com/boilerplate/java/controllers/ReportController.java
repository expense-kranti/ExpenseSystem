package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.java.entities.Report;
import com.boilerplate.service.interfaces.IReportService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This is Report controller having the methods that are executed on the
 * report
 * 
 * @author amit
 *
 */
@Api(value = "Report API's", basePath = "/reports", description = "API's for reports")
@Controller
public class ReportController extends BaseController {

	@Autowired
	IReportService reportService;

	/**
	 * This API is used to get the report for the current logged in user on the
	 * basis of it's id
	 * 
	 * @return reports for the current logged_in user
	 * @throws UnauthorizedException
	 *             This exception occurred if user is not logged in
	 * @throws NotFoundException
	 *             This exception occurred if the required report is not present in
	 *             database
	 */

	@ApiOperation(value = "Gets the report for the current logged in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 401, message = "user not logged in") })
	@RequestMapping(value = "/report", method = RequestMethod.GET)
	public @ResponseBody Report getReport() throws UnauthorizedException, NotFoundException {
		return reportService.getReport();

	}
}
