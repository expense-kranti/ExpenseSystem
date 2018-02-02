package com.boilerplate.asyncWork;

import java.io.IOException;
import java.net.URLEncoder;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ReferredContactDetailEntity;
import com.boilerplate.java.entities.ShortUrlEntity;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.IReferralService;
import com.boilerplate.service.interfaces.ISendSMSService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This class sends a SMS when a user is referred to the website
 * 
 * @author urvij
 *
 */
public class SendSmsToReferredUserObserver implements IAsyncWorkObserver {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(SendSmsToReferredUserObserver.class);

	/**
	 * This is the new instance of queue reader job
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader job
	 * 
	 * @param queueReaderJob
	 *            The queue reader job
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

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
	 * This is the subject list for publish
	 */
	private BoilerplateList<String> subjectsForPublishReferralReport = null;

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		ReferalEntity referralEntity = (ReferalEntity) asyncWorkItem.getPayload();
		try {
			// Get referring user first name and fetch phone number of referred
			// user one by one and send sms to each one
			referralEntity.setReferredContact(
					this.processReferRequest(referralEntity, userDataAccess.getUser(referralEntity.getUserId(), null)));
		} catch (Exception exception) {
			logger.logError("SendSmsToReferredUserObserver", "observe", "queueReaderJob catch block",
					"Exception :" + exception);
		}
		// Publish referral data to CRM
		this.publishReferralData(referralEntity);
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
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	public BoilerplateList<ReferredContactDetailEntity> processReferRequest(ReferalEntity referralEntity,
			ExternalFacingUser detailsOfReferringUser) throws JsonParseException, JsonMappingException, IOException {
		BoilerplateList<ReferredContactDetailEntity> referredContacts = new BoilerplateList<>();
		referralEntity.setReferralLink(this.getShortUrl(referralEntity.getReferralLink()));
		// Get referring user first name
		String referringUserFirstName = detailsOfReferringUser.getFirstName();
		// Iterating through contact list, fetching phone number from list and
		// sending SMS one by one
		for (Object contact : referralEntity.getReferralContacts()) {
			this.saveReferralData(referralEntity, (String) contact);
			// Get referral contact
			String phoneNumber = (String) contact;
			// Check is this phone is already registered with our system or not
			if (referralService.checkReferredContactExistence(phoneNumber, referralEntity.getReferralMediumType())) {
				try {
					// Send SMS
					this.sendSMS(referringUserFirstName, phoneNumber, referralEntity.getReferralLink());
				} catch (Exception ex) {
					// Log the exception
					logger.logException("SendSmsToReferredUserObserver", "prepareSmsDetailsAndSendSms",
							"While sending refer link throught the sms ~ This is the mobile number " + phoneNumber
									+ " ~ Sent by user : " + referralEntity.getUserId(),
							ex.toString(), ex);
				}
			} else {
				// If phone number is already registered then log the error
				logger.logError("SendSmsToReferredUserObserver", "prepareSmsDetailsAndSendSms",
						"Check contact existence",
						"referred phone number already register with our system" + "This is the mobile number "
								+ phoneNumber + " ~ Sent by user : " + referralEntity.getUserId());
			}
			referredContacts.add(new ReferredContactDetailEntity((String) contact, LocalDate.now().toString()));
		}
		return referredContacts;
	}

	/**
	 * This method is used to get the short URl against the Long URL
	 * 
	 * @param referralLink
	 *            this is the long url
	 * @return the short url
	 * @throws IOException
	 *             throw this exception in case of any error while trying to get
	 *             the short url
	 */
	private String getShortUrl(String referralLink) throws IOException {
		// Create new request header
		BoilerplateMap<String, BoilerplateList<String>> requestHeaders = new BoilerplateMap();
		// Declare new header value
		BoilerplateList<String> headerValue = new BoilerplateList();
		// Add header value
		headerValue.add("application/json");
		// Put key and value in request header
		requestHeaders.put("Content-Type", headerValue);
		// Get request body
		String requestBody = configurationManager.get("GET_SHORT_URL_REQUEST_BODY_TEMPLATE");
		// Replace @long URL with referral link
		requestBody = requestBody.replace("@longUrl", referralLink);
		// Make HTTP request
		HttpResponse httpResponse = HttpUtility.makeHttpRequest(configurationManager.get("URL_SHORTENER_API_URL"),
				requestHeaders, null, requestBody, "POST");
		// Get short url entity from the http response
		ShortUrlEntity shortUrlEntity = Base.fromJSON(httpResponse.getResponseBody(), ShortUrlEntity.class);
		// Return short URL
		return shortUrlEntity.getShortUrl();
	}

	/**
	 * This method is used to save the referral data to data store
	 * 
	 * @param referralEntity
	 *            The referral Entity it has all the details of the referral
	 *            details such as referral contacts, its referring userId
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private void saveReferralData(ReferalEntity referralEntity, String contact)
			throws JsonParseException, JsonMappingException, IOException {

		// Save referral contacts to data store
		referral.saveUserReferredExpireContacts(referralEntity, contact);
		// Save user referral details
		if (referral.getDayCount(referralEntity) == null) {
			referral.createDayCounter(referralEntity, "1");
		} else {
			referral.increaseDayCounter(referralEntity);
		}
		// save the refer contact details
		ReferredContactDetailEntity updateReferral = new ReferredContactDetailEntity(contact,
				LocalDate.now().toString());

		referral.saveUserReferContacts(referralEntity, updateReferral);
		
		// check in configuration to add in redisset
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			// add key in redis database to migrate data to MySQL
			referral.addInRedisSet(referralEntity);
		}
		
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
	 * This method is used to publish the referral data to sales force
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the user
	 *            Reference like refer unique id ,referral link,referral
	 *            contacts ,referral medium type etc.
	 */
	private void publishReferralData(ReferalEntity referalEntity) {
		subjectsForPublishReferralReport = new BoilerplateList<>();
		subjectsForPublishReferralReport.add("PublishReferReport");
		try {
			// Trigger back ground job to send referral link through Email
			queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForPublishReferralReport, "ReferalEntity",
					"publishReferralData");
		} catch (Exception ex) {
			logger.logException(
					"SendSmsToReferredUserObserver", "publishReferralData", "try-Queue Reader", ex.toString()
							+ " ReferalEntity inserting in queue is: " + Base.toJSON(referalEntity) + " Queue Down",
					ex);
		}
	}

}
