package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.RewardEntity;
import com.boilerplate.service.interfaces.IContentService;

/**
 * This class is the observer for sending email with reward winning user's
 * details
 * 
 * @author urvij
 *
 */
public class SendEmailWithRewardWinningUserDetailsObserver implements IAsyncWorkObserver {

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
	 * This is the configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This sets the configuration Manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		prepareForSendingEmail((RewardEntity) asyncWorkItem.getPayload());
	}

	/**
	 * This method prepare tosEmailList, bccEmailList, ccEmailList and content
	 * data values to be sent
	 * 
	 * @param rewardEntity
	 *            it contains the user details to be sent in email
	 * @throws Exception
	 *             thrown if exception occurs in sending email
	 */
	public void prepareForSendingEmail(RewardEntity rewardEntity) throws Exception {
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		tosEmailList.add(configurationManager.get("Reward_Person_Email"));
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();

		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();

		this.sendEmail(tosEmailList, ccsEmailList, bccsEmailList, rewardEntity.getName(), rewardEntity.getEmail(),
				rewardEntity.getPhoneNumber());
	}

	public void sendEmail(BoilerplateList<String> tosEmailList, BoilerplateList<String> ccsEmailList,
			BoilerplateList<String> bccsEmailList, String name, String email, String phoneNumber) throws Exception {

		String subject = contentService.getContent("REWARD_WINNING_USER_DETAILS_EMAIL_SUBJECT");

		String body = contentService.getContent("REWARD_WINNING_USER_DETAILS_EMAIL_BODY");
		body = body.replace("@RewardWinningUserName", name);
		body = body.replace("@RewardWinnigUserEmail", email);
		body = body.replace("@RewardWinnigUserMobileNumber", phoneNumber);

		EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, null);
	}

}
