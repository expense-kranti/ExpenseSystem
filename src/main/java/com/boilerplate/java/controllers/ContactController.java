package com.boilerplate.java.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.framework.EmailUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ContactUsEntity;
import com.boilerplate.service.implemetations.UserService;
import com.boilerplate.service.interfaces.IContactUsService;
import com.wordnik.swagger.annotations.ApiOperation;
@Controller
public class ContactController extends BaseController {
	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(ContactController.class);
	
	@Autowired
	IContactUsService contactUsService;
	
	@ApiOperation(value ="Sends an Email to Right Responsible Person")
	@RequestMapping(value="/contact/contactUs", method=RequestMethod.POST)
	public @ResponseBody void contactUs(@RequestBody ContactUsEntity contactUsEntity){
		this.contactUsService.contactUs(contactUsEntity);
	}
	
	@RequestMapping(value="/contact/email", method=RequestMethod.POST)
	public @ResponseBody void contactUsEmail() throws Exception{
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();

		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
		tosEmailList.add("love.kranti@clearmydues.com");
		String subject = "SES Email Test";
		String body ="Test Email";
		try{
			EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, null);
		}
		catch (Exception e) {
			logger.logException("ContactController", "contactUsEmail", "", "",e );
			throw e;
		}
		
		
		
	}
	
	
}
