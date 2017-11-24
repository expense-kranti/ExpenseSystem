package com.boilerplate.asyncWork;

import java.util.Map;

import org.apache.commons.codec.language.bm.PhoneticEngine;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.CampaignType;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.service.interfaces.IContentService;

/**
 * This class sends an Email to referred user
 * 
 * @author kranti123
 *
 */
public class SendEmailToReferredUserObserver implements IAsyncWorkObserver {

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
		
		ExternalFacingUser currentUser = RequestThreadLocal.getSession().getExternalFacingUser();

		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();

		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();

		String currentUserName = currentUser.getFirstName() + " " + currentUser.getMiddleName() + " "
				+ currentUser.getLastName();

		BoilerplateList<String> referralContacts = referralEntity.getReferralContacts();
		for (int i = 0; i < referralContacts.size(); i++) {
			tosEmailList.add((String) referralContacts.get(i));
			this.sendEmail(currentUserName, tosEmailList, ccsEmailList, bccsEmailList, currentUser.getPhoneNumber(),
					currentUser.getUserKey());
			tosEmailList.remove(0);
		}

	}

	public void sendEmail(String userName, BoilerplateList<String> tosEmailList, BoilerplateList<String> ccsEmailList,
			BoilerplateList<String> bccsEmailList, String phoneNumber, String userKey) throws Exception {
		String subject = contentService.getContent("JOIN_INVITATION_MESSAGE_EMAIL_SUBJECT");
		subject = subject.replace("@UserName", userName);
		String body = contentService.getContent("JOIN_INVITATION_MESSAGE_EMAIL_BODY");
		body = body.replace("@UserName", userName);
		// body = body.replace("@Email",(String) tosEmailList.get(0));
		body = body.replace("@PhoneNumber", phoneNumber);
		body = body.replace("@UserKey", userKey == null ? "" : userKey);
		if (!Boolean.valueOf(configurationManager.get("Db_Migrate"))) {
			EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, null);
		}

	}
}
