package com.boilerplate.asyncWork;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.framework.FileUtility;
import com.boilerplate.framework.Logger;

/**
 * This is the observer which sends Email when bulk publishing fails
 * @author Richil
 *
 */

public class SendEmailOnUnsuccessfulBulkPublish implements IAsyncWorkObserver {
	
	/**
	 * This is the instance of Logger
	 */
	Logger logger = Logger.getInstance(SendEmailOnUnsuccessfulBulkPublish.class);

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
	 * This is the instance of S3File Entity
	 */
	@Autowired 
	com.boilerplate.databases.s3FileSystem.implementations.S3File file;
	
	/**
	 * This method sets the instance of S3File Entity
	 * @param file The file
	 */
	public void setFile(com.boilerplate.databases.s3FileSystem.implementations.S3File file) {
		this.file = file;
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
	}

	/**
	 * Sends an email to the Backend Team on succesfull bulk publishing
	 * 
	 * @param to
	 *            Email to be sent to the Backend Team
	 * @param subject
	 *            The Email subject for attachment
	 * @param httpRequest and httpResponse
	 *            The httpRequest and Response to be sent in attachment
	 * @throws Exception
	 *             An exception
	 */
	public void sendEmail(BoilerplateList<String> tosEmailList, BoilerplateList<String> ccsEmailList,
			BoilerplateList<String> bccsEmailList, String publishSubject, String httpRequest, String httpResponse)
			throws Exception {
		String subject = publishSubject + "-" + contentService.getContent("BULK_PUBLISH_FAIL_EMAIL_SUBJECT");
		String fileData = "Bulk Publishing Request :" + httpRequest + "\n" + "Bulk Publishing Response :" + httpResponse;
		String fileAttachmentName = null;
		String s3FilePath = null;
		try{
			fileAttachmentName = FileUtility.createNewFile(fileData);
			MockMultipartFile multipartFile = new MockMultipartFile(fileAttachmentName, fileAttachmentName, "text/html", new FileInputStream(new File(configurationManager.get("RootFileDownloadLocation")+fileAttachmentName)));
			s3FilePath = file.saveFile(multipartFile);
		}
		catch(Exception exception){
			// We will log it in file as we can't do much if the 
			//path of attachment has not been returned.
			logger.logException("SendEmailOnUnsuccessfulBulkPublish", "sendEmail", "try-catch for attachment ", "Request and Reponse for Bulk Publish Attachment failure" + fileData, exception);
		}
		String body = "File Path: " + configurationManager.get("S3_Files_Path") + s3FilePath;
		BoilerplateList<String> attachments = new BoilerplateList<>();
		EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, attachments);
	}
}
