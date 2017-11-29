package com.boilerplate.asyncWork;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ReferralLinkEntity;
import com.boilerplate.java.entities.ShortUrlEntity;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.IReferralService;

/**
 * This class sends an Email to referred user
 * 
 * @author kranti123
 *
 */
public class SendEmailToReferredUserObserver implements IAsyncWorkObserver {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(SendEmailToReferredUserObserver.class);

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
	 * This is the content service.
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
	 * This is the configuration manager
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
	 * The autowired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;

	/**
	 * This is the setter for user data acess
	 * 
	 * @param iUser
	 */
	public void setUserDataAccess(IUser iUser) {
		this.userDataAccess = iUser;
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
		// Get the referral entity from the payload
		ReferalEntity referralEntity = (ReferalEntity) asyncWorkItem.getPayload();
		// Save user referral details
		referral.saveReferralDetail(referralEntity);
		// Save user referral details
		referral.saveUserReferralDetail(referralEntity);
		try {
			// Create email details and send email
			this.processReferRequest(referralEntity, userDataAccess.getUser(referralEntity.getUserId(), null));
		} catch (Exception exception) {
			logger.logError("SendEmailToReferredUserObserver", "observe", "queueReaderJob catch block",
					"Exception :" + exception);
		}
		// Publish referral data to CRM
		this.publishReferralData(referralEntity);
	}

	/**
	 * This method prepare email lists and email details and send the email to
	 * referred users one by one
	 * 
	 * @param referralEntity
	 *            The referral Entity it has all the details of the referral
	 *            details such as referral contacts, its referring userId
	 */
	public void processReferRequest(ReferalEntity referralEntity, ExternalFacingUser detailsOfReferringUser) {
		// Generate short URL
		this.generateShortUrl(referralEntity);
		// Save referral data to data store
		this.saveReferralData(referralEntity);
		// list of referred user(s)
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		// list of ccemailsIds for this email
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
		// list of bccemialIds for this email
		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
		// Getting referring user first name
		String referringUserFirstName = detailsOfReferringUser.getFirstName();
		// iterating through the list of all referred users and sending mails to
		// each one of them one by one
		for (Object contact : referralEntity.getReferralContacts()) {
			// Convert a object into entity
			ReferralLinkEntity referralLinkEntity = (ReferralLinkEntity) contact;
			// Add contact to tos list
			tosEmailList.add(referralLinkEntity.getContact());
			// Check is this email address already registered with our system or
			// not
			if (referralService.checkReferredContactExistence((String) tosEmailList.get(0),
					referralEntity.getReferralMediumType())) {
				try {
					// Send email
					this.sendEmail(referringUserFirstName, tosEmailList, ccsEmailList, bccsEmailList,
							referralLinkEntity.getReferralLink());
				} catch (Exception ex) {
					// Log the exception
					logger.logException("SendEmailToReferredUserObserver", "createEmailDetails",
							"While sending refer link throught the Email ~ This is the Email address "
									+ tosEmailList.get(0) + " ~ Sent by user : " + referralEntity.getUserId(),
							ex.toString(), ex);
				}
			} else {
				// If Email is already registered then log the error
				logger.logError("SendSmsToReferredUserObserver", "prepareSmsDetailsAndSendSms",
						"Check contact existence",
						"referred Email already register with our system" + "This is Email address "
								+ tosEmailList.get(0) + " ~ Sent by user : " + referralEntity.getUserId());
			}
			tosEmailList.remove(0);
		}
	}

	/**
	 * This method is used to generate short URL
	 * 
	 * @param referralEntity
	 *            The referral Entity it has all the details of the referral
	 *            details such as referral contacts, its referring userId
	 */
	private void generateShortUrl(ReferalEntity referralEntity) {
		// Run a for loop to generate the short URL for each contact
		for (Object o : referralEntity.getReferralContacts()) {
			// Convert a object into entity
			ReferralLinkEntity referralLinkEntity = (ReferralLinkEntity) o;
			try {
				// Get short URL
				referralLinkEntity.setReferralLink(this.getShortUrl(referralLinkEntity.getReferralLink()));
			} catch (IOException ex) {
				// Log the exception
				logger.logException("SendEmailToReferredUserObserver",
						"generateShortUrl", "While trying to get the short url ~ This is the conatct "
								+ referralLinkEntity.getContact() + " ~ Sent by user : " + referralEntity.getUserId(),
						ex.toString(), ex);
			}
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
	 */
	private void saveReferralData(ReferalEntity referralEntity) {
		// Save referral contacts to data store
		referral.saveUserReferredContacts(referralEntity);
		// Save user referral details
		referral.saveReferralDetail(referralEntity);
		// Save user referral details
		referral.saveUserReferralDetail(referralEntity);
	}

	/**
	 * This method sends the email to the given tosEmaillist, bccsEmaillist with
	 * prepared message to be send to those referred users in the list
	 * 
	 * @param referringUserFirstName
	 *            The first Name of referring user
	 * @param tosEmailList
	 *            The list of emailIds to whom email to be sent
	 * @param ccsEmailList
	 *            The list of ccemailIds to whom email to be sent
	 * @param bccsEmailList
	 *            The list of bccsemailIds to whom email to be sent
	 * @param referralLink
	 *            the referral Link sent by referring user to referred user
	 * @throws Exception
	 *             is thrown if any exception occurs while sending email
	 */
	public void sendEmail(String referringUserFirstName, BoilerplateList<String> tosEmailList,
			BoilerplateList<String> ccsEmailList, BoilerplateList<String> bccsEmailList, String referralLink)
			throws Exception {
		// Get subject of invitation email
		String subject = contentService.getContent("JOIN_INVITATION_MESSAGE_EMAIL_SUBJECT");
		// Replace @UserFirstName to referring user first name in email subject
		subject = subject.replace("@UserFirstName", referringUserFirstName);
		// Get the invitation email body
		String body = contentService.getContent("JOIN_INVITATION_MESSAGE_EMAIL_BODY");
		// Replace @UserFirstName with referring user first name in email body
		body = body.replace("@UserFirstName", referringUserFirstName);
		// Replace @link with real referral link in the email body
		body = body.replace("@link", referralLink);
		// Send the email after all preparations
		EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, null);

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
			throw new Exception();
		} catch (Exception ex) {
			logger.logException(
					"referralService", "publishReferralData", "try-Queue Reader", ex.toString()
							+ " ReferalEntity inserting in queue is: " + Base.toJSON(referalEntity) + " Queue Down",
					ex);
		}
	}

}
