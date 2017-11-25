package com.boilerplate.asyncWork;

import java.net.URLEncoder;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.IReferralService;
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
	 * This is a new instance of Referral
	 */
	IReferral referral;

	/**
	 * This method is used to set the referral
	 * 
	 * @param referral
	 *            the referral to set
	 */
	public void setReferral(IReferral referral) {
		this.referral = referral;
	}

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
	 * This is a new instance of referral service
	 */
	@Autowired
	IReferralService referralService;

	/**
	 * This method set referral service
	 * 
	 * @param referralService
	 *            the referralService to set
	 */
	public void setReferralService(IReferralService referralService) {
		this.referralService = referralService;
	}

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		ReferalEntity referralEntity = (ReferalEntity) asyncWorkItem.getPayload();
		// Save user referral details
		referral.saveReferralDetail(referralEntity);
		// Save user referral details
		referral.saveUserReferralDetail(referralEntity);
		// Get referring user first name and fetch phone number of referred user
		// one by one and send sms to each one
		this.prepareSmsDetailsAndSendSms(referralEntity, userDataAccess.getUser(referralEntity.getUserId(), null));

	}

	/**
	 * This method prepares message content and sends sms to the given phone
	 * number
	 * 
	 * @param referringUserFirstName
	 *            The first name of the referring user
	 * @param phoneNumber
	 *            The phone number of referred user to which message to be sent
	 * @param referralLink
	 *            the referral link sent by referring user to referred user
	 * @throws Exception
	 *             If there is an error in processing
	 */
	public void sendSMS(String referringUserFirstName, String phoneNumber, String referralLink) throws Exception {
		String url = configurationManager.get("SMS_ROOT_URL") + configurationManager.get("SMS_URL");
		url = url.replace("@apiKey", configurationManager.get("SMS_API_KEY"));
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		// Replace @to with referred user phone number to whom sms to be sent
		url = url.replace("@to", phoneNumber);
		// Get the message content to be sent
		String message = contentService.getContent("JOIN_INVITATION_SMS");
		// Replace @UserFirstName with referring user first name
		message = message.replace("@UserFirstName", referringUserFirstName);
		// Replace @link with real referral link in the SMS content
		message = message.replace("@link", referralLink);
		// Replace @message with the prepared message content
		url = url.replace("@message", URLEncoder.encode(message));
		String response = null;
		// Send the sms to referred user
		HttpResponse smsGatewayResponse = sendSmsService.send(url, phoneNumber, message);

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
		// Get referring user first name
		String referringUserFirstName = detailsOfReferringUser.getFirstName();
		// Getting the referred user contacts list
		BoilerplateList<String> referralContacts = referralEntity.getReferralContacts();
		// Iterating through contact list, fetching phone number from list and
		// sending sms
		// one by one
		for (int i = 0; i < referralContacts.size(); i++) {
			String phoneNumber = (String) referralContacts.get(i);
			if (referralService.checkReferredContactExistence(phoneNumber, referralEntity.getReferralMediumType())) {
				try {
					this.sendSMS(referringUserFirstName, phoneNumber, referralEntity.getReferralLink());
				} catch (Exception ex) {
					logger.logException("SendSmsToReferredUserObserver", "prepareSmsDetailsAndSendSms",
							"try-Queue Reader", ex.toString(), ex);
				}
			} else {
				logger.logError("SendSmsToReferredUserObserver", "prepareSmsDetailsAndSendSms",
						"Check contact existence", "referred phone number already register with our system");
			}

		}
	}

}
