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

		Set<String> referralContactEntitykeyList = referral
				.getUserAllReferredContactsKeys((String) asyncWorkItem.getPayload());
		Set<String> referralContactExpiredKeyList = referral
				.getUserAllReferredExpireContactKeys((String) asyncWorkItem.getPayload());

		// get referral contact entity list
		BoilerplateList<ReferralContactEntity> ReferralContactEntityList = getReferralContactList(
				referralContactEntitykeyList, referralContactExpiredKeyList);

		// get the userfile from Redis data store and save it in MySQL data base
		saveOrUpdateReferralContactEntityInMySQL((BoilerplateList<ReferralContactEntity>) ReferralContactEntityList,
				(String) asyncWorkItem.getPayload());

	}

	/**
	 * This is used to get Referral Contact Entity
	 * 
	 * @param referralContactEntitykeyList
	 * @return Referral Contact Entity List
	 * @throws ParseException
	 */
	private BoilerplateList<ReferralContactEntity> getReferralContactList(Set<String> referralContactEntitykeyList,
			Set<String> referralContactExpiredKeyList) throws ParseException {
		BoilerplateList<ReferralContactEntity> referralContactEntityList = new BoilerplateList<ReferralContactEntity>();

		// for each referred key get referal Contact Details
		for (String redisReferalKey : referralContactEntitykeyList) {
			// get ReferredContactDetailEntity from redis database key
			ReferralContactEntity referContact = new ReferralContactEntity();
			ReferredContactDetailEntity referredContactDetailEntity = referral
					.getReferredContactDetailEntity(redisReferalKey);
			// convert ReferredContactDetailEntity to ReferralContactEntity
			referContact = setValuesInReferralContactEntityEntity(referredContactDetailEntity, redisReferalKey);
			referralContactEntityList.add(referContact);
		}

		// get referal link from redis database
		for (String redisReferalExpiredKey : referralContactExpiredKeyList) {
			// get all key from redis data base
			String referralLink = referral.getUserReferralLink(redisReferalExpiredKey);
			// split the key
			String referralKeyData[] = redisReferalExpiredKey.split(":");
			
			// for each referral entity add the referral link from the referred expired contact 
			for (Object referal : referralContactEntityList) {
				if (((((ReferralContactEntity) referal).getUserReferId()).equalsIgnoreCase(referralKeyData[1]))
						&& ((((ReferralContactEntity) referal).getReferralMediumType())
								.equalsIgnoreCase(referralKeyData[2].toString()))
						&& ((((ReferralContactEntity) referal).getContact())
								.equalsIgnoreCase(referralKeyData[3].toString()))) {
					((ReferralContactEntity) referal).setReferralLink(referralLink);
				}
			}
		}

		return referralContactEntityList;
	}

	/**
	 * This method is used set ReferredContactDetailEntity to
	 * ReferralContactEntity
	 * 
	 * @param referredContactDetailEntity
	 * @param redisReferalKey
	 * @return
	 * @throws ParseException
	 */
	private ReferralContactEntity setValuesInReferralContactEntityEntity(
			ReferredContactDetailEntity referredContactDetailEntity, String redisReferalKey) throws ParseException {
		ReferralContactEntity referContact = new ReferralContactEntity();
		// spilt the key
		String redisReferalKeyData[] = redisReferalKey.split(":");
		//set date format to  add creation date in date format
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// set all values in ReferContact Entity
		referContact.setUserReferId(redisReferalKeyData[1]);
		referContact.setReferralMediumType(redisReferalKeyData[2]);
		referContact.setContact(referredContactDetailEntity.getContact());
		referContact.setCreationDate(dateFormat.parse(referredContactDetailEntity.getStringCreationDate()));
		referContact.setRefferedUserScore(referredContactDetailEntity.getrefferedUserScore());
		referContact.setComingUserScore(referredContactDetailEntity.getComingUserScore());
		referContact.setComingUserId(referredContactDetailEntity.getComingUserId());
		if(!referredContactDetailEntity.getStringCreationDate().isEmpty()){
			referContact.setComingTime(dateFormat.parse(referredContactDetailEntity.getComingTime()));
		}
		

		return referContact;
	}

	/**
	 * Method used to save redis data into MySql
	 * 
	 * @param ReferralContactEntity
	 * @param userReferId
	 * @throws Exception
	 */
	private void saveOrUpdateReferralContactEntityInMySQL(BoilerplateList<ReferralContactEntity> referralContactList,
			String userReferId) throws Exception {

		// Get the SQL query from configurations for get the list of user
		// Referal
		String sqlQuery = configurationManager.get(sqlQueryGetUserReferralContact);
		// get the ReferralContactEntity list to update the row for exist data
		referralContactList = getReferralContactEntityUpdatedList(referralContactList, sqlQuery);
		// save referal Contact to mySQL
		for(Object referalContact : referralContactList){
			mySqlRefralContact.mySqlSaveReferalData((ReferralContactEntity)referalContact);
		}
		
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
	private BoilerplateList<ReferralContactEntity> getReferralContactEntityUpdatedList(
			BoilerplateList<ReferralContactEntity> referralContactList, String sqlQuery) {
		for (Object referralData : referralContactList) {
			Map<String, Object> queryParameterMap = new HashMap<String, Object>();
			// Put user refer id in query parameter
			queryParameterMap.put("userReferId", ((ReferralContactEntity) referralData).getUserReferId());
			// put referral medium type
			queryParameterMap.put("referralMediumType", ((ReferralContactEntity) referralData).getReferralMediumType());
			// Put contact in query parameter
			queryParameterMap.put("contact", ((ReferralContactEntity) referralData).getContact());
			// Get the user articles from the data base
			List<ReferralContactEntity> requestedDataList = super.executeSelect(sqlQuery, queryParameterMap);

			// set the Id in the ReferralContactEntity
			if (requestedDataList.size() > 0) {
				for (ReferralContactEntity requestData : requestedDataList) {
					((ReferralContactEntity) referralData).setId(requestData.getId());
				}
			}
		}

		return referralContactList;
	}
}
