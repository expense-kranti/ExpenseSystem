package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.FeedBackEntity;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.IFeedbackService;

/**
 * This class implements the IFeedbackService class
 * 
 * @author urvij
 *
 */
public class FeedbackService implements IFeedbackService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(ReferralService.class);

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * This is the instance of the send email on feedback submit.
	 */
	@Autowired
	com.boilerplate.asyncWork.SendEmailOnFeedbackSubmitObserver sendEmailOnFeedbackSubmitObserver;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 *            The configuration manager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the observer for sending email on feedback submit
	 * 
	 * @param sendEmailOnFeedbackSubmitObserver
	 *            The email observer
	 */
	public void setSendEmailOnFeedbackSubmitObserver(
			com.boilerplate.asyncWork.SendEmailOnFeedbackSubmitObserver sendEmailOnFeedbackSubmitObserver) {
		this.sendEmailOnFeedbackSubmitObserver = sendEmailOnFeedbackSubmitObserver;
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
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	/**
	 * This variable is used to define the list of subjects, subjects are to
	 * define the background operations to be done on feedback submit
	 */
	BoilerplateList<String> subjectsForFeedbackSubmit = new BoilerplateList();

	/**
	 * Initializes the bean
	 */
	public void initialize() {
		subjectsForFeedbackSubmit.add("FeedbackSubmit");
	}

	/**
	 * This is the content service.
	 */
	@Autowired
	IContentService contentService;

	/**
	 * This sets the content service
	 * 
	 * @param contentService
	 *            This is the content service
	 */
	public void setContentService(IContentService contentService) {
		this.contentService = contentService;
	}

	/**
	 * @see IFeedBackService.sendEmailOnFeedback
	 */
	@Override
	public void sendEmailOnFeedbackByBackGroundJob(FeedBackEntity feedbackEntity)
			throws NotFoundException, ConflictException {
		// check if user already has given feedback
		if (RequestThreadLocal.getSession().getExternalFacingUser().isFeedBackSubmitted()) {
			throw new ConflictException("FeedBackEntity", "feedback has already been sent", null);
		}

		feedbackEntity.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());
		try {
			// Trigger back ground job to send selected feature through email
			queueReaderJob.requestBackroundWorkItem(feedbackEntity, subjectsForFeedbackSubmit, "FeedBackEntity",
					"sendEmailOnFeedbackByBackGroundJob");

		} catch (Exception ex) {
			// send email on failing of queue
			sendEmailOnFeedbackSubmitObserver.processUserFeedback(feedbackEntity);
			logger.logException("feedbackService",
					"sendEmailOnFeedbackByBackGroundJob", "try-Queue Reader", ex.toString()
							+ "FeedbackEntity inserting in queue is : " + Base.toJSON(feedbackEntity) + "Queue Down",
					ex);
		}
	}

}
