package com.boilerplate.java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.NotFoundException;
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
	 * This API is used to get the assessment data regarding the assessment id
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
	public @ResponseBody AssessmentEntity attemptAssessment(@RequestBody AssessmentEntity assessmentEntity)
			throws Exception {
		// Get the assessment data
		return assessmentService.getAssessment(assessmentEntity);
	}

	/**
	 * This API is used to get the assessments
	 * 
	 * @return the assessments
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the assessments
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/assessments", method = RequestMethod.GET)
	public @ResponseBody List<AssessmentEntity> getAssesments() throws Exception {
		// Get the all assessments
		return assessmentService.getAssessments();
	}

	/**
	 * This API is used to get the user assessment attempt details
	 * 
	 * @return the user assessment attempt details
	 * @throws NotFoundException
	 *             throw this exception if there is no user assessment attempt
	 *             found
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/attemptAssessment", method = RequestMethod.GET)
	public @ResponseBody AttemptAssessmentListEntity getAssessmentAttempt() throws NotFoundException {
		// Get the assessment attempt details
		return assessmentService.getAssessmentAttempt();
	}

	/**
	 * This API is used to save the assessment data to data store
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data store
	 * @throws Exception
	 *             throw this exception in case of any error while trying to
	 *             save the assessment data to data store
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/assessment", method = RequestMethod.POST)
	public void saveAssesment(@RequestBody AssessmentEntity assessmentEntity) throws Exception {
		// Save the assessment data
		assessmentService.saveAssesment(assessmentEntity);
	}

	/**
	 * This API is used to save the assessment data to data store after change the
	 * assessment status to submit.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data store
	 * @throws Exception
	 *             throw this exception in case of any error while trying to
	 *             save the assessment data to data store
	 */
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/submitAssessment", method = RequestMethod.POST)
	public void submitAssesment(@RequestBody AssessmentEntity assessmentEntity) throws Exception {
		// Save the assessment data
		assessmentService.submitAssesment(assessmentEntity);
	}
}
