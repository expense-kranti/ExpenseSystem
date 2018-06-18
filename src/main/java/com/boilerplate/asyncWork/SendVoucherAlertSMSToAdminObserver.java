package com.boilerplate.asyncWork;

import java.net.URLEncoder;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.Logger;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.ISendSMSService;

/**
 * This class sends a SMS when user password is changed
 * @author gaurav.verma.icloud
 *
 */
public class SendVoucherAlertSMSToAdminObserver implements IAsyncWorkObserver {
	
	/**
	 * This is the logger instance of the SendVoucherAlertEmailToAdmin observer
	 */
	private Logger logger = Logger.getInstance(SendVoucherAlertSMSToAdminObserver.class);
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
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		//This method sends voucher alert SMS to admin
		this.sendSMS(asyncWorkItem.getPayload().toString());
	}

	/**
	 * This method sends the SMS.
	 * The reason we are keeping it public is so that
	 * compensating sync code may be written if queues go down
	 * @param firstName The first name of the user
	 * @param The phone number
	 * @throws Exception If there is an error in processing
	 */
	public void sendSMS(String voucherCount) throws Exception{
		String phoneNumber = configurationManager.get("Admin_Contact_Number");
		String message = configurationManager.get("Voucher_Count_Message_Template");
		message = message.replace("@VoucherCount",voucherCount);
		String url = configurationManager.get("SMS_ROOT_URL")+configurationManager.get("SMS_URL");
		url = url.replace("@apiKey", configurationManager.get("SMS_API_KEY"));
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@to", phoneNumber);
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@message",URLEncoder.encode(message));
		sendSmsService.send(url, phoneNumber, message);
	}
}
