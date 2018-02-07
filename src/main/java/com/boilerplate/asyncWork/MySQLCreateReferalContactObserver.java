package com.boilerplate.asyncWork;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.activemq.filter.function.splitFunction;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.mysql.implementations.MySQLBaseDataAccessLayer;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ReferralContactEntity;
import com.boilerplate.java.entities.ArticleEntity;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ReferredContactDetailEntity;
import com.boilerplate.java.entities.ReferredContactEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class MySQLCreateReferalContactObserver extends MySQLBaseDataAccessLayer implements IAsyncWorkObserver {

	IReferral mySqlRefralContact;

	public void setMySqlRefralContact(IReferral mySqlRefralContact) {
		this.mySqlRefralContact = mySqlRefralContact;
	}

	IReferral referral;

	public void setReferral(IReferral referral) {
		this.referral = referral;
	}

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This method is used to set configuration Manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This variable provide the key for SQL query for configurations this SQL
	 * query is used to get the user article
	 */
	private static String sqlQueryGetUserReferralContact = "SQL_QUERY_GET_USER_REFER_CONTACT";

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {

		// get stored referral key from redis for further use
		
		ReferredContactDetailEntity referredContactDetailEntity = referral
				.getReferredContactDetailEntity((String) asyncWorkItem.getPayload());
		String referralContactLink = referral.getUserReferralLink((String) asyncWorkItem.getPayload());

		// get referral contact entity list
		ReferralContactEntity referralContactEntity = getReferralContact(referredContactDetailEntity,
				referralContactLink, (String) asyncWorkItem.getPayload());

		// get the userfile from Redis data store and save it in MySQL data base
		saveOrUpdateReferralContactEntityInMySQL(referralContactEntity, (String) asyncWorkItem.getPayload());

	}

	/**
	 * This is used to get Referral Contact Entity
	 * 
	 * @param referralContactEntitykeyList
	 * @return Referral Contact Entity List
	 * @throws ParseException
	 */
	private ReferralContactEntity getReferralContact(ReferredContactDetailEntity referredContactDetailEntity,
			String referralLink, String redisReferalKey) throws ParseException {

		ReferralContactEntity referredContactEntity = new ReferralContactEntity();
		// spilt the key
		String redisReferalKeyData[] = redisReferalKey.split(":");
		// set date format to add creation date in date format
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// set all values in ReferContact Entity
		referredContactEntity.setUserReferId(redisReferalKeyData[0]);
		referredContactEntity.setReferralMediumType(redisReferalKeyData[1]);
		referredContactEntity.setContact(referredContactDetailEntity.getContact());
		referredContactEntity.setCreationDate(dateFormat.parse(referredContactDetailEntity.getStringCreationDate()));
		referredContactEntity.setRefferedUserScore(referredContactDetailEntity.getrefferedUserScore());
		referredContactEntity.setComingUserScore(referredContactDetailEntity.getComingUserScore());
		referredContactEntity.setComingUserId(referredContactDetailEntity.getComingUserId());
		referredContactEntity.setReferralLink(referralLink);
		if (referredContactDetailEntity.getComingTime() != null) {
			referredContactEntity.setComingTime(dateFormat.parse(referredContactDetailEntity.getComingTime()));
		}
		if (referredContactDetailEntity.getStringUpdateDate() != null) {
			referredContactEntity.setUpdationDate(dateFormat.parse(referredContactDetailEntity.getStringUpdateDate()));
		}

		return referredContactEntity;
	}

	/**
	 * Method used to save redis data into MySql
	 * 
	 * @param ReferralContactEntity
	 * @param userReferId
	 * @throws Exception
	 */
	private void saveOrUpdateReferralContactEntityInMySQL(ReferralContactEntity referralContactEntity,
			String userReferId) throws Exception {

		// Get the SQL query from configurations for get the list of user
		// Referal
		String sqlQuery = configurationManager.get(sqlQueryGetUserReferralContact);
		// get the ReferralContactEntity list to update the row for exist data
		referralContactEntity = getReferralContactEntityUpdated(referralContactEntity, sqlQuery);
		// save referal Contact to mySQL
		mySqlRefralContact.mySqlSaveReferalData(referralContactEntity);

		// delete key from redis
		referral.deleteItemFromRedisUserReferIdSet(userReferId);
	}

	/**
	 * This method is used to get saved ReferralContactEntity and if exist
	 * update them with new ReferralContactEntity
	 * 
	 * @param referralContactList
	 * @param sqlQuery
	 * @return
	 */
	private ReferralContactEntity getReferralContactEntityUpdated(ReferralContactEntity referralContactEntity,
			String sqlQuery) {

		Map<String, Object> queryParameterMap = new HashMap<String, Object>();
		// Put user refer id in query parameter
		queryParameterMap.put("userReferId", ((ReferralContactEntity) referralContactEntity).getUserReferId());
		// put referral medium type
		queryParameterMap.put("referralMediumType",
				((ReferralContactEntity) referralContactEntity).getReferralMediumType());
		// Put contact in query parameter
		queryParameterMap.put("contact", ((ReferralContactEntity) referralContactEntity).getContact());
		// Get the user articles from the data base
		List<ReferralContactEntity> requestedDataList = super.executeSelect(sqlQuery, queryParameterMap);

		// set the Id in the ReferralContactEntity
		if (requestedDataList.size() > 0) {
			for (ReferralContactEntity requestData : requestedDataList) {
				((ReferralContactEntity) referralContactEntity).setId(requestData.getId());
			}
		}
		return referralContactEntity;
	}
}
