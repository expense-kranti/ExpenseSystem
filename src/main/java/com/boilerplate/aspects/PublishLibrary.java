package com.boilerplate.aspects;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.PublishEntity;

public class PublishLibrary {
	
	private Logger logger = Logger.getInstance(PublishLibrary.class);
	
	private BoilerplateList<String> subjects = null;
	
	/**
	 * This is the configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;
	
	/**
	 * This sets the configuration Manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager){
		this.configurationManager = configurationManager;
	}
	
	
	
	/**
	 * This is an instance of the queue job, to save the session
	 * back on to the database async
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;
	
	
	/**
	 * This sets the queue reader jon
	 * @param queueReaderJob The queue reader jon
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob){
		this.queueReaderJob = queueReaderJob;
	}
	
	
	
	public void requestPublishAsyncOffline(String url, String publishMethod,Object[] input, Object returnValue,String methodCalled, String publishSubject,
			String publishTemplate, boolean isDynamicPublishURl){
		if(subjects == null){
			subjects = new BoilerplateList<>();
			subjects.add(configurationManager.get("AKS_PUBLISH_SUBJECT"));
		}
		PublishEntity publishEntity = new PublishEntity();
		publishEntity.setUrl(url);
		publishEntity.setInput(input);
		publishEntity.setMethod(methodCalled);
		publishEntity.setReturnValue(returnValue);
		publishEntity.setPublishSubject(publishSubject);
		publishEntity.setPublishTemplate(publishTemplate);
		publishEntity.setDynamicPublishURl(isDynamicPublishURl);
		publishEntity.setPublishMethod(publishMethod);
		try{
			queueReaderJob.requestBackroundWorkItem(publishEntity, subjects, "PublishLibrary", "requestPublishAsyncOffline", configurationManager.get("AKS_PUBLISH_QUEUE"));
		}catch(Exception ex){
			try{
				// Send an Email in case of Error
				BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
				BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
				tosEmailList.add(configurationManager.get("tosEmailListForPublishBulkFailure"));
				ccsEmailList.add(configurationManager.get("ccsEmailListForPublishBulkFailure"));
				BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
				// sendEmailOnUnsuccessfulBulkPublish.sendEmail(tosEmailList,
				// ccsEmailList, bccsEmailList,
				// publishEntity.getPublishSubject() +"-Push in Publish queue
				// failure"
				// ,publishEntity.getReturnValue().toString(), "Exception :" +
				// ex);
			}catch(Exception exe){
				logger.logException("PublishLibrary", "requestPublishAsyncOffline", "ExceptionBlock - Sending Email failure on Push in Publish queue failure", ex.toString(), exe);
			}
		}
		
	}
	
}
