package com.boilerplate.java.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.java.entities.BlogActivityEntity;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on assessment.
 * 
 * @author Love
 *
 */
@Api(description = "This controller has api for operate the blog activities", value = "Blog API's", basePath = "/blog")
@Controller
public class BlogActivityController {
	
//	@Autowired
//	IBlogActivityService blogActivityService;
	
	/**
	 * This method saves the user's blog activity
	 * @param blogActivityEntity This contains the blog activity and blog action
	 */
	@ApiOperation(value = "This api is used to save the user blog activity , activity basically contain the activity and activity action")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/blog/activity", method = RequestMethod.POST)
	public @ResponseBody void saveActivity
				(@RequestBody BlogActivityEntity blogActivityEntity){
		// Save the user article
		//blogActivityService.saveActivity(blogActivityEntity);
	}

}
