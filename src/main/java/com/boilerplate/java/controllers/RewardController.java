package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.RewardEntity;
import com.boilerplate.service.interfaces.IRewardService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on reward entity that contains details of
 * the reward winning user.
 * 
 * @author urvij
 *
 */
@Api(description = "This api has controller for doing operations on rewards related data such as sending reward winning user's details in email", value = "Reward API's", basePath = "/reward")
@Controller
public class RewardController extends BaseController {

	/**
	 * This is the autowired instance of the reward service
	 */
	@Autowired
	IRewardService rewardService;

	/**
	 * This method is used to send reward winning user's details in email like
	 * its name, phoneNumber, emailId
	 * 
	 * @param rewardEntity
	 *            The rewardEntity that contains name, phoneNumber, emailId
	 * @throws ValidationFailedException
	 *             Thrown when name, phoneNumber, emailId is null or empty
	 */
	@ApiOperation(value = "sends reward winning user's details in email")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/reward", method = RequestMethod.POST)
	public @ResponseBody void sendRewardWinningUserDetailsInEmail(@RequestBody RewardEntity rewardEntity)
			throws ValidationFailedException {
		rewardService.sendRewardWinningUserDetailsInEmail(rewardEntity);
	}

}
