package com.boilerplate.java.controllers;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.service.interfaces.IFileService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

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
	public @ResponseBody FileEntity upload(
			@ApiParam(value = "The master tag to be applied to the file", required = true, name = "fileMasterTag", allowMultiple = false) @PathVariable String fileMasterTag,
			@RequestParam(value = "The file being uploaded") MultipartFile file)
			throws Exception {
		// find the name of file

		return fileService.saveFile(fileMasterTag, file);
	}
	
	/**
	 * This api gets the files on the basis of file master tag * @return The
	 * file entity list
	 */
	@ApiOperation(value = "Gets all files on the basis of file master tag")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 404, message = "If one of the ciritical servers is not rechable") })
	@RequestMapping(value = "/file/masterTag/{fileMasterTag}", method = RequestMethod.GET)
	public @ResponseBody BoilerplateList<FileEntity> getFileOnMasterTag(@PathVariable String fileMasterTag)
			throws Exception {
		return this.fileService.getAllFileListOnMasterTag(fileMasterTag);
	}
	
	/**
	 * This method gets a given file by Id
	 * 
	 * @param id
	 *            The id of the file
	 * @param response
	 *            The file stream
	 * @throws NotFoundException
	 *             If the file is not found
	 * @throws FileNotFoundException
	 *             If the file is not found
	 * @throws IOException
	 *             If there is an error in IO
	 */
	@ApiOperation(value = "Downlads a file")
	@ApiResponses(value = { @ApiResponse(code = 200, message = "Ok"),
			@ApiResponse(code = 404, message = "If one of the ciritical servers is not rechable") })
	@RequestMapping(value = "/file/{id}", method = RequestMethod.GET)
	public void download(
			@ApiParam(value = "The id of the file", required = true, name = "id", allowMultiple = false) @PathVariable String id,
			HttpServletResponse response) throws Exception, NotFoundException,
			FileNotFoundException, IOException {
		// Redirects to S3 file URL
		String preSignedS3Url = fileService.getPreSignedS3URL(id);
		response.sendRedirect(preSignedS3Url);
	}

}
