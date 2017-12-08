package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.controllers.BaseController;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.FeedBackEntity;
import com.boilerplate.service.interfaces.IFeedbackService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on user selected feature as its feedback
 * 
 * @author urvij
 *
 */
@Api(description = "This api has controllers for user selected feedback", value = "Feedback API", basePath = "/feedback")
@Controller
public class FeedbackController extends BaseController {

	/**
	 * This is the autowired instance of feedback service
	 */
	@Autowired
	IFeedbackService feedbackService;

	/**
	 * This method sends email on user feature selection as feedback submit by
	 * user
	 * 
	 * @param feedBackEntity
	 *            This contains the selected feature by user as user feedback
	 * @return ExternalFacingReturnedUser The external facing user with updated
	 *         feedback submit state
	 * @throws ConflictException
	 * @throws NotFoundException
	 */
	@ApiOperation(value = "sends email to user with selected feature given in feedback")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/feedBack", method = RequestMethod.POST)
	public @ResponseBody ExternalFacingReturnedUser sendEmailOnFeedbackSubmit(
			@RequestBody FeedBackEntity feedbackEntity) throws NotFoundException, ConflictException {
		return feedbackService.sendEmailOnFeedbackByBackGroundJob(feedbackEntity);
	}
}
