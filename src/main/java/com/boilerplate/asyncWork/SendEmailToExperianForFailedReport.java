package com.boilerplate.asyncWork;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.IUserService;

/**
 * This observer sends an email to experian in case the report was not available
 * due to any reason
 * 
 * @author gaurav.verma.icloud
 *
 */
public class SendEmailToExperianForFailedReport implements IAsyncWorkObserver {

	private Logger logger = Logger.getInstance(SendEmailToExperianForFailedReport.class);

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
	 * This is the user service
	 */
	@Autowired
	IUserService userService;

	/**
	 * sets the user service
	 * 
	 * @param userService
	 *            The user service
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
		ReportInputEntity reportInputEntity = (ReportInputEntity) asyncWorkItem.getPayload();
		ExternalFacingReturnedUser user = this.userService.get(reportInputEntity.getUserId());
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();

		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
		tosEmailList.add(configurationManager.get("ExperianToFirstEmailId"));
		tosEmailList.add(configurationManager.get("ExperianToSecondEmailId"));

		bccsEmailList.add(configurationManager.get("ExperianBCCEmailId"));
		this.sendEmail(tosEmailList, ccsEmailList, bccsEmailList, reportInputEntity.getFirstName(),
				reportInputEntity.getSurname(), reportInputEntity.getStage1Id(), reportInputEntity.getProofFiles(),
				reportInputEntity.getUserId());

	}

	/**
	 * Sends an email to experian
	 * 
	 * @param bccsEmailList
	 * @param ccsEmailList
	 * @param firstName
	 *            The first name
	 * @param surName
	 *            The surname
	 * @param stage1Id
	 *            The stade one id of the request
	 * @param proofFiles
	 *            This is a list of proof files
	 * @throws Exception
	 *             If there is an error in sending the email.
	 */
	public void sendEmail(BoilerplateList<String> tosEmailList, BoilerplateList<String> ccsEmailList,
			BoilerplateList<String> bccsEmailList, String firstName, String lastName, String stage1Id,
			BoilerplateList<String> proofFiles, String userId) throws Exception {
		
		String currentDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		String subject = null;
		if (stage1Id != null && !stage1Id.equals("")) {
			subject = contentService.getContent("EXPERIAN_KYC_EMAIL_SUBJECT");

			subject = subject.replace("@FirstName", firstName);
			subject = subject.replace("@LastName", lastName);
			subject = subject.replace("@Stage1Id", stage1Id == null ? "" : stage1Id);
			subject = subject.replace("@Date", currentDate);
			String body = contentService.getContent("EXPERIAN_KYC_EMAIL_Body");
			body = body.replace("@FirstName", firstName);
			body = body.replace("@LastName", lastName);
			body = body.replace("@Stage1Id", stage1Id == null ? "" : stage1Id);
			body = body.replace("@Date", currentDate);
			BoilerplateList<String> attachments = new BoilerplateList<>();
			for (Object file : proofFiles) {
				attachments.add(file);
			}
			EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, attachments);
			logger.logInfo("SendEmailToExperianForFailedReport", "sendEmail",
					"SuccessFul Send Email To Experian For Failed Report",
					"userId: " + userId + "UTI-ID: " + stage1Id + "AttachementList:" + proofFiles.toJSON());
		} else {
			logger.logError("SendEmailToExperianForFailedReport", "sendEmail", "ErrorSendEmailToExperianForFailedReport",
					"StageOneId in null or empty");
		}

	}

}