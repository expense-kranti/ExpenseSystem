package com.boilerplate.database.interfaces;

import com.boilerplate.java.entities.ReferalEntity;

/**
 * This class provide the method for referral related operations regarding data
 * store
 * 
 * @author shiva
 *
 */
public interface IReferral {

	/**
	 * This method is used to get all those contact which is referred by user in
	 * current date
	 * 
	 * @return all those contact which is referred by user in current date
	 * 
	 */
	public ReferalEntity getUserReferredContacts();

	/**
	 * This method is used to set all those contact which is referred by user in
	 * current date
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the user
	 *            referral contacts by current date
	 */
	public void setUserReferralContacts(ReferalEntity referalEntity);
}
