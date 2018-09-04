package com.boilerplate.java.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.AttachmentEntity;
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
	public @ResponseBody AttachmentEntity upload(
			@ApiParam(value = "The master tag to be applied to the file", required = true, name = "fileMasterTag", allowMultiple = false) @PathVariable String fileMasterTag,
			@RequestParam(value = "The file being uploaded") MultipartFile file) throws Exception {
		// call service layer
		return fileService.saveFileOnLocal(fileMasterTag, file);
	}

	@RequestMapping(value = "/pdf/{fileName:.+}", method = RequestMethod.GET)
	public void downloadPDFResource(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) {
		// If user is not authorized - he should be thrown out from here
		// itself

		// Authorized user will download the file
		// String dataDirectory =
		// request.getServletContext().getRealPath("/home/ruchi/Downloads/");
		String dataDirectory = "/home/ruchi/Downloads/";
		Path file = Paths.get(dataDirectory, fileName);
		if (Files.exists(file)) {
			response.setContentType("application/pdf");
			response.addHeader("Content-Disposition", "attachment; filename=" + fileName);
			try {
				Files.copy(file, response.getOutputStream());
				response.getOutputStream().flush();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
}
