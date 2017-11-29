package com.boilerplate.database.redis.implementation;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.CampaignType;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ReferralLinkEntity;
import com.boilerplate.java.entities.UserReferalMediumType;

/**
 * This class have method to manage user referral details
 * 
 * @author shiva
 *
 */
public class RedisReferral extends BaseRedisDataAccessLayer implements IReferral {

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This variable is used to a prefix for key of user ReferredContact
	 */
	private static final String ReferredContact = "ReferredContact:";

	/**
	 * This variable is used to a prefix for key of Campaign
	 */
	private static final String Campaign = "Campaign:";

	/**
	 * This variable is used to a prefix for key of user Referral
	 */
	private static final String UserReferral = "UserReferral:";

	/**
	 * @see IReferral.getUserReferredContacts
	 */
	@Override
	public ReferalEntity getUserReferredContacts() {
		ReferalEntity referalEntity = new ReferalEntity();
		// Declare a new map to store the referred data of user
		Map<String, Map<String, Map<String, String>>> userReferredContact = new BoilerplateMap<>();
		// Get all the referral dates made by user
		Set<String> listOfReferDates = this.getUserAllReferralDates();
		// For each type get data
		for (UserReferalMediumType referType : UserReferalMediumType.values()) {
			// Declare new map to hold referral data
			Map<String, Map<String, String>> userOneDayReferralData = new HashMap<>();
			// Run for loop to extract all date data
			for (String date : listOfReferDates) {
				// Get the referred data of user
				userOneDayReferralData.put(date, super.hgetAll(ReferredContact
						+ RequestThreadLocal.getSession().getUserId() + ":" + date + ":" + referType.toString()));
			}
			// Put type of referred medium and its data to map
			userReferredContact.put(referType.toString(), userOneDayReferralData);
		}
		// Set the referred data into entity
		referalEntity.setReferredContacts(userReferredContact);
		return referalEntity;
	}

	/**
	 * This method is used to get all the exist referral dates
	 * 
	 * @return user all referral dates
	 */
	private Set<String> getUserAllReferralDates() {
		// Get exist data date
		Set<String> listOfDatesKey = super.keys(
				ReferredContact + RequestThreadLocal.getSession().getUserId() + ":" + "*");
		// New set to store the referral dates
		Set<String> listOfReferDates = new HashSet<>();
		// Run for to get all referral dates
		for (String dates : listOfDatesKey) {
			// Split the keys
			String[] splittedKeys = dates.split(":");
			// Add date to list of referral dates
			listOfReferDates.add(splittedKeys[splittedKeys.length - 2]);
		}
		return listOfReferDates;
	}

	/**
	 * @see IReferral.saveUserReferredContacts
	 */
	@Override
	public void saveUserReferredContacts(ReferalEntity referalEntity) {
		// Run for loop to insert all referral contact to map
		for (Object contact : referalEntity.getReferralContacts()) {
			// Convert a object into entity
			ReferralLinkEntity referralLinkEntity = (ReferralLinkEntity) contact;
			// Save data to redis
			super.hset(
					ReferredContact + RequestThreadLocal.getSession().getUserId() + ":" + Date.valueOf(LocalDate.now())
							+ ":" + referalEntity.getReferralMediumType(),
					referralLinkEntity.getContact().toUpperCase(), referralLinkEntity.getReferralLink(),
					Integer.valueOf(configurationManager.get("REFERRED_CONTACT_EXPIRATION_TIME_IN_MINUTE")) * 60);
		}
	}

	/**
	 * @see IReferral.getTodayReferredContactsCount
	 */
	@Override
	public Integer getTodayReferredContactsCount(UserReferalMediumType referralMediumType) {
		// Get the referred data of user
		Map<String, String> referralData = super.hgetAll(ReferredContact + RequestThreadLocal.getSession().getUserId()
				+ ":" + Date.valueOf(LocalDate.now()) + ":" + referralMediumType.toString());
		// Return map size
		return referralData.size();
	}

	/**
	 * @see IReferral.saveUserReferralDetail
	 */
	@Override
	public void saveUserReferralDetail(ReferalEntity referalEntity) {
		// Run for loop to insert all referral contact to map
		for (Object contact : referalEntity.getReferralContacts()) {
			// Convert a object into entity
			ReferralLinkEntity referralLinkEntity = (ReferralLinkEntity) contact;
			// Save data to redis
			super.hset(UserReferral + referalEntity.getUserId() + ":" + referalEntity.getReferralUUID(),
					referralLinkEntity.getReferralUUID(), referralLinkEntity.getContact().toUpperCase());
		}
	}

	/**
	 * @see IReferral.saveReferralDetail
	 */
	@Override
	public void saveReferralDetail(ReferalEntity referalEntity) {
		// Save refer details
		super.hset(
				Campaign + CampaignType.valueOf("Refer").toString() + ":" + referalEntity.getReferralMediumType() + ":"
						+ referalEntity.getReferralUUID(),
				referalEntity.getUserId(), Date.valueOf(LocalDate.now()).toString());
	}

	/**
	 * @see IReferral.getUserReferredContactDeatils
	 */
	@Override
	public String getUserReferredContactDetails(ReferalEntity referalEntity) {
		// Get all the referral dates made by user
		Set<String> listOfReferDates = this.getUserAllReferralDates();
		String referredData = null;
		// Check in all date
		for (String date : listOfReferDates) {
			referredData = super.hget(
					ReferredContact + RequestThreadLocal.getSession().getUserId() + ":" + date + ":"
							+ referalEntity.getReferralMediumType().toString(),
					((String) referalEntity.getReferralContacts().get(0)).toUpperCase());
			if (referredData != null) {
				break;
			}
		}
		return referredData;
	}

	/**
	 * @see IReferral.getUserReferredContactDeatils
	 */
	@Override
	public Map<String, String> getCampaignDetails(String campaignSource, String mediumType, String uuid) {
		return super.hgetAll(Campaign + campaignSource + ":" + mediumType + ":" + uuid);
	}
}
