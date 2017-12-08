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
	private static Logger logger = Logger
			.getInstance(CreateReferUniqueIdObserver.class);
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
		ExternalFacingReturnedUser user = (ExternalFacingReturnedUser) asyncWorkItem
				.getPayload();
		this.CreateReferUUID(user);
	}
	/**
	 * This method create the user unique id
	 * @param user The user
	 * @throws ConflictException
	 */
	private void CreateReferUUID(ExternalFacingReturnedUser user) throws ConflictException {

		String userUUID = this.createUUID(Integer.valueOf(
				configurationManager.get("REFERRAL_LINK_UUID_LENGTH")));
		user.setUserReferId(userUUID);
		// update user
		userDataAccess.update(user);
		referral.saveUserReferUUID(
				new ReferalEntity(user.getUserId(), user.getUserReferId()));
		this.publishToCRM(user);

	}
	private void publishToCRM(ExternalFacingReturnedUser user) {
		Boolean isPublishReport = Boolean.valueOf(configurationManager.get("Is_Publish_Report"));
		if (isPublishReport) {
			PublishEntity publishEntity = this.createPublishEntity("CalculateTotalScoreObserver.publishToCRM",
					configurationManager.get(""),
					configurationManager.get("UPDATE_AKS_USER_SUBJECT"),
						user,
					configurationManager.get(""),
					configurationManager.get(""),
					configurationManager.get(""));
			if (subjects == null) {
				subjects = new BoilerplateList<>();
				subjects.add(configurationManager.get("AKS_PUBLISH_SUBJECT"));
			}
			try {
				
				queueReaderJob.requestBackroundWorkItem(publishEntity, subjects, "CalculateTotalScoreObserver",
						"publishToCRM", configurationManager.get("AKS_PUBLISH_QUEUE"));
			} catch (Exception exception) {
				logger.logError("CreateReferUniqueIdObserver", "publishToCRM", "queueReaderJob catch block",
						"Exception :" + exception);
			}
		}
	}

	/**
	 * This method creates the publish entity.
	 * 
	 * @param method
	 *            the publish method
	 * @param publishMethod
	 *            the publish method
	 * @param publishSubject
	 *            the publish subject
	 * @param returnValue
	 *            the object
	 * @param url
	 *            the publish url
	 * @return the publish entity
	 */
	private PublishEntity createPublishEntity(String method, String publishMethod, String publishSubject,
			Object returnValue, String url, String publishTemplate, String isDynamicPublishURl) {
		PublishEntity publishEntity = new PublishEntity();
		publishEntity.setInput(new Object[0]);
		publishEntity.setMethod(method);
		publishEntity.setPublishMethod(publishMethod);
		publishEntity.setPublishSubject(publishSubject);
		publishEntity.setReturnValue(returnValue);
		publishEntity.setUrl(url);
		publishEntity.setDynamicPublishURl(Boolean.parseBoolean(isDynamicPublishURl));
		publishEntity.setPublishTemplate(publishTemplate);
		return publishEntity;
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
