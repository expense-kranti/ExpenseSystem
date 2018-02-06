package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.database.interfaces.IRedisAssessment;
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
		String[] ids = data.split(",");
		ScoreEntity scoreEntity = redisAssessment.getUserMonthlyScore(ids[0], ids[1], ids[2]);
		// create user monthly score entity and populate it to be saved in MySQL
		UserMonthlyScoreEntity userMonthlyScore = new UserMonthlyScoreEntity();
		userMonthlyScore.setUserId(scoreEntity.getUserId());
		userMonthlyScore.setYear(ids[1]);
		userMonthlyScore.setMonth(ids[2]);
		userMonthlyScore.setMonthlyObtainedScore(scoreEntity.getObtainedScore());
		userMonthlyScore.setMonthlyReferScore(scoreEntity.getReferScore());
		userMonthlyScore.setMonthlyRank(scoreEntity.getRank());

		assessment.saveUserMonthlyScore(userMonthlyScore);
		//delete entry from Redis Set
		redisAssessment.deleteIdFromRedisSetForAssessmentMonthlyScore(data);
	}

}
