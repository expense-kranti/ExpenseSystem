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
import com.boilerplate.java.collections.BoilerplateSet;
import com.boilerplate.java.entities.AttachmentEntity;
import com.boilerplate.java.entities.ExpenseApproveOrRejectEntity;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseHistoryEntity;
import com.boilerplate.java.entities.ExpenseReportEntity;
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
	 * This is the autowired instance of IEmailService
	 */
	@Autowired
	IEmailService sendEmailService;

	/**
	 * This method is used to set autowired instance of IEmailService
	 * 
	 * @param sendEmailService
	 */
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
		ExternalFacingUser externalFacingUser = mySqlUser
				.getUser(RequestThreadLocal.getSession().getExternalFacingUser().getId());
		// check if user has been assigned all the approvers
		if (externalFacingUser.getApproverId() == null || externalFacingUser.getSuperApproverId() == null
				|| externalFacingUser.getFinanceId() == null)
			throw new BadRequestException("ExternalFacingUser",
					"User cannot create a new expense without being assigned all the approvers, please request your Admin to assign you all the required approvers.",
					null);
		// set status of expense as submitted
		expenseEntity.setStatus(ExpenseStatusType.Submitted);
		expenseEntity.setUserId(externalFacingUser.getId());
		// set creation date and update date
		expenseEntity.setCreationDate(new Date());
		expenseEntity.setUpdationDate(new Date());
		// save expense in database
		expenseEntity = mySqlExpense.createExpense(expenseEntity);
		// save attachment mapping
		fileService.saveFileMapping(expenseEntity);
		// send email for submission of expense
		sendEmailService.sendEmailOnSubmission(expenseEntity, false);
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
		// check if user has been assigned all the approvers
		if (externalFacingUser.getApproverId() == null || externalFacingUser.getSuperApproverId() == null
				|| externalFacingUser.getFinanceId() == null)
			throw new BadRequestException("ExternalFacingUser",
					"User cannot create/update an expense without being assigned all the approvers, please request your Admin to assign you all the required approvers.",
					null);
		// create a new expense history entity using the data from expense
		// entity
		ExpenseHistoryEntity expenseHistoryEntity = new ExpenseHistoryEntity(previousExpense.getId(),
				previousExpense.getCreationDate(), previousExpense.getUpdationDate(), previousExpense.getTitle(),
				previousExpense.getDescription(), previousExpense.getStatus(), previousExpense.getUserId(),
				previousExpense.getApproverComments(), previousExpense.getAmount());
		expenseHistoryEntity.setCreationDate(new Date());
		// save this history in mysql
		expenseHistoryEntity = mySqlExpense.saveExpenseHistory(expenseHistoryEntity);
		// if expense status is rejected then change it to re-submitted
		if (expenseEntity.getStatus() != null) {
			if (expenseEntity.getStatus() == ExpenseStatusType.Approver_Rejected
					|| expenseEntity.getStatus() == ExpenseStatusType.Finance_Rejected) {
				expenseEntity.setStatus(ExpenseStatusType.Re_Submitted);
				// send email for re-submission of expense
				sendEmailService.sendEmailOnSubmission(expenseEntity, true);
			}
		} else
			throw new BadRequestException("ExpenseEntity", "Status should be null", null);
		// set creation date and update date
		expenseEntity.setCreationDate(previousExpense.getCreationDate());
		expenseEntity.setUpdationDate(new Date());
		expenseEntity.getId();
		// update expense
		expenseEntity = mySqlExpense.updateExpense(expenseEntity);
		// update attachments
		List<AttachmentEntity> attachments = fileService.updateFileMapping(expenseEntity, expenseHistoryEntity);
		expenseEntity.setAttachments(attachments);
		// send email notification
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
		// fetch list of expenses from database
		List<ExpenseEntity> expenses = mySqlExpense.getExpenses(fetchExpenseEntity);
		// list of expense ids
		String expenseIds = "";
		// for each expense, fetch its attachment
		for (ExpenseEntity eachExpense : expenses) {
			// put the expense id in a list
			expenseIds += eachExpense.getId() + ",";
		}
		expenseIds = expenseIds.substring(0, expenseIds.length() - 1);
		// Fetch attachments for the given list of expense ids
		List<FileMappingEntity> fileMappings = mySqlExpense.getFileMappingsForExpenses(expenseIds);
		// for each expense mapping
		for (ExpenseEntity expense : expenses) {
			List<AttachmentEntity> attachments = new ArrayList<>();
			// fetch file mappings for current expense
			for (FileMappingEntity fileMapping : fileMappings) {
				if (fileMapping.getExpenseId().equals(String.valueOf(expense.getId()))) {
					AttachmentEntity attachmentEntity = new AttachmentEntity(fileMapping.getOriginalFileName(),
							fileMapping.getAttachmentId(), fileMapping.getContentType());
					attachments.add(attachmentEntity);
				}
			}
			expense.setAttachments(attachments);
		}
		return expenses;
	}

	/**
	 * @see IExpenseService.getExpensesForApproval
	 */
	@Override
	public List<ExpenseEntity> getExpensesForApproval(UserRoleType role)
			throws NotFoundException, ValidationFailedException, BadRequestException {
		// fetch approver id
		String approverId = RequestThreadLocal.getSession().getExternalFacingUser().getId();
		if (approverId == null)
			throw new ValidationFailedException("ExpenseEntity", "Approver id for fetching expenses is null or empty",
					null);
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
							fileMapping.getAttachmentId(), fileMapping.getContentType());
					attachments.add(attachmentEntity);
				}
			}
			// create a new expense entity
			ExpenseEntity expenseEntity = new ExpenseEntity(String.valueOf(expense.get("id")),
					String.valueOf(expense.get("title")), String.valueOf(expense.get("description")),
					ExpenseStatusType.valueOf(String.valueOf(expense.get("status"))), attachments,
					String.valueOf(expense.get("userId")), String.valueOf(expense.get("name")),
					String.valueOf(expense.get("approverComments")),
					Float.valueOf(String.valueOf(expense.get("amount"))), null, null);
			// add the expense entity in list
			expenseList.add(expenseEntity);
		}
		return expenseList;
	}

	/**
	 * @see IExpenseService.approverExpense
	 */
	@Override
	public ExpenseEntity approveExpenseForApprover(ExpenseApproveOrRejectEntity expenseApproveOrRejectEntity)
			throws Exception {
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
				expenseEntity.getApproverComments(), expenseEntity.getAmount());
		expenseHistoryEntity.setCreationDate(new Date());
		// save this history in mysql
		expenseHistoryEntity = mySqlExpense.saveExpenseHistory(expenseHistoryEntity);
		// set expense status as per the approving role
		expenseEntity.setStatus(expenseApproveOrRejectEntity.getStatus());
		// set approver comments
		expenseEntity.setApproverComments(expenseApproveOrRejectEntity.getApproverComments());
		// update expense
		expenseEntity = mySqlExpense.updateExpense(expenseEntity);
		if (expenseApproveOrRejectEntity.getStatus().equals(ExpenseStatusType.Approver_Approved))
			sendEmailService.sendEmailOnApproval(expenseEntity);
		else if (expenseApproveOrRejectEntity.getStatus().equals(ExpenseStatusType.Approver_Rejected))
			sendEmailService.sendEmailOnRejection(expenseEntity);
		return expenseEntity;

	}

	/**
	 * @see IExpenseService.getExpensesForFinance
	 */
	@Override
	public List<ExpenseReportEntity> getExpensesForFinance(ExpenseStatusType expenseStatus) throws BadRequestException {
		// get the currently logged in user's id
		String financeId = RequestThreadLocal.getSession().getExternalFacingUser().getId();
		// get list of expenses filed under the given approver
		List<Map<String, Object>> expenseMap = mySqlExpense.getExpensesForFinance(financeId, expenseStatus);
		// check if expenses are not null
		if (expenseMap.size() == 0)
			throw new BadRequestException("ExpenseEntity", "No expenses found", null);
		// list of expense ids
		String expenseIds = "";
		// set for expense ids
		BoilerplateSet<String> userIds = new BoilerplateSet<>();
		// for each expense, fetch its attachment
		for (Map<String, Object> eachExpense : expenseMap) {
			// put the expense id in a list
			expenseIds += eachExpense.get("id") + ",";
			// put id in set
			userIds.add(String.valueOf(eachExpense.get("userId")));
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
							fileMapping.getAttachmentId(), fileMapping.getContentType());
					attachments.add(attachmentEntity);
				}
			}

			// create a new expense entity
			ExpenseEntity expenseEntity = new ExpenseEntity(String.valueOf(expense.get("id")),
					String.valueOf(expense.get("title")), String.valueOf(expense.get("description")),
					ExpenseStatusType.valueOf(String.valueOf(expense.get("status"))), attachments,
					String.valueOf(expense.get("userId")), String.valueOf(expense.get("name")),
					String.valueOf(expense.get("approverComments")),
					Float.valueOf(String.valueOf(expense.get("amount"))), (Date) expense.get("creationDate"),
					(Date) expense.get("updatedDate"));
			// add the expense entity in list
			expenseList.add(expenseEntity);
		}
		// List of expense reports
		List<ExpenseReportEntity> reportEntities = new ArrayList<>();
		// for each expense in unique set
		for (String userId : userIds) {
			// List of expenses
			List<ExpenseEntity> expenses = new ArrayList<>();
			// total amount
			float totalAmount = 0f;
			// name of the user
			String name = null;
			// traverse through list of expense
			for (ExpenseEntity expenseEntity : expenseList) {
				// if expense belongs to user
				if (expenseEntity.getUserId().equals(userId)) {
					// add the expense to the list of expenses
					expenses.add(expenseEntity);
					// add the amount in total amount
					totalAmount += expenseEntity.getAmount();
					// set name
					name = expenseEntity.getUserName();
				}
			}
			// create a new expense report entity
			ExpenseReportEntity reportEntity = new ExpenseReportEntity(name, userId, totalAmount, expenses,
					expenseStatus);
			// add the report entity in list
			reportEntities.add(reportEntity);
		}
		return reportEntities;

	}

	/**
	 * @see IExpenseService.approveExpenseForFinance
	 */
	@Override
	public void approveExpenseForFinance(ExpenseReportEntity reportEntity) throws Exception {
		// validate the report entity
		// check if user in report exists
		ExternalFacingUser user = mySqlUser.getUser(reportEntity.getUserId());
		if (user == null || !user.getIsActive())
			throw new ValidationFailedException("ExternalFacingUser",
					"User id in report doesn not exist or the user is inactive", null);
		// for each expense in report entity
		for (ExpenseEntity expense : reportEntity.getExpenses()) {
			// check if user in expense is same as report
			if (!expense.getUserId().equals(reportEntity.getUserId()))
				throw new ValidationFailedException("ExpenseReportEntity",
						"One of the expense in expense list of the report does not belong to the user mentioned in report",
						null);
			// check if status of each expense is approver approved
			if (!expense.getStatus().equals(ExpenseStatusType.Approver_Approved))
				if (!expense.getStatus().equals(ExpenseStatusType.Finance_Approved))
					throw new ValidationFailedException("ExpenseReportEntity",
							"One of the expense in expense list of the report is not in desired status", null);
			// set status
			expense.setStatus(reportEntity.getStatus());
		}
		// update all expenses
		mySqlExpense.saveExpenseList(reportEntity.getExpenses());

	}

}
