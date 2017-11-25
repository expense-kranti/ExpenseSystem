package com.boilerplate.database.interfaces;

import java.util.Map;

import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.UserReferalMediumType;

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
	 * This method is used to save all those contact which is referred by user
	 * in current date
	 * 
	 * @param referalEntity
	 *            this parameter contains the information regarding the user
	 *            referral contacts by current date
	 */
	public void saveUserReferredContacts(ReferalEntity referalEntity);

	/**
	 * This method is used to save user referral details like the contact
	 * details of refer receiver and referral UUID
	 * 
	 * @param referalEntity
	 *            this parameter contains referral details like referral UUID
	 *            and contact details of referral receiver
	 */
	public void saveUserReferralDetail(ReferalEntity referalEntity);

	/**
	 * This method is used to save referral details like medium type ,referral
	 * UUID and campaign type refer to redis
	 * 
	 * @param referalEntity
	 *            this parameter contains referral details like medium type
	 *            ,referral UUID
	 */
	public void saveReferralDetail(ReferalEntity referalEntity);

	/**
	 * This method is used to get the user today referred contact size of
	 * specific referred medium type
	 * 
	 * @param referralMediumType
	 *            this parameter define of which type size we want to get
	 * @return the size of referred contacts by user
	 */
	public Integer getTodayReferredContactsCount(UserReferalMediumType referralMediumType);

	/**
	 * This method is used to get the user referred contact details
	 * 
	 * @param referalEntity
	 *            this parameter contain the information regarding the refer
	 *            contact and its medium type
	 * @return referred contact details
	 */
	public String getUserReferredContactDetails(ReferalEntity referalEntity);
}
