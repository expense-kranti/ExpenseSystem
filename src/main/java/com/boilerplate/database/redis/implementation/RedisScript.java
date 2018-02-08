package com.boilerplate.database.redis.implementation;

import java.util.Map;
import java.util.Set;

import com.boilerplate.database.interfaces.IRedisScript;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ReferalEntity;

public class RedisScript extends BaseRedisDataAccessLayer implements IRedisScript {

	/**
	 * This is the main key of user in redis data base
	 */
	private static final String User = "USER:AKS";

	private static final String ReferredContact = "ReferredContact:";
	/**
	 * This variable is used to a prefix for key of user attempt details
	 */
	private static final String Attempt = "Attempt:";

	/**
	 * This variable is the prefix for getting the assessments for a user
	 */
	private static final String Assessment = "Assessment:";
	/**
	 * This variable is the prefix for getting the blogacitvity for a user
	 */
	private static final String BLOGUSER = "BLOGUSER:";
	/**
	 * This variable is the prefix for getting the user files for a user
	 */
	private static final String UserFile = "User_File:";
	/**
	 * This variable is the prefix for getting the user files for a user
	 */
	private static final String MonthlyScore = "MonthlyScore:";

	/**
	 * @see IRedisScript.getAllUserKeys
	 */
	@Override
	public Set<String> getAllUserKeys() {
		Set<String> listOfUserKey = super.keys(User + "*");
		return listOfUserKey;
	}

	@Override
	public ReferalEntity getReferDetails(String userReferId, String referMedium) {
		Map<String, String> referalEntityMap = super.hgetAll(ReferredContact + userReferId + ":" + referMedium);
		ReferalEntity referalEntity = Base.fromMap(referalEntityMap, ReferalEntity.class);
		return referalEntity;
	}

	@Override
	public Set<String> getAllUserAttemptKeys() {
		Set<String> listOfUserKey = super.keys(Attempt + "*");
		return listOfUserKey;
	}

	/**
	 * @see IRedisScript.getUserAllAssessmentIdKeys
	 */
	@Override
	public Set<String> getUserAllAssessmentIdKeys(String userId) {
		return super.keys(Assessment + userId + "*");
	}

	/**
	 * @see IRedisScript.getUserAllBlogActivityKeys
	 */
	@Override
	public Set<String> getUserAllBlogActivityKeys(String userId) {
		return super.keys(BLOGUSER + "*" + userId);
	}

	/**
	 * @see IRedisScript.getUserAllFileKeys
	 */
	@Override
	public Set<String> getUserAllFileKeys(String userId) {
		return super.keys(UserFile + userId);
	}

	/**
	 * @see IRedisScript.getUserAllReferralKeys
	 */
	@Override
	public Set<String> getUserAllReferralKeys(String mediumType) {
		return super.keys(ReferredContact + mediumType);
	}

	/**
	 * @see IRedisScript.getUserAllFileKeys
	 */
	@Override
	public Set<String> getUserAllMonthlyScoreKeys(String userId) {
		return super.keys(MonthlyScore + "*" + "*" + userId);
	}

}
