package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.java.entities.ReferalEntity;

/**
 * This class have method to publish the refer report
 * 
 * @author shiva
 *
 */
public class PublishReferReportObserver implements IAsyncWorkObserver {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(PublishReferReportObserver.class);

	/**
	 * This is the new instance of queue reader job
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader job
	 * 
	 * @param queueReaderJob
	 *            The queue reader job
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	/**
	 * This is the configuration manager wired instance
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This sets the configuration Manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the subject list for publish
	 */
	private BoilerplateList<String> subjects = null;

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		ReferalEntity referralEntity = (ReferalEntity) asyncWorkItem.getPayload();
		// Publish referral report
		this.publishToCRM(referralEntity);
	}

	/**
	 * This method is used to publish the referral details to sales force
	 * 
	 * @param referralEntity
	 *            this parameter contains the information regarding the user
	 *            Reference like refer unique id ,referral link,referral
	 *            contacts ,referral medium type etc.
	 */
	private void publishToCRM(ReferalEntity referralEntity) {
		// Get the status of is refer report publishing is enabled or not
		if (Boolean.valueOf(configurationManager.get("Is_REFERRAL_REPORT_PUBLISH_ENABLED"))) {
			// Create the publish entity
			PublishEntity publishEntity = this.createPublishEntity("PublishReferReportObserver.publishToCRM",
					configurationManager.get("AKS_REFER_PUBLISH_METHOD"),
					configurationManager.get("AKS_REFER_PUBLISH_SUBJECT"), referralEntity,
					configurationManager.get("AKS_REFER_PUBLISH_URL"),
					configurationManager.get("AKS_REFER_PUBLISH_TEMPLATE"),
					configurationManager.get("AKS_REFER_DYNAMIC_PUBLISH_URL"));
			if (subjects == null) {
				subjects = new BoilerplateList<>();
				subjects.add(configurationManager.get("AKS_PUBLISH_SUBJECT"));
			}
			try {
				logger.logInfo("PublishReferReportObserver", "publishToCRM", "Publishing report",
						"publish referral report status" + referralEntity.getUserId());
				queueReaderJob.requestBackroundWorkItem(publishEntity, subjects, "PublishReferReportObserver",
						"publishToCRM", configurationManager.get("AKS_PUBLISH_QUEUE"));
			} catch (Exception exception) {
				logger.logError("PublishReferReportObserver", "publishToCRM", "queueReaderJob catch block",
						"Exception :" + exception);
			}
		}
	}

	/**
	 * This method creates the publish entity.
	 * 
	 * @param method
	 *            the publish method
	 * @param publishMethod
	 *            the publish method
	 * @param publishSubject
	 *            the publish subject
	 * @param returnValue
	 *            the object
	 * @param url
	 *            the publish url
	 * @return the publish entity
	 */
	private PublishEntity createPublishEntity(String method, String publishMethod, String publishSubject,
			Object returnValue, String url, String publishTemplate, String isDynamicPublishURl) {
		PublishEntity publishEntity = new PublishEntity();
		publishEntity.setInput(new Object[0]);
		publishEntity.setMethod(method);
		publishEntity.setPublishMethod(publishMethod);
		publishEntity.setPublishSubject(publishSubject);
		publishEntity.setReturnValue(returnValue);
		publishEntity.setUrl(url);
		publishEntity.setDynamicPublishURl(Boolean.parseBoolean(isDynamicPublishURl));
		publishEntity.setPublishTemplate(publishTemplate);
		return publishEntity;
	}

}
