package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.framework.EmailUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.service.interfaces.IEmailService;

/**
 * this class implements IEmailService
 * 
 * @author ruchi
 *
 */
public class SendEmailService implements IEmailService {

	/**
	 * This is the instance of IUser
	 */
	@Autowired
	IUser mySqlUser;

	/**
	 * This method set the mysqluser
	 * 
	 * @param mySqlUser
	 *            the mySqlUser to set
	 */
	public void setMySqlUser(IUser mySqlUser) {
		this.mySqlUser = mySqlUser;
	}

	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(SendEmailService.class);

	/**
	 * This is the instance of configuration manager
	 */
	@Autowired
	ConfigurationManager configurationManager;

	/**
	 * This method is used to set the configurationManager
	 * 
	 * @param configurationManager
	 *            the configurationManager to set
	 */
	public void setConfigurationManager(ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @see IEmailService.sendEmailOnSubmission
	 */
	@Override
	public void sendEmailOnSubmission(ExpenseEntity expenseEntity, boolean isResubmitted) throws Exception {
		// fetch user from expense
		ExternalFacingUser expenseUser = mySqlUser.getUser(expenseEntity.getUserId());
		// fetch approver from database for this user
		ExternalFacingUser approver = mySqlUser.getUser(expenseUser.getApproverId());
		// fetch super approver
		// ExternalFacingUser superApprover =
		// mySqlUser.getUser(expenseUser.getSuperApproverId());
		// prepare tos list
		BoilerplateList<String> tos = new BoilerplateList<>();
		tos.add(approver.getEmail());
		// prepare ccs list
		BoilerplateList<String> ccs = new BoilerplateList<>();
		// ccs.add(superApprover.getEmail());
		// prepare bcc list
		BoilerplateList<String> bccs = new BoilerplateList<>();
		// employee name
		String name = expenseUser.getFirstName() + " " + expenseUser.getLastName();
		// subject for email
		String subject = null;
		if (isResubmitted)
			subject = configurationManager.get("SUBJECT_FOR_EXPENSE_RE_SUBMISSION");
		else
			subject = configurationManager.get("SUBJECT_FOR_EXPENSE_SUBMISSION");
		try {
			// send email
			EmailUtility.send(tos, ccs, bccs, subject,
					configurationManager.get("CONTENT_FOR_EXPENSE_SUBMISSION").replace("@employeeName", name), null);
		} catch (Exception ex) {
			logger.logException("SendEmailService", "sendEmailOnSubmission", "exceptionSendEmailOnSubmission",
					"Exception occurred while sending email on submission of expense id :" + expenseEntity.getId(), ex);
		}

	}

	@Override
	public void sendEmailOnRejection(ExpenseEntity expenseEntity) throws BadRequestException {
		// fetch user from expense
		ExternalFacingUser expenseUser = mySqlUser.getUser(expenseEntity.getUserId());
		// prepare tos list
		BoilerplateList<String> tos = new BoilerplateList<>();
		tos.add(expenseUser.getEmail());
		// prepare ccs list
		BoilerplateList<String> ccs = new BoilerplateList<>();
		// prepare bcc list
		BoilerplateList<String> bccs = new BoilerplateList<>();
		try {
			// send email
			EmailUtility.send(tos, ccs, bccs, configurationManager.get("SUBJECT_FOR_EXPENSE_REJECTION"),
					configurationManager.get("CONTENT_FOR_EXPENSE_REJECTION"), null);
		} catch (Exception ex) {
			logger.logException("SendEmailService", "sendEmailOnRejection", "exceptionSendEmailOnRejection",
					"Exception occurred while sending email for rejection of expense id :" + expenseEntity.getId(), ex);
		}

	}

	@Override
	public void sendEmailOnApproval(ExpenseEntity expenseEntity) throws BadRequestException {
		// fetch user from expense
		ExternalFacingUser expenseUser = mySqlUser.getUser(expenseEntity.getUserId());
		// fetch the finance email id
		// ExternalFacingUser financeUser =
		// mySqlUser.getUser(expenseUser.getFinanceId());
		// prepare tos list
		BoilerplateList<String> tos = new BoilerplateList<>();
		// tos.add(financeUser.getEmail());
		// prepare ccs list
		BoilerplateList<String> ccs = new BoilerplateList<>();
		ccs.add(expenseUser.getEmail());
		// prepare bcc list
		BoilerplateList<String> bccs = new BoilerplateList<>();
		// employee name
		String name = expenseUser.getFirstName() + " " + expenseUser.getLastName();
		String body = configurationManager.get("CONTENT_FOR_EXPENSE_APPROVED").replace("@employeeName", name);

		try {
			// send email
			EmailUtility.send(tos, ccs, bccs, configurationManager.get("SUBJECT_FOR_EXPENSE_APPROVED"), body, null);
		} catch (Exception ex) {
			logger.logException("SendEmailService", "sendEmailOnRejection", "exceptionSendEmailOnRejection",
					"Exception occurred while sending email for rejection of expense id :" + expenseEntity.getId(), ex);
		}

	}

}
