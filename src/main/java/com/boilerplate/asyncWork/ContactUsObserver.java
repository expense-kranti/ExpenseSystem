package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ContactUsEntity;
import com.boilerplate.service.interfaces.IContentService;

/**
 * This class is the observer for contact us
 * @author mohit
 *
 */
public class ContactUsObserver implements IAsyncWorkObserver {
	
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
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		ContactUsEntity contactUsEntity = (ContactUsEntity)asyncWorkItem.getPayload();
		
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		tosEmailList.add(configurationManager.get("Contact_Person_Email"));
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();

		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();

		this.sendEmail(tosEmailList, ccsEmailList, bccsEmailList
				,contactUsEntity.getContactPersonName(), contactUsEntity.getContactPersonEmail(), contactUsEntity.getContactPersonMobileNumber(),contactUsEntity.getContactPersonMessage());

	}

	/**
	 * This method sends the email to right responsible contact person
	 * @param firstName The FirstName
	 * @param tosEmailList The tosEmailList
	 * @param ccsEmailList The ccsEmailList
	 * @param bccsEmailList The bccsEmailList
	 * @param phoneNumber The phoneNumber
	 * @param userKey The userKey
	 * @throws Exception The Exception
	 */
	public void sendEmail(BoilerplateList<String> tosEmailList, BoilerplateList<String> ccsEmailList, BoilerplateList<String> bccsEmailList,
			String name, String email, String phoneNumber,String message) throws Exception{
		
		String subject=contentService.getContent("WELCOME_MESSAGE_EMAIL_SUBJECT");
		subject = subject.replace("@FirstName",name);
		String body = contentService.getContent("CONTACT_US_EMAIL_BODY");
		body = body.replace("@ContactPersonName", name);
		body = body.replace("@ContactPersonEmail",email==null?"":email);
		body = body.replace("@ContactPersonMobileNumber",phoneNumber);
		body = body.replace("@ContactPersonMessage",message==null?"":message);
		
		EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, null);
	}
}
