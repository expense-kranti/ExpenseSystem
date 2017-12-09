package com.boilerplate.asyncWork;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.FeedBackEntity;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.IFileService;

/**
 * This class sends email on getting feedback(a selected feature as value) from
 * user
 * 
 * @author urvij
 *
 */
public class SendEmailOnFeedbackSubmitObserver implements IAsyncWorkObserver {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(SendEmailOnFeedbackSubmitObserver.class);

	// template of email to send
	private static String userSelectionTemplate = "";

	/**
	 * This is the content service
	 */
	@Autowired
	IContentService contentService;

	/**
	 * This sets the content service
	 * 
	 * @param contentService
	 *            This is the content service to set
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
	 * This sets the configuration manager
	 * 
	 * @param configurationManager
	 *            The configuration manager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * The autowired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;

	/**
	 * This is the setter for user data access
	 * 
	 * @param iUser
	 */
	public void setUserDataAccess(IUser iUser) {
		this.userDataAccess = iUser;
	}

	/**
	 * This is the instance of file service
	 */
	@Autowired
	private IFileService fileService;

	/**
	 * Sets the file service
	 * 
	 * @param fileService
	 *            The file service to set
	 */
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * This is the instance S3File Entity
	 */
	@Autowired
	com.boilerplate.databases.s3FileSystem.implementations.S3File file;

	/**
	 * This method sets the instance of S3File Entity
	 * 
	 * @param file
	 *            The file
	 */
	public void setFile(com.boilerplate.databases.s3FileSystem.implementations.S3File file) {
		this.file = file;
	}

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		FeedBackEntity feedbackEntity = (FeedBackEntity) asyncWorkItem.getPayload();
		try {
			this.processUserFeedback(feedbackEntity);
		} catch (Exception exception) {
			logger.logError("SendEmailOnFeedbackSubmitObserver", "observer", "catch block", "Exception : " + exception);
		}
	}

	/**
	 * This method saves the feedback state of user and sends the selected
	 * feature got as feedback in email to required receiver
	 * 
	 * @param feedbackEntity
	 *            The feedback entity contains selected feature of user and the
	 *            user id of the logged in user
	 * @throws NotFoundException
	 *             thrown when user with given id not found
	 * @throws ConflictException
	 *             
	 */
	public void processUserFeedback(FeedBackEntity feedbackEntity) throws NotFoundException,ConflictException {
		ExternalFacingReturnedUser user = userDataAccess.getUser(feedbackEntity.getUserId(),
					null);
		// list of emailId to whom to send
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		// list of ccemailsIds for this email
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
		// list of bccemialIds for this email
		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
		// email id of receiver
		tosEmailList.add(configurationManager.get("AXISBANK_EMAILID1_FOR_FEEDBACK_SUBMITTED"));
		tosEmailList.add(configurationManager.get("AXISBANK_EMAILID2_FOR_FEEDBACK_SUBMITTED"));
		tosEmailList.add(configurationManager.get("FEEDBACK_EMAIL"));
		if(configurationManager.get("Enviornment").equals("PRODUCTION")){
			tosEmailList.add(configurationManager.get("AXISBANK_EMAILID3_FOR_FEEDBACK_SUBMITTED"));
			tosEmailList.add(configurationManager.get("AXISBANK_EMAILID4_FOR_FEEDBACK_SUBMITTED"));
		}
		try {
			this.sendEmail(tosEmailList, ccsEmailList, bccsEmailList, 
					feedbackEntity,user);
		} catch (Exception ex) {
			// Log the exception
			logger.logException("SendEmailOnFeedbackSubmitObserver", "processUserFeedback",
					"While sending email and selected feature through the Email ~ This is the Email address "
							+ tosEmailList.get(0),
					ex.toString(), ex);
		}
	}

	/**
	 * This method sends the email
	 * 
	 * @param tosEmailList
	 *            The list of emailids in tos list
	 * @param ccsEmailList
	 *            The list of emailids in ccs list
	 * @param bccsEmailList
	 *            The list of emailids in bccs list
	 * @param selectedFeature
	 *            The user's selected feature got in feedback
	 * @throws Exception
	 *             is thrown if any exception occurs while sending email
	 */
	public void sendEmail(BoilerplateList<String> tosEmailList, BoilerplateList<String> ccsEmailList,
			BoilerplateList<String> bccsEmailList, 
			FeedBackEntity feedbackEntity,ExternalFacingReturnedUser user) throws Exception {
		String subject = contentService.getContent("FEEDBACK_EMAIL_SUBJECT");
		// Get the feature selection information email body
		if (userSelectionTemplate.equals("")) {
			FileEntity fileEntity = this.fileService
					.getFile(configurationManager.get("REGISTERATION_FEEDBACK_EMAIL_CONTENT"));
			String fileNameInURL = null;
			// Get file from local if not found then downloads
			if (!new File(configurationManager.get("RootFileDownloadLocation"), fileEntity.getFileName()).exists()) {

				fileNameInURL = this.file.downloadFileFromS3ToLocal(fileEntity.getFullFileNameOnDisk());
			} else {
				fileNameInURL = fileEntity.getFileName();
			}
			// Open the file
			userSelectionTemplate = FileUtils
					.readFileToString(new File(configurationManager.get("RootFileDownloadLocation") + fileNameInURL));
		}
		String body = userSelectionTemplate.replace("@UserFirstName", "FeatureSelectingTestUser");
		/// Replace @selectedFeature with the real selected input value
		body = body.replace("@Creditdig", feedbackEntity.getUserSelectedFeature());
		body = body.replace("@FinancialPlan", feedbackEntity.getPlatformType());
		body = body.replace("@username", user.getFirstName());
		body = body.replace("@userLastname", user.getLastName()==null?"":user.getLastName());
		// send email after all preparations
		EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, null);
	}

}
