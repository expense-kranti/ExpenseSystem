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
import com.boilerplate.database.interfaces.ISFUpdateHash;
import com.boilerplate.database.mysql.implementations.MySQLBaseDataAccessLayer;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ReferralContactEntity;
import com.boilerplate.java.entities.ArticleEntity;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.java.entities.ReferredContactDetailEntity;
import com.boilerplate.java.entities.ReferredContactEntity;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;

/**
 * This class is used to pop or read queue and process each element
 * @author Yash
 *
 */
public class MySQLCreateReferalContactObserver extends MySQLBaseDataAccessLayer implements IAsyncWorkObserver {

	/**
	 * This is the instance of redissfupdatehashaccess
	 */
	@Autowired
	ISFUpdateHash redisSFUpdateHashAccess;
	
	public void setRedisSFUpdateHashAccess(ISFUpdateHash redisSFUpdateHashAccess) {
		this.redisSFUpdateHashAccess = redisSFUpdateHashAccess;
	}

	/**
	 * This is the instance of logger MySQLCreateOrUpdateUserObserver logger
	 */
	private static Logger logger = Logger.getInstance(MySQLCreateOrUpdateUserObserver.class);
	/**
	 * This is the instance of Referral Contact
	 */
	IReferral mySqlRefralContact;

	/**
	 * Set the mySqlRefralContact 
	 * @param mySqlRefralContact this is the mySqlRefralContact
	 */
	public void setMySqlRefralContact(IReferral mySqlRefralContact) {
		this.mySqlRefralContact = mySqlRefralContact;
	}

	/**
	 * This is the instance of Referral instance
	 */
	IReferral referral;

	/**
	 * This method is used to set referral
	 * @param referral
	 */
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

	/**
	 * This method get the user from Redis data store using supplied userId and
	 * save it in MySQL Database
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {

		// get the referral contact detail entity by redis key get from queue
		ReferredContactDetailEntity referredContactDetailEntity = referral
				.getReferredContactDetailEntity((String) asyncWorkItem.getPayload());
		
		// get the referral link from the redis data base
		String referralContactLink = referral.getUserReferralLink((String) asyncWorkItem.getPayload());

		// create the referral contact entity using referralcontactdetail and referral link and redis queue key
		ReferralContactEntity referralContactEntity = getReferralContact(referredContactDetailEntity,
				referralContactLink, (String) asyncWorkItem.getPayload());

		// get the userfile from Redis data store and save it in MySQL data base
		saveOrUpdateReferralContactEntityInMySQL(referralContactEntity, (String) asyncWorkItem.getPayload());

	}

	/**
	 * This is used to get Referral Contact Entity
	 * @param referredContactDetailEntity This is the referradContact  detail entiyt
	 * @param referralLink This is the referral link
	 * @param redisReferalKey this the redis key saved in queue
	 * @return the ReferralContactEntity
	 * @throws ParseException
	 */
	private ReferralContactEntity getReferralContact(ReferredContactDetailEntity referredContactDetailEntity,
			String referralLink, String redisReferalKey) throws ParseException {
		
		//instance of ReferarlContactEntity to return
		ReferralContactEntity referredContactEntity = new ReferralContactEntity();
		// split the key to get UserreferId , ReferralMediumType
		String redisReferalKeyData[] = redisReferalKey.split(":");
		// set date format to update date in Date format in (yyyy-MM-dd) - for coming time
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

		// set all values in ReferContact Entity		
		// set userreferid
		referredContactEntity.setUserReferId(redisReferalKeyData[0]);
		// set userId
		referredContactEntity.setUserId( this.redisSFUpdateHashAccess.hget(configurationManager.get("AKS_UUID_USER_HASH_BASE_TAG"), referredContactEntity.getUserReferId()));
		// set referral medium
		referredContactEntity.setReferralMediumType(redisReferalKeyData[1]);
		// set contact
		referredContactEntity.setContact(referredContactDetailEntity.getContact());
		// set refferedUserScore
		referredContactEntity.setRefferedUserScore(referredContactDetailEntity.getrefferedUserScore());
		// set coming user score
		referredContactEntity.setComingUserScore(referredContactDetailEntity.getComingUserScore());
		// set coming userId
		referredContactEntity.setComingUserId(referredContactDetailEntity.getComingUserId());
		// set referral link
		referredContactEntity.setReferralLink(referralLink);		
		// set coming time if ComiingTime is not empty
		if (referredContactDetailEntity.getComingTime() != null && !(referredContactDetailEntity.getComingTime().isEmpty())) {
			referredContactEntity.setComingTime(dateFormat.parse(referredContactDetailEntity.getComingTime()));
		}		
		
		return referredContactEntity;
	}

	/**
	 * Method used to save redis data into MySql 
	 * @param ReferralContactEntity This is the referralContactEntity
	 * @param userReferId This is the userReferId
	 * @throws Exception
	 */
	private void saveOrUpdateReferralContactEntityInMySQL(ReferralContactEntity referralContactEntity,
			String userReferId) throws Exception {

		// Get the SQL query from configurations for get the list of user Referal
		String sqlQuery = configurationManager.get(sqlQueryGetUserReferralContact);
		// Get the ReferralContactEntity list to update the row for exist data
		referralContactEntity = getReferralContactEntityUpdated(referralContactEntity, sqlQuery);
		// save referal Contact to mySQL
		try {
			// add user to the mysql database
			mySqlRefralContact.mySqlSaveReferalData(referralContactEntity);
		} catch (Exception ex) {
			logger.logException("MySQLCreateReferalContactObserver", "mySqlSaveReferalData",
					"try-catch block calling save method", ex.getMessage(), ex);
		}
		// delete key from redis
		referral.deleteItemFromRedisUserReferIdSet(userReferId);
	}

	/**
	 * This method is used to get saved ReferralContactEntity and if exist
	 * update them with new ReferralContactEntity
	 * @param referralContactEntity This is the referralCotactEntity
	 * @param sqlQuery the Sqlquery
	 * @return the updated ReferralContactEntity with Id
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
