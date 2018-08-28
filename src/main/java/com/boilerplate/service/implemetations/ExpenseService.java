package com.boilerplate.service.implemetations;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IExpense;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.AttachmentEntity;
import com.boilerplate.java.entities.ExpenseApproveOrRejectEntity;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseHistoryEntity;
import com.boilerplate.java.entities.ExpenseStatusType;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.FetchExpenseEntity;
import com.boilerplate.java.entities.FileMappingEntity;
import com.boilerplate.java.entities.UserRoleType;
import com.boilerplate.service.interfaces.IEmailService;
import com.boilerplate.service.interfaces.IExpenseService;
import com.boilerplate.service.interfaces.IFileService;

/**
 * This class implements IExpenseService
 * 
 * @author ruchi
 *
 */
public class ExpenseService implements IExpenseService {

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
	 * This is the autowired instance of IExpense
	 */
	@Autowired
	IExpense mySqlExpense;

	/**
	 * This method is used to set autowired instance of IExpense
	 * 
	 * @param mySqlExpense
	 */
	public void setMySqlExpense(IExpense mySqlExpense) {
		this.mySqlExpense = mySqlExpense;
	}

	/**
	 * This is the autowired instance of IFileService
	 */
	@Autowired
	IFileService fileService;

	/**
	 * This method is used to set fileService
	 * 
	 * @param fileService
	 */
	public void setFileService(IFileService fileService) {
		this.fileService = fileService;
	}

	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(ExpenseService.class);

	@Autowired
	IEmailService sendEmailService;

	public void setSendEmailService(IEmailService sendEmailService) {
		this.sendEmailService = sendEmailService;
	}

	/**
	 * @see IExpenseService.createExpense
	 */
	@Override
	public ExpenseEntity createExpense(ExpenseEntity expenseEntity) throws Exception {
		// check if expense entity is valid or not
		expenseEntity.validate();
		// entity should not have any id
		if (expenseEntity.getId() != null)
			throw new ValidationFailedException("ExpenseEntity", "Id should be null", null);
		// Check whether user id exists and is active
		ExternalFacingUser externalFacingUser = mySqlUser.getUser(expenseEntity.getUserId());
		if (externalFacingUser == null || !externalFacingUser.getIsActive())
			throw new NotFoundException("ExpenseEntity", "USer not found or is inactive", null);
		// set status of expense as submitted
		expenseEntity.setStatus(ExpenseStatusType.Submitted);
		List<AttachmentEntity> attachments = expenseEntity.getAttachments();
		// set creation date and update date
		expenseEntity.setCreationDate(new Date());
		expenseEntity.setUpdationDate(new Date());
		// save expense in database
		expenseEntity = mySqlExpense.createExpense(expenseEntity);
		// save attachment mapping
		fileService.saveFileMapping(expenseEntity);
		// send email for submission of expense
		sendEmailService.sendEmailOnSubmission(expenseEntity);
		return expenseEntity;
	}

	/**
	 * @see IExpenseService.updateExpense
	 */
	@Override
	public ExpenseEntity updateExpense(ExpenseEntity expenseEntity) throws Exception {
		// check if expense entity is valid or not
		expenseEntity.validate();
		// entity should not have any id
		if (expenseEntity.getId() == null)
			throw new ValidationFailedException("ExpenseEntity", "Id should not be null", null);
		// check whether expense entity exists or not
		ExpenseEntity previousExpense = mySqlExpense.getExpense(expenseEntity.getId());
		if (previousExpense == null)
			throw new NotFoundException("ExpenseEntity", "Expense entity not found", null);

		// Check whether user id exists and is active
		ExternalFacingUser externalFacingUser = mySqlUser.getUser(expenseEntity.getUserId());
		if (externalFacingUser == null || !externalFacingUser.getIsActive())
			throw new NotFoundException("ExpenseEntity", "USer not found or is inactive", null);
		// if expense status is rejected then change it to re-submitted
		if (expenseEntity.getStatus() != null) {
			if (expenseEntity.getStatus() == ExpenseStatusType.Approver_Rejected
					|| expenseEntity.getStatus() == ExpenseStatusType.Finance_Rejected)
				expenseEntity.setStatus(ExpenseStatusType.Re_Submitted);
		} else
			throw new BadRequestException("ExpenseEntity", "Status should be null", null);
		// set creation date and update date
		expenseEntity.setCreationDate(previousExpense.getCreationDate());
		expenseEntity.setUpdationDate(new Date());

		// create a new expense history entity using the data from expense
		// entity
		ExpenseHistoryEntity expenseHistoryEntity = new ExpenseHistoryEntity(previousExpense.getId(),
				previousExpense.getCreationDate(), previousExpense.getUpdationDate(), previousExpense.getTitle(),
				previousExpense.getDescription(), previousExpense.getStatus(), previousExpense.getUserId(),
				previousExpense.getApproverComments());
		expenseHistoryEntity.setCreationDate(new Date());
		// save this history in mysql
		expenseHistoryEntity = mySqlExpense.saveExpenseHistory(expenseHistoryEntity);
		// update expense
		expenseEntity = mySqlExpense.updateExpense(expenseEntity);
		// update attachments
		List<AttachmentEntity> attachments = fileService.updateFileMapping(expenseEntity, expenseHistoryEntity);
		expenseEntity.setAttachments(attachments);
		return expenseEntity;
	}

	/**
	 * @see IExpenseService.getExpenses
	 * 
	 */
	@Override
	public List<ExpenseEntity> getExpenses(FetchExpenseEntity fetchExpenseEntity)
			throws ValidationFailedException, NotFoundException, BadRequestException {
		// validate entity
		fetchExpenseEntity.validate();
		// check if user exists
		if (mySqlUser.getUser(fetchExpenseEntity.getUserId()) == null)
			throw new NotFoundException("ExternalFacingUser", "User not found", null);
		// fetch list of expenses from database
		return mySqlExpense.getExpenses(fetchExpenseEntity);
	}

	/**
	 * @see IExpenseService.getExpensesForApproval
	 */
	@Override
	public List<ExpenseEntity> getExpensesForApproval(String approverId, UserRoleType role)
			throws NotFoundException, ValidationFailedException, BadRequestException {
		// check if approver id is not null or empty
		if (approverId == null)
			throw new ValidationFailedException("ExpenseEntity", "Approver id for fetching expenses is null or empty",
					null);
		// check if approver exists
		if (mySqlUser.getUser(approverId) == null)
			throw new NotFoundException("ExpenseEntity", "No user with gievn approver id found", null);
		// get list of expenses filed under the given approver
		List<Map<String, Object>> expenseMap = mySqlExpense.getExpensesForApprover(approverId, role);
		// check if expenses are not null
		if (expenseMap.size() == 0)
			throw new BadRequestException("ExpenseEntity", "No expenses found", null);
		// list of expense ids
		String expenseIds = "";
		// for each expense, fetch its attachment
		for (Map<String, Object> eachExpense : expenseMap) {
			// put the expense id in a list
			expenseIds += eachExpense.get("id") + ",";
		}
		expenseIds = expenseIds.substring(0, expenseIds.length() - 1);
		// Fetch attachments for the given list of expense ids
		List<FileMappingEntity> fileMappings = mySqlExpense.getFileMappingsForExpenses(expenseIds);
		// create a list of expenses
		List<ExpenseEntity> expenseList = new ArrayList<>();
		// for each expense mapping
		for (Map<String, Object> expense : expenseMap) {
			List<AttachmentEntity> attachments = new ArrayList<>();
			// fetch file mappings for current expense
			for (FileMappingEntity fileMapping : fileMappings) {
				if (fileMapping.getExpenseId().equals(String.valueOf(expense.get("id")))) {
					AttachmentEntity attachmentEntity = new AttachmentEntity(fileMapping.getOriginalFileName(),
							fileMapping.getAttachmentId());
					attachments.add(attachmentEntity);
				}

			}
			// create a new expense entity
			ExpenseEntity expenseEntity = new ExpenseEntity(String.valueOf(expense.get("id")),
					String.valueOf(expense.get("title")), String.valueOf(expense.get("description")),
					ExpenseStatusType.valueOf(String.valueOf(expense.get("status"))), attachments,
					String.valueOf(expense.get("userId")), String.valueOf(expense.get("name")));
			// add the expense entity in list
			expenseList.add(expenseEntity);
		}
		return expenseList;
	}

	/**
	 * @see IExpenseService.approverExpense
	 */
	@Override
	public ExpenseEntity approveExpense(ExpenseApproveOrRejectEntity expenseApproveOrRejectEntity) throws Exception {
		// check if user id or role is not null or empty
		if (expenseApproveOrRejectEntity == null)
			throw new ValidationFailedException("ExpenseEntity", "ExpenseId should not be null or empty", null);
		// check if expense exists and is active
		ExpenseEntity expenseEntity = mySqlExpense.getExpense(expenseApproveOrRejectEntity.getExpenseId());
		// check if expense is active
		if (expenseEntity == null)
			throw new NotFoundException("ExpenseEntity", "Expense not found", null);
		// fetch user of this expense
		ExternalFacingUser externalFacingUser = mySqlUser.getUser(expenseEntity.getUserId());
		// check if user is not null or inactive
		if (externalFacingUser == null || !externalFacingUser.getIsActive())
			throw new BadRequestException("ExternalFacingUser", "User not found for the given expense or is inactive",
					null);
		String approverId = RequestThreadLocal.getSession().getExternalFacingUser().getId();
		// match the approver/super approver with currently logged in user
		if (!externalFacingUser.getApproverId().equals(approverId))
			if (!externalFacingUser.getSuperApproverId().equals(approverId))
				throw new UnauthorizedException("ExpenseEntity",
						"User is not assigned as approver/super approver for this expense", null);
		// create a new expense history entity using the data from expense
		// entity
		ExpenseHistoryEntity expenseHistoryEntity = new ExpenseHistoryEntity(expenseEntity.getId(),
				expenseEntity.getCreationDate(), expenseEntity.getUpdationDate(), expenseEntity.getTitle(),
				expenseEntity.getDescription(), expenseEntity.getStatus(), expenseEntity.getUserId(),
				expenseEntity.getApproverComments());
		expenseHistoryEntity.setCreationDate(new Date());
		// save this history in mysql
		expenseHistoryEntity = mySqlExpense.saveExpenseHistory(expenseHistoryEntity);
		// set expense status as per the approving role
		expenseEntity.setStatus(expenseApproveOrRejectEntity.getStatus());
		// set approver comments
		expenseEntity.setApproverComments(expenseApproveOrRejectEntity.getApproverComments());
		// update expense
		return mySqlExpense.updateExpense(expenseEntity);

	}

}
