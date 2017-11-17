package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ArticleEntity;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.IUserService;

/**
 * This class send a email for when user submitted a new article
 * 
 * @author shiva
 *
 */
public class SendEmailArticleObserver implements IAsyncWorkObserver {

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

	IUserService userService;

	/**
	 * This method set the user service
	 * 
	 * @param userService
	 *            the userService to set
	 */
	public void setUserService(IUserService userService) {
		this.userService = userService;
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
		ArticleEntity articleEntity = (ArticleEntity) asyncWorkItem.getPayload();
		// Declare a new list of tos of email list
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		// Add one contact into list of tos
		tosEmailList.add(configurationManager.get("Post_Article_Contact_Person_Email"));
		// Declare a new list of ccs of email list
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
		// Declare a new list of bccs of email list
		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
		// Get user details regarding the user id
		ExternalFacingReturnedUser userDetails = userService.get(articleEntity.getUserId());
		// Concatenate the user name
		String userName = (userDetails.getFirstName() == null ? "" : userDetails.getFirstName())
				+ (userDetails.getMiddleName() == null ? "" : " " + userDetails.getMiddleName())
				+ (userDetails.getLastName() == null ? "" : " " + userDetails.getLastName());
		// Send an email
		this.sendEmail(tosEmailList, ccsEmailList, bccsEmailList, userName, userDetails.getEmail(),
				userDetails.getPhoneNumber(), articleEntity.getContent(), articleEntity.getTitle(),
				articleEntity.getKeyWords());
	}

	/**
	 * This method sends the email to right responsible contact person
	 * 
	 * @param tosEmailList
	 *            this is tos list for email
	 * @param ccsEmailList
	 *            this is ccs list for email
	 * @param bccsEmailList
	 *            this is bccs list for email
	 * @param name
	 *            this is the name of user
	 * @param email
	 *            this is the user email
	 * @param phoneNumber
	 *            this is the user phone number
	 * @param contentText
	 *            this is the content of user submitted article
	 * @param title
	 *            this is the title of user submitted article
	 * @param keyWords
	 *            this is the keyWords of user submitted articles
	 * @throws Exception
	 *             throw this exception in case we get any error while trying to
	 *             publish a email
	 */
	public void sendEmail(BoilerplateList<String> tosEmailList, BoilerplateList<String> ccsEmailList,
			BoilerplateList<String> bccsEmailList, String name, String email, String phoneNumber, String contentText,
			String title, String keyWords) throws Exception {
		// Get post article email subject template
		String subject = contentService.getContent("POST_ARTICLE_EMAIL_SUBJECT");
		// Replace @ subject with title
		subject = subject.replace("@Subject", title);
		// Get email body for post article
		String body = contentService.getContent("POST_ARTICLE_EMAIL_BODY");
		// Replace @UserName with name
		body = body.replace("@UserName", name);
		// Replace @UserEmail with email if email is null then set it blank
		body = body.replace("@UserEmail", email == null ? "" : email);
		// Replace @UserMobileNumber with UserMobileNumber
		body = body.replace("@UserMobileNumber", phoneNumber);
		// Replace @Title with Title
		body = body.replace("@Title", title);
		// Replace @Content with Content
		body = body.replace("@Content", contentText);
		// Replace @KeyWords with KeyWords
		body = body.replace("@KeyWords", keyWords);
		// Send email
		EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, null);
	}
}
