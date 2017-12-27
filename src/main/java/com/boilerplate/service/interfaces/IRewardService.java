package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.RewardEntity;

public interface IRewardService {

	/**
	 * This method is used to send, details of the user who won the rewards in
	 * email
	 * 
	 * @param rewardEntity
	 *            The reward Entity that contains reward winning user's name,
	 *            phonenumber, emailid
	 * @throws ValidationFailedException
	 *             thrown when name, phonenumber, emailid is null or empty
	 */
	public void sendRewardWinningUserDetailsInEmail(RewardEntity rewardEntity) throws ValidationFailedException;
}
