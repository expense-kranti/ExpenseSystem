package com.boilerplate.database.interfaces;

import java.util.Set;

import com.boilerplate.java.entities.ReferalEntity;

/**
 * This class provide the method for Script related operations regarding redis
 * 
 * @author shiva
 *
 */
public interface IRedisScript {

	/**
	 * This method is used to get all users keys
	 * 
	 * @return list of keys
	 */
	public Set<String> getAllUserKeys();

	/**
	 * This method gets the details of referrals by user on a medium
	 * 
	 * @param referId
	 * @return ReferalEntity contains details of referrals
	 */
	public ReferalEntity getReferDetails(String userReferId, String referMedium);

	public Set<String> getAllUserAttemptKeys();

	/**
	 * This method is used to get the Assessment keys with ids of assessments
	 * attempted by user with given userId
	 * 
	 * @param userId
	 *            used to get the assessment keys of the user with given userids
	 * @return the set of all the assessments attempted by user
	 */
	public Set<String> getUserAllAssessmentIdKeys(String userId);

	/**
	 * This method is used to get the all the blogactivity keys with given
	 * userId
	 * 
	 * @param userId
	 *            used to get the blogactivities keys of the user with given
	 *            userids
	 * @return the set of all the blogactivity by user
	 */
	public Set<String> getUserAllBlogActivityKeys(String userId);

	/**
	 * This method is used to get the all the user files keys with given userId
	 * 
	 * @param userId
	 *            used to get the files keys of the user with given userids
	 * @return the set of all the user files keys
	 */
	public Set<String> getUserAllFileKeys(String userId);

	/**
	 * This method is used to get the all the user referral keys with given
	 * mediumType
	 * 
	 * @param mediumType
	 *            used to get the referral keys of the user with given referral
	 *            medium type
	 * @return the set of all the user referral keys
	 */
	public Set<String> getUserAllReferralKeys(String mediumType);

	/**
	 * Used to get all monthly score keys for given userId
	 * 
	 * @param userId
	 *            the userId against whom all keys to be fetched
	 * @return the Set of monthly score keys
	 */
	public Set<String> getUserAllMonthlyScoreKeys(String userId);
}
