package com.boilerplate.database.redis.implementation;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;

/**
 * This class implements the IRedisAssessment
 * 
 * @author shiva
 *
 */
public class RedisAssessment extends BaseRedisDataAccessLayer implements IRedisAssessment {

	/**
	 * This variable is used to a prefix for key of user attempt details
	 */
	private static final String Attempt = "Attempt:";

	/**
	 * This variable is used to a prefix for key of user assessment details
	 */
	private static final String Assessment = "Assessment:";

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
		super.set(Assessment + assessmentEntity.getAttemptId(), assessmentEntity.clone());

	}

	/**
	 * @see IRedisAssessment.getAssessment
	 */
	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity) {
		AssessmentEntity assessmentDataEntity = super.get(Assessment + assessmentEntity.getAttemptId(),
				AssessmentEntity.class);
		return assessmentDataEntity;
	}

}
