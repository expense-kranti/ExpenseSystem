package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.java.entities.ContactUsEntity;
import com.boilerplate.service.interfaces.IContactUsService;
import com.wordnik.swagger.annotations.ApiOperation;
@Controller
public class ContactController extends BaseController {
	@Autowired
	IContactUsService contactUsService;
	
	@ApiOperation(value ="Sends an Email to Right Responsible Person")
	@RequestMapping(value="/contact/contactUs", method=RequestMethod.POST)
	public @ResponseBody void contactUs(@RequestBody ContactUsEntity contactUsEntity){
		this.contactUsService.contactUs(contactUsEntity);
	}
}
