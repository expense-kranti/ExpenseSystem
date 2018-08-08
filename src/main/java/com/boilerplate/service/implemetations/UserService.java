package com.boilerplate.service.implemetations;

import java.nio.charset.CharacterCodingException;
import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.asyncWork.SendRegistrationEmailObserver;
import com.boilerplate.asyncWork.SendSMSOnPasswordChange;
import com.boilerplate.database.interfaces.IMySQLReport;
import com.boilerplate.database.interfaces.IRedisAssessment;
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
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.ExperianQuestionAnswer;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ManageUserEntity;
import com.boilerplate.java.entities.MethodState;
import com.boilerplate.java.entities.ReportInputEntity;
import com.boilerplate.java.entities.Role;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.java.entities.UpdateUserEntity;
import com.boilerplate.java.entities.UpdateUserPasswordEntity;
import com.boilerplate.java.entities.ExperianDataPublishEntity.State;
import com.boilerplate.service.interfaces.IAssessmentService;
import com.boilerplate.service.interfaces.IBlogActivityService;
import com.boilerplate.service.interfaces.IReferralService;
import com.boilerplate.service.interfaces.IReportService;
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
	 * These variables are used in checking the operation type to be done on
	 * TotalScore with given score points
	 */
	public static final String SUBTRACT = "SUBTRACT";

	public static final String ADD = "ADD";

	// format of double values in two places decimal
	DecimalFormat df = new DecimalFormat("#.##");

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
	 * This is the setter for user data acess
	 * 
	 * @param iUser
	 */
	public void setUserDataAccess(IUser iUser) {
		this.userDataAccess = iUser;
	}

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
	 * This is the instance of IUser
	 */
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
	 * This is the instance of the assessment service
	 */
	@Autowired
	IAssessmentService assessmentService;

	/**
	 * Sets the assessment service
	 * 
	 * @param assessmentService
	 *            the assessmentService to set
	 */
	public void setAssessmentService(IAssessmentService assessmentService) {
		this.assessmentService = assessmentService;
	}

	/**
	 * This is the instance of referral service
	 */
	@Autowired
	IReferralService referralService;

	/**
	 * Sets the referral service
	 * 
	 * @param referralService
	 *            the referralService to set
	 */
	public void setReferralService(IReferralService referralService) {
		this.referralService = referralService;
	}

	/**
	 * This is the blog activity service instance
	 */
	@Autowired
	IBlogActivityService blogActivityService;

	/**
	 * Sets the blog activity service
	 * 
	 * @param blogActivityService
	 *            the blogActivityService to set
	 */
	public void setBlogActivityService(IBlogActivityService blogActivityService) {
		this.blogActivityService = blogActivityService;
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
	 * This is the instance of report service
	 */
	@Autowired
	private IReportService reportService;

	/**
	 * This method set the report service
	 * 
	 * @param reportService
	 */
	public void setReportService(IReportService reportService) {
		this.reportService = reportService;
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
	 * This is the new instance of redis assessment
	 */
	@Autowired
	IRedisAssessment redisAssessment;

	/**
	 * This method set the redisAssessment
	 * 
	 * @param redisAssessment
	 *            the redisAssessment to set
	 */
	public void setRedisAssessment(IRedisAssessment redisAssessment) {
		this.redisAssessment = redisAssessment;
	}

	/**
	 * This is an instance of mysqlReport
	 */
	IMySQLReport mysqlReport;

	/**
	 * @param mysqlReport
	 *            the mysqlReport to set
	 */
	public void setMysqlReport(IMySQLReport mysqlReport) {
		this.mysqlReport = mysqlReport;
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
		if (!BaseEntity.isNullOrEmpty(externalFacingUser.getEmail())) {
			this.checkOrCreateEmailInHash(externalFacingUser);
		}

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
		// set user state
		externalFacingUser.setUserState(MethodState.Registered);

		// call the database to save the user
		externalFacingUser = (ExternalFacingUser) userDataAccess.create(externalFacingUser).transformToExternal();

		logger.logInfo("UserService", "create", "CreateUser",
				"adding user id into Redis Set. UserId is : " + externalFacingUser.getUserId());
		// add the user id in redis set to be later fetched and saved in MysqlDB
		// using job
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			userDataAccess.addInRedisSet(externalFacingUser);
		}

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

			logger.logException("UserService", "create", "try-Queue Reader", ex.toString(), ex);
		}
		// we dont want to share the hash hence sending bacj the text
		externalFacingUser.setPassword("Password Encrypted");
		// generate otp list
		BoilerplateList<Integer> randomOtpList = new BoilerplateList<Integer>();
		try {
			randomOtpList = generateOtp(Long.toString(otpPassword));
		} catch (Exception e) {
			logger.logException("UserService", "create", "catch block of random otp list", e.toString(), e);
		}
		// set otp list in external facing user
		externalFacingUser.setOtpList(randomOtpList);
		this.checkIfUserRegisteredThroughCampaign(externalFacingUser);
		return externalFacingUser;
	}

	/**
	 * This method generate random otp list. otherwise throw conflict exception
	 * 
	 * @param email
	 * @throws ConflictException
	 */
	private BoilerplateList<Integer> generateOtp(String otpPassword) throws Exception {
		// generate otp list
		Random random = new Random();
		int lowRangeValue = 100000;
		int highRangeValue = 999999;
		BoilerplateList<Integer> randomOtpList = new BoilerplateList<>();
		for (int i = 0; i < 3; i++) {
			randomOtpList.add(random.nextInt(highRangeValue - lowRangeValue) + lowRangeValue);
		}
		randomOtpList.add(Integer.parseInt(otpPassword));
		Collections.shuffle(randomOtpList);
		return randomOtpList;
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
	 * @see IUserService.checkUserExistence
	 */
	@Override
	public boolean checkUserExistence(ExternalFacingUser externalFacingUser)
			throws ValidationFailedException, ConflictException {
		if ((externalFacingUser.getUserId()) == null || (externalFacingUser.getUserId()).isEmpty()) {
			throw new ValidationFailedException("UserEntity", "UserId is null or empty", null);
		}
		if (this.userExists(normalizeUserId(externalFacingUser.getUserId()))) {
			throw new ConflictException("UserEntity", "User already exists with this id", null);
		}
		return false;
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
			// update user state, if older user means they do not have user
			// state set or new user who have user state Registered
			if (user.getUserState() == MethodState.Registered || user.getUserState() == null) {
				user.setUserState(MethodState.Validated);
			}
			// set last logged in time
			user.setLastLoginTime(new Date());
			// save user with last login time
			userDataAccess.update(user);
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
			// COMMENTED AS EXPERIAN INTEGRATION NOT BEING MADE LIVE TO
			// PRODUCTION
			// // get reportInput entity to show to UI
			// user = getReportInputEntity(user);

			// get the roles, ACL and there details of this user
			// if the user is valid create a new session, in the session add
			// details
			Session session = sessionManager.createNewSession(user);
			// push the refer unique id task in queue
			if (user.getUserReferId() == null) {
				try {

					String userUUID = this
							.createUUID(Integer.valueOf(configurationManager.get("REFERRAL_LINK_UUID_LENGTH")));
					user.setUserReferId(userUUID);
					queueReaderJob.requestBackroundWorkItem(user, subjectForReferUUID, "UserService", "Authenticate");
				} catch (Exception efe) {
					// log in case of queue failure
					logger.logException("UserService", "authenticate", "Queue Refer id failure",
							"UserId" + user.getUserId(), null);

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
	//COMMENTED AS EXPERIAN INTEGRATION NOT BEING MADE LIVE TO PRODUCTION
//	/**
//	 * @param user
//	 */
//	public ExternalFacingReturnedUser getReportInputEntity(ExternalFacingReturnedUser user) {
//		// check state of user if user state is experian attempt or
//		// authquestion then give its report input entity
//
//		List<ReportInputEntity> reportInputEntityList = mysqlReport.getReportInputEntity(user.getUserId());
//		ReportInputEntity reportInputEntity = null;
//		// check if report input entity is present for user
//		if (reportInputEntityList.size() > 0) {
//			reportInputEntity = reportInputEntityList.get(0);
//			user.setReportInputEntity(reportInputEntity);
//		}
//
//		return user;
//	}

	/**
	 * This method is used to create the UUID
	 * 
	 * @return the UUID
	 */
	private String createUUID(Integer uuidLength) {
		// New instance of random
		Random rand = new Random();
		String userReferId = "";
		// Run a for loop to generate a configurations define length uuid
		for (int i = 0; i < uuidLength; i++) {
			// Get random number
			int randomNum = rand.nextInt(26 - 0);
			// Concatenate new char to string
			userReferId = userReferId + String.valueOf((char) (randomNum + 97));
		}
		return userReferId;
	}

	/**
	 * @see IUserService.get
	 */
	@Override
	public ExternalFacingReturnedUser get(String userId) throws NotFoundException, BadRequestException {
		// retrun the user with password as a string
		ExternalFacingReturnedUser externalFacingUser = get(userId, true);
		// COMMENTED AS EXPERIAN INTEGRATION NOT BEING MADE LIVE TO PRODUCTION
		// // no need to check user null as already check in above method
		// externalFacingUser = this.getReportInputEntity(externalFacingUser);
		return externalFacingUser;
	}

	/**
	 * @see IUserService.get
	 */
	@Override
	public ExternalFacingReturnedUser get(String userId, boolean encryptPasswordString)
			throws NotFoundException, BadRequestException {
		if (userId == null || userId.isEmpty())
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
		// generate otp list
		BoilerplateList<Integer> randomOtpList = new BoilerplateList<Integer>();
		try {
			randomOtpList = generateOtp(password);
		} catch (Exception e) {
			logger.logException("UserService", "create", "catch block of random otp list", e.toString(), e);
		}
		// set otp list in external facing user
		externalFacingReturnedUser.setOtpList(randomOtpList);
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

		// below first name,lastname, email is set for new requirements of
		// creditworthiness landing page
		// update user first name
		if (updateUserEntity.getFirstName() != null && updateUserEntity.getFirstName().equals("") == false) {
			returnedUser.setFirstName(updateUserEntity.getFirstName());
		}
		if (updateUserEntity.getLastName() != null && updateUserEntity.getLastName().equals("") == false) {
			returnedUser.setLastName(updateUserEntity.getLastName());
		}
		if (updateUserEntity.getEmail() != null && updateUserEntity.getEmail().equals("") == false) {
			returnedUser.setEmail(updateUserEntity.getEmail());
			this.checkOrCreateEmailInHash(returnedUser);
		}

		// if this is set true means user is not interested and skipped inputing
		// his/her emailid and will always remain true, once set true
		if (updateUserEntity.getHasSkippedEmailInput()) {
			// set hasSkippedEmailInput to true
			returnedUser.setHasSkippedEmailInput(true);
		}

		if (updateUserEntity.getIsPasswordChanged()) {
			// Set is password reset to true
			returnedUser.setIsPasswordChanged(true);
		}

		if (updateUserEntity.getScoreUpdateOperation() != null && (updateUserEntity.getScoresToUpdate() > 0)) {
			// for updating user score
			if (updateUserEntity.getScoreUpdateOperation().equals(ADD)) {

				ScoreEntity scoreEntity = null;

				scoreEntity = prepareScoreEntityForScoreUpdate(scoreEntity, userId);

				// update the TotalScore key entry for given userId
				scoreEntity.setObtainedScore(df.format(
						Float.parseFloat(scoreEntity.getObtainedScore()) + updateUserEntity.getScoresToUpdate()));
				// no need to check for null and empty
				scoreEntity.setObtainedScoreInDouble(Double.parseDouble(scoreEntity.getObtainedScore()));

				updateUserTotalScore(scoreEntity, returnedUser);

			} else if (updateUserEntity.getScoreUpdateOperation().equals(SUBTRACT)) {
				ScoreEntity scoreEntity = null;

				scoreEntity = prepareScoreEntityForScoreUpdate(scoreEntity, userId);
				// or for negative value case we can use,
				// scoreEntity.getObtainedScore().contains("-") if applicable in
				// future
				if (scoreEntity.getObtainedScore().equals("0") == false) {
					// update the TotalScore key entry for given userId
					scoreEntity.setObtainedScore(df.format(
							Float.parseFloat(scoreEntity.getObtainedScore()) - updateUserEntity.getScoresToUpdate()));
					// no need to check for null and empty
					scoreEntity.setObtainedScoreInDouble(Double.parseDouble(scoreEntity.getObtainedScore()));

					updateUserTotalScore(scoreEntity, returnedUser);
				}

			}
		}

		if ((this.get(userId, true).getRoles()) != null && (this.get(userId, true).getRoles()).size() != 0)
			returnedUser.setRoles(get(userId, true).getRoles());
		else
			returnedUser.setRoles(new BoilerplateList<Role>());

		// update the user in the database
		this.userDataAccess.update(returnedUser);

		logger.logInfo("UserService", "update", "UpdateUser",
				"adding user id into Redis Set for user update. UserId is : " + returnedUser.getUserId());
		// update user in MySQLdatabase
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			userDataAccess.addInRedisSet(returnedUser);
		}

		// if we deleted the user we will not get it back
		if (returnedUser.getUserStatus() == 0) {
			return null;
		} else {
			// also returning report input entity here in below method
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

	/**
	 * This api updates a user
	 * 
	 * @param user
	 *            The user
	 * @throws ConflictException
	 *             if there is an error updating the user
	 */
	@Override
	public void update(ExternalFacingReturnedUser user) throws ConflictException {
		this.userDataAccess.update(user);
	}

	/**
	 * @see IUserService.deleteUser
	 */
	@Override
	public void deleteUser(ManageUserEntity manageUserEntity)
			throws NotFoundException, UnauthorizedException, BadRequestException, ValidationFailedException {
		if (!checkIsAdmin()) {
			throw new UnauthorizedException("User", "User is not authorized to perform this action", null);
		}
		manageUserEntity.validate();
		String userId = manageUserEntity.getUserId();
		// normalized userId with authentication provider concatenated
		userId = normalizeUserId(userId);
		if (this.userExists(userId)) {
			ExternalFacingUser user = userDataAccess.getUser(userId, null);
			// delete user all assessment related data
			assessmentService.deleteUserAllAssessmentData(userId);
			// delete user blog activity related data
			blogActivityService.deleteUserBlogActivityService(userId);
			// delete user email entry in emailhashmap
			if (!BaseEntity.isNullOrEmpty(user.getEmail())) {
				this.redisSFUpdateHashAccess.hdel(configurationManager.get("AKS_USER_EMAIL_HASH_BASE_TAG"),
						user.getAuthenticationProvider() + ":" + user.getEmail().toUpperCase());
			}
			// delete hash map entries in database
			this.redisSFUpdateHashAccess.hdel(configurationManager.get("AKS_USER_UUID_HASH_BASE_TAG"),
					user.getUserId().toUpperCase());
			// check if user refer id is not null then there might be entries in
			// database which are using user refer id
			if (user.getUserReferId() != null) {
				// delete user's all referral related data
				referralService.deleteUserAllReferralData(user.getUserReferId());
				this.redisSFUpdateHashAccess.hdel(configurationManager.get("AKS_UUID_USER_HASH_BASE_TAG"),
						user.getUserReferId());
			}
			this.redisSFUpdateHashAccess.del(configurationManager.get("SF_Update_Hash_Name") + ":" + userId);
			// delete user
			userDataAccess.deleteUser(user);
		} else {
			throw new NotFoundException("User Entity", "No User Found with this id", null);
		}
	}

	@Override
	public ExternalFacingReturnedUser updateAUser(UpdateUserEntity updateUserEntity)
			throws ConflictException, NotFoundException, BadRequestException, ValidationFailedException {

		// check if first name null or empty then throw exception as first name
		// is mandatory to update here
		if (updateUserEntity.getFirstName() == null || updateUserEntity.getFirstName().isEmpty())
			throw new ValidationFailedException("UpdateUserEntity", "First name is null or emtpy", null);
		// check if the user exists, if so get it validation has been handled
		ExternalFacingUser user = this.get(updateUserEntity.getPhoneNumber(), false);
		ExternalFacingReturnedUser returnedUser = new ExternalFacingReturnedUser(user);
		// below first name,lastname, email is set for new requirements of
		// creditworthiness landing page
		// update user first name
		returnedUser.setFirstName(updateUserEntity.getFirstName());

		if (updateUserEntity.getLastName() != null && updateUserEntity.getLastName().equals("") == false) {
			returnedUser.setLastName(updateUserEntity.getLastName());
		}
		if (updateUserEntity.getEmail() != null && updateUserEntity.getEmail().equals("") == false) {
			returnedUser.setEmail(updateUserEntity.getEmail());
			this.checkOrCreateEmailInHash(returnedUser);
		}
		// update user in redis datastore
		this.userDataAccess.update(returnedUser);

		logger.logInfo("UserService", "updateAUser", "UpdateANotLoggedInUser",
				"adding user id into Redis Set for user update. UserId is : " + returnedUser.getUserId());
		// update user in MySQLdatabase
		if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
			userDataAccess.addInRedisSet(returnedUser);
		}

		return returnedUser;
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
			ExternalFacingReturnedUser user = this
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
	 * This method is used to prepare score entity to be able to be saved for
	 * compatibility for assessments score addition to them
	 * 
	 * @param scoreEntity
	 *            the score Entity
	 * @param userId
	 *            the userId of user
	 * @return the prepared score entity
	 */
	private ScoreEntity prepareScoreEntityForScoreUpdate(ScoreEntity scoreEntity, String userId) {
		scoreEntity = redisAssessment.getTotalScore(userId);

		if (scoreEntity == null) {
			scoreEntity = new ScoreEntity();
			scoreEntity.setUserId(userId);
		}

		// check if any used values to update score are already null
		// then make to "0"
		makeNullValuesToZero(scoreEntity);

		return scoreEntity;
	}

	// This method is used to make the null values to "0"
	private void makeNullValuesToZero(ScoreEntity scoreEntity) {
		if (scoreEntity.getObtainedScore() == null || scoreEntity.getObtainedScore().isEmpty()) {
			scoreEntity.setObtainedScore("0");
		}
		if (scoreEntity.getReferScore() == null || scoreEntity.getObtainedScore().isEmpty()) {
			scoreEntity.setReferScore("0");
		}
		// added for handling when score entity is newly made and on saving
		// assessment we need max score to have a value that can be converted to
		// Float value
		if (scoreEntity.getMaxScore() == null || scoreEntity.getMaxScore().isEmpty()) {
			scoreEntity.setMaxScore("0");
		}
	}

	/**
	 * This method is used to update and save user total score
	 * 
	 * @param scoreEntity
	 *            the score entity that contains the updated score to be saved
	 *            and updated in User's TotalScore
	 * @param userPhoneNumber
	 *            the phone number to get user from data store to update its
	 *            total score
	 * @throws NotFoundException
	 *             thrown when user is not found
	 */
	private void updateUserTotalScore(ScoreEntity scoreEntity, ExternalFacingReturnedUser returnedUser)
			throws NotFoundException {

		// set rank in scoreentity
		scoreEntity.setRank(this.calculateRank(
				Float.parseFloat(scoreEntity.getObtainedScore()) + Float.parseFloat(scoreEntity.getReferScore())));

		// save updated score in data store
		// no relation in saving it to mysql so not making its entry in redis
		// set as same total score is is updated and saved in user
		redisAssessment.saveTotalScore(scoreEntity);
		// null or empty values have been handled before this method call
		returnedUser.setTotalScore(df.format(
				Float.parseFloat(scoreEntity.getObtainedScore()) + Float.parseFloat(scoreEntity.getReferScore())));
		// no need to check for null or empty
		returnedUser.setTotalScoreInDouble(Double.parseDouble(returnedUser.getTotalScore()));
		// set rank of user downcasting to float from double cause score not
		// gonna increase to that precision
		returnedUser.setRank(calculateRank((float) returnedUser.getTotalScoreInDouble()));

	}

	/**
	 * This method set the rank of the user.
	 * 
	 * @param score
	 *            This is the current user score.
	 * @return The rank of the user.
	 */
	private String calculateRank(Float score) {
		String rank = "";
		if (score > 0 && score < 500) {
			rank = configurationManager.get("Rank1");
		} else if (score >= 500 && score < 1000) {
			rank = configurationManager.get("Rank2");
		} else if (score >= 1000 && score < 5000) {
			rank = configurationManager.get("Rank3");
		} else if (score >= 5000 && score < 10000) {
			rank = configurationManager.get("Rank4");
		} else if (score >= 10000 && score < 15000) {
			rank = configurationManager.get("Rank5");
		} else if (score >= 15000 && score < 20000) {
			rank = configurationManager.get("Rank6");
		} else if (score >= 20000 && score < 25000) {
			rank = configurationManager.get("Rank7");
		} else if (score >= 25000 && score < 30000) {
			rank = configurationManager.get("Rank8");
		} else if (score >= 30000) {
			rank = configurationManager.get("Rank9");
		}
		return rank;
	}

	/**
	 * @see IUserService.getUserByExperianRequestUniqueKey
	 */
	@Override
	public ExternalFacingReturnedUser getUserByExperianRequestUniqueKey(String experianRequestUniqueKey)
			throws NotFoundException, BadRequestException {
		String userId = this.userDataAccess.getUserIdByExperianRequestUniqueKey(experianRequestUniqueKey);
		if (userId == null)
			throw new NotFoundException("User", "User with this unique key " + experianRequestUniqueKey + " not found",
					null);
		return this.get(userId, false);
	}

}