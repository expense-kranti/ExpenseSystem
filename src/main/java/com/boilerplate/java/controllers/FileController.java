package com.boilerplate.java.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.java.entities.FileMappingEntity;
import com.boilerplate.service.interfaces.IFileService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * This controller has apis for file related CRUD operations
 * 
 * @author ruchi
 *
 */
@Api(value = "File API's", basePath = "/file", description = "API's for file and content management")
@Controller
public class FileController extends BaseController {
	/**
	 * This is the file service
	 */
	@Autowired
	IFileService fileService;

	/**
	 * This method uploads a file, saves it attributes and metadata and assignes
	 * owner to the file (the logged in user)
	 * 
	 * @param fileName
	 *            The name of the file
	 * @param file
	 *            The file
	 * @param fileMetaProperties
	 *            The metadata associated with the file
	 * @throws Exception
	 * @returns the Id of the file for fetching in future
	 */
	@ApiOperation(value = "Uploads a file")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 404, message = "If one of the ciritical servers is not rechable") })
	@RequestMapping(value = "/file/{fileMasterTag}", method = RequestMethod.POST)
	public @ResponseBody FileMappingEntity upload(
			@ApiParam(value = "The master tag to be applied to the file", required = true, name = "fileMasterTag", allowMultiple = false) @PathVariable String fileMasterTag,
			@RequestParam(value = "The file being uploaded") MultipartFile file) throws Exception {
		// call service layer
		return fileService.saveFileOnLocal(fileMasterTag, file);
	}

	/**
	 * This API is used to download a file
	 * 
	 * @param request
	 *            This is the http request
	 * @param response
	 *            this is the repsonse containing the file
	 * @param fileName
	 *            This is the file name
	 * @throws BadRequestException
	 *             Throw this exception if user sends bad request
	 * @throws NotFoundException
	 *             Throw this exception if file mapping or file is not found
	 * @throws UnauthorizedException
	 */
	@ApiOperation(value = "Donloads a file")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 404, message = "If file or file mapping is not found") })
	@RequestMapping(value = "/downoad/{fileName:.+}", method = RequestMethod.GET)
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName)
			throws BadRequestException, NotFoundException, UnauthorizedException {
		fileService.downloadFile(request, response, fileName);
	}
}
