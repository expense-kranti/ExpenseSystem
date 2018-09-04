package com.boilerplate.service.interfaces;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.ExpenseEntity;

/**
 * This interface has methods for sending email to users
 * 
 * @author ruchi
 *
 */
public interface IEmailService {

	/**
	 * This method is used to send email to employee and its approvers on
	 * submission for an expense
	 * 
	 * @param expenseEntity
	 *            this is the expense entity
	 * @throws Exception
	 *             throw this exception if any exception occurs
	 */
	public void sendEmailOnSubmission(ExpenseEntity expenseEntity, boolean isResubmitted) throws Exception;

	public void sendEmailOnRejection(ExpenseEntity expenseEntity) throws BadRequestException;

	public void sendEmailOnApproval(ExpenseEntity expenseEntity) throws BadRequestException;

}
