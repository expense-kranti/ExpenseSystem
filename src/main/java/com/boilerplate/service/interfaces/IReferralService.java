package com.boilerplate.service.interfaces;

import com.boilerplate.java.entities.ReferalEntity;

/**
 * This class has the services for referral related operations
 * 
 * @author shiva
 *
 */
public interface IReferralService {

	/**
	 * This method is used to get all those contact which is referred by user in
	 * current date
	 * 
	 * @return all those contact which is referred by user in current date
	 * 
	 */
	public ReferalEntity getUserReferredContacts();

	/**
	 * This method is used to send referral link to those contact referred by
	 * user
	 * 
	 * @param referalEntity
	 *            this parameter contains the details of referred contact by
	 *            user and the type of referred medium
	 */
	public void sendReferralLink(ReferalEntity referalEntity);

}
