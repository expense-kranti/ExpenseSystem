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
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ShortUrlEntity;
import com.boilerplate.service.interfaces.IContentService;

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
		// Create email details and send email
		this.createEmailDetailsAndSendEmail(referralEntity, userDataAccess.getUser(referralEntity.getUserId(), null));
	}

	/**
	 * This method sends the email to the given tosEmaillist, bccsEmaillist with
	 * prepared message to be send to those referred users in the list
	 * 
	 * @param userName
	 *            The userName of referring user
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
	public void sendEmail(String referringUserName, BoilerplateList<String> tosEmailList,
			BoilerplateList<String> ccsEmailList, BoilerplateList<String> bccsEmailList, String referralLink)
			throws Exception {
		// Get subject of invitation email
		String subject = contentService.getContent("JOIN_INVITATION_MESSAGE_EMAIL_SUBJECT");
		// Replace @UserName to referring user name
		subject = subject.replace("@UserName", referringUserName);
		// Get the invitation email body
		String body = contentService.getContent("JOIN_INVITATION_MESSAGE_EMAIL_BODY");
		// Replace @UserName with referring user name
		body = body.replace("@UserName", referringUserName);
		// Replace @ReferralLink with real referral link in the email body
		body = body.replace("@ReferralLink", referralLink);
		// Send the email after all preparations
		EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, null);

	}

	/**
	 * This method prepare email lists and email details and send the email to
	 * referred users one by one
	 * 
	 * @param referralEntity
	 *            The referral Entity it has all the details of the referral
	 *            details such as referral contacts, its referring userId
	 * @throws NotFoundException
	 *             thrown when referring user not found
	 */
	public void createEmailDetailsAndSendEmail(ReferalEntity referralEntity, ExternalFacingUser detailsOfReferringUser)
			throws NotFoundException {
		// list of referred user(s)
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		// list of ccemailsIds for this email
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
		// list of bccemialIds for this email
		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
		// Preparing full name of referring user by concatenating its first
		// name,middle name and last name
		String referringUserName = detailsOfReferringUser.getFirstName()
				+ (detailsOfReferringUser.getMiddleName() == null ? "" : " " + detailsOfReferringUser.getMiddleName())
				+ (detailsOfReferringUser.getLastName() == null ? "" : " " + detailsOfReferringUser.getLastName());
		// iterating through the list of all referred users and sending mails to
		// each one of them one by one
		for (int i = 0; i < referralEntity.getReferralContacts().size(); i++) {
			tosEmailList.add((String) referralEntity.getReferralContacts().get(i));
			try {
				this.sendEmail(referringUserName, tosEmailList, ccsEmailList, bccsEmailList,
						referralEntity.getReferralLink());
			} catch (Exception ex) {
				logger.logException("SendEmailToReferredUserObserver", "createEmailDetails", "try-Queue Reader",
						ex.toString(), ex);
			}
			tosEmailList.remove(0);
		}
	}

}
