package com.boilerplate.java.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.CheckListEntity;
import com.boilerplate.service.interfaces.ICheckListService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This class contains api for getting the checklist and saving checklist
 * 
 * @author urvij
 *
 */

@Api(description = "This api has controllers for saving and getting checklist", value = "CheckList API's", basePath = "/checklist")
@Controller
public class CheckListController extends BaseController {

	/**
	 * This is the instance of checklist service
	 */
	@Autowired
	ICheckListService checkListService;

	/**
	 * Sets the checklist service
	 * 
	 * @param checkListService
	 *            the checkListService to set
	 */
	public void setCheckListService(ICheckListService checkListService) {
		this.checkListService = checkListService;
	}

	/**
	 * This method is used to save checklist map against logged in user
	 *
	 * @param checkListEntity
	 *            contains the checklistmap to save
	 * @return the checklistentity with the user id of user against whom
	 *         checklist is saved
	 * @throws ValidationFailedException
	 *             thrown if input checklistmap is null or empty
	 * @throws UnauthorizedException
	 *             thrown if user is not logged in
	 */
	@ApiOperation(value = "Save the checkList with elements and their values against logged in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/checkList", method = RequestMethod.POST)
	public @ResponseBody CheckListEntity saveCheckList(@RequestBody CheckListEntity checkListEntity)
			throws ValidationFailedException, UnauthorizedException {
		// call service layer
		return checkListService.save(checkListEntity);
	}

	/**
	 * This method is used to get the checklist against the logged in user
	 * 
	 * @return the checklist entity containing the checklistmap against the
	 *         logged in user
	 * @throws UnauthorizedException
	 *             thrown if user is not logged in
	 * @throws NotFoundException
	 *             thrown when no checklist map is found against the logged in
	 *             user
	 */
	@ApiOperation(value = "Gets the checkList with elements and their values against logged in user")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"), @ApiResponse(code = 404, message = "Not Found") })
	@RequestMapping(value = "/checkList", method = RequestMethod.GET)
	public @ResponseBody CheckListEntity getCheckList() throws UnauthorizedException, NotFoundException {
		// call service layer
		return checkListService.getCheckList();
	}

}
