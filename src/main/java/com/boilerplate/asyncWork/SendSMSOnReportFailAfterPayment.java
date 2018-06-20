package com.boilerplate.asyncWork;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.ISendSMSService;
import com.boilerplate.service.interfaces.IUserService;

public class SendSMSOnReportFailAfterPayment  implements IAsyncWorkObserver {
	
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
	 * This is the user service
	 */
	@Autowired
	IUserService userService;
	
	/**
	 * Set the user service
	 * @param userService
	 */
	public void setUserService(IUserService userService){
		this.userService = userService;
	}
	
	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		ExternalFacingReturnedUser externalFacingReturnedUser = (ExternalFacingReturnedUser)asyncWorkItem.getPayload();
		sendSMS(externalFacingReturnedUser);
	}
	
	/**
	 * sends a sucessful report received message to user
	 * @param userId The login id of the user
	 */
	public void sendSMS(ExternalFacingReturnedUser user) throws Exception{
		String phoneNumber = user.getPhoneNumber();
		if (user.getDsaId() !=null){
			phoneNumber = getDsaPhoneNumber(user.getDsaId());
		}
		String firstName = user.getFirstName();
		String url = configurationManager.get("SMS_ROOT_URL")+configurationManager.get("SMS_URL");
		url = url.replace("@apiKey", configurationManager.get("SMS_API_KEY"));
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@to", phoneNumber);
		String message = contentService.getContent("REPORT_FAIL_AFTER_PAYMENT_SMS_BODY");
		message = message.replace("@FirstName",firstName);
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@message",URLEncoder.encode(message));
		String response=null;
		HttpResponse smsGatewayResponse= sendSmsService.send(url, phoneNumber, message);
	}
	
	/**
	 * This method gets the DSA phone Number
	 * @param dsaId The dsaId of DSA
	 * @return The DSA Phone Number
	 */
	private String getDsaPhoneNumber(String dsaId){
		String[] keySplitArray = dsaId.split(":");
		return keySplitArray[1];
	}
}
