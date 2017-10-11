package com.boilerplate.asyncWork;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.ISendSMSService;
import com.boilerplate.service.interfaces.IUserService;

/**
 * This class sends a SMS when user requests password reset
 * @author gaurav.verma.icloud
 *
 */
public class SendPasswordResetSMSObserver implements IAsyncWorkObserver {
	
	/**
	 * this is the send sms service
	 */
	@Autowired
	ISendSMSService sendSmsService;

	/**
	 * This method set the sms service.
	 * @param sendSmsService the sendSmsService to set
	 */
	public void setSendSmsService(ISendSMSService sendSmsService) {
		this.sendSmsService = sendSmsService;
	}
	
	/**
	 * This is the content service.
	 */
	@Autowired
	IContentService contentService;
	
	/**
	 * This sets the content service
	 * @param contentService This is the content service
	 */
	public void setContentService(IContentService contentService){
		this.contentService = contentService;
	}
	@Autowired
	IUserService userService;
	
	/**
	 * sets the user service
	 * @param userService The user service
	 */
	public void setUserService(IUserService userService){
		this.userService = userService;
	}
	/**
	 * This is the configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;
	
	/**
	 * This sets the configuration Manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager){
		this.configurationManager = configurationManager;
	}
	
	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		BoilerplateMap<String, String> userPasswordMap = (BoilerplateMap) asyncWorkItem.getPayload();
		ExternalFacingUser externalFacingUser = (ExternalFacingUser)this.userService.get(userPasswordMap.get("userId"));
		this.sendSMS(externalFacingUser.getFirstName(), userPasswordMap.get("password"),externalFacingUser.getPhoneNumber());
		
	}

	/**
	 * This method sends the SMS.
	 * The reason we are keeping it public is so that
	 * compensating sync code may be written if queues go down
	 * @param firstName The first name of the user
	 * @param password The password
	 * @param The phone number
	 * @throws Exception If there is an error in processing
	 */
	public void sendSMS(String firstName, String password,String phoneNumber) throws Exception{
		String url = configurationManager.get("SMS_ROOT_URL")+configurationManager.get("SMS_URL");
		url = url.replace("@apiKey", configurationManager.get("SMS_API_KEY"));
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@to", phoneNumber);
		String message = contentService.getContent("RESET_PASSWORD_SMS");
		message = message.replace("@FirstName",firstName);
		message = message.replace("@Password",password);
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@message",URLEncoder.encode(message));
		String response=null;
		HttpResponse smsGatewayResponse= sendSmsService.send(url, phoneNumber, message);
	}
	
}
