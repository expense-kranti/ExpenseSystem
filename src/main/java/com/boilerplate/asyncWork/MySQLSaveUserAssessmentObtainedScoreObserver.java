package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.database.interfaces.IRedisAssessment;
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
		AssessmentEntity assessmentEntity = new AssessmentEntity();

		String[] ids = ((String) asyncWorkItem.getPayload()).split(",");
		assessmentEntity.setUserId(ids[0]);
		assessmentEntity.setId(ids[1]);
		assessmentEntity = redisAssessment.getUserAssessment(assessmentEntity);
		// prepare score entity for saving user score in MySql
		ScoreEntity scoreEntity = new ScoreEntity();
		scoreEntity.setAssessmentId(assessmentEntity.getId());
		scoreEntity.setUserId(assessmentEntity.getUserId());
		scoreEntity.setObtainedScore(assessmentEntity.getObtainedScore());
		// fetch total score of user with given userId
		assessment.saveUserAssessmentScore(scoreEntity);
		// delete the user id from Redis set after saving the total score in
		// MySQL
		redisAssessment.deleteIdFromRedisSetForAssessmentScore((String) asyncWorkItem.getPayload());
	}

}
