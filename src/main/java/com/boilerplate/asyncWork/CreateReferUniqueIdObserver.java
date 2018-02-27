package com.boilerplate.asyncWork;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentStatusPubishEntity;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.java.entities.ReferalEntity;

public class CreateReferUniqueIdObserver implements IAsyncWorkObserver {

	/**
	 * CalculateTotalScoreObserver logger
	 */
	private static Logger logger = Logger.getInstance(CreateReferUniqueIdObserver.class);
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader job
	 * 
	 * @param queueReaderJob
	 *            The queue reader jon
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * The autowired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;

	/**
	 * This is the setter for user data acess
	 * 
	 * @param iUser
	 */
	public void setUserDataAccess(IUser iUser) {
		this.userDataAccess = iUser;
	}

	/**
	 * This is a new instance of Referral
	 */
	IReferral referral;

	/**
	 * This method is used to set the referral
	 * 
	 * @param referral
	 *            the referral to set
	 */
	public void setReferral(IReferral referral) {
		this.referral = referral;
	}

	private BoilerplateList<String> subjects = null;

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		ExternalFacingReturnedUser user = (ExternalFacingReturnedUser) asyncWorkItem.getPayload();
		this.CreateReferUUID(user);
	}

	/**
	 * This method create the user unique id
	 * 
	 * @param user
	 *            The user
	 * @throws ConflictException
	 */
	private void CreateReferUUID(ExternalFacingReturnedUser user) throws ConflictException {
		// update user
		userDataAccess.update(user);

		referral.saveUserReferUUID(new ReferalEntity(user.getUserId(), user.getUserReferId()));

		logger.logInfo("CalculateTotalScoreObserver", "saveUserTotalScore", "SaveUserTotalScore",
				"Adding in Redis Set to update user for referId, UserReferId: " + user.getUserReferId());
		// add the user id in redis set to be later fetched and saved in MysqlDB
		// using job
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			userDataAccess.addInRedisSet(user);
		}


	}
	/**
	 * This method is used to create the UUID
	 * 
	 * @return the UUID
	 */
	private String createUUID(Integer uuidLength) {
		// New instance of random
		Random rand = new Random();
		String userReferId = "";
		// Run a for loop to generate a configurations define length uuid
		for (int i = 0; i < uuidLength; i++) {
			// Get random number
			int randomNum = rand.nextInt(26 - 0);
			// Concatenate new char to string
			userReferId = userReferId + String.valueOf((char) (randomNum + 97));
		}
		return userReferId;
	}
}
