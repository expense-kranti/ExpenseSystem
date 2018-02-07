package com.boilerplate.asyncWork;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ReferredContactDetailEntity;
import com.boilerplate.java.entities.ShortUrlEntity;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.IFileService;
import com.boilerplate.service.interfaces.IReferralService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

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

	private static String referTemplate = "";

	@Autowired
	private IFileService fileService;

	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

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
	 * This is the setter for user data access
	 * 
	 * @param iUser
	 */
	public void setUserDataAccess(IUser iUser) {
		this.userDataAccess = iUser;
	}

	/**
	 * This is the instance of S3File Entity
	 */
	@Autowired
	com.boilerplate.databases.s3FileSystem.implementations.S3File file;

	/**
	 * This method sets the instance of S3File Entity
	 * 
	 * @param file
	 *            The file
	 */
	public void setFile(com.boilerplate.databases.s3FileSystem.implementations.S3File file) {
		this.file = file;
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
		try {
			// Create email details and send email
			referralEntity.setReferredContact(
					this.processReferRequest(referralEntity, userDataAccess.getUser(referralEntity.getUserId(), null)));
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
	 * @throws IOException
	 */
	public BoilerplateList<ReferredContactDetailEntity> processReferRequest(ReferalEntity referralEntity,
			ExternalFacingUser detailsOfReferringUser) throws IOException {
		BoilerplateList<ReferredContactDetailEntity> referredContacts = new BoilerplateList<>();
		// Generate short URL
		referralEntity.setReferralLink(this.getShortUrl(referralEntity.getReferralLink()));
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
			// Add contact to to's list
			this.saveReferralData(referralEntity, (String) contact);

			tosEmailList.add((String) contact);
			// Check is this email address already registered with our system or
			// not
			if (referralService.checkReferredContactExistence((String) tosEmailList.get(0),
					referralEntity.getReferralMediumType())) {
				try {
					// Send email
					this.sendEmail(referringUserFirstName, tosEmailList, ccsEmailList, bccsEmailList,
							referralEntity.getReferralLink());
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
			referredContacts.add(new ReferredContactDetailEntity((String) contact, LocalDate.now().toString()));
			tosEmailList.remove(0);
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
			referral.addInRedisSet(referralEntity, updateReferral);
		}
		
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
		// Get the invitation email body
		if (referTemplate.equals("")) {
			// template from s3
			FileEntity fileEntity = this.fileService
					.getFile(configurationManager.get("REGISTERATION_REFER_EMAIL_CONTENT"));
			String fileNameInURL = null;
			// Get file from local if not found then downloads
			if (!new File(configurationManager.get("RootFileDownloadLocation"), fileEntity.getFileName()).exists()) {
				
				fileNameInURL = this.file.downloadFileFromS3ToLocal(fileEntity.getId());
				
			} else {
				fileNameInURL = fileEntity.getFileName();
			}

			// Open the file
			referTemplate = FileUtils
					.readFileToString(new File(configurationManager.get("RootFileDownloadLocation") + fileNameInURL));
		}
		String body = referTemplate.replace("@UserFirstName", referringUserFirstName);
		// Replace @UserFirstName with referring user first name in email body
		body = body.replace("@referLink", referralLink);
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
		} catch (Exception ex) {
			logger.logException(
					"SendEmailToReferredUserObserver", "publishReferralData", "try-Queue Reader", ex.toString()
							+ " ReferalEntity inserting in queue is: " + Base.toJSON(referalEntity) + " Queue Down",
					ex);
		}
	}

}
