package com.boilerplate.aspects;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.InvalidStateException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.Constants;
import com.boilerplate.java.entities.MethodPermissions;
import com.boilerplate.java.entities.UserRoleType;
import com.boilerplate.service.interfaces.IMethodPermissionService;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.sessions.Session;

/**
 * This class is used to log and trace an exception. Any controller method when
 * called will be intercepted and logged If an exception takes place then it
 * will be handled and a web error is sent back
 * 
 * @author gaurav
 */
// This code is an aspect
@Aspect
@Component
public class LogAndTraceExceptionAspect {

	/**
	 * This is the method permission service
	 */
	@Autowired
	IMethodPermissionService methodPermissionService;
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
	 * This is an instance of the configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	private boolean isDebugEnabled = false;
	/**
	 * This is the logger
	 */
	private Logger logger = Logger.getInstance(LogAndTraceExceptionAspect.class);

	/**
	 * This method logs every entry to the controller. It logs class and method
	 * name, arguments and return value if any
	 * 
	 * @param proceedingJoinPoint
	 *            The join point of the method
	 * @throws Throwable
	 *             A Http error for the business case.
	 */
	@Around("execution(public* com.boilerplate.java.controllers.*.*(..))")
	public Object logTraceAndHandleException(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodPermissions methodPermissions = null;
		try {
			Session session = RequestThreadLocal.getSession();
			String methodExecuted = proceedingJoinPoint.getSignature().toLongString();
			methodPermissions = this.methodPermissionService.getMethodPermissions().get(methodExecuted);

			if (methodPermissions == null) {
				throw new UnauthorizedException("Api Permissions", "Api do not have permissions", null);
			}

			if (methodPermissions != null) {
				// changed by Ruchi for expense system
				// check if user needs to be authenticated to execute the method
				if (methodPermissions.getIsAuthenticationRequired()) {
					if (session == null)
						throw new UnauthorizedException("User", "User is not logged in", null);
					if (!session.getExternalFacingUser().getRoles().contains(UserRoleType.Admin))
						throw new UnauthorizedException("User", "User is not authorized", null);
				}
				// check if user needs to be approver or super-approver to
				// execute this method
				if (methodPermissions.getIsApproverRoleRequired()) {
					if (session == null)
						throw new UnauthorizedException("User", "User is not logged in", null);
					if (!session.getExternalFacingUser().getRoles().contains(UserRoleType.Super_Approver))
						if (!session.getExternalFacingUser().getRoles().contains(UserRoleType.Approver))
							throw new UnauthorizedException("User", "User is not authorized", null);
				}
				// check if user needs to be finance to execute this method
				if (methodPermissions.getIsFinanceRoleRequired()) {
					if (session == null)
						throw new UnauthorizedException("User", "User is not logged in", null);
					if (!session.getExternalFacingUser().getRoles().contains(UserRoleType.Finance))
						throw new UnauthorizedException("User", "User is not authorized", null);
				}
			}

			if (RequestThreadLocal.threadLocal.get() != null) {
				if (RequestThreadLocal.threadLocal.get().getHttpServletResponse() != null) {
					String[] headers = this.configurationManager.get("CrossDomainHeaders").split(";");
					for (String header : headers) {
						String keyValue[] = header.split(":");
						RequestThreadLocal.threadLocal.get().getHttpServletResponse().setHeader(keyValue[0],
								keyValue[1]);
					}
				}
			}

			Object returnValue = getReturnValue(proceedingJoinPoint, methodPermissions);
			return returnValue;

		} catch (InvalidStateException ex) {
			throw ex;
		}
		// Logged UnauthorizedException as Warning
		catch (UnauthorizedException unauthorizedException) {
			logger.logWarning(proceedingJoinPoint.getSignature().getDeclaringTypeName(),
					proceedingJoinPoint.getSignature().getName(), " UnauthorizedException ",
					" Arguments List: " + Base.toJSON(proceedingJoinPoint.getArgs()) + " Throwable: "
							+ unauthorizedException.toString());
			throw unauthorizedException;
		}
		// Logged ConflictException as Warning
		catch (ConflictException conflictException) {
			logger.logWarning(proceedingJoinPoint.getSignature().getDeclaringTypeName(),
					proceedingJoinPoint.getSignature().getName(), " ConflictException ",
					" Arguments List: " + Base.toJSON(proceedingJoinPoint.getArgs()) + " Throwable: "
							+ conflictException.toString());
			throw conflictException;
		} catch (Throwable th) {
			// TODO - Must do-In case of file upload all args and details should
			// not be logged
			// or it will cause performance issue

			// update user state in case of generic error update

			logger.logTraceExitException(proceedingJoinPoint.getSignature().getDeclaringTypeName(),
					proceedingJoinPoint.getSignature().getName(), proceedingJoinPoint.getArgs(), th,
					RequestThreadLocal.getSession());
			throw th;
		}
	}

	/**
	 * This method get the api return value.
	 * 
	 * @param proceedingJoinPoint
	 *            the proceedingJoinPoint
	 * @param methodPermissions
	 *            the method permissions
	 * @return return value
	 * @throws Throwable
	 */
	private Object getReturnValue(ProceedingJoinPoint proceedingJoinPoint, MethodPermissions methodPermissions)
			throws Throwable {
		Object returnValue = proceedingJoinPoint.proceed();
		// log the details including class, method, input arguments and return
		if (isDebugEnabled && (methodPermissions == null ? false : methodPermissions.getIsLoggingRequired())) {
			logger.logTraceExit(proceedingJoinPoint.getSignature().getDeclaringTypeName(),
					proceedingJoinPoint.getSignature().toLongString(), proceedingJoinPoint.getArgs(), returnValue,
					RequestThreadLocal.getSession());
		} else {
			logger.logTraceExit(proceedingJoinPoint.getSignature().getDeclaringTypeName(),
					proceedingJoinPoint.getSignature().toLongString(), new String[] { "Not_Printed" }, "Not_Printed",
					RequestThreadLocal.getSession());
		}
		return returnValue;
	}

	/**
	 * This method is called when the bean is initialized
	 */
	public void init() {
		isDebugEnabled = Boolean.parseBoolean(configurationManager.get(Constants.IsDebugEnabled));
	}

}
