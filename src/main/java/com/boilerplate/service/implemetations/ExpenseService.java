package com.boilerplate.service.implemetations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IExpense;
import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseHistoryEntity;
import com.boilerplate.java.entities.ExpenseListViewEntity;
import com.boilerplate.java.entities.ExpenseReportEntity;
import com.boilerplate.java.entities.ExpenseReviewEntity;
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
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(ExpenseService.class);

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
	 * This is the instance of filePointer
	 */
	@Autowired
	IFilePointer filePointer;

	/**
	 * Sets the file pointer
	 * 
	 * @param filePointer
	 *            to set
	 */
	public void setFilePointer(IFilePointer filePointer) {
		this.filePointer = filePointer;
	}

	/**
	 * @see IExpenseService.createExpense
	 */
	@Override
	public ExpenseEntity createExpense(ExpenseEntity expenseEntity) throws Exception {
		// check if expense entity is valid or not
		expenseEntity.validate();
		// entity should not have any id
		if (expenseEntity.getId() != null || expenseEntity.getApproverComments() != null)
			throw new ValidationFailedException("ExpenseEntity", "Id and approver comments should be null", null);
		// check whether file mappings provided exists
		fileService.checkFileExistence(expenseEntity.getAttachmentIds());
		// set status of expense as submitted
		expenseEntity.setStatus(ExpenseStatusType.SUBMITTED);
		expenseEntity.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getId());
		expenseEntity.setUserName(RequestThreadLocal.getSession().getExternalFacingUser().getFirstName() + " "
				+ RequestThreadLocal.getSession().getExternalFacingUser().getLastName());
		// set creation date and update date
		expenseEntity.setCreationDate(new Date());
		expenseEntity.setUpdationDate(new Date());
		// save expense in database
		expenseEntity = mySqlExpense.createExpense(expenseEntity);
		// save attachment mapping
		if (!fileService.saveFileMapping(expenseEntity)) {
			// if file mappings were not saved, delete the expense
			mySqlExpense.deleteExpense(expenseEntity);
			throw new BadRequestException("ExpenseEntity", "Could not save file mapping, please upload again", null);
		}
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
		if (expenseEntity.getId() == null || expenseEntity.getId().isEmpty())
			throw new ValidationFailedException("ExpenseEntity", "Id should not be null or empty", null);
		// check whether expense entity exists or not
		ExpenseEntity previousExpense = mySqlExpense.getExpense(expenseEntity.getId());
		if (previousExpense == null)
			throw new NotFoundException("ExpenseEntity", "Expense entity not found", null);
		// check if expense is in rejected state
		if (previousExpense.getStatus() != ExpenseStatusType.APPROVER_REJECTED
				&& previousExpense.getStatus() != ExpenseStatusType.FINANCE_REJECTED)
			throw new BadRequestException("ExpenseEntity", "User can only update rejected expenses", null);
		// Check whether expense belongs to the logged in user
		if (!previousExpense.getUserId().equals(RequestThreadLocal.getSession().getExternalFacingUser().getId()))
			throw new BadRequestException("ExpenseEntity",
					"User cannot update this expense since he is not the owner of this expense", null);
		// check if attachment ids exist
		fileService.checkFileExistence(expenseEntity.getAttachmentIds());
		// create a new expense history entity using the data from expense
		// entity
		ExpenseHistoryEntity expenseHistoryEntity = new ExpenseHistoryEntity(previousExpense.getId(),
				previousExpense.getCreationDate(), previousExpense.getUpdationDate(), previousExpense.getTitle(),
				previousExpense.getDescription(), previousExpense.getStatus(), previousExpense.getUserId(),
				previousExpense.getApproverComments(), previousExpense.getAmount());
		expenseHistoryEntity.setCreationDate(new Date());

		// if expense status is rejected then change it to re-submitted
		expenseEntity.setStatus(ExpenseStatusType.RE_SUBMITTED);
		// send email for re-submission of expense
		sendEmailService.sendEmailOnSubmission(expenseEntity, true);
		// set creation date and update date
		expenseEntity.setCreationDate(previousExpense.getCreationDate());
		expenseEntity.setUpdationDate(new Date());
		// set approver comments to null
		expenseEntity.setApproverComments(null);
		// update expense
		expenseEntity = mySqlExpense.updateExpense(expenseEntity);
		// save this history in mysql
		expenseHistoryEntity = mySqlExpense.saveExpenseHistory(expenseHistoryEntity);
		// update attachments
		fileService.updateFileMapping(expenseEntity, expenseHistoryEntity);
		// save this history in mysql
		expenseHistoryEntity = mySqlExpense.saveExpenseHistory(expenseHistoryEntity);
		// send email notification
		return expenseEntity;
	}

	/**
	 * @see IExpenseService.getExpensesForUser
	 * 
	 */
	@Override
	public List<ExpenseEntity> getExpensesForUser(FetchExpenseEntity fetchExpenseEntity)
			throws ValidationFailedException, NotFoundException, BadRequestException, ParseException {
		// validate entity
		fetchExpenseEntity.validate();
		try {
			if (fetchExpenseEntity.getStatusString() != null && !fetchExpenseEntity.getStatusString().isEmpty())
				fetchExpenseEntity.setExpenseStatusType(
						ExpenseStatusType.valueOf(fetchExpenseEntity.getStatusString().toUpperCase()));
		} catch (Exception ex) {
			throw new ValidationFailedException("FetchExpenseEntity", "Invalid value for status string", null);
		}
		// check if end date not before than start date
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		try {
			if (fetchExpenseEntity.getStartDate() != null) {
				// check if end date is greater than start date
				if (format.parse(fetchExpenseEntity.getStartDate())
						.compareTo(format.parse(fetchExpenseEntity.getEndDate())) > 0)
					throw new ValidationFailedException("FetchExpenseEntity", "End date is less than start date", null);
			}
		} catch (ParseException ex) {
			// log the parse exception
			logger.logException("ExpenseService", "getExpensesForUser", "exceptionGetExpensesForUser",
					"Some exception occurred while parsing date", ex);
			throw new ValidationFailedException("FetchExpenseEntity", "Some exception occurred while parsing date", ex);
		}
		// fetch list of expenses from database
		List<ExpenseEntity> expenses = mySqlExpense.getExpenses(fetchExpenseEntity);
		// check if expenses were present for the present user or not
		if (expenses == null || expenses.isEmpty())
			throw new NotFoundException("ExpenseEntity", "No expenses were found for the currently logged in user",
					null);
		// for each expense, fetch attachments
		for (ExpenseEntity expenseEntity : expenses) {
			List<String> attachmentIds = new ArrayList<>();
			List<FileMappingEntity> mappings = filePointer.getFileMappingByExpenseId(expenseEntity.getId());
			for (FileMappingEntity fileMappingEntity : mappings) {
				attachmentIds.add(fileMappingEntity.getAttachmentId());
			}
			expenseEntity.setAttachmentIds(attachmentIds);
		}
		return expenses;
	}

	/**
	 * @see IExpenseService.getExpensesForApproval
	 */
	@Override
	public List<ExpenseEntity> getExpensesForApproval()
			throws NotFoundException, ValidationFailedException, BadRequestException {
		// fetch approver
		ExternalFacingUser approver = RequestThreadLocal.getSession().getExternalFacingUser();
		// list of expenses
		List<ExpenseEntity> expenses = new ArrayList<>();
		// check if user is approver or super/approver
		if (approver.getRoleTypes().contains(UserRoleType.SUPER_APPROVER))
			// get all the expenses
			expenses = mySqlExpense.getExpensesForSuper();
		else if (approver.getRoleTypes().contains(UserRoleType.APPROVER))
			// get expense of user whose approver is currently logged in user
			expenses = mySqlExpense.getExpensesForApprover(approver.getId());
		// check if expenses are not null
		if (expenses == null || expenses.size() == 0)
			throw new BadRequestException("ExpenseEntity", "No expenses found", null);
		// else return expenses
		return expenses;
	}

	/**
	 * @see IExpenseService.approverExpense
	 */
	@Override
	public ExpenseEntity approveExpenseForApprover(ExpenseReviewEntity expenseReviewEntity) throws Exception {
		// check if user id or role is not null or empty
		expenseReviewEntity.validate();
		// check if status has only approved or rejected status
		if (expenseReviewEntity.getStatus() != ExpenseStatusType.APPROVER_APPROVED
				&& expenseReviewEntity.getStatus() != ExpenseStatusType.APPROVER_REJECTED)
			throw new ValidationFailedException("ExpenseReviewEntity",
					"Approver/Super_Approver can only assign Approver_approved or Approver_rejected status, any other status is not allowed",
					null);
		// check if expense is being rejected, comments are mandatory
		if (expenseReviewEntity.getStatus().equals(ExpenseStatusType.APPROVER_REJECTED)
				&& expenseReviewEntity.getApproverComments() == null)
			throw new ValidationFailedException("ExpenseReviewEntity",
					"Comments are mandatory if expense is being rejected", null);
		// check if expense exists and is active
		ExpenseEntity expenseEntity = mySqlExpense.getExpense(expenseReviewEntity.getExpenseId());
		// check if expense is active
		if (expenseEntity == null)
			throw new NotFoundException("ExpenseEntity", "Expense not found", null);
		// check if expense is in submitted or re-submitted state
		if (!expenseEntity.getStatus().equals(ExpenseStatusType.SUBMITTED)
				&& !expenseEntity.getStatus().equals(ExpenseStatusType.RE_SUBMITTED))
			throw new BadRequestException("ExpenseEntity",
					"Expense is not in desired state for approver/super-approver to take action", null);
		// fetch user of this expense
		ExternalFacingUser externalFacingUser = mySqlUser.getUser(expenseEntity.getUserId());
		// check if user is not null or inactive
		if (externalFacingUser == null || !externalFacingUser.getIsActive())
			throw new BadRequestException("ExternalFacingUser", "User not found for the given expense or is inactive",
					null);
		String approverId = RequestThreadLocal.getSession().getExternalFacingUser().getId();
		// check if approver is super-approver or matches with approver id
		// assigned to the user
		if (!RequestThreadLocal.getSession().getExternalFacingUser().getRoleTypes()
				.contains(UserRoleType.SUPER_APPROVER) && !approverId.equals(externalFacingUser.getApproverId()))
			throw new UnauthorizedException("ExpenseEntity", "User is not authorized to approve/reject this expense",
					null);
		// check that approver cannot approve his own expense
		if (!RequestThreadLocal.getSession().getExternalFacingUser().getRoleTypes()
				.contains(UserRoleType.SUPER_APPROVER) && approverId.equals(externalFacingUser.getId()))
			throw new BadRequestException("ExternalFacingUser", "Approver is not allowed to approve his own expense",
					null);
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
		expenseEntity.setStatus(expenseReviewEntity.getStatus());
		// set approver comments
		expenseEntity.setApproverComments(expenseReviewEntity.getApproverComments());
		// update expense
		expenseEntity = mySqlExpense.updateExpense(expenseEntity);
		if (expenseReviewEntity.getStatus().equals(ExpenseStatusType.APPROVER_APPROVED))
			sendEmailService.sendEmailOnApproval(expenseEntity);
		else if (expenseReviewEntity.getStatus().equals(ExpenseStatusType.APPROVER_REJECTED))
			sendEmailService.sendEmailOnRejection(expenseEntity);
		return expenseEntity;

	}

	/**
	 * @see IExpenseService.approveExpenseForFinance
	 */
	@Override
	public void approveExpenseForFinance(ExpenseReportEntity reportEntity) throws Exception {
		// validate the report entity
		reportEntity.validate();
		// check if finance is not changing status to invalid status
		if (!Arrays.asList(ExpenseStatusType.FINANCE_APPROVED, ExpenseStatusType.READY_FOR_PAYMENT,
				ExpenseStatusType.FINANCE_REJECTED).contains(reportEntity.getStatus()))
			throw new BadRequestException("ExpenseReportEntity",
					"Finance can only approver, reject or change status to ready for payment", null);
		// check if user in report exists
		ExternalFacingUser user = mySqlUser.getUser(reportEntity.getUserId());
		if (user == null || !user.getIsActive())
			throw new ValidationFailedException("ExternalFacingUser",
					"User id in report doesn not exist or the user is inactive", null);
		List<ExpenseEntity> entities = new ArrayList<>();
		// for each expense in report entity
		for (ExpenseEntity expense : reportEntity.getExpenses()) {
			// fetch the expense
			ExpenseEntity expenseEntity = mySqlExpense.getExpense(expense.getId());
			// check if expense exists or not
			if (expenseEntity == null)
				throw new NotFoundException("ExpenseEntity", "Expense with id :" + expense.getId() + " not found",
						null);
			// check if user in expense is same as report
			if (!expenseEntity.getUserId().equals(reportEntity.getUserId()))
				throw new ValidationFailedException("ExpenseReportEntity",
						"One of the expense in expense list of the report does not belong to the user mentioned in report",
						null);
			// check if status of each expense is is appropriate for incoming
			// status
			if ((reportEntity.getStatus().equals(ExpenseStatusType.FINANCE_APPROVED)
					&& !expenseEntity.getStatus().equals(ExpenseStatusType.APPROVER_APPROVED))
					|| ((reportEntity.getStatus().equals(ExpenseStatusType.READY_FOR_PAYMENT)
							&& !expenseEntity.getStatus().equals(ExpenseStatusType.FINANCE_APPROVED)))
					|| (reportEntity.getStatus().equals(ExpenseStatusType.FINANCE_REJECTED)
							&& !expenseEntity.getStatus().equals(ExpenseStatusType.APPROVER_APPROVED)))
				throw new ValidationFailedException("ExpenseReportEntity",
						"One of the expense in expense list of the report is not in desired status", null);
			// set status
			expenseEntity.setStatus(reportEntity.getStatus());
			// set comments
			expenseEntity.setApproverComments(reportEntity.getApproverComments());
			entities.add(expenseEntity);
		}
		reportEntity.setExpenses(entities);
		// update all expenses
		mySqlExpense.saveExpenseList(reportEntity.getExpenses());
	}

	/**
	 * @see IExpenseService.expenseReviewByFinance
	 */
	@Override
	public void expenseReviewByFinance(ExpenseReviewEntity expenseReviewEntity)
			throws ValidationFailedException, BadRequestException, NotFoundException {
		// validate the expenseReviewEntity
		expenseReviewEntity.validate();
		// check if finance is not changing status to invalid status
		if (!Arrays.asList(ExpenseStatusType.FINANCE_APPROVED, ExpenseStatusType.READY_FOR_PAYMENT,
				ExpenseStatusType.FINANCE_REJECTED).contains(expenseReviewEntity.getStatus()))
			throw new BadRequestException("ExpenseReviewEntity",
					"Finance can only approver, reject or change status to ready for payment", null);
		// check if status is rejected than approver comments should be present
		if (expenseReviewEntity.getStatus() == ExpenseStatusType.FINANCE_REJECTED)
			if (expenseReviewEntity.getApproverComments().equals(null))
				throw new ValidationFailedException("ExpenseReviewEntity",
						"Approver comments are mandatory if expense is being rejected", null);
		// check if expense is present in database or not
		ExpenseEntity expenseEntity = mySqlExpense.getExpense(expenseReviewEntity.getExpenseId());
		// check if expense is not null
		if (expenseEntity == null)
			throw new NotFoundException("ExpenseEntity",
					"Expense not found with given expense id: " + expenseReviewEntity.getExpenseId(), null);
		// check if status of each expense is is appropriate for incoming status
		if ((expenseReviewEntity.getStatus().equals(ExpenseStatusType.FINANCE_APPROVED)
				&& !expenseEntity.getStatus().equals(ExpenseStatusType.APPROVER_APPROVED))
				|| ((expenseReviewEntity.getStatus().equals(ExpenseStatusType.READY_FOR_PAYMENT)
						&& !expenseEntity.getStatus().equals(ExpenseStatusType.FINANCE_APPROVED)))
				|| (expenseReviewEntity.getStatus().equals(ExpenseStatusType.FINANCE_REJECTED)
						&& !expenseEntity.getStatus().equals(ExpenseStatusType.APPROVER_APPROVED)))
			throw new ValidationFailedException("ExpenseReportEntity",
					"One of the expense in expense list of the report is not in desired status", null);
		// update the expense entity
		expenseEntity.setStatus(expenseReviewEntity.getStatus());
		// set comments
		expenseEntity.setApproverComments(expenseReviewEntity.getApproverComments());
		// update the expense entity in the system
		mySqlExpense.updateExpense(expenseEntity);
	}

	/**
	 * @see IExpenseService.getExpensesForFinance
	 */
	@Override
	public List<ExpenseEntity> getExpensesForFinance(String status)
			throws BadRequestException, NotFoundException, ValidationFailedException {
		// check if status is not null or empty
		if (status == null || status.isEmpty())
			throw new ValidationFailedException("ExpenseStatusType", "Status should not be null or empty", null);
		// check if status given is a valid status type
		ExpenseStatusType expenseStatusType = ExpenseStatusType.convert(status);
		if (expenseStatusType == null)
			throw new ValidationFailedException("ExpenseStatusType", "Invalid value for status", null);
		// check if all status type are not accessible to finance
		if (!Arrays.asList(ExpenseStatusType.APPROVER_APPROVED, ExpenseStatusType.FINANCE_APPROVED,
				ExpenseStatusType.READY_FOR_PAYMENT).contains(expenseStatusType))
			throw new BadRequestException("ExpenseStatusType",
					"Finance is not allowed to fetch expenses in status :" + expenseStatusType.toString(), null);
		List<ExpenseEntity> expenses = new ArrayList<>();
		expenses = mySqlExpense.getExpensesByStatus(expenseStatusType);
		// check if expense list is not null or empty
		if (expenses == null || expenses.size() == 0)
			throw new NotFoundException("ExpenseEntity",
					"No expense found in " + expenseStatusType.toString() + " state", null);
		// return list of expense
		return expenses;
	}

	/**
	 * @see IExpenseService.getExpenseReportsForFinance
	 */
	@Override
	public List<ExpenseReportEntity> getExpenseReportsForFinance(String status)
			throws BadRequestException, NotFoundException, ValidationFailedException {
		// check if status is not null or empty
		if (status == null || status.isEmpty())
			throw new ValidationFailedException("ExpenseStatusType", "Status should not be null or empty", null);
		ExpenseStatusType statusType = ExpenseStatusType.convert(status);
		// check if status is a vaid value for expense status
		if (statusType == null)
			throw new ValidationFailedException("ExpenseStatusType", "Invalid value for status passed in parameter",
					null);
		// check if all status type are not accessible to finance
		if (!Arrays.asList(ExpenseStatusType.APPROVER_APPROVED, ExpenseStatusType.FINANCE_APPROVED,
				ExpenseStatusType.READY_FOR_PAYMENT).contains(statusType))
			throw new BadRequestException("ExpenseStatusType",
					"Finance is not allowed to fetch expenses in status :" + statusType.toString(), null);
		// fetch the list of user ids with there total amount
		List<Map<String, Object>> userAmounts = mySqlExpense.getUserAmountsForFinance(status);
		// fetch all expenses with finance approved status
		List<ExpenseEntity> expenses = mySqlExpense.getExpensesByStatus(statusType);
		if (expenses != null && !expenses.isEmpty()) {
			// list of reports
			List<ExpenseReportEntity> reports = new ArrayList<>();
			// for each expense, add it to expense report of that user
			for (Map<String, Object> userAmount : userAmounts) {
				List<ExpenseEntity> expenseList = new ArrayList<>();
				for (ExpenseEntity expenseEntity : expenses) {
					if (String.valueOf(userAmount.get("UserId")).equals(expenseEntity.getUserId()))
						expenseList.add(expenseEntity);
				}
				ExpenseReportEntity expenseReportEntity = new ExpenseReportEntity(
						String.valueOf(userAmount.get("Name")), String.valueOf(userAmount.get("UserId")),
						Float.valueOf(String.valueOf(userAmount.get("TotalAmount"))), expenseList,
						ExpenseStatusType.FINANCE_APPROVED);
				reports.add(expenseReportEntity);

			}
			return reports;
		} else
			throw new NotFoundException("ExpenseEntity",
					"No expenses found for finance in status : " + statusType.toString(), null);
	}

	/**
	 * @see IExpenseService.getExpenseListForApprovers
	 */
	@Override
	public List<ExpenseListViewEntity> getExpenseListForApprovers()
			throws NotFoundException, ValidationFailedException, BadRequestException {
		// get list of expenses
		List<ExpenseListViewEntity> expenseList = new ArrayList<ExpenseListViewEntity>();
		// get expenses for approval
		for (ExpenseEntity expenseEntity : this.getExpensesForApproval()) {
			expenseList.add(new ExpenseListViewEntity(expenseEntity.getId(), expenseEntity.getUserName(),
					expenseEntity.getTitle(), expenseEntity.getAmount()));
		}
		return expenseList;
	}

	/**
	 * @see IExpenseService.getExpenseById
	 */
	@Override
	public ExpenseEntity getExpenseById(String id)
			throws ValidationFailedException, NotFoundException, BadRequestException, UnauthorizedException {
		// check if id is not null or empty
		if (id == null || id.isEmpty())
			throw new ValidationFailedException("ExpenseEntity", "Id is null or empty", null);
		// fetch the expense
		ExpenseEntity expenseEntity = mySqlExpense.getExpense(id);
		// check if expense exists or not
		if (expenseEntity == null)
			throw new NotFoundException("ExpenseEntity", "Expense not found", null);
		// fetch the current user
		ExternalFacingUser currentUser = RequestThreadLocal.getSession().getExternalFacingUser();
		// check if current user is finance/super-approver
		if (!currentUser.getRoleTypes().contains(UserRoleType.SUPER_APPROVER)
				&& !currentUser.getRoleTypes().contains(UserRoleType.FINANCE)) {
			// fetch the expense owner
			ExternalFacingUser expenseOwner = mySqlUser.getUser(expenseEntity.getUserId());
			// check if current user is approver for the expense owner or is
			// expense owner
			if (!expenseOwner.getId().equals(currentUser.getId()))
				if (expenseOwner.getApproverId() == null || !expenseOwner.getApproverId().equals(currentUser.getId())) {
					throw new UnauthorizedException("ExpenseEntity", "User is not authorized to view this expense",
							null);
				}
		}
		return expenseEntity;
	}

}
