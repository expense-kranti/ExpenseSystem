package com.boilerplate.java.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.java.entities.ArticleEntity;
import com.boilerplate.service.interfaces.IReferralService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on user referral.
 * 
 * @author shiva
 *
 */
@Api(description = "This controller has api for operate referral", value = "Referral API's", basePath = "/referral")
public class ReferralController extends BaseController {
	
	IReferralService referralService;
	
	/**
	 * This API is used to get all the referral contact list refered by user in current date 
	 * 
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the user articles
	 */
	@ApiOperation(value = "Get the all the user articles which is saved by user in our data store")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/userArticle", method = RequestMethod.GET)
	public @ResponseBody List<ArticleEntity> getAssesments() throws Exception {
		// Get the all user articles
		return referralService.getUserArticle();
	}
	
	
	/**
	 * This API is used to save the user articles into the data store.
	 * 
	 * @param articleEntity
	 *            this parameter contains the articles details, details
	 *            basically contain the article title and article content
	 * 
	 * @throws Exception
	 *             throw this exception in case of any error while trying to
	 *             save the user articles into the data store
	 */
	@ApiOperation(value = "This api is used to save the user articles details, details basically contain the article title and article content")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/userArticle", method = RequestMethod.POST)
	public @ResponseBody void checkExistance(@RequestBody ArticleEntity articleEntity) throws Exception {
		// Save the user article
		referralService.saveUserArticle(articleEntity);
	}
	
	
	
}
