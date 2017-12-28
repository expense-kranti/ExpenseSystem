package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.SendEmailWithRewardWinningUserDetailsObserver;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.RewardEntity;
import com.boilerplate.service.interfaces.IRewardService;

public class RewardService implements IRewardService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(RewardService.class);
	/**
	 * This is an instance of the queue job
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
	 * This is the instance of SendEmailWithRewardWinningUserDetailsObserver
	 */
	private SendEmailWithRewardWinningUserDetailsObserver sendEmailWithRewardWinningUserDetailsObserver;

	/**
	 * Sets the SendEmailWithRewardWinningUserDetailsObserver
	 * 
	 * @param sendEmailWithRewardWinningUserDetailsObserver
	 *            the sendEmailWithRewardWinningUserDetailsObserver to set
	 */
	public void setSendEmailWithRewardWinningUserDetailsObserver(
			SendEmailWithRewardWinningUserDetailsObserver sendEmailWithRewardWinningUserDetailsObserver) {
		this.sendEmailWithRewardWinningUserDetailsObserver = sendEmailWithRewardWinningUserDetailsObserver;
	}

	/**
	 * This is the subjects list for Rewards
	 */
	BoilerplateList<String> subjectsForReward = new BoilerplateList<>();

	/**
	 * Initializes the bean
	 */
	public void initialize() {
		subjectsForReward.add("SendRewardWinningUserDetailsInEmail");
	}

	/**
	 * @see IRewardService.sendRewardWinningUserDetailsInEmail
	 */
	@Override
	public void sendRewardWinningUserDetailsInEmail(RewardEntity rewardEntity) throws ValidationFailedException {
		// validate input data for null or empty
		rewardEntity.validate();
		try {
			queueReaderJob.requestBackroundWorkItem(rewardEntity, subjectsForReward, "RewardService",
					"SendRewardWinningUserDetailsInEmail");
		} catch (Exception ex) {
			try {
				sendEmailWithRewardWinningUserDetailsObserver.prepareForSendingEmail(rewardEntity);
			} catch (Exception exEmail) {
				// now we can only log the exception
				logger.logException("RewardService", "SendRewardWinningUserDetailsInEmail",
						"try-Queue Reader - prepareForSendingEmail", exEmail.toString(), exEmail);
			}
		}

	}

}
