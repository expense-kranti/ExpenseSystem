package com.boilerplate.service.implemetations;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.SendEmailToReferredUserObserver;
import com.boilerplate.asyncWork.SendSmsToReferredUserObserver;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.CampaignType;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ShortUrlEntity;
import com.boilerplate.service.interfaces.IReferralService;

/**
 * This class provide service for referral related operations
 * 
 * @author shiva
 *
 */
public class ReferralService implements IReferralService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(ReferralService.class);

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
	 * This is an instance of the queue job, to save the session back on to the
	 * database async
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader jon
	 * 
	 * @param queueReaderJob
	 *            The queue reader jon
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	BoilerplateList<String> subjectsForInvitingReferredUser = new BoilerplateList<>();

	/**
	 * This is the instance of SendSmsToReferredUserObserver
	 */
	@Autowired
	SendSmsToReferredUserObserver sendSmsToReferredUserObserver;

	/**
	 * This sets the SendSmsToReferredUserObserver
	 * 
	 * @param sendSmsToReferredUserObserver
	 */
	public void setSendSmsToReferredUserObserver(SendSmsToReferredUserObserver sendSmsToReferredUserObserver) {
		this.sendSmsToReferredUserObserver = sendSmsToReferredUserObserver;
	}

	/**
	 * This is the instance of SendEmailToReferredUserObserver
	 */
	@Autowired
	SendEmailToReferredUserObserver sendEmailToReferredUserObserver;

	/**
	 * This is the instance of SendEmailToReferredUserObserver
	 */
	public void setSendEmailToReferredUserObserver(SendEmailToReferredUserObserver sendEmailToReferredUserObserver) {
		this.sendEmailToReferredUserObserver = sendEmailToReferredUserObserver;
	}

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the publish subject list for send SMS.
	 */
	BoilerplateList<String> subjectsForSendSMS = new BoilerplateList<>();

	/**
	 * This is the publish subject list for send email.
	 */
	BoilerplateList<String> subjectsForSendEmail = new BoilerplateList<>();

	/**
	 * Initializes the bean
	 */
	public void initialize() {
		subjectsForSendSMS.add("SendSMSToReferredUser");
		subjectsForSendEmail.add("SendEmailToReferredUser");
	}

	/**
	 * @see IReferralService.getUserReferredContacts
	 */
	@Override
	public ReferalEntity getUserReferredContacts() {
		return referral.getUserReferredContacts();
	}

	/**
	 * @see IReferralService.sendReferralLink
	 */
	@Override
	public void sendReferralLink(ReferalEntity referalEntity) throws ValidationFailedException, IOException {
		// Validate refer request for max and min limit
		referalEntity.validate();
		// Validate referral request
		this.validateReferRequest(referalEntity);
		// Set the userId
		referalEntity.setUserId(RequestThreadLocal.getSession().getUserId());
		// Get referral link
		referalEntity.setReferralLink(this.generateUserReferralLink(referalEntity));
		// Get short URL
		referalEntity.setReferralLink(this.getShortUrl(referalEntity.getReferralLink()));
		// Save referral contacts to data store
		referral.saveUserReferredContacts(referalEntity);
		try {
			// According to type trigger back ground job
			switch (referalEntity.getReferralMediumType()) {
			case Email:
				// Send referral link through email
				this.sendReferralLinkThroughEmail(referalEntity);
				break;
			case Phone_Number:
				// Send referral link through SMS
				this.sendReferralLinkThroughSMS(referalEntity);
				break;
			case Facebook:
				// get referral link
				// TODO need to discuss
				break;
			default:
				throw new NotFoundException("ReferalEntity", "Not a valid Referral medium type", null);
			}
		} catch (Exception ex) {
			// Log error
			logger.logError("ReferralService", "sendReferralLink", "Inside try-catch block", ex.toString());
		}
	}

	/**
	 * This method is used to send referral link through SMS,using background
	 * job, in case we fail to trigger background job ,we fall back to send SMS
	 * on the thread
	 * 
	 * @param referalEntity
	 *            this parameter contains referral details
	 * @throws NotFoundException
	 *             thrown if referring user not found
	 */
	private void sendReferralLinkThroughSMS(ReferalEntity referalEntity) throws NotFoundException {
		try {
			// Trigger back ground job to send referral link through SMS
			queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForSendSMS, "ReferalEntity",
					"sendSmsOrEmail");
			throw new Exception();
		} catch (Exception ex) {
			// if queue is not working we send sms on the thread
			sendSmsToReferredUserObserver.prepareSmsDetailsAndSendSms(referalEntity,
					RequestThreadLocal.getSession().getExternalFacingUser());
			logger.logException("referralService",
					"sendReferralLinkThroughSMS", "try-Queue Reader", ex.toString()
							+ " ReferalEntity inserting in queue is: " + Base.toJSON(referalEntity) + " Queue Down",
					ex);
		}
	}

	/**
	 * This method is used to send referral link through Email,using background
	 * job, in case we fail to trigger background job ,we fall back to send
	 * Email on the thread
	 * 
	 * @param referalEntity
	 *            this parameter contains referral details
	 * @throws NotFoundException
	 *             thrown if referring user is not found
	 */
	private void sendReferralLinkThroughEmail(ReferalEntity referalEntity) throws NotFoundException {
		try {
			// Trigger back ground job to send referral link through Email
			queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForSendEmail, "ReferalEntity",
					"sendSmsOrEmail");
			throw new Exception();
		} catch (Exception ex) {
			// if queue is not working we send email on the thread
			sendEmailToReferredUserObserver.createEmailDetailsAndSendEmail(referalEntity,
					RequestThreadLocal.getSession().getExternalFacingUser());
			logger.logException("referralService",
					"sendReferralLinkThroughEmail", "try-Queue Reader", ex.toString()
							+ " ReferalEntity inserting in queue is: " + Base.toJSON(referalEntity) + " Queue Down",
					ex);
		}
	}

	/**
	 * This method is used to generate user referral link the
	 * 
	 * @param referralEntity
	 *            this parameter is used to define the refer medium type
	 * @return the referral link
	 */
	private String generateUserReferralLink(ReferalEntity referralEntity) {
		// Create UUID
		referralEntity.createUUID(Integer.valueOf(configurationManager.get("REFERRAL_LINK_UUID_LENGTH")));
		// Get base referral link from configurations
		String baseReferralLink = configurationManager.get("BASE_REFERRAL_LINK");
		// Replace @campaignType with refer
		baseReferralLink.replace("@campaignType", CampaignType.valueOf("Refer").toString());
		// Replace @campaignSource with refer medium type
		baseReferralLink.replace("@campaignSource", referralEntity.getReferralMediumType().toString());
		// Replace @UUID with uuid
		baseReferralLink.replace("@UUID", referralEntity.getReferralUUID());
		// Return the referral link
		return baseReferralLink;
	}

	/**
	 * This method is used to validate the user refer request
	 * 
	 * @param referalEntity
	 *            this parameter contains the details about user refer request
	 * @throws ValidationFailedException
	 *             throw this exception in case of user request is not valid
	 */
	private void validateReferRequest(ReferalEntity referalEntity) throws ValidationFailedException {
		// Get today referred contacts size
		Integer todayReferredContactsSize = referral.getTodayReferredContactsCount(referalEntity.getReferralMediumType());
		// Get max size of one day referral contacts
		Integer maxSizeOfReferralContacts = Integer
				.valueOf(configurationManager.get("MAX_SIZE_OF_REFERRAL_CONTACTS_PER_DAY"));
		// Get today left max refer contacts
		Integer todayLeftReferralContacts = maxSizeOfReferralContacts - todayReferredContactsSize;
		// validate today left size
		if (referalEntity.getReferralContacts().size() > todayLeftReferralContacts) {
			// Throw validation failed exception
			throw new ValidationFailedException("ReferalEntity",
					"Today limit reach, you can refer " + todayLeftReferralContacts + "contacts more", null);
		}
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
		// Replace @lonurl with referral link
		requestBody.replace("@longUrl", referralLink);
		// Make HTTP request
		HttpResponse httpResponse = HttpUtility.makeHttpRequest(configurationManager.get("URL_SHORTENER_API_URL"),
				requestHeaders, null, requestBody, "POST");
		// Get short url entity from the http response
		ShortUrlEntity shortUrlEntity = Base.fromJSON(httpResponse.getResponseBody(), ShortUrlEntity.class);
		// Return short URL
		return shortUrlEntity.getShortUrl();
	}
}
