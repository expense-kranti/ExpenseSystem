package com.boilerplate.asyncWork;

import java.io.IOException;
import java.util.Map;
import java.lang.StringBuffer;

import org.springframework.beans.factory.annotation.Autowired;
import com.boilerplate.framework.Logger;
import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.AssessmentStatusPubishEntity;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ICRMPublishEntity;
import com.boilerplate.java.entities.ICRMPublishDynamicURl;
import com.boilerplate.java.entities.PublishData;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.service.interfaces.IAuthTokenService;
import com.boilerplate.service.interfaces.IUserService;

/**
 * This is the bulk publishing observer to publish data on SF
 * 
 * @author Richil
 *
 */

public class PublishBulkObserver implements IAsyncWorkObserver {

	private Logger logger = Logger.getInstance(PublishBulkObserver.class);

	private BoilerplateList<String> subjects = null;

	private static final String CreateUserQueueName = "CREATE_USER_AKS";
	private static final String PublishReportQueueName = "REPORT_CREATED_AKS";
	/**
	 * This is the user service
	 */

	@Autowired
	IUserService userService;

	/**
	 * This method set the user service
	 * 
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
	}

	/**
	 * This is the configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This sets the configuration Manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(
			ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	String authToken = "";

	/**
	 * Instance of the Auth token service
	 */
	@Autowired
	IAuthTokenService authTokenService;

	/**
	 * Sets the organization service
	 * 
	 * @param organizationService
	 *            The organization service
	 */
	public void setAuthTokenService(IAuthTokenService authTokenService) {
		this.authTokenService = authTokenService;
	}

	/**
	 * This is the observer to send email
	 */
	@Autowired
	SendEmailOnUnsuccessfulBulkPublish sendEmailOnUnsuccessfulBulkPublish;

	public void setSendEmailOnUnsuccessfulBulkPublish(
			SendEmailOnUnsuccessfulBulkPublish sendEmailOnUnsuccessfulBulkPublish) {
		this.sendEmailOnUnsuccessfulBulkPublish = sendEmailOnUnsuccessfulBulkPublish;
	}

	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	@Autowired
	PublishBulkObserver publishObserver;

	/**
	 * This sets the queue reader jon
	 * 
	 * @param queueReaderJob
	 *            The queue reader jon
	 */
	public void setQueueReaderJob(
			com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		AsyncWorkItemList items = new AsyncWorkItemList();
		items = (AsyncWorkItemList) asyncWorkItem.getPayload();
		this.publishWithRetry(items.getItems());
	}

	/**
	 * This method retry to publish the task if getting 401 error from Sales
	 * force.
	 * 
	 * @param publishEntity
	 *            The public entity
	 * @throws Exception
	 */
	public void publishWithRetry(BoilerplateList<AsyncWorkItem> items)
			throws Exception {
		BoilerplateList<PublishEntity> publishList = new BoilerplateList<>();
		for (Object obj : items) {
			publishList.add(((AsyncWorkItem<PublishEntity>) obj).getPayload());
		}
		try {
			this.publish(publishList);
		} catch (Exception ex) {
			authToken = "";
			// request body to send in Email below
			String requestBody = createRequestBody(publishList);
			// Send Email when fails to publish again or in fetching auth token
			sendEmailForFailedBulkPublish(
					((PublishEntity) publishList.get(0)).getPublishSubject(),
					requestBody,
					"Code didn't execute properly in bulk publishing so caught in exception before http call.");
		}
	}

	/**
	 * This method publish the task Sales force.
	 * 
	 * @param publishList
	 *            The public entity
	 * @throws IOException
	 *             The io exception exception
	 * @throws NotFoundException
	 *             The not found exception
	 * @throws UnauthorizedException
	 *             The unauthorized exception
	 */
	public void publish(BoilerplateList<PublishEntity> publishList)
			throws IOException, NotFoundException, UnauthorizedException,
			Exception {
		if (publishList.size() < 1) {
			return;
		}
		// Creating request body to publish it on CRM
		String jsonArray = createRequestBody(publishList);
		if (!(jsonArray.equals("[]"))) {
			PublishEntity publishEntity = (PublishEntity) publishList.get(0);
			PublishData publishData = new PublishData();
			// fetching method signature from publishEntity
			publishData.setSubject(publishEntity.getPublishSubject());
			publishData.setMethod(publishEntity.getMethod());
			publishData.setData(Base.toMap(publishEntity.getReturnValue()));
			// check url is dynamic
			String publishUrl = "";
			if (publishEntity.isDynamicPublishURl()) {
				publishUrl = ((ICRMPublishDynamicURl) publishEntity
						.getReturnValue())
								.createPublishUrl(publishEntity.getUrl());
			} else {
				publishUrl = publishEntity.getUrl();
			}
			System.out.println(jsonArray);

			BoilerplateMap<String, BoilerplateList<String>> requestHeaders = new BoilerplateMap<String, BoilerplateList<String>>();
			requestHeaders = createRequestHeaders();
			HttpResponse httpResponse = HttpUtility.makeHttpRequest(publishUrl,
					requestHeaders, null, jsonArray,
					publishEntity.getPublishMethod());
			if (httpResponse.getHttpStatus() == 401) {
				// If http response code is 401 try again and make another hit.
				authToken = "";
				BoilerplateMap<String, BoilerplateList<String>> requestHeaderToHitAgain = new BoilerplateMap<String, BoilerplateList<String>>();
				requestHeaderToHitAgain = createRequestHeaders();
				HttpResponse httpResponseSF = HttpUtility.makeHttpRequest(
						publishUrl, requestHeaderToHitAgain, null, jsonArray,
						publishEntity.getPublishMethod());
				if (httpResponseSF.getHttpStatus() == 200) {
					logger.logInfo("PublishBulkObserver",
							"publish" + publishEntity.getPublishSubject(),
							"publishResponse",
							httpResponseSF.getResponseBody());
				} else {
					// Send Email if fails again to POST data to SF
					logger.logError("PublishBulkObserver", "Publish",
							publishEntity.getPublishSubject(),
							httpResponseSF.getResponseBody());
					sendEmailForFailedBulkPublish(publishData.getSubject(),
							jsonArray, httpResponseSF.getResponseBody());
				}
				return;
			}
			if (httpResponse.getHttpStatus() == 200) {
				logger.logInfo("PublishBulkObserver",
						"publish" + publishEntity.getPublishSubject(),
						"publishResponse", httpResponse.getResponseBody());
			} else {
				logger.logError("PublishBulkObserver", "Publish",
						publishEntity.getPublishSubject(),
						httpResponse.getResponseBody().toString());
				// Send Email when fails
				sendEmailForFailedBulkPublish(publishData.getSubject(),
						jsonArray, httpResponse.getResponseBody());
			}
		}

	}

	private void requestBackroundWorkItem(PublishEntity publishEntity,
			String subject) throws Exception {
		BoilerplateList<String> subjects = new BoilerplateList<>();
		subjects.add(subject);
		this.queueReaderJob.requestBackroundWorkItem(publishEntity, subjects,
				"PublishObserver", "requestBackroundWorkItem", subject);
	}

	/**
	 * This method sends an email to CMD when bulk publishing fails
	 * 
	 * @param subject
	 *            The subject of queue for bulk publishing
	 * @param requestBody
	 *            The request body sent for publishing
	 * @param responseBody
	 *            The response body sent for publishing
	 * @throws exEmail
	 *             Exception when Email fails
	 */
	private void sendEmailForFailedBulkPublish(String subject,
			String requestBody, String responseBody) {
		try {
			// Send an Email in case of Error
			BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
			BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
			tosEmailList.add(configurationManager
					.get("tosEmailListForPublishBulkFailure"));
			ccsEmailList.add(configurationManager
					.get("ccsEmailListForPublishBulkFailure"));
			BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
			sendEmailOnUnsuccessfulBulkPublish.sendEmail(tosEmailList,
					ccsEmailList, bccsEmailList, subject, requestBody,
					responseBody);
		} catch (Exception exEmail) {
			// if an exception takes place here we can't do much hence just log
			// it and move forward
			logger.logException("PublishBulkObserver",
					"sendEmailForFailedBulkPublish",
					"try-Queue Reader - Send Email", exEmail.toString(),
					exEmail);
		}
	}

	/**
	 * This method creates request body to publish it on SF
	 * 
	 * @param publishList
	 *            The list of item that needs to be pushed on Sales force
	 */
	private String createRequestBody(BoilerplateList<PublishEntity> publishList)
			throws Exception {
		// PublishData instantiation
		String jsonArray = "[";

		BoilerplateMap<String, String> userPublishMap = new BoilerplateMap<>();
		BoilerplateList<String> userReportPublishList = new BoilerplateList<>();
		
		if (((PublishEntity)publishList.get(0)).getPublishSubject().contentEquals(PublishReportQueueName)){
			for(int i =publishList.size()-1; i>= 0;i--){
				Object object = ((PublishEntity)publishList.get(i)).getReturnValue();
				AssessmentStatusPubishEntity publishReportEntity = (AssessmentStatusPubishEntity) object;
				if(userReportPublishList.contains(publishReportEntity.getUserId())){
					continue;
				}
				else{
					userReportPublishList.add(publishReportEntity.getUserId());
					jsonArray += ((ICRMPublishEntity)	((PublishEntity)publishList.get(i)).getReturnValue()).createPublishJSON(((PublishEntity)publishList.get(i)).getPublishTemplate());
					if(i+1 < publishList.size()){
		  				jsonArray+=",";
		  			}
				}
			}
		}
		else{
			for (int i = 0; i < publishList.size(); i++) {
			// Check if user being pushed into jsonArray again
			if (((PublishEntity) publishList.get(i)).getPublishSubject()
					.contentEquals(CreateUserQueueName)) {
				
					Object object = ((PublishEntity) publishList.get(i))
							.getReturnValue();
					ExternalFacingReturnedUser user = (ExternalFacingReturnedUser) object;
					if (userPublishMap.get(user.getUserId()) == null) {
						userPublishMap.put(user.getUserId(), "1");
						jsonArray += ((ICRMPublishEntity) ((PublishEntity) publishList
								.get(i)).getReturnValue()).createPublishJSON(
										((PublishEntity) publishList.get(i))
												.getPublishTemplate());
						if (i + 1 < publishList.size()) {
							jsonArray += ",";
						}
					} else {
						logger.logInfo("PublishBulkObserver", "createRequestBody",
								"In else block of User already added in publish Json check",
								"Prevented duplicate user entry from adding in publishing json"
										+ user.getUserId());
					}
				
			}
			else{
				jsonArray += ((ICRMPublishEntity)	((PublishEntity)publishList.get(i)).getReturnValue()).createPublishJSON(((PublishEntity)publishList.get(i)).getPublishTemplate());
				if(i+1 < publishList.size()){
	  				jsonArray+=",";
	  			}
			}
			}
		}
	

	/*
	 * Remove extra ',' if exists at the End of Json being published. This
	 * happened when we were preventing already added user to get added in Json.
	 * It was leaving ',' at the End. As in some cases duplicate Users were
	 * pushed into queue twice so added in Json as well.
	 */
	if(jsonArray!=null&&jsonArray.length()>0&&jsonArray.charAt(jsonArray.length()-1)==','){

	StringBuffer stringBufferJson = new StringBuffer(
			jsonArray);stringBufferJson.deleteCharAt(jsonArray.length()-1);jsonArray=stringBufferJson.toString();

	stringBufferJson.setLength(0);
	}

	jsonArray+="]";
	return jsonArray;}

	/**
	 * This method push the publish task in publish queue.
	 * 
	 * @param publishEntity
	 *            The publish entity
	 */
	private void pushPublishTaskAgainInQueue(PublishEntity publishEntity) {
		try {
			if (subjects == null) {
				subjects = new BoilerplateList<>();
				subjects.add("Publish");
			}
			queueReaderJob.requestBackroundWorkItem(publishEntity, subjects,
					"PublishLibrary", "requestPublishAsyncOffline",
					"_PUBLISH_QUEUE_");
		} catch (Exception ex) {
			try {
				// Send an Email in case of Error
				BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
				BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
				tosEmailList.add(configurationManager
						.get("tosEmailListForPublishBulkFailure"));
				ccsEmailList.add(configurationManager
						.get("ccsEmailListForPublishBulkFailure"));
				BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
				sendEmailOnUnsuccessfulBulkPublish.sendEmail(tosEmailList,
						ccsEmailList, bccsEmailList,
						publishEntity.getPublishSubject()
								+ "-Push in Publish queue failure",
						publishEntity.getReturnValue().toString(),
						"Exception :" + ex);
			} catch (Exception exe) {
				logger.logException("PublishBulkObserver",
						"pushPublishTaskAgainInQueue",
						"ExceptionBlock - Sending Email failure on Push in Publish queue failure",
						ex.toString(), exe);
			}
		}
	}

	/**
	 * This method creates request header body to publish it on SF
	 */
	private BoilerplateMap createRequestHeaders() throws Exception {
		BoilerplateMap<String, BoilerplateList<String>> requestHeaders = new BoilerplateMap<String, BoilerplateList<String>>();
		BoilerplateList<String> headerValue = new BoilerplateList<>();
		headerValue.add("application/json;charset=UTF-8");
		requestHeaders.put("Content-Type", headerValue);
		if (authToken.equals("")) {
			authToken = authTokenService.getAuthToken();
		}
		// Make HttpRequest with jsonBody on Url provided by publishEntity
		headerValue = new BoilerplateList<String>();
		headerValue.add("Bearer " + authToken);
		requestHeaders.put("authorization", headerValue);
		return requestHeaders;
	}

}
