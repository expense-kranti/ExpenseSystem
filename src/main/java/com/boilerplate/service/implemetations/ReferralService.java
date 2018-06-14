package com.boilerplate.service.implemetations;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.SendEmailToReferredUserObserver;
import com.boilerplate.asyncWork.SendSmsToReferredUserObserver;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.interfaces.ISFUpdateHash;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.CampaignType;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ShortUrlEntity;
import com.boilerplate.java.entities.UserReferalMediumType;
import com.boilerplate.java.entities.UserReferredSignedUpUsersCountEntity;
import com.boilerplate.service.interfaces.IReferralService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This class provide service for referral related operations
 * 
 * @author shiva
 *
 */
public class ReferralService implements IReferralService {

	/**
	 * The dates will be stored in yyyy-MM-dd format
	 */
	private static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	ISFUpdateHash redisSFUpdateHashAccess;

	/**
	 * This method set the instance of redisSFUpdateHashAccess
	 * 
	 * @param redisSFUpdateHashAccess
	 *            the redisSFUpdateHashAccess to set
	 */
	public void setRedisSFUpdateHashAccess(ISFUpdateHash redisSFUpdateHashAccess) {
		this.redisSFUpdateHashAccess = redisSFUpdateHashAccess;
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
	 * This is the instance of Referral Contact for mysql
	 */
	IReferral mySqlRefralContact;

	/**
	 * Set the mySqlRefralContact
	 * 
	 * @param mySqlRefralContact
	 *            this is the mySqlRefralContact
	 */
	public void setMySqlRefralContact(IReferral mySqlRefralContact) {
		this.mySqlRefralContact = mySqlRefralContact;
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
	 * This is the publish subject list for send email.
	 */
	BoilerplateList<String> subjectsForPublishReferralReport = new BoilerplateList<>();

	/**
	 * Initializes the bean
	 */
	public void initialize() {
		subjectsForSendSMS.add("SendSMSToReferredUser");
		subjectsForSendEmail.add("SendEmailToReferredUser");
		subjectsForPublishReferralReport.add("PublishReferReport");
	}

	/**
	 * @see IReferralService.getUserReferredContacts
	 */
	@Override
	public ReferalEntity getUserReferredContacts() {
		ReferalEntity referalEntity = new ReferalEntity();
		if (RequestThreadLocal.getSession().getExternalFacingUser().getUserReferId() != null) {

			referalEntity.setUserReferId(RequestThreadLocal.getSession().getExternalFacingUser().getUserReferId());
			BoilerplateMap<String, String> dayCountMap = new BoilerplateMap<>();
			for (UserReferalMediumType dir : UserReferalMediumType.values()) {
				referalEntity.setreferralMediumType(dir);
				dayCountMap.put(dir.toString(), referral.getDayCount(referalEntity));
			}
			referalEntity.setDayCount(dayCountMap);
		}

		return referalEntity;

	}

	/**
	 * This method is used to create the UUID
	 * 
	 * @return the UUID
	 */
	private String createUUID(Integer uuidLength) {
		// New instance of random
		Random rand = new Random();
		String userReferId = "";
		// Run a for loop to generate a configurations define length uuid
		for (int i = 0; i < uuidLength; i++) {
			// Get random number
			int randomNum = rand.nextInt(26 - 0);
			// Concatenate new char to string
			userReferId = userReferId + String.valueOf((char) (randomNum + 97));
		}
		return userReferId;
	}

	/**
	 * @see IReferralService.sendReferralLink
	 */
	@Override
	public void sendReferralLink(ReferalEntity referalEntity)
			throws ValidationFailedException, IOException, ConflictException {
		// Set user id
		referalEntity.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		// Set user refer id
		referalEntity.setUserReferId(this.getReferUserId());
		// Validate request
		this.validateReferRequest(referalEntity);
		// Generate referral link
		this.generateReferralLink(referalEntity);
		// Save referral contacts to data store
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
			case LinkedIn:
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
	 * @throws IOException
	 * @throws JsonMappingException
	 * @throws JsonParseException
	 */
	private void sendReferralLinkThroughSMS(ReferalEntity referalEntity)
			throws NotFoundException, JsonParseException, JsonMappingException, IOException {
		try {
			// Trigger back ground job to send referral link through SMS
			queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForSendSMS, "ReferalEntity",
					"sendReferralLinkThroughSMS");
		} catch (Exception ex) {
			// if queue is not working we send SMS on the thread
			sendSmsToReferredUserObserver.processReferRequest(referalEntity,
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
	 * @throws IOException
	 */
	private void sendReferralLinkThroughEmail(ReferalEntity referalEntity) throws NotFoundException, IOException {
		try {
			// Trigger back ground job to send referral link through Email

			queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForSendEmail, "ReferalEntity",
					"sendReferralLinkThroughEmail");

		} catch (Exception ex) {
			// if queue is not working we send email on the thread
			sendEmailToReferredUserObserver.processReferRequest(referalEntity,
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
	 */
	private void generateReferralLink(ReferalEntity referralEntity) {
		// Get base referral link from configurations
		String baseReferralLink = configurationManager.get("BASE_REFERRAL_LINK");
		// Replace @campaignType with refer
		baseReferralLink = baseReferralLink.replace("@utm_campaign", referralEntity.getUserReferId());
		// Replace @campaignSource with refer medium type
		baseReferralLink = baseReferralLink.replace("@utm_medium", referralEntity.getReferralMediumType().toString());
		// Set referral link
		referralEntity.setReferralLink(baseReferralLink);
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
		// Validate refer request for max and min limit
		referalEntity.validate();
		// If today day count is not null then validate request
		if (referral.getDayCount(referalEntity) != null) {
			// Get today referred contacts count
			Integer todayReferredContactsCount = Integer.parseInt(referral.getDayCount(referalEntity));
			// Get max size of one day referral contacts
			Integer maxSizeOfReferralContacts = Integer
					.valueOf(configurationManager.get("MAX_SIZE_OF_REFERRAL_CONTACTS_PER_DAY"));
			// Get today left max refer contacts
			Integer todayLeftReferralContacts = maxSizeOfReferralContacts - todayReferredContactsCount;
			// validate today left size
			if (referalEntity.getReferralContacts().size() > todayLeftReferralContacts) {
				// Throw validation failed exception
				throw new ValidationFailedException("ReferalEntity",
						"Today limit reach, you can refer " + todayLeftReferralContacts + " contacts more", null);
			}
		}
	}

	/**
	 * @see IReferralService.validateReferContact
	 */
	@Override
	public void validateReferContact(ReferalEntity referalEntity)
			throws ConflictException, NotFoundException, ValidationFailedException {
		// Validate referral entity
		referalEntity.validate();
		// Set user id
		referalEntity.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		// Set user refer id
		referalEntity.setUserReferId(this.getReferUserId());
		// According to type trigger back ground job
		String referralLink = referral.getUserReferredExpireContacts(referalEntity);

		// If referred contact exist in referred contact list then throw
		// exception
		if (referralLink != null) {
			throw new ConflictException("User",
					"This contact has already been referred by you using this referral link :" + referralLink, null);
		}
		// Check is this contact exists or not in our data store
		if (!(this.checkReferredContactExistence((String) referalEntity.getReferralContacts().get(0),
				referalEntity.getReferralMediumType()))) {
			throw new ConflictException("User", "This contact is already registered with us", null);
		}
	}

	/**
	 * @see IReferralService.checkReferredContactExistence
	 */
	@Override
	public boolean checkReferredContactExistence(String contactDetail, UserReferalMediumType contactType) {
		switch (contactType) {
		case Email:
			// Check is this email is exist or not in our data store
			if (this.redisSFUpdateHashAccess.hget(configurationManager.get("AKS_USER_EMAIL_HASH_BASE_TAG"),
					"AKS" + ":" + contactDetail.toUpperCase()) != null) {
				return false;
			}
			break;
		case Phone_Number:
			// Check is this mobile number is exist or not in our data store
			if (userDataAccess.userExists("AKS" + ":" + contactDetail)) {
				return false;
			}
			break;
		default:
			return true;
		}
		return true;
	}

	/**
	 * @see IReferralService.getFaceBookReferralLink
	 */
	@Override
	public ReferalEntity getFaceBookReferralLink() {

		String userReferId = this.getReferUserId();
		// Create a new instance of referral entity
		ReferalEntity referalEntity = new ReferalEntity(UserReferalMediumType.Facebook,
				RequestThreadLocal.getSession().getUserId());
		referalEntity.setUserReferId(userReferId);
		// Get referral link
		this.generateReferralLink(referalEntity);
		// Get short URL
		// referalEntity.setReferralLink(this.getShortUrl(referalEntity.getReferralLink()));
		return referalEntity;
	}

	/**
	 * @see IReferralService.getLinkedInReferralLink
	 */
	@Override
	public ReferalEntity getLinkedInReferralLink() {
		String userReferId = this.getReferUserId();
		// Create a new instance of referral entity
		ReferalEntity referalEntity = new ReferalEntity(UserReferalMediumType.LinkedIn,
				RequestThreadLocal.getSession().getUserId());
		referalEntity.setUserReferId(userReferId);
		// Get referal link
		this.generateReferralLink(referalEntity);

		return referalEntity;
	}

	private String getReferUserId() {
		// Get user details
		ExternalFacingReturnedUser user = RequestThreadLocal.getSession().getExternalFacingUser();
		// Check is user contains its user refer id if not then create
		String userReferId;
		if (user.getUserReferId() == null) {
			userReferId = this.createUUID(Integer.valueOf(configurationManager.get("REFERRAL_LINK_UUID_LENGTH")));
			user.setUserReferId(userReferId);
			// update user
			userDataAccess.update(user);
			referral.saveUserReferUUID(new ReferalEntity(user.getUserId(), user.getUserReferId()));
		} else {
			userReferId = user.getUserReferId();
		}
		return userReferId;
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
	 * @see IReferral.getLoggedInUserReferredSignedUpUsersCount
	 */
	@Override
	public UserReferredSignedUpUsersCountEntity getLoggedInUserReferredSignedUpUsersCount()
			throws UnauthorizedException {

		// get currently logged in user from session
		// here we are not checking for session is null as we have put
		// authentication required to true in method permission
		ExternalFacingUser user = RequestThreadLocal.getSession().getExternalFacingUser();
		UserReferredSignedUpUsersCountEntity userReferredSignedUpUsersCountEntity = new UserReferredSignedUpUsersCountEntity();
		int totalSignedUpCount = 0;
		// loop through all referral medium type and get sign-up count for each
		// referral medium
		for (UserReferalMediumType referMediumType : UserReferalMediumType.values()) {
			// get sign up count for the referral medium from data store
			String signedUpCountString = referral.getSignUpCount(
					new ReferalEntity(referMediumType.toString(), user.getUserId(), user.getUserReferId()));
			// if referral sign-up count is null then do nothing and loop
			// forward
			if (signedUpCountString == null || signedUpCountString.isEmpty()) {

			} else {
				// if count available parse it to integer and add it to total
				// count
				totalSignedUpCount += Integer.parseInt(signedUpCountString);
			}
		}
		userReferredSignedUpUsersCountEntity.setUserId(user.getUserId());
		// set total sign-up count to entity we are returning
		userReferredSignedUpUsersCountEntity.setReferredUsersTotalSignUpCount(totalSignedUpCount);
		return userReferredSignedUpUsersCountEntity;
	}

	/**
	 * @see IReferralService.deleteUserAllReferralData
	 */
	@Override
	public void deleteUserAllReferralData(String userReferId) {
		referral.deleteUserAllReferredContactsData(userReferId);
		referral.deleteUserAllReferSignUpCountData(userReferId);
	}

	/**
	 * @see IReferralService.getLoggedInUserReferredSignedUpUsersCountCurrentMonth
	 */
	@Override
	public List<Map<String, Object>> getLoggedInUserReferredUsersCountCurrentMonth() {
		// get current date for year and month
		String currentDate = formatter.format(new Date());
		// get date portion with current month and year in yyyy-MM format
		String datePart = currentDate.substring(0, 7);
		// get sign up user refered by logged in user by preparing first date
		// and lastdate of current month
		return mySqlRefralContact.getCurrentMonthReferalCount(
				RequestThreadLocal.getSession().getExternalFacingUser().getUserId(), datePart + "-01",
				datePart + "-31");
	}

}
