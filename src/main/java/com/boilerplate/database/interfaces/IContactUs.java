package com.boilerplate.database.interfaces;

import com.boilerplate.java.entities.ContactUsEntity;
/**
 * This interface saves the contact us queries
 * @author mohit
 *
 */
public interface IContactUs {
	/**
	 * This method sets the Contact Us Query Data in DataBase
	 * @param contactUsEntity The ContactUsEntity
	 * @return The contactUs Entity
	 */
	public ContactUsEntity saveContactUsQueries(ContactUsEntity contactUsEntity);
}
