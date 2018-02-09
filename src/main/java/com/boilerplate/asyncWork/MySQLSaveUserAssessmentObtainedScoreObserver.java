package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.ScoreEntity;

/**
 * This class is used to read userId from queue and fetch user total score from
 * Redis Store to save user Total Score in MySQL
 * 
 * @author urvij
 *
 */
public class MySQLSaveUserAssessmentObtainedScoreObserver implements IAsyncWorkObserver {

	/**
	 * This is the instance of logger
	 * MySQLSaveUserAssessmentObtainedScoreObserver logger
	 */
	private static Logger logger = Logger.getInstance(MySQLSaveUserAssessmentObtainedScoreObserver.class);

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

	/**
	 * read userId from queue and fetch User's total score from Redis and save
	 * it in MySQL
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		saveAssessmentObtainedScore((String) asyncWorkItem.getPayload());
	}

	/**
	 * This method is used to save the assessment obtained score
	 * 
	 * @param data
	 *            it contains the ids to be used to save obtained score
	 * @throws Exception
	 *             thrown when exception occurs while saving assessment obtained
	 *             score
	 */
	private void saveAssessmentObtainedScore(String data) throws Exception {

		AssessmentEntity assessmentEntity = new AssessmentEntity();
		String[] ids = data.split(",");
		assessmentEntity.setUserId(ids[0]);
		assessmentEntity.setId(ids[1]);
		assessmentEntity = redisAssessment.getUserAssessment(assessmentEntity);
		// prepare score entity for saving user score in MySql
		ScoreEntity scoreEntity = new ScoreEntity();
		scoreEntity.setAssessmentId(assessmentEntity.getId());
		scoreEntity.setUserId(assessmentEntity.getUserId());
		// check if obtained score is null or empty then set obtained score to
		// "0"
		if (assessmentEntity.getObtainedScore() == null || assessmentEntity.getObtainedScore().isEmpty())
			assessmentEntity.setObtainedScore("0");
		scoreEntity.setObtainedScore(assessmentEntity.getObtainedScore());
		// set obtained score in double for saving in MySQL
		scoreEntity.setObtainedScoreInDouble(Double.parseDouble(assessmentEntity.getObtainedScore()));
		try {
			// fetch total score of user with given userId
			assessment.saveUserAssessmentScore(scoreEntity);
		} catch (Exception ex) {
			logger.logException("MySQLSaveUserAssessmentObtainedScoreObserver", "saveAssessmentObtainedScore",
					"try-catch calling saveUserAssessmentScore method", ex.getMessage(), ex);
			throw ex;
		}

		// delete the user id from Redis set after saving the total score in
		// MySQL
		redisAssessment.deleteIdFromRedisSetForAssessmentScore(data);

	}

}
