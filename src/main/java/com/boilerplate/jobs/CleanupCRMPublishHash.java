package com.boilerplate.jobs;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.SendEmailOnUnsuccessfulBulkPublish;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.SFUpdateHashEntity;
import com.boilerplate.service.interfaces.IAuthTokenService;

/**
 * This is the cleanup job of CRMPublishHash
 * @author mohit
 *
 */
public class CleanupCRMPublishHash {


	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(CleanupCRMPublishHash.class);
	
	String authToken = "";
	
	/**
	 * Instance of the Auth token service
	 */
	@Autowired IAuthTokenService authTokenService;
	
	/**
	 * Sets the organization service
	 * @param IAuthTokenService The authtoken service
	 */
	public void setAuthTokenService(IAuthTokenService authTokenService){
		this.authTokenService = authTokenService;
	}
	
	
	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(
			com.boilerplate.configurations.ConfigurationManager 
			configurationManager){
		this.configurationManager = configurationManager;
	}
	
	/**
	 * This is the observer to send email
	 */
	@Autowired
	SendEmailOnUnsuccessfulBulkPublish sendEmailOnUnsuccessfulBulkPublish;
	
	/**
	 * This method sets the SendEmailOnUnsuccessfulBulkPublish Oberver
	 * @param sendEmailOnUnsuccessfulBulkPublish
	 */
	public void setSendEmailOnUnsuccessfulBulkPublish(
			SendEmailOnUnsuccessfulBulkPublish sendEmailOnUnsuccessfulBulkPublish) {
		this.sendEmailOnUnsuccessfulBulkPublish = sendEmailOnUnsuccessfulBulkPublish;
	}

	/**
	 * The RedisSFUpdateHash
	 */
	@Autowired
	com.boilerplate.database.redis.implementation.RedisSFUpdateHash redisSFUpdateHashAccess;
	
	/**
	 * This method sets RedisSFUpdateHash
	 * @return
	 */
	public void setRedisSFUpdateHashAccess(
			com.boilerplate.database.redis.implementation.RedisSFUpdateHash redisSFUpdateHashAccess) {
		this.redisSFUpdateHashAccess = redisSFUpdateHashAccess;
	}
	
	/**
	 * This method initializes
	 */
	public void initialize(){
		
	}
	
	/**
	 * Cleans up the Hash
	 * @throws Exception 
	 */
	public void cleanupSFUpdateHash() throws Exception{
		//if publish queue flag is true only then clean up SFUpdateHash and Publishing will work.
		if(Boolean.parseBoolean(configurationManager.get("IsPublishQueueEnabled"))){
			logger.logInfo("CleanupCRMPublishHash", "cleanupSFUpdateHash", "", "Starts SFUpdateHash Traversing");
			try {
				Set<String> hashKeysSet = redisSFUpdateHashAccess.keys(configurationManager.get("SF_Update_Hash_Name")+":AKS:??????????");
				BoilerplateList<SFUpdateHashEntity> list = new BoilerplateList<>();
				for(String hashKey: hashKeysSet){
					Map<String, String> hashmap = redisSFUpdateHashAccess.hgetAll(hashKey);
					if ((hashmap.get("accountId")!=null) && !hashmap.get("accountId").isEmpty()){
						if (list.size()==Integer.parseInt(configurationManager.get("Process_Bulk_Count"))){
							publishBulkHashData(Base.toJSON(list));
							list.clear();
						}
						list.add(createPublishJSON(hashmap));
						redisSFUpdateHashAccess.del(hashKey);
						redisSFUpdateHashAccess.del(hashKey + ":userMetaData");
					}
				}
				if (!list.isEmpty()){
					publishBulkHashData(Base.toJSON(list));
					list.clear();
				}
				
				logger.logInfo("CleanupCRMPublishHash", "cleanupSFUpdateHash", "", "Ends SFUpdateHash Traversing");
			} catch (Exception exception) {
				logger.logException("CleanupCRMPublishHash", "PublishBulkHashData",
						"inside try block" + exception.getMessage(),
	    				"during traversing of hashmap exception comes",
	    				exception);
			} 
		}
	}
	

	/**
	 * This method creates the user data for publishing
	 * @param hashmap The hashmap
	 * @return The SFUpdateHashEntity
	 */
	private SFUpdateHashEntity createPublishJSON(Map<String, String> hashmap) {
		SFUpdateHashEntity sfUpdateHashEntity = new SFUpdateHashEntity();
		sfUpdateHashEntity.setAccountId(hashmap.get("accountId"));
		if ((hashmap.get("location")!=null) && !hashmap.get("location").isEmpty())
			sfUpdateHashEntity.setLocation(hashmap.get("location"));
		if ((hashmap.get("dob")!=null) && !hashmap.get("dob").isEmpty())
			sfUpdateHashEntity.setDob(hashmap.get("dob"));
		if ((hashmap.get("alternateNumber")!=null) && !hashmap.get("alternateNumber").isEmpty())
			sfUpdateHashEntity.setAlternateNumber(hashmap.get("alternateNumber"));
		if ((hashmap.get("employmentStatus")!=null) && !hashmap.get("employmentStatus").isEmpty())
			sfUpdateHashEntity.setEmploymentStatus(hashmap.get("employmentStatus"));
		
	
		return sfUpdateHashEntity;
	}
	
	/**
	 * This method Publish Bulk Hash Data
	 * @param jsonBody The jsonBody
	 * @throws IOException The IOException
	 * @throws UnauthorizedException The UnauthorizedException
	 * @throws NotFoundException The NotFoundException
	 */
	private void publishBulkHashData(String jsonBody) throws IOException, UnauthorizedException, NotFoundException{
		logger.logInfo("CleanupCRMPublishHash", "PublishBulkHashData", "", "Starts Publishing");
		BoilerplateMap<String,BoilerplateList<String>> requestHeaders = new BoilerplateMap<String, BoilerplateList<String>>();
		BoilerplateList<String> headerValue = new BoilerplateList<>();
		headerValue.add("application/json;charset=UTF-8");
		requestHeaders.put("Content-Type",headerValue);
		if(authToken.equals("")){
			authToken = authTokenService.getAuthToken();
		}
		// Make HttpRequest with jsonBody on Url provided by publishEntity 
		headerValue = new BoilerplateList<String>();
		headerValue.add("Bearer "+authToken);
		requestHeaders.put("authorization", headerValue);
		logger.logInfo("CleanupCRMPublishHash", "PublishBulkHashData", "", "Publishing user update data"+jsonBody+"to url"+configurationManager.get("SF_Update_Account_Publish_URL"));
		HttpResponse httpResponse = HttpUtility.makeHttpRequest(
				configurationManager.get("SF_Update_Account_Publish_URL"), requestHeaders, null, jsonBody, configurationManager.get("SF_Update_Account_Publish_Method"));
		String responseBody = httpResponse.getResponseBody();
		if (responseBody==null){
			responseBody = "publish response is null";
		}	
		logger.logInfo("CleanupCRMPublishHash", "PublishBulkHashData", "", "Response status:"+httpResponse.getHttpStatus() + "Publish Response Body:" + responseBody);
		// If http response code is 401 try again
		if(httpResponse.getHttpStatus() == 401){
			// Again try for new authtoken and then again hit on SF
			rePublish(jsonBody);
			return;
		}
		if(httpResponse.getHttpStatus() == 200){
			logger.logInfo("CleanupCRMPublishHash", "PublishBulkHashData", "publishResponse", httpResponse.getResponseBody());
		}
		else{
			logger.logError("CleanupCRMPublishHash", "PublishBulkHashData", "publishResponse", httpResponse.getResponseBody().toString());
			// Send Email when fails
			sendExceptionEmail(httpResponse);
		}
		
		logger.logInfo("CleanupCRMPublishHash", "PublishBulkHashData", "", "Ends Publishing");
	}

	/**
	 * This method sends exception email
	 * @param httpResponse The httpResponse
	 */
	private void sendExceptionEmail(HttpResponse httpResponse){
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		tosEmailList.add(configurationManager.get("tosEmailListForPublishBulkFailure"));
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
		ccsEmailList.add(configurationManager.get("ccsEmailListForPublishBulkFailure"));
		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
		try {
			sendEmailOnUnsuccessfulBulkPublish.sendEmail(tosEmailList, ccsEmailList, bccsEmailList, configurationManager.get("SF_Update_Account_Publish_Subject"), httpResponse.getRequestBody(), httpResponse.getResponseBody());
		} catch (Exception e) {
			logger.logInfo("CleanupCRMPublishHash", "PublishBulkHashData"+ configurationManager.get("Publish_Bulk_HashData"), "publishResponse", httpResponse.getResponseBody());
		}
	}
	
	/**
	 * This method republish the data to SF
	 * @param jsonBody The jsonBody
	 * @throws IOException The IOException
	 * @throws NotFoundException The NotFoundException
	 */
	private void rePublish(String jsonBody) throws IOException, NotFoundException{
		BoilerplateMap<String,BoilerplateList<String>> requestHeaders = new BoilerplateMap<String, BoilerplateList<String>>();
		BoilerplateList<String> headerValue = new BoilerplateList<>();
		headerValue.add("application/json;charset=UTF-8");
		requestHeaders.put("Content-Type",headerValue);
		authToken = "";
		// Fecth new token and make another hit on CRM1
		authToken = authTokenService.getAuthToken();
		// Make HttpRequest with jsonBody on Url provided by publishEntity 
		headerValue = new BoilerplateList<String>();
		headerValue.add("Bearer "+authToken);
		requestHeaders.put("authorization", headerValue);
		logger.logInfo("CleanupCRMPublishHash", "PublishBulkHashData", "", "Publishing user update data"+jsonBody+"to url"+configurationManager.get("SF_Update_Account_Publish_URL"));
		HttpResponse httpResponse = HttpUtility.makeHttpRequest(
				configurationManager.get("SF_Update_Account_Publish_URL"), requestHeaders, null, jsonBody, configurationManager.get("SF_Update_Account_Publish_Method"));
		String responseBody = httpResponse.getResponseBody();
		if (responseBody==null){
			responseBody = "publish response is null";
		}	
		logger.logInfo("CleanupCRMPublishHash", "PublishBulkHashData", "", "Response status:"+httpResponse.getHttpStatus() + "Publish Response Body:" + responseBody);
		if(httpResponse.getHttpStatus() == 200){
			logger.logInfo("CleanupCRMPublishHash", "PublishBulkHashData", "publishResponse", httpResponse.getResponseBody());
		}
		else{
			// Send Email if fails again to POST data to SF
			logger.logError("CleanupCRMPublishHash", "PublishBulkHashData", "rePublishResponse", httpResponse.getResponseBody());
			sendExceptionEmail(httpResponse);
		}
	}
}
