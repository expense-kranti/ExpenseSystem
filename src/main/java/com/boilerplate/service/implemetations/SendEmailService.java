package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.configurations.ConfigurationManager;
import com.boilerplate.database.interfaces.IUser;
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
	public void sendEmailOnSubmission(ExpenseEntity expenseEntity) throws Exception {
		// fetch user from expense
		ExternalFacingUser expenseUser = mySqlUser.getUser(expenseEntity.getUserId());
		// fetch approver from database for this user
		ExternalFacingUser approver = mySqlUser.getUser(expenseUser.getApproverId());
		// fetch super approver
		ExternalFacingUser superApprover = mySqlUser.getUser(expenseUser.getSuperApproverId());
		// prepare tos list
		BoilerplateList<String> tos = new BoilerplateList<>();
		tos.add(approver.getEmail());
		// prepare ccs list
		BoilerplateList<String> ccs = new BoilerplateList<>();
		ccs.add(superApprover.getEmail());
		// prepare bcc list
		BoilerplateList<String> bccs = new BoilerplateList<>();
		// employee name
		String name = expenseUser.getFirstName() + " "
				+ (expenseUser.getMiddleName() == null ? " " : expenseUser.getMiddleName()) + " "
				+ expenseUser.getLastName();
		// send email
		EmailUtility.send(tos, ccs, bccs,
				configurationManager.get("SUBJECT_FOR_EXPENSE_SUBMISSION").replace("@employeeName", name),
				configurationManager.get("CONTENT_FOR_EXPENSE_SUBMISSION"), null);

	}

}
