package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.service.interfaces.IVoucherService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * This class provides methods to manage vouchers like storing vouchers in DB from json or file
 * @author urvij
 *
 */
@Api(value = "Voucher Upload API", basePath = "/voucher", description = "This API provides methods to upload Vouchers by csv file")
@Controller
public class VoucherController extends BaseController{
	
	/**
	 * This is the instance of voucherService
	 */
	@Autowired
	IVoucherService voucherService;
	
	/**
	 * This method uploads Vouchers to DB from Csv File
	 * @param fileId The fileId
	 */
	@ApiOperation(value = "This method uploads Vouchers to DB from Csv File")
	@RequestMapping(value = "/voucher/{fileId}", method = RequestMethod.POST)
	public @ResponseBody void voucherUploadFromFile(@ApiParam(value="This is the file Id from which Vouchers is to pick and store in DB", required = true,
	name = "fileId", allowMultiple = false)@PathVariable String fileId ) throws Exception{
		voucherService.voucherUploadFromCSV(fileId);
	}

}
