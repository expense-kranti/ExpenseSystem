package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ModuleEntity;
import com.boilerplate.java.entities.ModuleQuizEntity;
import com.boilerplate.service.interfaces.IModuleService;
import com.boilerplate.service.interfaces.IScriptsService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

@Api(description = "This api has controllers for Lessons CRUD operations", value = "Lesson APIs", basePath = "/lesson")
@Controller
public class LessonsController extends BaseController {

	/**
	 * This is the instance of moduleService
	 */
	@Autowired
	IModuleService moduleService;

	/**
	 * This API is used to create a new module
	 * 
	 * @param module
	 *            This is the new module entity to be saved
	 * @return The saved module entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation
	 */
	@ApiOperation(value = "Creates a new module entity in the system", notes = "A module contains sub modules, The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, some attributes are missing") })
	@RequestMapping(value = "/lesson", method = RequestMethod.POST)
	public @ResponseBody ModuleEntity createModule(@RequestBody ModuleEntity module)
			throws BadRequestException, ValidationFailedException {
		// call the business layer
		return moduleService.createModule(module);
	}

	/**
	 * This API is used o update a module
	 * 
	 * @param module
	 *            This is the module to be updated
	 * @return Saved module entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws ValidationFailedException
	 *             Throw this exception if entity fails any validation
	 * @throws NotFoundException
	 *             Throw this exception if module is not found or does not exist
	 */
	@ApiOperation(value = "Updates an existing module entity in the system", notes = "A module contains sub modules, The creation date and updated date are automatically filled.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, some attributes are missing") })
	@RequestMapping(value = "/lesson", method = RequestMethod.PUT)
	public @ResponseBody ModuleEntity updateModule(@RequestBody ModuleEntity module)
			throws BadRequestException, ValidationFailedException, NotFoundException {
		// call the business layer
		return moduleService.updateModule(module);
	}

	/**
	 * This API is used to create a new module
	 * 
	 * @param module
	 *            This is the new module quiz entity to be saved
	 * @return The saved module quiz entity
	 * @throws Exception
	 *             thrown when any exception occurs in saving module entity
	 */
	@ApiOperation(value = "Creates a new module quiz entity in the system", notes = "A module quiz is associated to a module.")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 400, message = "Bad request, some required fields are missing") })
	@RequestMapping(value = "/lesson/moduleQuiz", method = RequestMethod.POST)
	public @ResponseBody ModuleQuizEntity createModuleQuiz(@RequestBody ModuleQuizEntity moduleQuiz) throws Exception {
		// call the business layer
		return moduleService.createModuleQuiz(moduleQuiz);
	}

}
