package com.boilerplate.service.interfaces;

import com.boilerplate.framework.HttpResponse;
public interface ISendSMSService {
	/**
	 * This  method is used to send the sms
	 * @param url The sms url
	 * @param phoneNumber The user phone number
	 * @param message The message
	 * @return The http response
	 * @throws Exception This exception throws in case of queue inserting failure.
	 */
	public HttpResponse send(String url, String phoneNumber, String message)
			throws Exception ;
	
	
}
