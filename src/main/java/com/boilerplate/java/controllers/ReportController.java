package com.boilerplate.java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.java.entities.Report;
import com.boilerplate.service.interfaces.IReportService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This is Report controller having the methods that are executed on the report
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
	 * This API is used to get the latest report for the current logged in user
	 * 
	 * @return report for the current logged_in user
	 * @throws Exception
	 *             This exception occurred when any exception occurred
	 */

	@ApiOperation(value = "Gets the latest report for the current logged in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 401, message = "user not logged in") })
	@RequestMapping(value = "/report/latestReport", method = RequestMethod.GET)
	public @ResponseBody Report getLatestReport() throws Exception {
		return reportService.getLatestReport();

	}

	/**
	 * This API is used to get the report for the given reportId
	 * 
	 * @return report for the given report id
	 * @throws NotFoundException
	 *             This exception occurred if the data not found in database
	 * @throws Exception
	 *             This exception occurred if any exception occurred
	 */

	@ApiOperation(value = "Gets the report for the given report Id")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/report/reportId/{reportId}", method = RequestMethod.GET)
	public @ResponseBody Report getReportById(
			@ApiParam(value = "The reportId for getting report", required = true, name = "ReportId", allowMultiple = false) @PathVariable String reportId)
			throws NotFoundException, Exception {
		return reportService.getReportById(reportId);
	}
}
