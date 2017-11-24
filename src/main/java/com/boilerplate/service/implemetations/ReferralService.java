package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.UserReferalMediumType;
import com.boilerplate.service.interfaces.IReferralService;

public class ReferralService implements IReferralService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(ScriptService.class);

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

	/**
	 * This is the publish subject list for send SMS.
	 */
	BoilerplateList<String> subjectsForSendSMS = new BoilerplateList<>();

	/**
	 * This is the publish subject list for send Email.
	 */
	BoilerplateList<String> subjectsForSendEmail = new BoilerplateList<>();

	/**
	 * Initializes the bean
	 */
	public void initialize() {
		subjectsForSendSMS.add("");
		subjectsForSendEmail.add("");
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
	public void sendReferralLink(ReferalEntity referalEntity) {
		try {
			// According to type trigger back ground job
			switch (referalEntity.getReferralMediumType()) {
			case Email:
				// Send email
				queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForSendEmail, "ReferralService",
						"sendReferralLink");
				break;
			case Phone_Number:
				// Send sms
				queueReaderJob.requestBackroundWorkItem(referalEntity, subjectsForSendSMS, "ReferralService",
						"sendReferralLink");
				break;
			default:
				break;
			}
		} catch (Exception ex) {
			// Log error
			logger.logError("ReferralService", "sendReferralLink", "Inside try-catch block", ex.toString());
		}
	}

}
