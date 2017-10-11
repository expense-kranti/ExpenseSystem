package com.boilerplate.service.implemetations;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.service.interfaces.ISendSMSService;

public class SendSMSService implements ISendSMSService{
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
	/**
	 * This is the required date format.
	 */
	private static final SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	
	/**
	 * @see ISendSMSService.send
	 */
	@Override
	public HttpResponse send(String url, String phoneNumber, String message)
			throws Exception {
		HttpResponse smsGatewayResponse = HttpUtility.makeHttpRequest(url, null, null, null,"GET");
		return smsGatewayResponse;
	}


	

}
