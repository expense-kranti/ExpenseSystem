package com.boilerplate.service.implemetations;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.SendRegistrationEmailObserver;
import com.boilerplate.asyncWork.SendSMSOnPasswordChange;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.interfaces.ISFUpdateHash;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.database.interfaces.IUserRole;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.Encryption;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.AuthenticationRequest;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.Role;
import com.boilerplate.java.entities.UpdateUserEntity;
import com.boilerplate.java.entities.UpdateUserPasswordEntity;
import com.boilerplate.service.interfaces.IRoleService;
import com.boilerplate.service.interfaces.IUserService;
import com.boilerplate.sessions.Session;

/**
 * This class Implements the IUserService class
 * 
 * @author shiva
 *
 */
public class UserService implements IUserService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(UserService.class);

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * This is the instance of the send Registration SMS Observer.
	 */
	@Autowired
	com.boilerplate.asyncWork.SendRegistrationSMSObserver sendRegistrationSMSObserver;

	/**
	 * This is the observer for sending SMS for user registration
	 * 
	 * @param sendRegistrationSMSObserver
	 *            The sms observer
	 */
	public void setSendRegistrationSMSObserver(
			com.boilerplate.asyncWork.SendRegistrationSMSObserver sendRegistrationSMSObserver) {
		this.sendRegistrationSMSObserver = sendRegistrationSMSObserver;
	}

	/**
	 * This is the observer to send email
	 */
	@Autowired
	SendRegistrationEmailObserver sendRegistrationEmailObserver;

	/**
	 * Sets the observer for sending email
	 * 
	 * @param sendRegistrationEmailObserver
	 *            Sending email observer
	 */
	public void setSendRegistrationEmailObserver(SendRegistrationEmailObserver sendRegistrationEmailObserver) {
		this.sendRegistrationEmailObserver = sendRegistrationEmailObserver;
	}

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * The autowired instance of session manager
	 */
	@Autowired
	com.boilerplate.sessions.SessionManager sessionManager;

	/**
	 * This sets the session manager
	 * 
	 * @param sessionManager
	 *            The session manager
	 */
	public void setSessionManager(com.boilerplate.sessions.SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	/**
	 * The autowired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;

	/**
	 * This is the instance of the role service
	 */
	@Autowired
	private IRoleService roleService;

	/**
	 * Sets the role service
	 * 
	 * @param roleService
	 *            The role service
	 */
	public void setRoleService(IRoleService roleService) {
		this.roleService = roleService;
	}

	/**
	 * This is the setter for user data acess
	 * 
	 * @param iUser
	 */
	public void setUserDataAccess(IUser iUser) {
		this.userDataAccess = iUser;
	}

	/**
	 * This is the user role database access layer
	 */
	@Autowired
	private IUserRole userRole;

	/**
	 * This sets the user role
	 * 
	 * @param userRole
	 *            This is user role
	 */
	public void setUserRole(IUserRole userRole) {
		this.userRole = userRole;
	}

	/**
	 * This is a new instance of Referral
	 */
	IReferral referral;

	/**
	 * This method is used to set the referral
	 * 
	 * @param referral
	 *            the referral to set
	 */
	public void setReferral(IReferral referral) {
		this.referral = referral;
	}

	/**
	 * This is an instance of the queue job, to save the session back on to the
	 * database async
	 */
	@Autowired
	com.boilerplate.jobs.QueueReaderJob queueReaderJob;

	/**
	 * This sets the queue reader job
	 * 
	 * @param queueReaderJob
	 *            The queue reader job
	 */
	public void setQueueReaderJob(com.boilerplate.jobs.QueueReaderJob queueReaderJob) {
		this.queueReaderJob = queueReaderJob;
	}

	/**
	 * This is the observer for password changes
	 */
	@Autowired
	SendSMSOnPasswordChange sendSMSOnPasswordChange;

	/**
	 * Sets the observer for password change
	 * 
	 * @param sendSMSOnPasswordChange
	 */
	public void setSendSMSOnPasswordChange(SendSMSOnPasswordChange sendSMSOnPasswordChange) {
		this.sendSMSOnPasswordChange = sendSMSOnPasswordChange;
	}

	/**
	 * This variable is used to define the list of subjects ,subjects basically
	 * define the background operations need to be perform this user
	 */
	BoilerplateList<String> subjectsForCreateUser = new BoilerplateList();

	BoilerplateList<String> subjectsForAutomaticPasswordReset = new BoilerplateList();

	BoilerplateList<String> subjectForPasswordChange = new BoilerplateList();

	BoilerplateList<String> subjectForCampaign = new BoilerplateList<>();

	BoilerplateList<String> subjectForUpdateRefererScore = new BoilerplateList<>();
	
	BoilerplateList<String> subjectForReferUUID = new BoilerplateList<>();

	/**
	 * This variable define the default status for the current user
	 */
	private int defaultUserStatus = 1;

	/**
	 * Observer for SMS registration
	 */
	@Autowired
	com.boilerplate.asyncWork.SendPasswordResetSMSObserver sendPasswordResetSMSObserver;

	/**
	 * This is the observer for sending SMS for user password reset
	 * 
	 * @param sendRegistrationSMSObserver
	 *            The sms observer
	 */
	public void setSendPasswordResetSMSObserver(
			com.boilerplate.asyncWork.SendPasswordResetSMSObserver sendPasswordResetSMSObserver) {
		this.sendPasswordResetSMSObserver = sendPasswordResetSMSObserver;
	}

	/**
	 * This is the instance of redissfupdatehashaccess
	 */
	@Autowired
	ISFUpdateHash redisSFUpdateHashAccess;

	/**
	 * This method set the instance of redisSFUpdateHashAccess
	 * 
	 * @param redisSFUpdateHashAccess
	 *            the redisSFUpdateHashAccess to set
	 */
	public void setRedisSFUpdateHashAccess(ISFUpdateHash redisSFUpdateHashAccess) {
		this.redisSFUpdateHashAccess = redisSFUpdateHashAccess;
	}

	/**
	 * Initializes the bean
	 */
	public void initilize() {
		subjectsForCreateUser.add("CreateUser");
		defaultUserStatus = Integer.parseInt(this.configurationManager.get("DefaultUserStatus") == null ? "1"
				: this.configurationManager.get("DefaultUserStatus"));
		subjectsForAutomaticPasswordReset.add("AutomaticPasswordReset");
		subjectForPasswordChange.add("PasswordChange");
		subjectForCampaign.add("CampaignRelated");
		subjectForUpdateRefererScore.add("UpdateReferScore");
		subjectForReferUUID.add("CheckAndCreateReferUUID");
	}

	/**
	 * @see IUserService.create
	 */
	@Override
	public ExternalFacingUser create(ExternalFacingUser externalFacingUser)
			throws ValidationFailedException, ConflictException, NotFoundException, BadRequestException {
		// if password is blank set it
		Long otpPassword = 0L;
		if (externalFacingUser.getPassword() == null || externalFacingUser.getPassword().isEmpty()) {

			otpPassword = Math.abs(UUID.randomUUID().getMostSignificantBits());
			externalFacingUser.setPassword(Long.toString(otpPassword).substring(0, 6));
			otpPassword = Long.parseLong(Long.toString(otpPassword).substring(0, 6));
		}

		String passwordReceived = externalFacingUser.getPassword();
		// if user Id is blank set it
		if (externalFacingUser.getUserId() == null || externalFacingUser.getUserId().isEmpty()) {
			externalFacingUser.setUserId(externalFacingUser.getPhoneNumber());
		}
		externalFacingUser.validate();

		if (externalFacingUser.getAuthenticationProvider() == null) {
			externalFacingUser
					.setAuthenticationProvider(this.configurationManager.get("DefaultAuthenticationProvider"));
		}

		// check if a user with given Id exists
		if (this.userExists(externalFacingUser.getAuthenticationProvider() + ":" + externalFacingUser.getUserId())) {
			throw new ConflictException("User", "User already exist with this mobile", null);
		}
		/*
		 * check if a user with given email exists then throw conflict exception
		 * otherwise put the email entry in email list hash
		 */
		this.checkOrCreateEmailInHash(externalFacingUser);
		externalFacingUser
				.setUserId(externalFacingUser.getAuthenticationProvider() + ":" + externalFacingUser.getUserId());

		// before save lets hash the password
		externalFacingUser.hashPassword();
		// set create and update date
		externalFacingUser.setCreationDate(new Date());
		externalFacingUser.setUpdationDate(externalFacingUser.getCreationDate());
		externalFacingUser.setUserStatus(this.defaultUserStatus);
		externalFacingUser.setUserKey(UUID.randomUUID().toString());

		if (externalFacingUser.getReferalSource() == null) {
			externalFacingUser.setReferalSource("None");
		}

		if (externalFacingUser.getReferalSource().isEmpty()) {
			externalFacingUser.setReferalSource("None");
		}

		// call the database to save the user
		externalFacingUser = (ExternalFacingUser) userDataAccess.create(externalFacingUser).transformToExternal();

		// publish the created user
		ExternalFacingUser externalFacingUserClone = null;
		try {
			externalFacingUserClone = (ExternalFacingUser) externalFacingUser.clone();
			externalFacingUserClone.setPassword(passwordReceived);
			queueReaderJob.requestBackroundWorkItem(externalFacingUserClone, subjectsForCreateUser, "UserService",
					"create");
		} catch (Exception ex) {
			// now if the queue is not working we send sms and email on the
			// thread
			try {
				sendRegistrationSMSObserver.sendSMS(externalFacingUserClone.getFirstName(),
						externalFacingUserClone.getPassword(), externalFacingUserClone.getPhoneNumber());
			} catch (Exception exSms) {
				// if an exception takes place here we cant do much hence just
				// log it and move
				// forward
				logger.logException("UserService", "create", "try-Queue Reader - Send SMS", exSms.toString(), exSms);
			}

			try {
				BoilerplateList<String> tosEmailList = new BoilerplateList<String>();
				tosEmailList.add(externalFacingUserClone.getEmail());
				BoilerplateList<String> ccsEmailList = new BoilerplateList<String>();
				BoilerplateList<String> bccsEmailList = new BoilerplateList<String>();
				sendRegistrationEmailObserver.sendEmail(externalFacingUserClone.getFirstName(), tosEmailList,
						ccsEmailList, bccsEmailList, externalFacingUserClone.getPhoneNumber(),
						externalFacingUserClone.getUserKey());

			} catch (Exception exEmail) {
				// if an exception takes place here we cant do much hence just
				// log it and move
				// forward
				logger.logException("UserService", "create", "try-Queue Reader - Send Email", exEmail.toString(),
						exEmail);
			}

			logger.logException("UserService", "create", "try-Queue Reader", ex.toString(), ex);
		}
		// we dont want to share the hash hence sending bacj the text
		externalFacingUser.setPassword("Password Encrypted");
		// generate otp list
		this.checkIfUserRegisteredThroughCampaign(externalFacingUser);
		return externalFacingUser;
	}

	/**
	 * This method email checks exists or not if not then put email entry in
	 * email_list_hash otherwise throw conflict exception
	 * 
	 * @param email
	 * @throws ConflictException
	 */
	private void checkOrCreateEmailInHash(ExternalFacingUser user) throws ConflictException {
		if (this.redisSFUpdateHashAccess.hget(configurationManager.get("AKS_USER_EMAIL_HASH_BASE_TAG"),
				user.getAuthenticationProvider() + ":" + user.getEmail().toUpperCase()) != null) {
			throw new ConflictException("User", "User with given email already exists", null);

		} else {
			this.redisSFUpdateHashAccess.hset(configurationManager.get("AKS_USER_EMAIL_HASH_BASE_TAG"),
					user.getAuthenticationProvider() + ":" + user.getEmail().toUpperCase(), user.getUserId());
		}
	}

	/**
	 * This method is used to check the user exists whether the user is exits in
	 * our data base or not if exits then return true else return false.
	 * 
	 * @param userId
	 *            this parameter define userId
	 * @return false if user not exist else true
	 */
	private boolean userExists(String userId) {
		return this.userDataAccess.userExists(userId);
	}

	/**
	 * @see IUserService.normalizeUserId
	 */
	@Override
	public String normalizeUserId(String userId) {
		userId = userId.toUpperCase();
		// check if user id contains :
		if (userId.contains(":") == false) {
			// check if the user starts with DEFAULT:, if not then put in
			// Default: before it
			if (!userId
					.startsWith(this.configurationManager.get("DefaultAuthenticationProvider").toUpperCase() + ":")) {
				userId = this.configurationManager.get("DefaultAuthenticationProvider") + ":" + userId;
			}
		}
		return userId;
	}

	/**
	 * @see IUserService.authenticate
	 */
	@Override
	public Session authenticate(AuthenticationRequest authenitcationRequest) throws UnauthorizedException {
		authenitcationRequest.setUserId(this.normalizeUserId(authenitcationRequest.getUserId()));
		ExternalFacingReturnedUser user = null;
		// Call the database and check if the user is
		// we store everything in upper case hence chanhing it to upper
		try {
			user = userDataAccess.getUser(authenitcationRequest.getUserId().toUpperCase(), roleService.getRoleIdMap());
			String hashedPassword = String.valueOf(Encryption.getHashCode(authenitcationRequest.getPassword()));
			if (!user.getPassword().equals(hashedPassword)) {
				throw new UnauthorizedException("USER", "User name or password incorrect", null);
			}
			// check for dsa approval
			if (user.getUserId()
					.startsWith(this.configurationManager.get("DSAAuthenticationProvider").toUpperCase() + ":")) {
				if (user.getApproved() != null && !(user.getApproved().equals("1"))) {
					throw new UnauthorizedException("USER",
							"Your account is waiting for CMD approval. Please contact us at 9599814087.", null);
				}
			}
			user.setPassword("Password Encrypted");
			// get the roles, ACL and there details of this user
			// if the user is valid create a new session, in the session add
			// details
			Session session = sessionManager.createNewSession(user);
			//push the refer unique id task in queue
			if (user.getUserReferId() == null) {
				try{
					queueReaderJob.requestBackroundWorkItem(user, subjectForReferUUID,
							"UserService", "Authenticate");
				}catch (Exception efe) {
					//log in case of queue failure
					logger.logException("UserService", "authenticate", "Queue Refer id failure", "UserId"+user.getUserId(), null);
					
				}
				
				
			}
			return session;
		} catch (NotFoundException nfe) {
			logger.logException("UserService", "authenticate",
					"External Facing User: " + authenitcationRequest.getUserId().toUpperCase()
							+ " With entered Password: " + authenitcationRequest.getPassword() + " not found",
					"Converting this exception to Unauthorized for security", nfe);
			throw new UnauthorizedException("USER", "User name or password incorrect", null);
		}
		
	}

	/**
	 * @see IUserService.get
	 */
	@Override
	public ExternalFacingReturnedUser get(String userId) throws NotFoundException, BadRequestException {
		// retrun the user with password as a string
		return get(userId, true);

	}

	/**
	 * @see IUserService.get
	 */
	@Override
	public ExternalFacingReturnedUser get(String userId, boolean encryptPasswordString)
			throws NotFoundException, BadRequestException {
		if (userId == null)
			throw new BadRequestException("User", "User Id not populated", null);
		// convert user names to upper
		userId = this.normalizeUserId(userId);
		// get the user from database
		ExternalFacingReturnedUser externalFacingUser = this.userDataAccess.getUser(userId, roleService.getRoleIdMap());
		// if no user with given id was found then throw exception
		if (externalFacingUser == null)
			throw new NotFoundException("ExternalFacingUser", "User with id " + userId + " doesnt exist", null);
		// set the password as encrypted
		if (encryptPasswordString) {
			externalFacingUser.setPassword("Password Encrypted");
		}
		// return the user
		return externalFacingUser;

	}

	/**
	 * @see IUserService.logout
	 */
	@Override
	public void logout(String sessionId) {
		sessionManager.logout(sessionId);
	}

	/**
	 * @see IUser.automaticPasswordReset
	 */
	@Override
	public ExternalFacingReturnedUser automaticPasswordReset(ExternalFacingUser externalFacingUser)
			throws ValidationFailedException, ConflictException, NotFoundException, UnauthorizedException,
			BadRequestException {
		
		
		// get the user requested for
		ExternalFacingUser returnedUser = this.get(externalFacingUser.getUserId());

		// generate the password
		UpdateUserPasswordEntity updatePasswordEntity = new UpdateUserPasswordEntity();

		String password = Long.toString(Math.abs(UUID.randomUUID().getMostSignificantBits())).substring(0, 6);
		updatePasswordEntity.setPassword(password);

		// update the entity
		ExternalFacingReturnedUser externalFacingReturnedUser = this.update(externalFacingUser.getUserId(),
				updatePasswordEntity.convertToUpdateUserEntity());
		// Send a message
		ExternalFacingUser externalFacingUserClone = null;
		try {

			externalFacingUserClone = (ExternalFacingUser) externalFacingReturnedUser.clone();
			externalFacingUserClone.setPassword(password);
			// only send user id and password because metadata create problem in
			// case of report extracted
			BoilerplateMap<String, String> userPasswordMap = new BoilerplateMap<>();
			userPasswordMap.put("userId", externalFacingUserClone.getUserId());
			userPasswordMap.put("password", externalFacingUserClone.getPassword());
			queueReaderJob.requestBackroundWorkItem(userPasswordMap, subjectsForAutomaticPasswordReset, "UserService",
					"create");
		} catch (Exception ex) {
			try {
				this.sendPasswordResetSMSObserver.sendSMS(externalFacingUserClone.getFirstName(), password,
						externalFacingUserClone.getPhoneNumber());
			} catch (Exception exSendPassword) {
				logger.logException("UserService", "Reset password", "inside catch: try-Queue Reader",
						exSendPassword.toString() + "" + "Gateway down", exSendPassword);
			}
			logger.logException("UserService", "Reset password", "try-Queue Reader",
					ex.toString() + " ExternalFacingUser inserting in queue is: " + Base.toJSON(externalFacingUserClone)
							+ " Queue Down",
					ex);
		}
		return externalFacingReturnedUser;
	}

	/**
	 * @see IUserService.update
	 */
	@Override
	public ExternalFacingReturnedUser update(String userId, UpdateUserEntity updateUserEntity)
			throws ValidationFailedException, ConflictException, NotFoundException, BadRequestException {
		// check if the user exists, if so get it
		ExternalFacingUser user = this.get(userId, false);
		// Update the user items from the incomming entity
		if (updateUserEntity.getPassword() != null) {
			if (updateUserEntity.getPassword().equals("") == false) {
				user.setPassword(updateUserEntity.getPassword());
				// and hash the password
				user.hashPassword();
			}
		}
		// for each key updte the metadata
		for (String key : updateUserEntity.getUserMetaData().keySet()) {
			user.getUserMetaData().put(key, updateUserEntity.getUserMetaData().get(key));
		}

		user.setUpdationDate(new Date());

		// validate the entity
		user.validate();
		ExternalFacingReturnedUser returnedUser = new ExternalFacingReturnedUser(user);
		if (updateUserEntity.getDateOfBirth() != null && updateUserEntity.getDateOfBirth().equals("") == false) {
			returnedUser.setDateOfBirth(updateUserEntity.getDateOfBirth());
		}
		if (updateUserEntity.getEmploymentStatus() != null
				&& updateUserEntity.getEmploymentStatus().equals("") == false) {
			returnedUser.setEmploymentStatus(updateUserEntity.getEmploymentStatus());
		}
		if (updateUserEntity.getAlternateNumber() != null
				&& updateUserEntity.getAlternateNumber().equals("") == false) {
			returnedUser.setAlternateNumber(updateUserEntity.getAlternateNumber());
		}
		if (updateUserEntity.getLocation() != null && updateUserEntity.getLocation().equals("") == false) {
			returnedUser.setLocation(updateUserEntity.getLocation());
		}
		if (updateUserEntity.getCrmid() != null && updateUserEntity.getCrmid().equals("") == false) {
			returnedUser.setCrmid(updateUserEntity.getCrmid());
		}
		if (updateUserEntity.getIsPasswordChanged()) {
			// Set is password reset to true
			returnedUser.setIsPasswordChanged(true);
		}
		if ((this.get(userId, true).getRoles()) != null && (this.get(userId, true).getRoles()).size() != 0)
			returnedUser.setRoles(get(userId, true).getRoles());
		else
			returnedUser.setRoles(new BoilerplateList<Role>());
		// update the user in the database
		this.userDataAccess.update(returnedUser);
		// if we deleted the user we will not get it back
		if (returnedUser.getUserStatus() == 0) {
			return null;
		} else {
			return this.get(user.getUserId());
		}
	}

	/**
	 * @see IUser.updateUser
	 */
	@Override
	public ExternalFacingReturnedUser update(UpdateUserPasswordEntity updateUserPasswordEntity)
			throws ValidationFailedException, ConflictException, NotFoundException, UnauthorizedException,
			BadRequestException {
		// find the current logged in user
		if (RequestThreadLocal.getSession() == null) {
			throw new UnauthorizedException("User", "User not logged in for update", null);
		}
		if (RequestThreadLocal.getSession().getUserId() == null
				|| RequestThreadLocal.getSession().getUserId().isEmpty()) {
			throw new UnauthorizedException("User", "User not logged in for update", null);
		}
		
		// convert update user password entity to update user entity
		UpdateUserEntity updateUserEntity = updateUserPasswordEntity.convertToUpdateUserEntity();
		// Set is password change to true
		updateUserEntity.setIsPasswordChanged(true);
		ExternalFacingReturnedUser externalFacingReturnedUser = this
				.update(RequestThreadLocal.getSession().getExternalFacingUser().getUserId(), updateUserEntity);

		// Send a message
		try {
			queueReaderJob.requestBackroundWorkItem(externalFacingReturnedUser, subjectForPasswordChange, "UserService",
					"update");
		} catch (Exception ex) {
			try {
				sendSMSOnPasswordChange.sendSMS(externalFacingReturnedUser.getFirstName(),
						externalFacingReturnedUser.getPhoneNumber());
			} catch (Exception smsException) {
				logger.logException("UserService", "create", "try-Queue Reader", ex.toString() + " SMS Gateway Down",
						ex);
			}
			logger.logException("UserService", "create", "try-Queue Reader", ex.toString() + " Queue Down", ex);
		}
		return externalFacingReturnedUser;
	}

	/**
	 * This method is used to check is user come through campaign if it is then
	 * call a background job update refer by user score
	 * 
	 * @param externalFacingUser
	 *            this parameter contain the information regarding the sing up
	 *            user like if it is come through campaign then its will contain
	 *            information regarding the campaign source,campaign type and
	 *            uuid
	 */
	public void checkIfUserRegisteredThroughCampaign(ExternalFacingUser externalFacingUser) {
		// Check is campaign uuid is not null if it not null then check its
		// Existence
		if (externalFacingUser.getCampaignUUID() != null
				&& referral.getReferUser(externalFacingUser.getCampaignUUID()) != null) {
			try {
				queueReaderJob.requestBackroundWorkItem(externalFacingUser, subjectForUpdateRefererScore, "UserService",
						"checkIfUserRegisteredThroughCampaign");
			} catch (Exception ex) {
				logger.logException("UserService", "checkIfUserRegisteredThroughCampaign", "try-Queue Reader",
						ex.toString() + "Fail to update referer score", ex);
			}
		}
	}
	
	@Override
	public String getReferUserId(String uuid) {
		// Save user's id and refer UUID in hash map
		return userDataAccess.getReferUser(uuid);
	}

}