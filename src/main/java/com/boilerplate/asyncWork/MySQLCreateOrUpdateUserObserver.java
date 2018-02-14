package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.ScoreEntity;

/**
 * This class is used to pop or read queue and process each element
 * 
 * @author urvij
 *
 */
public class MySQLCreateOrUpdateUserObserver implements IAsyncWorkObserver {

	/**
	 * This is the instance of logger MySQLCreateOrUpdateUserObserver logger
	 */
	private static Logger logger = Logger.getInstance(MySQLCreateOrUpdateUserObserver.class);
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
	 * The autowired instance of user data access
	 */
	@Autowired
	private IUser userDataAccess;

	/**
	 * Sets the user data access
	 * 
	 * @param userDataAccess
	 *            the userDataAccess to set
	 */
	public void setUserDataAccess(IUser userDataAccess) {
		this.userDataAccess = userDataAccess;
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
	 * This method get the user from Redis data store using supplied userId and
	 * save it in MySQL Database
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {

		// get the user from Redis data store and save it in MySQL data base
		saveOrUpdateUserInMySQL((ExternalFacingUser) userDataAccess.getUser((String) asyncWorkItem.getPayload(), null));
	}

	/**
	 * This method is used to save user in MySQLdatabase
	 * 
	 * @param externalFacingUser
	 *            the user to save
	 * @throws ConflictException
	 *             thrown if the user already exists in the database for the
	 *             given provider.
	 */
	private void saveOrUpdateUserInMySQL(ExternalFacingUser externalFacingUser) throws ConflictException {
		if (externalFacingUser.getUserId().equals("AKS:ADMIN")
				|| externalFacingUser.getUserId().equals("AKS:ANNONYMOUS")
				|| externalFacingUser.getUserId().equals("AKS:BACKGROUND")
				|| externalFacingUser.getUserId().equals("AKS:ROLEASSIGNER")
				|| externalFacingUser.getPhoneNumber().length() > 10) {
			try {
				// check if total score which is in String is not null or empty
				if (externalFacingUser.getTotalScore() != null && !(externalFacingUser.getTotalScore().isEmpty()))
					externalFacingUser.setTotalScoreInDouble(Double.parseDouble(externalFacingUser.getTotalScore()));
				// check if users total refer score is 0 means user either have
				// no refer score or refer score in externalFacingUserEntity is
				// not present on second condition we will get refer score from
				// "TotalScore" key from Redis in ScoreEntity Form and assign it
				// to externalFacingUserEntity
				if (externalFacingUser.getTotalReferScore() == 0) {
					ScoreEntity scoreEntity = redisAssessment.getTotalScore(externalFacingUser.getUserId());
					if (scoreEntity != null)
						externalFacingUser.setTotalReferScore(
								scoreEntity.getReferScore() == null || scoreEntity.getReferScore().isEmpty() ? 0
										: Float.parseFloat(scoreEntity.getReferScore()));
				}
				// add user to the mysql database
				mySqlUser.create(externalFacingUser);
			} catch (Exception ex) {
				logger.logException("MySQLCreateOrUpdateUserObserver", "saveOrUpdateUserInMySQL",
						"try-catch block calling create method",
						"Exception message is : " + ex.getMessage() + " and Exception cause : " + ex.getMessage(), ex);
				// simply delete that user id from set
				userDataAccess.deleteItemFromRedisUserIdSet(externalFacingUser.getUserId());
				
				throw ex;
			}
		}
		// after getting work done by using userId delete that user id from set
		userDataAccess.deleteItemFromRedisUserIdSet(externalFacingUser.getUserId());
	}

}
