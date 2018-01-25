package com.boilerplate.asyncWork;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IIncomeTax;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.java.entities.IncomeTaxEntity;
import com.boilerplate.service.interfaces.IContentService;
import com.boilerplate.service.interfaces.IFileService;

/**
 * This class sends an email with income tax details of the user to the user
 * when it fills contact details after calculating income tax
 * 
 * @author urvij
 *
 */
public class SendEmailWithIncomeTaxDetailsObserver implements IAsyncWorkObserver {

	private static String incomeTaxDetailsTemplate = "";

	// add dependency in root context
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
	 * This is the instance of IFileService
	 */
	@Autowired
	private IFileService fileService;

	/**
	 * Sets the fileService
	 * 
	 * @param fileService
	 *            to set
	 */
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * This is the instance of S3File Entity
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

	// add dependency in root context
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

	// add dependency in root context
	/**
	 * This is an instance of IIncomeTax
	 */
	IIncomeTax incomeTaxDataAccess;

	/**
	 * Sets the incomeTaxDataAccess
	 * 
	 * @param incomeTaxDataAccess
	 *            the incomeTaxDataAccess to set
	 */
	public void setIncomeTaxDataAccess(IIncomeTax incomeTaxDataAccess) {
		this.incomeTaxDataAccess = incomeTaxDataAccess;
	}

	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		// this payload is the user whose incometaxuuid will be used to get
		// income tax details
		IncomeTaxEntity incomeTaxEntity = (IncomeTaxEntity) asyncWorkItem.getPayload();
		// get the income tax details with matching uuid
		IncomeTaxEntity incomeTaxEntityFromDatastore = incomeTaxDataAccess.getIncomeTaxData(incomeTaxEntity.getUuid());
		// prepare email content and send income tax details to user email id
		prepareEmailContentAndSendToEmailReceiver(incomeTaxEntityFromDatastore);

	}

	public void prepareEmailContentAndSendToEmailReceiver(IncomeTaxEntity incomeTaxEntity) throws Exception {
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		tosEmailList.add(incomeTaxEntity.getEmailId());
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();

		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();

		this.sendEmail(tosEmailList, ccsEmailList, bccsEmailList, incomeTaxEntity);
	}

	/**
	 * This method sends the email to the given tosEmaillist, bccsEmaillist with
	 * prepared message to be send to those referred users in the list
	 * 
	 * @param referringUserFirstName
	 *            The first Name of referring user
	 * @param tosEmailList
	 *            The list of emailIds to whom email to be sent
	 * @param ccsEmailList
	 *            The list of ccemailIds to whom email to be sent
	 * @param bccsEmailList
	 *            The list of bccsemailIds to whom email to be sent
	 * @param referralLink
	 *            the referral Link sent by referring user to referred user
	 * @throws Exception
	 *             is thrown if any exception occurs while sending email
	 */
	public void sendEmail(BoilerplateList<String> tosEmailList, BoilerplateList<String> ccsEmailList,
			BoilerplateList<String> bccsEmailList, IncomeTaxEntity incomeTaxEntity) throws Exception {
		// Get subject of invitation email
		String subject = contentService.getContent("INCOME_TAX_DETAILS_EMAIL_SUBJECT");
		// Get the invitation email body
		if (incomeTaxDetailsTemplate.equals("")) {
			// template from s3

			// TODO add the key or the link of file location in baseredis
			// against INCOME_TAX_DETAILS_EMAIL_CONTENT
			FileEntity fileEntity = this.fileService
					.getFile(configurationManager.get("INCOME_TAX_DETAILS_EMAIL_CONTENT"));
			String fileNameInURL = null;
			// Get file from local if not found then downloads
			if (!new File(configurationManager.get("RootFileDownloadLocation"), fileEntity.getFileName()).exists()) {

				fileNameInURL = this.file.downloadFileFromS3ToLocal(fileEntity.getId());

			} else {
				fileNameInURL = fileEntity.getFileName();
			}

			// Open the file
			incomeTaxDetailsTemplate = FileUtils
					.readFileToString(new File(configurationManager.get("RootFileDownloadLocation") + fileNameInURL));
		}
		// prepare email content
		String body = incomeTaxDetailsTemplate.replace("@ctc",
				String.valueOf((long) Math.round(incomeTaxEntity.getCtcForLacAbreviation())));

		body = body.replace("@estimatedIncomeTax", String.valueOf(incomeTaxEntity.getEstimatedTax()));
		body = body.replace("@investmenmtUnderSection80C", String.valueOf(incomeTaxEntity.getInvestmentIn80C()));
		body = body.replace("@contributionUnderSection80CCD1B",
				String.valueOf(incomeTaxEntity.getInvestmentIn80CCD1B()));
		body = body.replace("@claimUnderSection80D", String.valueOf(incomeTaxEntity.getInvestmentIn80D()));
		body = body.replace("@claimUnderSection80E", String.valueOf(incomeTaxEntity.getInvestmentIn80E()));
		body = body.replace("@claimUnderSection24", String.valueOf(incomeTaxEntity.getInvestmentInSection24()));
		body = body.replace("@hraExempted", String.valueOf(incomeTaxEntity.getHraExempted()));
		body = body.replace("@medicalAllowance", String.valueOf(incomeTaxEntity.getMedicalAllowance()));
		body = body.replace("@travelAllowance", String.valueOf(incomeTaxEntity.getTravelAllowance()));
		body = body.replace("@basicSalary", String.valueOf(incomeTaxEntity.getBasicSalary()));
		body = body.replace("@rentPaidPerMonth", String.valueOf(incomeTaxEntity.getHouseRentPaidMonthly()));

		// Send the email after all preparations
		EmailUtility.send(tosEmailList, ccsEmailList, bccsEmailList, subject, body, null);

	}

}
