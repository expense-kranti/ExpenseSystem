package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;
import com.boilerplate.database.interfaces.IContactUs;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ContactUsEntity;
import com.boilerplate.service.interfaces.IContactUsService;

public class ContactUsService implements IContactUsService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(ArticleService.class);

	/**
	 * This is the instance of IContactUs
	 */
	@Autowired
	private IContactUs contactUsDataAccess;

	/**
	 * This method sets the instance of IContactUs
	 * 
	 * @param contactUsDataAccess
	 *            The contactUsDataAccess
	 */
	public void setContactUsDataAccess(IContactUs contactUsDataAccess) {
		this.contactUsDataAccess = contactUsDataAccess;
	}

	/**
	 * This is an instance of the queue job, to save the session back on to the
	 * database async
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader job
	 * 
	 * @param queueReaderJob
	 *            The queue reader job
	 */
	public void setQueueReaderJob(
			com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	/**
	 * This is the subjects List For ContactUs
	 */
	BoilerplateList<String> subjectsForContactUs = new BoilerplateList();

	/**
	 * Initializes the Bean
	 */
	public void initialize() {
		subjectsForContactUs.add("ContactUs");
	}

	/**
	 * @see IContactUsService.contactUs
	 */
	@Override
	public void contactUs(ContactUsEntity contactUsEntity) {
		try {
			ContactUsEntity returnedContactUsEntity = this.contactUsDataAccess
					.saveContactUsQueries(contactUsEntity);
			queueReaderJob.requestBackroundWorkItem(contactUsEntity,
					subjectsForContactUs, "contactUsService", "contactUs");
		} catch (Exception exEmail) {
			// if an exception takes place here we cant do much hence just log
			// it and move forward
			logger.logException("ContactUsService", "contactUs",
					"try-Queue Reader - Send Email", exEmail.toString(),
					exEmail);
		}
	}


}
