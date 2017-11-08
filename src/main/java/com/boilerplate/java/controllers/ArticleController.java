package com.boilerplate.java.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ArticleEntity;
import com.boilerplate.service.interfaces.IArticleService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class has methods to operate on assessment.
 * 
 * @author shiva
 *
 */
@Api(description = "This controller has api for operate articles", value = "Articles API's", basePath = "/article")
@Controller
public class ArticleController extends BaseController {

	/**
	 * This is the instance of the assessment service
	 */
	@Autowired
	IArticleService articleService;

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
	public @ResponseBody void attemptAssessment(@RequestBody ArticleEntity articleEntity) throws Exception {
		// Save the user article
		articleService.saveUserArticle(articleEntity);
	}

	/**
	 * This API is used to get all the user articles which is saved by user in
	 * our data store.
	 * 
	 * @return the list of all the user articles which is saved by user in our
	 *         data store.
	 * @throws Exception
	 *             throw this exception in case of any error while trying to get
	 *             the user articles
	 */
	@ApiOperation(value = "Get the all the user articles which is saved by user in our data store")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/userArticle", method = RequestMethod.GET)
	public @ResponseBody List<ArticleEntity> getAssesments() throws Exception {
		// Get the all user articles
		return articleService.getUserArticle();
	}

	/**
	 * This API is used to change article approve status to approved.
	 * 
	 * @param articleEntity
	 *            this parameter contains the articles details, details
	 *            basically contain the userId and article Id
	 * 
	 * @throws Exception
	 *             throw this exception in case of any error while trying to
	 *             change article approved status to approved.
	 */
	@ApiOperation(value = "This api is used to to change article approve status to approved")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/approveArticle", method = RequestMethod.POST)
	public @ResponseBody void approveArticle(@RequestBody ArticleEntity articleEntity) throws Exception {
		// approve user article
		articleService.approveArticle(articleEntity);
	}
}
