package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.java.entities.UserMonthlyScoreEntity;

/**
 * This method is used to save user monthly score from Redis To MYSQL by reading
 * userId from RedisSet and getting Monthly Score
 * 
 * @author urvij
 *
 */
public class MySQLSaveUserMonthlyScoreObserver implements IAsyncWorkObserver {

	/**
	 * This is the instance of logger MySQLSaveUserMonthlyScoreObserver logger
	 */
	private static Logger logger = Logger.getInstance(MySQLSaveUserMonthlyScoreObserver.class);

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
	 * This is the instance of IAssessment
	 */
	IAssessment assessment;

	/**
	 * Sets the assessment
	 * 
	 * @param assessment
	 *            the assessment to set
	 */
	public void setAssessment(IAssessment assessment) {
		this.assessment = assessment;
	}

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {

		// extract userId,year,month from payload
		saveUserMonthlyScoreInMySQL(((String) asyncWorkItem.getPayload()));
	}

	/**
	 * This method is used to save user monthly score by extracting required ids
	 * from the data provided and getting monthly score from Rdis Store and then
	 * save it to MYSQL
	 * 
	 * @param data
	 *            the ids
	 * @throws Exception
	 *             thrown when exception occurs in saving Montlhy score in MYSQL
	 */
	public void saveUserMonthlyScoreInMySQL(String data) throws Exception {
		logger.logInfo("MySQLSaveUserMonthlyScoreObserver", "saveUserMonthlyScoreInMySQL",
				"First statement in method body", "About to process monthly data id which is : " + data);
		String[] ids = data.split(",");
		// get the user's monthly score from Redis database
		ScoreEntity scoreEntity = redisAssessment.getUserMonthlyScore(ids[0], ids[1], ids[2]);
		if (scoreEntity != null) {
			// create user monthly score entity and populate it with saved
			// monthly score got from Redis
			UserMonthlyScoreEntity userMonthlyScore = new UserMonthlyScoreEntity();
			userMonthlyScore.setUserId(scoreEntity.getUserId());
			userMonthlyScore.setYear(ids[1]);
			userMonthlyScore.setMonth(ids[2]);

			if (scoreEntity.getReferScore() != null && !(scoreEntity.getReferScore().isEmpty())) {
				userMonthlyScore.setMonthlyReferScore(Float.valueOf(scoreEntity.getReferScore()));
			}
			if (scoreEntity.getObtainedScore() != null && !(scoreEntity.getObtainedScore().isEmpty())) {
				userMonthlyScore.setMonthlyObtainedScore(Float.valueOf(scoreEntity.getObtainedScore()));
			}
			// userMonthlyScore.setMonthlyObtainedScore(scoreEntity.getObtainedScoreInDouble());
			// userMonthlyScore.setMonthlyReferScore(scoreEntity.getReferScoreInDouble());
			userMonthlyScore.setMonthlyRank(scoreEntity.getRank());
			try {
				// save monthly score in MySQL
				assessment.saveUserMonthlyScore(userMonthlyScore);
			} catch (Exception ex) {
				logger.logException("MySQLSaveUserMonthlyScoreObserver", "saveUserMonthlyScoreInMySQL",
						"try-catch block calling saveUserMonthlyScore method",
						"Key from Redis Set for which exception occured : " + data + "Exception Message : "
								+ ex.getMessage(),
						ex);
				throw ex;
			}

		}
		logger.logInfo("MySQLSaveUserMonthlyScoreObserver", "saveUserMonthlyScoreInMySQL", "Outside the if statement ",
				"About to delete the monthly score id after being saved in MySQL, id is : " + data);
		// delete entry from Redis Set
		redisAssessment.deleteIdFromRedisSetForAssessmentMonthlyScore(data);
	}

}
