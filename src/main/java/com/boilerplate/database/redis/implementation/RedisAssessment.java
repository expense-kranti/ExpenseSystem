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
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.ScoreEntity;

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
}
