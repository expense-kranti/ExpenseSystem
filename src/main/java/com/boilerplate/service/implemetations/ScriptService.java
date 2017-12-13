package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.Role;
import com.boilerplate.service.interfaces.IScriptsService;
import com.boilerplate.service.interfaces.IUserService;

public class ScriptService implements IScriptsService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(ScriptService.class);

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
	 * This is an instance of the queue job, to save the session back on to the
	 * database async
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader jon
	 * 
	 * @param queueReaderJob
	 *            The queue reader jon
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	/**
	 * This is the publish subject list.
	 */
	BoilerplateList<String> subjectsForPublishUserReportData = new BoilerplateList<>();

	/**
	 * 
	 */
	BoilerplateList<String> subjectsForSetUserChangePasswordStatus = new BoilerplateList<>();
	
	/**
	 * This is publish subject list for AKS Report and Refer User
	 */
	BoilerplateList<String> subjectsForAKSOrReferReportPublish = new BoilerplateList<>();

	/**
	 * Initializes the bean
	 */
	public void initialize() {
		subjectsForPublishUserReportData.add("PublishUserData");
		subjectsForSetUserChangePasswordStatus.add("SetUserChangePasswordStatus");
		subjectsForAKSOrReferReportPublish.add("PublishUserAKSOrReferReport");
	}

	@Override
	public void publishUserAndAssessmentReport() throws UnauthorizedException, NotFoundException, BadRequestException {
		if (!checkIsAdmin()) {
			throw new UnauthorizedException("User", "User is not authorized to perform this action.", null);
		}
		try {
			queueReaderJob.requestBackroundWorkItem("", subjectsForPublishUserReportData, "ScriptService",
					"publishUserAndAssessmentReport");
		} catch (Exception ex) {
			logger.logError("ScriptService", "publishUserAndAssessmentReport", "Inside try-catch block",
					ex.toString());
		}
	}
	
	/**
	 * @see IScriptsService.publishUserAKSOrReferReport
	 */
	@Override
	public void publishUserAKSOrReferReport() throws UnauthorizedException, NotFoundException, BadRequestException {
		if (!checkIsAdmin()) {
			throw new UnauthorizedException("User", "User is not authorized to perform this action.", null);
		}
		try{
			queueReaderJob.requestBackroundWorkItem("", subjectsForAKSOrReferReportPublish,"ScriptService","publishUserAKSOrReferReport");
		}catch(Exception ex){
		   logger.logError("ScriptService", "publishUserAKSOrReferReport", "Inside try-catch block", ex.toString());	
		}
	}

	/**
	 * This method checks the roles of the user and tells about whether that
	 * user have managerial role or not
	 * 
	 * @return isAdmin The isAdmin returns true/false
	 * @throws NotFoundException
	 *             The NotFoundException
	 * @throws BadRequestException
	 *             The BadRequestException
	 */
	private boolean checkIsAdmin() throws NotFoundException, BadRequestException {
		boolean isAdmin = false;
		if (RequestThreadLocal.getSession() != null) {
			ExternalFacingReturnedUser user = userService
					.get(RequestThreadLocal.getSession().getExternalFacingUser().getUserId(), false);
			// check user state exists in input states
			if (user.getRoles() != null) {
				for (Role role : user.getRoles()) {
					if (role.getRoleName().toUpperCase().equals("ADMIN")
							|| role.getRoleName().toUpperCase().equals("BACKOFFICEUSER")
							|| role.getRoleName().toUpperCase().equals("BANKADMIN")
							|| role.getRoleName().toUpperCase().equals("BANKUSER")) {
						isAdmin = true;
						break;
					}
				}
			}
		}
		return isAdmin;
	}

	/**
	 * @see IScriptsService.setUserChangePasswordStatus
	 */
	@Override
	public void setUserChangePasswordStatus() throws UnauthorizedException, NotFoundException, BadRequestException {
		// Check is user have admin access if not then throw exception
		if (!checkIsAdmin()) {
			throw new UnauthorizedException("User", "User is not authorized to perform this action.", null);
		}
		try {
			// Call background job for set all user is password change flag
			queueReaderJob.requestBackroundWorkItem("", subjectsForSetUserChangePasswordStatus, "ScriptService",
					"setUserChangePasswordStatus");
		} catch (Exception ex) {
			logger.logError("ScriptsService", "setUserChangePasswordStatus", "Inside try-catch block", ex.toString());
		}
	}
	
	

}
