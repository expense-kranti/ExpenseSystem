package com.boilerplate.service.interfaces;

import java.io.IOException;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
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
	 * @throws ValidationFailedException
	 * @throws IOException
	 *             throw this exception in case we failed to get short url
	 */
	public void sendReferralLink(ReferalEntity referalEntity) throws ValidationFailedException, IOException;

	/**
	 * This method is used to validate the contact check is this contact is
	 * exist in our data store or not if exist then throw exception other wise
	 * return ok response
	 * 
	 * @param referalEntity
	 *            this parameter contains the referral details like refer
	 *            contact and its type
	 * @throws ConflictException
	 *             throw this exception in case the refer contact exist in our
	 *             data store
	 */
	public void validateReferContact(ReferalEntity referalEntity) throws ConflictException;
}
