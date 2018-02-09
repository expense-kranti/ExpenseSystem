package com.boilerplate.database.interfaces;

import java.util.List;
import java.util.Set;

import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.java.entities.UserAssessmentDetailEntity;

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

	/**
	 * This method deletes all the monthly score data of a user with given
	 * userId
	 * 
	 * @param userId
	 *            The userId of the user whose monthly score need to be deleted
	 */
	public void deleteUserMonthlyScoreData(String userId);

	/**
	 * This method gets all the monthly score key of the user with given userId
	 * 
	 * @param userId
	 *            The userId of the user whose all monthly score keys are to be
	 *            retrieved
	 * @return the Set of all monthly score keys of a user with given userId
	 */
	public Set<String> getAllMonthlyScoreKeys(String userId);

	/**
	 * This method is used to delete the total score of the user with given
	 * userId
	 * 
	 * @param userId
	 *            The userId of the user whose total score is to be deleted
	 */
	public void deleteUserTotalScoreData(String userId);

	/**
	 * This method is used to delete attempt data of the user with given userId
	 * 
	 * @param userId
	 *            The userId of the user whose attempt data is to be deleted
	 */
	public void deleteUserAttemptsData(String userId);

	/**
	 * This method is used to delete assessment data entry of the user with
	 * given userId
	 * 
	 * @param userId
	 *            The userId of the user whose assessment data is to be deleted
	 */
	public void deleteUserAssessmentsData(String userId);

	/**
	 * This method is used to used to get all the keys of assessment of the user
	 * with given usesrId
	 * 
	 * @param userId
	 *            The userId of the user whose all assessment keys are to be
	 *            retrieved
	 * @return the Set of all assessment keys of the user with given userId
	 */
	public Set<String> getAllAssessmentKeysForUser(String userId);

	/**
	 * This method is used to add the userassessmentdetailentity in Redis Set
	 * 
	 * @param userAssessmentDetailsEntity
	 *            contains the user id , assessment id to get the user attempted
	 *            assessments
	 */
	public void addInRedisSetForUserAssessment(UserAssessmentDetailEntity userAssessmentDetailEntity);

	/**
	 * This method is used to fetch all the assessment ids stored in redis set
	 * 
	 * @return the assessmentIds set
	 */
	public Set<String> fetchAssessmentIdsFromRedisSet();

	/**
	 * This method is used to delete the id related to assessment present in
	 * Redis Set
	 * 
	 * @param assessmentId
	 *            the id to delete
	 */
	public void deleteRedisAssessmentIdFromSet(String assessmentId);

	/**
	 * This method is used to add ids to the Redis Set for saving assessment
	 * score of the assessment attempted by user, to MySQL
	 * 
	 * @param id
	 *            the id to be saved in Redis Set which comprises of userId and
	 *            assessmentId
	 */
	public void addIdInRedisSetForAssessmentScore(String id);

	/**
	 * This method is used to fetch the user ids from redis set of
	 * AssessmentObtainedScore save or update
	 * 
	 * @return the set of keys
	 */
	public Set<String> fetchIdsFromAssessmentScoreRedisSet();

	/**
	 * This method is used to delete userId from totalScore Redis Set
	 * 
	 * @param id
	 *            the id to be deleted
	 */
	public void deleteIdFromRedisSetForAssessmentScore(String id);

	/**
	 * This method is used to get user assessment
	 * 
	 * @param assessmentEntity
	 *            contains userId of the user whose assessment is to get,
	 *            assessmentId of assessment to get
	 * @return the assessment details of the assessment
	 */
	public AssessmentEntity getUserAssessment(AssessmentEntity assessmentEntity);

	/**
	 * This method is used to add the userId whose monthly score is to save in
	 * MySQL
	 * 
	 * @param id
	 *            the user id
	 */
	public void addIdInRedisSetForAssessmentMonthlyScore(String id);

	/**
	 * This method is used to add ids to the Redis Set for saving assessment
	 * monthly score of the assessment attempted by user, to MySQL
	 * 
	 * @return the set of keys
	 */
	public Set<String> fetchIdsFromAssessmentMonthlyScoreRedisSet();

	/**
	 * This method is used to delete userId from MontlhyScore Redis Set
	 * 
	 * @param userId
	 *            the user id present in Redis Set that has been processed
	 */
	public void deleteIdFromRedisSetForAssessmentMonthlyScore(String userId);

	/**
	 * This method is used to get the user monthly score with user's given
	 * userId, month, and year
	 * 
	 * @param userId
	 *            is the user id of the user whose monthly score to get
	 * @param year
	 *            the year for which monthly score to get
	 * @param month
	 *            the month for which monthly score to get
	 * @return the scoreEntity
	 */
	// to be used in db migration
	// not throwing not found exception as it must be present
	public ScoreEntity getUserMonthlyScore(String userId, String year, String month);

	/**
	 * This method is overloaded method used to set in Redis Set
	 * 
	 * @param userId
	 *            the userId of the user
	 * @param assessmentId
	 *            the assessment id for assessment attempted by user
	 */
	public void addInRedisSetForUserAssessment(String userId, String assessmentId);

	/**
	 * This method is used to get all total score keys from database
	 * 
	 * @return the set of all total score keys
	 */
	public Set<String> getAllTotalScoreKeys();

	/**
	 * This method is used to get all the assessment keys
	 * 
	 * @return the set of all assessment keys
	 */
	public Set<String> getAllAssessmentKeys();

	/**
	 * This method is used to get all the monthly score keys
	 * 
	 * @return the set of all monthly keys
	 */
	public Set<String> getAllMonthlyScoreKeys();

}
