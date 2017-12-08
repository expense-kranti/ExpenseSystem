package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.FeedBackEntity;

/**
 * This interface has method to send email on
 * 
 * @author urvij
 *
 */
public interface IFeedbackService {

	/**
	 * This method sends the email to user on feedback submission
	 * 
	 * @param feedbackEntity
	 *            The feedback Entity contains the user's selected feature
	 * @return ExternalFacingReturnUser The external facing user with updated
	 *         feedback submit status
	 * @throws ConflictException
	 *             if feedback has already been sent
	 * @throws NotFoundException
	 *             If user is not found whose feedback state is change
	 */
	public ExternalFacingReturnedUser sendEmailOnFeedbackByBackGroundJob(FeedBackEntity feedbackEntity)
			throws NotFoundException, ConflictException;

}
