package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.service.interfaces.IContentService;

public class SendVoucherAlertEmailToAdminObserver implements IAsyncWorkObserver{

	/**
	 * This is the logger instance of the SendVoucherAlertEmailToAdmin observer
	 */
	private Logger logger = Logger.getInstance(SendVoucherAlertEmailToAdminObserver.class);

	/**
	 * This is the content service.
	 */
	@Autowired
	IContentService contentService;
	
	/**
	 * This sets the content service
	 * @param contentService This is the content service
	 */
	public void setContentService(IContentService contentService){
		this.contentService = contentService;
	}
	
	/**
	 * This is the configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;
	
	/**
	 * This sets the configuration Manager
	 * @param configurationManager
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager){
		this.configurationManager = configurationManager;
	}
	
	/**
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		//This method sends voucher alert email to admin
		this.sendEmail(asyncWorkItem.getPayload().toString());
	}
	
	/**
	 * Sends an email to experian
	 * @param bccsEmailList 
	 * @param ccsEmailList 
	 * @param firstName The first name
	 * @param surName The surname
	 * @param stage1Id The stade one id of the request
	 * @param proofFiles This is a list of proof files
	 * @throws Exception If there is an error in sending the email.
	 */
	public void sendEmail(String voucherCount) throws Exception{
		BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
		tosEmailList.add(configurationManager.get("Voucher_Count_Email_To"));
		BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
		BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
		BoilerplateList<String> attachmentsList = new BoilerplateList<String>();
		String subject=configurationManager.get("Voucher_Count_Email_Subject");
		String body = configurationManager.get("Voucher_Count_Message_Template");
		body = body.replace("@VoucherCount",voucherCount);
		EmailUtility.send(tosEmailList,ccsEmailList, bccsEmailList, subject, body, attachmentsList);
		logger.logInfo("SendVoucherCountEmailToAdmin", "sendEmail", "SuccessFully Sent Voucher Count Email To Admin", "Total Available Vouchers Count is:" + voucherCount);
	}
}
