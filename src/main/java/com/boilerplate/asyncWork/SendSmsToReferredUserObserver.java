package com.boilerplate.asyncWork;

import java.net.URLEncoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.ISendSMSService;

/**
 * This class sends a SMS when a user is referred to the website
 * 
 * @author kranti123
 *
 */
public class SendSmsToReferredUserObserver implements IAsyncWorkObserver {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(SendEmailToReferredUserObserver.class);

	/**
	 * This is the wired content service instance
	 */
	@Autowired
	IContentService contentService;

	/**
	 * This sets the content service
	 * 
	 * @param contentService
	 *            This is the content service
	 */
	public void setContentService(IContentService contentService) {
		this.contentService = contentService;
	}

	/**
	 * This is the configuration manager wired instance
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This sets the configuration Manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * this is the SendSmsService wired instance
	 */
	@Autowired
	ISendSMSService sendSmsService;

	/**
	 * This method set the sms service.
	 * 
	 * @param sendSmsService
	 *            the sendSmsService to set
	 */
	public void setSendSmsService(ISendSMSService sendSmsService) {
		this.sendSmsService = sendSmsService;
	}

	/**
	 * The wired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;

	/**
	 * This is the setter for user data access
	 * 
	 * @param iUser
	 */
	public void setUserDataAccess(IUser iUser) {
		this.userDataAccess = iUser;
	}

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {

		ReferalEntity referralEntity = (ReferalEntity) asyncWorkItem.getPayload();
		this.prepareSmsDetailsAndSendSms(referralEntity, userDataAccess.getUser(referralEntity.getUserId(), null));

	}

	/**
	 * This method sends sms to the given phone number
	 * 
	 * @param userName
	 *            The name of the referring user
	 * @param phoneNumber
	 *            The phone number of referred user to which message to be sent
	 * @throws Exception
	 *             If there is an error in processing
	 */
	public void sendSMS(String userName, String phoneNumber) throws Exception {
		String url = configurationManager.get("SMS_ROOT_URL") + configurationManager.get("SMS_URL");
		url = url.replace("@apiKey", configurationManager.get("SMS_API_KEY"));
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		//Replace @to with referred user phone number
		url = url.replace("@to", phoneNumber);
		//Get the message content to be sent
		String message = contentService.getContent("JOIN_INVITATION_SMS");
		//Replace @UserName with referring user name
		message = message.replace("@UserName", userName);
		//Replace @message with the prepared message content
		url = url.replace("@message", URLEncoder.encode(message));
		String response = null;
		//Send the sms to referred user
		//HttpResponse smsGatewayResponse = sendSmsService.send(url, phoneNumber, message);

	}

	/**
	 * This method gets the referring user name and phone number to be used for
	 * sending sms
	 *
	 * @param referralEntity
	 *            The referralEntity with list of referred users' contacts
	 * @param detailsOfReferringUser
	 *            details of referring user to be used to fetch the required
	 *            information
	 */
	public void prepareSmsDetailsAndSendSms(ReferalEntity referralEntity, ExternalFacingUser detailsOfReferringUser) {
        // Preparing full name of referring user by concatenating its first
		// name,middle name and last name
		String referringUserName = detailsOfReferringUser.getFirstName()
				+ (detailsOfReferringUser.getMiddleName() == null ? "" : " " + detailsOfReferringUser.getMiddleName())
				+ (detailsOfReferringUser.getLastName() == null ? "" : " " + detailsOfReferringUser.getLastName());
		// Getting the referred user contacts list
		BoilerplateList<String> referralContacts = referralEntity.getReferralContacts();
		//Iterating through contact list, fetching phone number and sending sms one by one
		for (int i = 0; i < referralContacts.size(); i++) {
			String phoneNumber = (String) referralContacts.get(i);
			try {
				this.sendSMS(referringUserName, phoneNumber);
			} catch (Exception ex) {
				logger.logException("SendSmsToReferredUserObserver", "getReferringUserNameAndPhoneNumberAndSendSms",
						"try-Queue Reader", ex.toString(), ex);
			}
		}
	}

}
