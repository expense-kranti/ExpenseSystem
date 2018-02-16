package com.boilerplate.database.redis.implementation;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.database.interfaces.IUser;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.ScoreEntity;
import com.boilerplate.java.entities.UserAssessmentDetailEntity;

/**
 * This class implements the IRedisAssessment
 * 
 * @author shiva
 *
 */
public class RedisAssessment extends BaseRedisDataAccessLayer implements IRedisAssessment {

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This variable is used to a prefix for key of user attempt details
	 */
	private static final String Attempt = "Attempt:";

	/**
	 * This is the main key of user in redis data base
	 */
	private static final String User = "USER:AKS";

	/**
	 * This variable is used to a prefix for key of user assessment details
	 */
	private static final String Assessment = "Assessment:";

	private static final String TotalScore = "TotalScore:";
	private static final String AssessmentScore = "AssessmentScore:";

	/**
	 * This variable is used to a prefix for key of user assessment details
	 */
	private static final String MonthlyScore = "MonthlyScore:";

	/**
	 * This is the variable for representing key to be used in Redis Set
	 */
	private static final String UserAssessmentKeyForSet = "AKS_USER_ASSESSMENT_MYSQL";

	private static final String UserAssessmentScoreKeyForSet = "AKS_USER_ASSESSMENT_SCORE_MYSQL";

	private static final String UserMonthlyScoreKeyForSet = "AKS_USER_MONTHLY_SCORE_MYSQL";

	/**
	 * @see IRedisAssessment.getAssessmentAttempt
	 */
	@Override
	public AttemptAssessmentListEntity getAssessmentAttempt() {
		AttemptAssessmentListEntity attemptAssessmentListEntity = super.get(
				Attempt + RequestThreadLocal.getSession().getUserId(), AttemptAssessmentListEntity.class);
		return attemptAssessmentListEntity;
	}

	/**
	 * @see IRedisAssessment.saveAssessmentAttempt
	 */
	@Override
	public void saveAssessmentAttempt(AttemptAssessmentListEntity attemptAssessmentListEntity) {
		super.set(Attempt + attemptAssessmentListEntity.getUserId(), attemptAssessmentListEntity);

	}

	/**
	 * @see IRedisAssessment.saveAssessment
	 */
	@Override
	public void saveAssessment(AssessmentEntity assessmentEntity) {
		super.set(Assessment + RequestThreadLocal.getSession().getUserId() + ":" + assessmentEntity.getId(),
				assessmentEntity.clone());

	}

	/**
	 * @see IRedisAssessment.getAssessment
	 */
	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity) {
		AssessmentEntity assessmentDataEntity = super.get(
				Assessment + RequestThreadLocal.getSession().getUserId() + ":" + assessmentEntity.getId(),
				AssessmentEntity.class);
		return assessmentDataEntity;
	}

	/**
	 * @see IRedisAssessment.getAssessment
	 */
	@Override
	public AssessmentEntity getUserAssessment(AssessmentEntity assessmentEntity) {
		AssessmentEntity assessmentDataEntity = super.get(
				Assessment + assessmentEntity.getUserId() + ":" + assessmentEntity.getId(), AssessmentEntity.class);
		return assessmentDataEntity;
	}

	/**
	 * @see IRedisAssessment.saveTotalScore
	 */
	@Override
	public void saveTotalScore(ScoreEntity scoreEntity) {
		super.set(TotalScore + scoreEntity.getUserId(), scoreEntity);
	}

	/**
	 * @see IRedisAssessment.getTotalScore
	 */
	@Override
	public ScoreEntity getTotalScore(String userId) {
		ScoreEntity scoreEntity = super.get(TotalScore + userId, ScoreEntity.class);
		return scoreEntity;
	}

	/**
	 * @see IRedisAssessment.saveAssessmentScore
	 */
	@Override
	public void saveAssessmentScore(ScoreEntity scoreEntity) {
		super.set(AssessmentScore + scoreEntity.getUserId() + ":" + scoreEntity.getAssessmentId(), scoreEntity);
	}

	/**
	 * @see IRedisAssessment.getAssessmentScore
	 */
	@Override
	public ScoreEntity getAssessmentScore(String userId, String assessmentId) {
		ScoreEntity scoreEntity = super.get(AssessmentScore + userId + ":" + assessmentId, ScoreEntity.class);
		return scoreEntity;
	}

	/**
	 * @see IRedisAssessment.saveMonthlyScore
	 */
	@Override
	public void saveMonthlyScore(ScoreEntity scoreEntity) {
		super.set(MonthlyScore + (LocalDateTime.now().getYear()) + ":" + (LocalDateTime.now().getMonth()) + ":"
				+ scoreEntity.getUserId(), scoreEntity);
	}

	/**
	 * @see IRedisAssessment.getMonthlyScore
	 */
	@Override
	public ScoreEntity getMonthlyScore(String userId) {
		ScoreEntity scoreEntity = super.get(
				MonthlyScore + (LocalDateTime.now().getYear()) + ":" + (LocalDateTime.now().getMonth()) + ":" + userId,
				ScoreEntity.class);
		return scoreEntity;
	}

	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity, String userId) {
		AssessmentEntity assessmentDataEntity = super.get(Assessment + userId + ":" + assessmentEntity.getId(),
				AssessmentEntity.class);
		return assessmentDataEntity;
	}

	/**
	 * @see IRedisAssessment.getAssessmentAttempt
	 */
	@Override
	public AttemptAssessmentListEntity getAssessmentAttempt(String userId) {
		AttemptAssessmentListEntity attemptAssessmentListEntity = super.get(Attempt + userId,
				AttemptAssessmentListEntity.class);
		return attemptAssessmentListEntity;
	}

	/**
	 * @see IRedisAssessment.getTopScorrer
	 */
	@Override
	public List getTopScorrer() {
		// Declare new list to hold top scorer
		BoilerplateList<ScoreEntity> topScorer = new BoilerplateList<>();
		// Get all user score
		Set<String> listOfUserKey = super.keys(TotalScore + "AKS*");

		for (String userKey : listOfUserKey) {
			// Get score
			ScoreEntity scoreEntity = super.get(userKey, ScoreEntity.class);
			// Check if refer score is null then set it to 0
			if (scoreEntity.getReferScore() == null) {
				scoreEntity.setReferScore(String.valueOf(0f));
			}
			// Check if obtained score is null then set it to 0
			if (scoreEntity.getObtainedScore() == null) {
				scoreEntity.setObtainedScore(String.valueOf(0f));
			}
			// Add to list
			topScorer.add(scoreEntity);
		}
		// Sort the list
		Collections.sort(topScorer, ScoreEntity.COMPARATOR.reversed());
		int maxSize = 0;
		if (topScorer.size() > Integer.valueOf(configurationManager.get("Top_Scorer_Size"))) {
			maxSize = Integer.valueOf(configurationManager.get("Top_Scorer_Size"));
		} else {
			maxSize = topScorer.size();
		}
		// Return top n scorer n get from configurations
		return topScorer.subList(0, maxSize);
	}

	/**
	 * @see IRedisAssessment.addInRedisSetForUserAssessment
	 */
	@Override
	public void addInRedisSetForUserAssessment(UserAssessmentDetailEntity userAssessmentDetailEntity) {
		// here , is used as separator because userId already have ":" in it so
		// we cant split on ":"
		super.sadd(UserAssessmentKeyForSet,
				userAssessmentDetailEntity.getUserId() + "," + userAssessmentDetailEntity.getAssessmentId());
	}

	/**
	 * @see IRedisAssessment.addInRedisSet
	 */
	@Override
	public void addInRedisSetForUserAssessment(String userId, String assessmentId) {
		// here , is used as separator because userId already have ":" in it so
		// we cant split on ":"
		super.sadd(UserAssessmentKeyForSet, userId + "," + assessmentId);
	}

	@Override
	public Set<String> getAllTotalScoreKeys() {
		return super.keys(TotalScore + "*");
	}

	/**
	 * @see IRedisAssessment.fetchAssessmentIdsFromRedisSet
	 */
	@Override
	public Set<String> fetchAssessmentIdsFromRedisSet() {
		return super.smembers(UserAssessmentKeyForSet);
	}

	/**
	 * @see IRedisAssessment.deleteRedisAssessmentIdFromSet
	 */
	@Override
	public void deleteRedisAssessmentIdFromSet(String assessmentId) {
		super.srem(UserAssessmentKeyForSet, assessmentId);
	}

	/**
	 * @see IRedisAssessment.addIdInRedisSetForAssessmentScore
	 */
	@Override
	public void addIdInRedisSetForAssessmentScore(String id) {
		super.sadd(UserAssessmentScoreKeyForSet, id);
	}

	/**
	 * @see IRedisAssessment.fetchAssessmentIdsFromRedisSet
	 */
	@Override
	public Set<String> fetchIdsFromAssessmentScoreRedisSet() {
		return super.smembers(UserAssessmentScoreKeyForSet);
	}

	/**
	 * @see IRedisAssessment.deleteUserIdFromRedisSetForTotalScore
	 */
	@Override
	public void deleteIdFromRedisSetForAssessmentScore(String userId) {
		super.srem(UserAssessmentScoreKeyForSet, userId);
	}

	/**
	 * @see IRedisAssessment.deleteAssessmentsData
	 */
	@Override
	public void deleteUserAssessmentsData(String userId) {
		Set<String> assessmentKeys = this.getAllAssessmentKeysForUser(userId);
		for (String key : assessmentKeys) {
			super.del(key);
		}
		// super.del(Assessment + userId);
	}

	/**
	 * @see IRedisAssessment.deleteUserAttemptsData
	 */
	@Override
	public void deleteUserAttemptsData(String userId) {
		super.del(Attempt + userId);
	}

	/**
	 * @see IRedisAssessment.deleteUserTotalScoreData
	 */
	@Override
	public void deleteUserTotalScoreData(String userId) {
		super.del(TotalScore + userId);
	}

	/**
	 * @see IRedisAssessment.deleteUserMonthlyScoreData
	 */
	@Override
	public void deleteUserMonthlyScoreData(String userId) {
		// get all the monthly score keys with given userId
		Set<String> monthlyScoreKeys = getAllMonthlyScoreKeys(userId);
		for (String key : monthlyScoreKeys) {
			super.del(key);
		}

	}

	/**
	 * @see IRedisAssessment.getAllMonthlyScoreKeys
	 */
	@Override
	public Set<String> getAllMonthlyScoreKeys(String userId) {
		return super.keys(MonthlyScore + "*" + "*" + userId);
	}

	/**
	 * @see IRedisAssessment.getAllMonthlyScoreKeys
	 */
	@Override
	public Set<String> getAllMonthlyScoreKeys() {
		return super.keys(MonthlyScore + "*" + "*" + "AKS:" + "*");
	}

	/**
	 * @see IRedisAssessment.getAllAssessmentKeysForUser
	 */
	@Override
	public Set<String> getAllAssessmentKeysForUser(String userId) {
		return super.keys(Assessment + userId + "*");
	}

	/**
	 * @see IRedisAssessment.getAllAssessmentKeys
	 */
	@Override
	public Set<String> getAllAssessmentKeys() {
		return super.keys(Assessment + "AKS:" + "*" + "*");
	}

	/**
	 * @see IRedisAssessment.addIdInRedisSetForAssessmentMonthlyScore
	 */
	@Override
	public void addIdInRedisSetForAssessmentMonthlyScore(String id) {
		super.sadd(UserMonthlyScoreKeyForSet, id);
	}

	/**
	 * @see IRedisAssessment.fetchIdsFromAssessmentMonthlyScoreRedisSet
	 */
	@Override
	public Set<String> fetchIdsFromAssessmentMonthlyScoreRedisSet() {
		return super.smembers(UserMonthlyScoreKeyForSet);
	}

	/**
	 * @see IRedisAssessment.deleteIdFromRedisSetForAssessmentMonthlyScore
	 */
	@Override
	public void deleteIdFromRedisSetForAssessmentMonthlyScore(String userId) {
		super.srem(UserMonthlyScoreKeyForSet, userId);
	}

	/**
	 * @see IRedisAssessment.getMonthlyScore
	 */
	@Override
	public ScoreEntity getUserMonthlyScore(String userId, String year, String month) {
		ScoreEntity scoreEntity = super.get(MonthlyScore + year + ":" + month + ":" + userId, ScoreEntity.class);
		return scoreEntity;
	}

}
