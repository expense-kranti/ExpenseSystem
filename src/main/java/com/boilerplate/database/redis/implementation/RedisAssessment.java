package com.boilerplate.database.redis.implementation;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.framework.RequestThreadLocal;
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
	 * This variable is used to a prefix for key of user attempt details
	 */
	private static final String Attempt = "Attempt:";

	/**
	 * This variable is used to a prefix for key of user assessment details
	 */
	private static final String Assessment = "Assessment:";
	
	private static final String TotalScore = "TotalScore:";
	private static final String AssessmentScore = "AssessmentScore:";

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
		super.set(Assessment +RequestThreadLocal.getSession().getUserId()+":"+assessmentEntity.getId(), assessmentEntity.clone());

	}

	/**
	 * @see IRedisAssessment.getAssessment
	 */
	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity) {
		AssessmentEntity assessmentDataEntity = super.get(Assessment + RequestThreadLocal.getSession().getUserId()+":"+assessmentEntity.getId(),
				AssessmentEntity.class);
		return assessmentDataEntity;
	}
	
	@Override
	public void saveTotalScore(ScoreEntity scoreEntity){
		super.set(TotalScore +scoreEntity.getUserId() , scoreEntity);
	}
	
	@Override
	public ScoreEntity getTotalScore(String userId){
		ScoreEntity scoreEntity = super.get(TotalScore +userId , ScoreEntity.class);
		return scoreEntity;
	}
	
	@Override
	public void saveAssessmentScore(ScoreEntity scoreEntity){
		super.set(AssessmentScore +scoreEntity.getUserId()+":"+scoreEntity.getAssessmentId() , scoreEntity);
	}
	@Override
	public ScoreEntity getAssessmentScore(String userId,String assessmentId){
		ScoreEntity scoreEntity = super.get(AssessmentScore +userId+":"+ assessmentId, ScoreEntity.class);
		return scoreEntity;
	}

}