package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.WordpressDataEntity;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.service.interfaces.IStatisticsService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This controller contains apis for getting the various types of statistics
 * like article counts, user referred sign up contacts
 * 
 * @author urvij
 *
 */
@Api(description = "This api has controllers for getting statistical data from various modules in the system like articles etc", value = "Statistics API's", basePath = "/statistics")
@Controller
public class StatisticsController extends BaseController {

	// TODO give dependency in root context
	/**
	 * This is the instance of the user service
	 */
	@Autowired
	IStatisticsService statisticsService;

	/**
	 * This api is used to get the wordpress required details for statistics
	 * 
	 * @return the WordpressDataEntity containing the required statistical
	 *         wordpress details
	 * @throws BadRequestException
	 *             thrown when wrong query mechanismm is used to mysql database
	 */
	@ApiOperation(value = "Used to get wordpress details")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/statistics/wordPress", method = RequestMethod.GET)
	public @ResponseBody WordpressDataEntity getWordPressDataStatistics() throws BadRequestException, Exception {
		// call service layer
		return statisticsService.getWordPressStatistics();
	}
}
