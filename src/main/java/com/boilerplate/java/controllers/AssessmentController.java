package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.service.interfaces.IAssessmentService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on assessment.
 * 
 * @author love
 *
 */
@Api(description = "This api has controllers for operate assessment", value = "Assessment API's", basePath = "/assessment")
@Controller
public class AssessmentController extends BaseController {
	
	/**
	 * This is the instance of the assessment service
	 */
	@Autowired
	IAssessmentService assessmentService;

	/**
	 * 
	 * @param assessmentEntity
	 * @return
	 * @throws UnauthorizedException
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/assessment", method = RequestMethod.POST)
	public @ResponseBody AssessmentEntity authenticate(@RequestBody AssessmentEntity assessmentEntity)
			throws Exception {
				return assessmentService.getAssessment(assessmentEntity);
	}
}
