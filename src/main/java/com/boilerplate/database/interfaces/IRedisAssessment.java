package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.ScoreEntity;

/**
 * This class provide the mechanism to perform assessment related operations
 * regarding redis
 * 
 * @author shiva
 *
 */
public interface IRedisAssessment {

	/**
	 * This method is used to get the user attempted assessment details means
	 * all those assessment which is attempted by user in past
	 * 
	 * @return the user attempted assessment details means all those assessment
	 *         which was attempt by user in past
	 */
	public AttemptAssessmentListEntity getAssessmentAttempt();

	/**
	 * This method is used to save the user attempt assessment details to Redis
	 * ,attempt assessment details like user id and list of assessments
	 * 
	 * @param attemptAssessmentListEntity
	 *            this parameter contains the user attempt assessment details
	 *            which we want to save to data base details like the user id
	 *            its attempted assessment list
	 */
	public void saveAssessmentAttempt(AttemptAssessmentListEntity attemptAssessmentListEntity);

	/**
	 * This method is used to save the assessment data to Redis ,assessment data
	 * like assessment id, assessment section,assessment questions etc.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data redis ,assessment data like assessment id,
	 *            assessment section,assessment questions etc.
	 */
	public void saveAssessment(AssessmentEntity assessmentEntity);

	/**
	 * This method is used to get the assessment data regarding the assessment
	 * id from the data store
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment details like assessment
	 *            id
	 * @return the assessment data like assessment id, assessment
	 *         section,assessment questions etc.
	 */
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity);

	/**
	 * This method is used to save the total score
	 * 
	 * @param scoreEntity
	 *            this parameter contains the score entity like user id ,user
	 *            score
	 */
	public void saveTotalScore(ScoreEntity scoreEntity);

	/**
	 * This method is used to get the total score
	 * 
	 * @param userId
	 *            this parameter contains the user id , this define whose score
	 *            need to be get
	 */
	public ScoreEntity getTotalScore(String userId);

	/**
	 * This method is used to save the assessment score
	 * 
	 * @param scoreEntity
	 *            this parameter contains the score entity like user id ,user
	 *            score
	 */
	public void saveAssessmentScore(ScoreEntity scoreEntity);

	/**
	 * This method is used to get the assessment score regarding the userId and
	 * assessment id
	 * 
	 * @param userId
	 *            this is the user id
	 * @param assessmentId
	 *            this is the assessment id
	 * @return the score entity which contains the user id ,assessment id,user
	 *         obtained score and max score
	 */
	public ScoreEntity getAssessmentScore(String userId, String assessmentId);

	/**
	 * This method is used to save the monthly score of user
	 * 
	 * @param scoreEntity
	 */
	public void saveMonthlyScore(ScoreEntity scoreEntity);

	/**
	 * This method is used to get the user monthly score
	 * 
	 * @param userId
	 *            this is the user id
	 * @return scoreEntity this contains user id and user score
	 */
	public ScoreEntity getMonthlyScore(String userId);

	/**
	 * This method is used to get the assessment data regarding the assessment
	 * id from the data store
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment details like assessment
	 *            id
	 * @param userId
	 *            This is the user id of user
	 * @return the assessment data like assessment id, assessment
	 *         section,assessment questions etc.
	 */
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity, String userId);

	/**
	 * This method is used to get the user attempted assessment details means
	 * all those assessment which is attempted by user in past
	 * 
	 * @param userId
	 *            This is the user id
	 * @return the user attempted assessment details means all those assessment
	 *         which was attempt by user in past
	 */
	public AttemptAssessmentListEntity getAssessmentAttempt(String userId);

	/**
	 * This method is used to get the top scorer
	 * 
	 * @return the list of top scorer
	 */
	public List getTopScorrer();
}
