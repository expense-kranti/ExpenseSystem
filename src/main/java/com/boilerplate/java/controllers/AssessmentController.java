package com.boilerplate.java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
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
	 * This method is used to get the assessment details regarding the
	 * assessment id
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment details like assessment
	 *            id
	 * @return the assessment data
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the assessment data
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/attemptAssessment", method = RequestMethod.POST)
	public @ResponseBody AssessmentEntity authenticate(@RequestBody AssessmentEntity assessmentEntity)
			throws Exception {
		// Get the assessment data
		return assessmentService.getAssessment(assessmentEntity);
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/assessment", method = RequestMethod.GET)
	public @ResponseBody List<AssessmentEntity> getAssesment()throws Exception {
				return assessmentService.getAssessment();
	}
	
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/attemptAssessment", method = RequestMethod.GET)
	public @ResponseBody AttemptAssessmentListEntity getAssessmentAttempt()throws NotFoundException{
				return assessmentService.getAssessmentAttempt();
	}
	
}
