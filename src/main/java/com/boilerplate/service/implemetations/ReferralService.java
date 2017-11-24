package com.boilerplate.service.implemetations;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.SendEmailToReferredUserObserver;
import com.boilerplate.asyncWork.SendSmsToReferredUserObserver;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.CampaignType;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.UserReferalMediumType;
import com.boilerplate.service.interfaces.IReferralService;

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
	public void sendReferralLink(ReferalEntity referalEntity) throws ValidationFailedException {
		// Validate refer request for max and min limit
		referalEntity.validate();
		// Validate referral request
		this.validateReferRequest(referalEntity);
		// Set the userId
		referalEntity.setUserId(RequestThreadLocal.getSession().getUserId());
		// Get referral link
		referalEntity.setReferralLink(this.generateUserReferralLink(referalEntity));
		// Save referral contacts to data store
		referral.saveUserReferredContacts(referalEntity);
		try {
			// According to type trigger back ground job
			switch (referalEntity.getReferralMediumType()) {
			case Email:
				// Send email
				queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForSendEmail, "ReferralService",
						"sendReferralLink");
				break;
			case Phone_Number:
				// Send SMS
				queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForSendSMS, "ReferralService",
						"sendReferralLink");
				break;
			case Facebook:
				// get referral link
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
		Integer todayReferredContactsSize = referral
				.getTodayReferredContactsSize(referalEntity.getReferralMediumType());
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
	 * @author kranti123
	 * @param referalEntity
	 */
	public void sendSmsOrEmail(ReferalEntity referalEntity) {
		UserReferalMediumType userReferralMediumType = referalEntity.getReferralMediumType();

		ExternalFacingUser currentUser = RequestThreadLocal.getSession().getExternalFacingUser();
		String currentUserName = currentUser.getFirstName() + " " + currentUser.getMiddleName() + " "
				+ currentUser.getLastName();

		switch (userReferralMediumType) {
		case Email:
			subjectsForInvitingReferredUser = subjectsForSendEmail;
			break;

		case Phone_Number:
			subjectsForInvitingReferredUser = subjectsForSendSMS;
			break;

		case Facebook:
			break;
		}

		try {
			queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForInvitingReferredUser, "ReferalEntity",
					"sendSmsOrEmail");
		} catch (Exception ex) {

			if (subjectsForInvitingReferredUser == subjectsForSendEmail) {
				BoilerplateList<String> tosEmailList = new BoilerplateList<>();
				BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
				BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();

				for (Map.Entry<String, String> emailReferralMap : referalEntity.getEmailReferrals().entrySet()) {
					tosEmailList.add(emailReferralMap.getValue());
					try {
						sendEmailToReferredUserObserver.sendEmail(currentUserName, tosEmailList, ccsEmailList,
								bccsEmailList, currentUser.getPhoneNumber(), currentUser.getUserKey());
					} catch (Exception exEmail) {
						// if an exception takes place here we cant do much
						// hence just
						// log it and move
						// forward
						logger.logException("ReferralService", "sendSmsOrEmail", "try-Queue Reader - Send Email",
								exEmail.toString(), exEmail);
					}
					tosEmailList.remove(0);

				}
			} else if (subjectsForInvitingReferredUser == subjectsForSendSMS) {
				BoilerplateMap<String, String> phoneNumberReferrals = referalEntity.getPhoneNumberReferrals();
				for (Map.Entry<String, String> phoneNumberMap : phoneNumberReferrals.entrySet()) {
					String phoneNumber = phoneNumberMap.getValue();
					try {
						sendSmsToReferredUserObserver.sendSMS(currentUserName, phoneNumber);
					} catch (Exception exSendPassword) {
						logger.logException("ReferralService", "sendSmsOrEmail", "inside catch: try-Queue Reader",
								exSendPassword.toString() + "" + "Gateway down", exSendPassword);
					}

					logger.logException("referralService", "sendSmsOrEmail", "try-Queue Reader", ex.toString()
							+ " ReferalEntity inserting in queue is: " + Base.toJSON(referalEntity) + " Queue Down",
							ex);
				}
			}

		}

	}

}
