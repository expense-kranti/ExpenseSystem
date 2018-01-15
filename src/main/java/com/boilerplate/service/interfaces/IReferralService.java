package com.boilerplate.service.interfaces;

import java.io.IOException;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.UserReferalMediumType;
import com.boilerplate.java.entities.UserReferredSignedUpUsersCountEntity;

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
	 *             throw this exception in case we failed to get short URL
	 * @throws ConflictException
	 */
	public void sendReferralLink(ReferalEntity referalEntity)
			throws ValidationFailedException, IOException, ConflictException;

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
	 * @throws NotFoundException
	 *             throw this exception in case the referral medium is not match
	 *             with our referral medium types
	 * @throws ValidationFailedException
	 */
	public void validateReferContact(ReferalEntity referalEntity)
			throws ConflictException, NotFoundException, ValidationFailedException;

	/**
	 * This method is used to check the contact existence in our data store
	 * 
	 * @param contactDetail
	 *            this is the user contact details like its phone number or
	 *            email
	 * @param contactType
	 *            like email or phone number
	 * @return true in case contact does not exist
	 */
	public boolean checkReferredContactExistence(String contactDetail, UserReferalMediumType contactType);

	/**
	 * This method is used to get the referral link for facebook sharing
	 * 
	 * @return the referral link
	 * @throws IOException
	 * @throws ConflictException
	 */
	public ReferalEntity getFaceBookReferralLink();

	/**
	 * This method is used to delete all the user related referral data like
	 * referSignUpCounts, ReferreContacts etc
	 * 
	 * @param userReferId
	 *            The userReferId of the user whose all referral data is to be
	 *            deleted
	 */
	public void deleteUserAllReferralData(String userReferId);

	/**
	 * This method is used to get the referral link for linked In sharing to a
	 * referred user
	 * 
	 * @return the referral link the generated referral link to post to referred
	 *         users
	 * @throws ConflictException
	 */
	public ReferalEntity getLinkedInReferralLink();

	/**
	 * This method is used to get the current logged in user referred signed-up
	 * users total count of all medium
	 * 
	 * @return the userReferredSignedUpUsersCountEntity with userId of logged in
	 *         user and total count of referred signed-up users of all mediums
	 * @throws UnauthorizedException
	 *             thrown when user is not found in session means is not logged
	 *             in
	 */
	public UserReferredSignedUpUsersCountEntity getLoggedInUserReferredSignedUpUsersCount()
			throws UnauthorizedException;
}
