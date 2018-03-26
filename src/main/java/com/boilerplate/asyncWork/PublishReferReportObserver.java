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
	}

}
