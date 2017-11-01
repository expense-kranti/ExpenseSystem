package com.boilerplate.asyncWork;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import com.boilerplate.framework.Logger;
import com.boilerplate.aspects.PublishLibrary;
import com.boilerplate.database.redis.implementation.BaseRedisDataAccessLayer;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.service.interfaces.IAuthTokenService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

import scala.util.parsing.combinator.testing.Str;

public class PublishObserver extends BaseRedisDataAccessLayer implements IAsyncWorkObserver {
	
	private Logger logger = Logger.getInstance(PublishObserver.class);
	
	private BoilerplateList<String> subjects = null;
	
	/**
	 * The configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;
	
	/**
	 * This is the observer to send email
	 */
	@Autowired
	SendEmailOnUnsuccessfulBulkPublish sendEmailOnUnsuccessfulBulkPublish;
	
	public void setSendEmailOnUnsuccessfulBulkPublish(
			SendEmailOnUnsuccessfulBulkPublish sendEmailOnUnsuccessfulBulkPublish) {
		this.sendEmailOnUnsuccessfulBulkPublish = sendEmailOnUnsuccessfulBulkPublish;
	}
	
	
	/**
	 * The UserService
	 * @return
	 */
	
	@Autowired
	com.boilerplate.service.implemetations.UserService userService;

	
	public com.boilerplate.service.implemetations.UserService getUserService() {
		return userService;
	}
	public void setUserService(com.boilerplate.service.implemetations.UserService userService) {
		this.userService = userService;
	}
	
	/**
	 * Sets the configuration manager
	 * @param configurationManager The configuration manager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager){
		this.configurationManager = configurationManager;
	}
	
	String authToken = "";
			
	/**
	 * Instance of the Auth token service
	 */
	@Autowired IAuthTokenService authTokenService;
	
	/**
	 * Sets the organization service
	 * @param organizationService The organization service
	 */
	public void setAuthTokenService(IAuthTokenService authTokenService){
		this.authTokenService = authTokenService;
	}
	
	
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;
	
	@Autowired PublishObserver publishObserver;
	
	/**
	 * This sets the queue reader jon
	 * @param queueReaderJob The queue reader jon
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob){
		this.queueReaderJob = queueReaderJob;
	}
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		PublishEntity publishEntity = (PublishEntity) asyncWorkItem.getPayload();
		this.publishWithRetry(publishEntity);
	}
	/**
	 * This method retry to publish the task if getting 401 error from Sales force.
	 * @param publishEntity The public entity
	 * @throws Exception 
	 */
	public void publishWithRetry(PublishEntity publishEntity) throws Exception {
		try{
			this.publish(publishEntity);
		}
		catch(Exception ex){
			authToken = "";
			this.publish(publishEntity);
		}
	}
	/**
	 * This method push again the publish task in queue.
	 * @param publishEntity The public Entity
	 */
	private void pushAgainQueue(PublishEntity publishEntity){
		if(subjects == null){
			subjects = new BoilerplateList<>();
			subjects.add("Publish");
		}
		
		try{
			queueReaderJob.requestBackroundWorkItem(publishEntity, subjects, "PublishLibrary", "requestPublishAsyncOffline");
		}catch(Exception ex){
			
			logger.logException("PublishLibrary", "requestPublishAsyncOffline", "ExceptionBlock", ex.toString(), ex);
			
		}
	}
	
	/**
	 * This method publish the task  Sales force.
	 * @param publishEntity The public entity
	 * @throws IOException The io exception exception
	 * @throws NotFoundException The not found exception
	 * @throws UnauthorizedException The unauthorized exception
	 */
	public void publish(PublishEntity publishEntity) throws IOException, NotFoundException, UnauthorizedException,Exception {
		// PublishData instantiation
			switch (publishEntity.getPublishSubject()) {
				default:
					// This will push in Publish queue
					this.requestBackroundWorkItem(publishEntity, publishEntity.getPublishSubject());
					break;
		}
	}

private void requestBackroundWorkItem(PublishEntity publishEntity,String subject) throws Exception{
		BoilerplateList<String> subjects = new BoilerplateList<>();
		subjects.add(subject);
		try{
			this.queueReaderJob.requestBackroundWorkItem(publishEntity, subjects, "PublishObserver", "requestBackroundWorkItem", subject+"_");
		}catch(Exception ex){
			try{
				// Send an Email in case of Error
				BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
				BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
				tosEmailList.add(configurationManager.get("tosEmailListForPublishBulkFailure"));
				ccsEmailList.add(configurationManager.get("ccsEmailListForPublishBulkFailure"));
				BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
				sendEmailOnUnsuccessfulBulkPublish.sendEmail(tosEmailList, ccsEmailList, bccsEmailList, publishEntity.getPublishSubject() +"Push in Bulk Publish queue failure"
					,publishEntity.getReturnValue().toString(), "Exception :" + ex);
			}catch(Exception exe){
				logger.logException("PublishObserver", "requestBackroundWorkItem", "Exception Block - Sending Email failure while Pushing in Bulk Publish queue failure", ex.toString(), exe);
			}
		}
}


	
}
