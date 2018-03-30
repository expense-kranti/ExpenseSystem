package com.boilerplate.service.implemetations;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IRedisScript;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.ExternalFacingReturnedUser;
import com.boilerplate.java.entities.PublishEntity;
import com.boilerplate.java.entities.Role;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.service.interfaces.IScriptsService;
import com.boilerplate.service.interfaces.IUserService;
import com.opencsv.CSVReader;

public class ScriptService implements IScriptsService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(ScriptService.class);

	/**
	 * These variables are used in checking the operation type to be done on
	 * TotalScore with given score points
	 */
	public static final String SUBTRACT = "SUBTRACT";

	public static final String ADD = "ADD";

	// format of double values in two places decimal
	DecimalFormat df = new DecimalFormat("#.##");

	/**
	 * This is the instance of IRedisScript
	 */
	IRedisScript scriptDataAccess;

	/**
	 * Sets the scriptDataAccess
	 * 
	 * @param scriptDataAccess
	 *            the scriptDataAccess to set
	 */
	public void setScriptDataAccess(IRedisScript scriptDataAccess) {
		this.scriptDataAccess = scriptDataAccess;
	}

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
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 *            The configuration manager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the instance of S3File Entity
	 */
	@Autowired
	com.boilerplate.databases.s3FileSystem.implementations.S3File file;

	/**
	 * This method sets the instance of S3File Entity
	 * 
	 * @param file
	 *            The file
	 */
	public void setFile(com.boilerplate.databases.s3FileSystem.implementations.S3File file) {
		this.file = file;
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
	 * This is the fetch user and user related data key from Redis Database
	 */
	BoilerplateList<String> subjectsForFetchingUserAndUserRelatedKey = new BoilerplateList<>();

	/**
	 * Subject for publishing to crm
	 */
	private BoilerplateList<String> subjects = null;

	/**
	 * Initializes the bean
	 */
	public void initialize() {
		subjectsForPublishUserReportData.add("PublishUserData");
		subjectsForSetUserChangePasswordStatus.add("SetUserChangePasswordStatus");
		subjectsForAKSOrReferReportPublish.add("PublishUserAKSOrReferReport");
		subjectsForFetchingUserAndUserRelatedKey.add("AddUserAndRelatedDataKeysToRedisSet");
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
			logger.logError("ScriptService", "publishUserAndAssessmentReport", "Inside try-catch block", ex.toString());
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
		try {
			queueReaderJob.requestBackroundWorkItem("", subjectsForAKSOrReferReportPublish, "ScriptService",
					"publishUserAKSOrReferReport");
		} catch (Exception ex) {
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

	/**
	 * @see IScriptsService.fetchScorePointsFromFileAndUpdateUserTotalScore
	 */
	@Override
	public void fetchScorePointsFromFileAndUpdateUserTotalScore(String fileId)
			throws UnauthorizedException, NotFoundException, BadRequestException, IOException {
		// checks for user role if not admin then throw unauthorized exception
		if (!checkIsAdmin()) {
			throw new UnauthorizedException("User",
					"Currently logged in User is not allowed to change User's score points from CSV", null);
		}
		String csvFileName = null;
		String[] row = null;
		File file;
		// Get file from local if not found then downloads
		if (!(new File(configurationManager.get("RootFileDownloadLocation"), fileId)).exists()) {
			csvFileName = this.file.downloadFileFromS3ToLocal(configurationManager.get("S3_Files_Path") + fileId);
		} else {
			csvFileName = fileId;
		}
		// Reading CSv File and setting vouchersList from CSV Data
		CSVReader csvReader = new CSVReader(
				new FileReader(configurationManager.get("RootFileDownloadLocation") + csvFileName));

		String[] headerLine = csvReader.readNext();
		while ((row = csvReader.readNext()) != null) {

			ScoreEntity scoreEntity = redisAssessment.getTotalScore(userService.normalizeUserId(row[0]));

			if (row[2].equals(SUBTRACT) && scoreEntity != null) {
				// check if any used values to update score are already null
				// then make to "0"
				makeNullValuesToZero(scoreEntity);
				// update the TotalScore key entry for given userId
				// here row[1] is expected to be the score points to subtract
				scoreEntity.setObtainedScore(
						df.format(Float.parseFloat(scoreEntity.getObtainedScore()) - Float.parseFloat(row[1])));
				// no need to check for null and empty
				scoreEntity.setObtainedScoreInDouble(Double.parseDouble(scoreEntity.getObtainedScore()));
				// update and save user total score
				updateAndSaveUserTotalScore(scoreEntity, row[0]);

			} else if (row[2].equals(ADD) && scoreEntity != null) {
				// check if any used values to update score are already null
				// then make to "0"
				makeNullValuesToZero(scoreEntity);
				// update the TotalScore key entry for given userId
				// here row[1] is expected to be the score points to add
				scoreEntity.setObtainedScore(
						df.format(Float.parseFloat(scoreEntity.getObtainedScore()) + Float.parseFloat(row[1])));
				// no need to check for null and empty
				scoreEntity.setObtainedScoreInDouble(Double.parseDouble(scoreEntity.getObtainedScore()));
				// update and save user total score
				updateAndSaveUserTotalScore(scoreEntity, row[0]);
			}
		}

	}
	
	@Override
	public void increasePoint(String userId) throws NotFoundException, BadRequestException{
		ExternalFacingReturnedUser exUser = userService.get(userId);
		if(exUser.isIncreaseScore()==false){
			ScoreEntity scoreEntity = redisAssessment.getTotalScore(userService.normalizeUserId(userId));		
			// check if any used values to update score are already null
			// then make to "0"
			makeNullValuesToZero(scoreEntity);
			// update the TotalScore key entry for given userId
			// here row[1] is expected to be the score points to add
			scoreEntity.setObtainedScore(
					df.format(Float.parseFloat(scoreEntity.getObtainedScore()) + 300));
			// no need to check for null and empty
			scoreEntity.setObtainedScoreInDouble(Double.parseDouble(scoreEntity.getObtainedScore()));
			// update and save user total score
			updateAndSaveUserTotalScore(scoreEntity, userId);
		}
		
		
	}

	/**
	 * @throws IOException
	 * @see IScriptsService.publi
	 */
	@Override
	public void publishUserAndUserRelatedDataToMySQL(String fileId)
			throws UnauthorizedException, NotFoundException, BadRequestException, IOException {
		if (!checkIsAdmin()) {
			throw new UnauthorizedException("User", "User is not authorized to perform this action.", null);
		}

		try {
			// Call background job for fetching all keys from Redis database for
			// migrating Saved User and User data to MySQL
			queueReaderJob.requestBackroundWorkItem(fileId, subjectsForFetchingUserAndUserRelatedKey, "ScriptService",
					"publishUserAndUserRelatedDataToMySQL");
		} catch (Exception ex) {
			logger.logError("ScriptService", "publishUserAndUserRelatedDataToMySQL", "Inside try-catch block",
					ex.toString());
		}

	}

	// This method is used to make the null values to "0"
	private void makeNullValuesToZero(ScoreEntity scoreEntity) {
		if (scoreEntity.getObtainedScore() == null || scoreEntity.getObtainedScore().isEmpty()) {
			scoreEntity.setObtainedScore("0");
		}
		if (scoreEntity.getReferScore() == null || scoreEntity.getObtainedScore().isEmpty()) {
			scoreEntity.setReferScore("0");
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
	private void updateAndSaveUserTotalScore(ScoreEntity scoreEntity, String userPhoneNumber) throws NotFoundException {
		// save updated score in data store
		// no relation in saving it to mysql so not making its entry in redis
		// set as same total score is is updated and saved in user
		redisAssessment.saveTotalScore(scoreEntity);

		// update the Total Score in saved User in data store
		ExternalFacingReturnedUser savedUser = userDataAccess.getUser(userService.normalizeUserId(userPhoneNumber),
				null);
		savedUser.setTotalScore(df.format(
				Float.parseFloat(scoreEntity.getObtainedScore()) + Float.parseFloat(scoreEntity.getReferScore())));
		//no need to check for null or empty
		savedUser.setTotalScoreInDouble(Double.parseDouble(savedUser.getTotalScore()));
		
		savedUser.setIncreaseScore(true);
		// save the user with updated score
		userDataAccess.update(savedUser);

	}

}
