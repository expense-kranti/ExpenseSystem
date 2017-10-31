package com.boilerplate.service.interfaces;

import com.boilerplate.java.entities.ContactUsEntity;
import com.boilerplate.java.entities.ResumeProfileEntity;

/**
 * 
 * @author mohit
 *
 */
public interface IContactUsService {
	
	/**
	 * This method sends the Notification-Email/SMS to Right REsponsible Person
	 * @param contactUsEntity The Contact Us Entity
	 */
	public void contactUs(ContactUsEntity contactUsEntity);

}
