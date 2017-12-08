package com.boilerplate.java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentQuestionSectionEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.QuestionEntity;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.java.entities.TopScorerEntity;
import com.boilerplate.service.interfaces.IAssessmentService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on assessment.
 * 
 * @author love
 *
 */
@Api(description = "This controller has api for operate assessment", value = "Assessment API's", basePath = "/assessment")
@Controller
public class AssessmentController extends BaseController {

	/**
	 * This is the instance of the assessment service
	 */
	@Autowired
	IAssessmentService assessmentService;

	/**
	 * This API is used to get the assessment data regarding the assessment id
	 * from the data store
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment details like assessment
	 *            id
	 * @return the assessment data like assessment id, assessment
	 *         section,assessment questions etc.
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the assessment data
	 */
	@ApiOperation(value = "This API will get the assessment data regarding the assessment id ,When user want to get specific assessment data then this api will get the assessment data for any valid assessment id,valid means assessment is available for user or not,assessment data contains all the sections and each section conatins some questions")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/attemptAssessment", method = RequestMethod.POST)
	public @ResponseBody AssessmentEntity attemptAssessment(@RequestBody AssessmentEntity assessmentEntity)
			throws Exception {
		// Get the assessment data
		return assessmentService.getAssessment(assessmentEntity);
	}

	/**
	 * This API is used to get the all assessment which is exist in our system.
	 * 
	 * @return the list of all assessment exist in system and available for user
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the assessments
	 */
	@ApiOperation(value = "Get the all the assessments which is available for the current user,only assessment details means like assessment name,assessment id etc.,not questions data")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/assessments", method = RequestMethod.GET)
	public @ResponseBody List<AssessmentEntity> getAssesments() throws Exception {
		// Get the all assessments
		return assessmentService.getAssessments();
	}

	/**
	 * This API is used to get the user attempted assessment details means all
	 * those assessment which is attempted by user in past
	 * 
	 * @return the user attempted assessment details means all those assessment
	 *         which was attempt by user in past
	 * @throws NotFoundException
	 *             throw this exception if user has never attempt any assessment
	 *             before
	 */
	@ApiOperation(value = "Get the user attemp assessment details,this details basically contain all those assessment which alreay attemped by user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/attemptAssessment", method = RequestMethod.GET)
	public @ResponseBody AttemptAssessmentListEntity getAssessmentAttempt() throws NotFoundException {
		// Get the assessment attempt details
		return assessmentService.getAssessmentAttempt();
	}

	/**
	 * This API is used to save the assessment data to data store ,assessment
	 * data like assessment id, assessment section,assessment questions etc.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data store ,assessment data like assessment id,
	 *            assessment section,assessment questions etc.
	 * @throws Exception
	 *             throw this exception in case of any error while trying to
	 *             save the assessment data to data store
	 */
	@ApiOperation(value = "Save the assessment data,assessment data which also contain the answer of the assessment question submitted by user", notes = "Data shold be correct")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/assessment", method = RequestMethod.POST)
	public @ResponseBody void saveAssesment(@RequestBody AssessmentEntity assessmentEntity) throws Exception {
		// Save the assessment data
		assessmentService.saveAssesment(assessmentEntity);
	}

	/**
	 * This API is used to save the assessment data to data store after change
	 * the assessment status to submit, assessment data like assessment id,
	 * assessment section,assessment questions etc.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data store and has information like assessment id,
	 *            assessment section,assessment questions etc.
	 * @throws Exception
	 *             throw this exception in case of any error while trying to
	 *             save the assessment data to data store
	 * 
	 * @return the assessment data which is now contains the user total score
	 *         for this assessment and count of question count of correct
	 *         question
	 */
	@ApiOperation(value = "Submit the assessment data,means save the assessment data but after this user is not able to change this assessment data any more ", notes = "Data shold be correct,and after submit user can't be able to re attemp this assessment")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/submitAssessment", method = RequestMethod.POST)
	public @ResponseBody AssessmentEntity submitAssesment(@RequestBody AssessmentEntity assessmentEntity)
			throws Exception {
		// Save the assessment data
		return assessmentService.submitAssesment(assessmentEntity);
	}

	/**
	 * This API is used to get the all survey which is exist in our system.
	 * 
	 * @return the list of all survey exist in system and available for user
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the surveys
	 */
	@ApiOperation(value = "Get all the surveys which is available for the current user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/survey", method = RequestMethod.GET)
	public @ResponseBody List<AssessmentEntity> getSurveys() throws Exception {
		// Get the all assessments
		return assessmentService.getSurveys();
	}

	/**
	 * This API is used to get user total score.
	 * 
	 * @return the user total score
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             user total score
	 */
	@ApiOperation(value = "Get user score")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/score", method = RequestMethod.GET)
	public @ResponseBody ScoreEntity getTotalScore() throws Exception {
		// Get the all assessments
		return assessmentService.getTotalScore();
	}

	/**
	 * This API is used to validate the question answer is answer is correct or
	 * not.
	 * 
	 * @param assessmentQuestionSectionEntity
	 *            this parameter contains the question data like section id,
	 *            question id,question type ,answer etc.
	 * @throws Exception
	 *             throw this exception in case of any error while trying to
	 *             save the assessment data to data store
	 * 
	 * @return the assessment question section entity which is now also contain
	 *         the explanation and answer status is correct or not
	 */
	@ApiOperation(value = "Validate the answer is it correct or not", notes = "")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/validateAnswer", method = RequestMethod.POST)
	public @ResponseBody AssessmentQuestionSectionEntity validateAnswer(
			@RequestBody AssessmentQuestionSectionEntity assessmentQuestionSectionEntity) throws Exception {
		// Validate the answer
		return assessmentService.validateAnswer(assessmentQuestionSectionEntity);
	}
	
	/**
	 * This API is used to get the user attempted assessment details means all
	 * those assessment which is attempted by user in past
	 * 
	 * @return the user attempted assessment details means all those assessment
	 *         which was attempt by user in past
	 * @throws NotFoundException
	 *             throw this exception if user has never attempt any assessment
	 *             before
	 */
	@ApiOperation(value = "Get the top 10 scorrer")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/topScorrer", method = RequestMethod.GET)
	public @ResponseBody BoilerplateList<TopScorerEntity> getTopScorrer() throws NotFoundException {
		// Get the assessment attempt details
		return assessmentService.getTopScorrer();
	}

}
