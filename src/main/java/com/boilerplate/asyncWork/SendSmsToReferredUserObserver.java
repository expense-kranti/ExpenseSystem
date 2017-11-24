package com.boilerplate.asyncWork;

import java.net.URLEncoder;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ReferalEntity;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.ISendSMSService;

/**
 * This class sends a SMS when a user is referred to the website
 * @author kranti123
 *
 */
public class SendSmsToReferredUserObserver implements IAsyncWorkObserver{

	/**
	 * This is the content service.
	 */
	@Autowired
	IContentService contentService;
	
	/**
	 * This sets the content service
	 * @param contentService This is the content service
	 */
	public void setContentService(IContentService contentService){
		this.contentService = contentService;
	}
	
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
	 * this is the send sms service
	 */
	@Autowired
	ISendSMSService sendSmsService;
	
	/**
	 * This method set the sms service.
	 * @param sendSmsService the sendSmsService to set
	 */
	public void setSendSmsService(ISendSMSService sendSmsService) {
		this.sendSmsService = sendSmsService;
	}

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		
		ReferalEntity referralEntity = (ReferalEntity)asyncWorkItem.getPayload();
				
		ExternalFacingUser currentUser = RequestThreadLocal.getSession().getExternalFacingUser();
		
		String currentUserName = currentUser.getFirstName() + " " +currentUser.getMiddleName() + " " +currentUser.getLastName();
		
		
		BoilerplateList<String> referralContacts = referralEntity.getReferralContacts();
		for(int i = 0; i< referralContacts.size(); i++){
			String phoneNumber = (String)referralContacts.get(i);
			
            this.sendSMS(currentUserName, phoneNumber);
		}
	}
	
	/**
	 * This method sends sms to the given phone number
	 * @param userName The userName of
	 * @param phoneNumber
	 * @throws Exception
	 */
	public void sendSMS(String userName, String phoneNumber) throws Exception{
		String url = configurationManager.get("SMS_ROOT_URL")+configurationManager.get("SMS_URL");
		url = url.replace("@apiKey", configurationManager.get("SMS_API_KEY"));
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@to", phoneNumber);
		String message = contentService.getContent("JOIN_INVITATION_SMS");
		message = message.replace("@UserName",userName);
		url = url.replace("@sender", configurationManager.get("SMS_SENDER"));
		url = url.replace("@message",URLEncoder.encode(message));
		String response=null;
		HttpResponse smsGatewayResponse= sendSmsService.send(url, phoneNumber, message);
	
	}
	
	
	
	


	
}
