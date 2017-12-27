package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.RewardEntity;
import com.boilerplate.service.interfaces.IRewardService;

public class RewardService implements IRewardService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(ReferralService.class);
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
	 * This is the subjects list for Rewards
	 */
	BoilerplateList<String> subjectsForReward = new BoilerplateList<>();

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
		} catch (Exception exEmail) {
			// if exception occurs log it
			logger.logException("RewardService", "SendRewardWinningUserDetailsInEmail", "try-Queue Reader - Send Email",
					exEmail.toString(), exEmail);
		}

	}

}
