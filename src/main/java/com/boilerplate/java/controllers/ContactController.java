package com.boilerplate.java.controllers;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.ContactUsEntity;
import com.boilerplate.java.entities.EmailEntity;
import com.boilerplate.service.interfaces.IContactUsService;
import com.wordnik.swagger.annotations.ApiOperation;
import java.util.Properties;

import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;

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
	public @ResponseBody void contactUsEmail(@RequestBody EmailEntity emailEntity) throws Exception{
		final String username =emailEntity.getUsername();
		final String password = emailEntity.getPassword();

		Properties props = new Properties();
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.host", emailEntity.getSmtpHost());
		props.put("mail.smtp.port", emailEntity.getSmtpPort());

		Session session = Session.getInstance(props,
		  new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		  });

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(emailEntity.getSmtpFrom()));
			message.setRecipients(Message.RecipientType.TO,
				InternetAddress.parse(emailEntity.getToemail()));
			message.setSubject("Testing Subject");
			message.setText("Dear Mail Crawler,"
				+ "\n\n No spam to my email, please!");

			Transport.send(message);

			System.out.println("Done");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		
		
		
	}
	
	
}
