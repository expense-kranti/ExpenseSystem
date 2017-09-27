package com.boilerplate.database.redis.implementation;


import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;

public class RedisAssessment extends BaseRedisDataAccessLayer implements IRedisAssessment {
	
	private static final String Attempt = "Attempt:";
	
	private static final String Assessment = "Assessment:";
	
	@Override
	public AttemptAssessmentListEntity getAssessmentAttempt() {
		AttemptAssessmentListEntity attemptAssessmentListEntity =  super.get(Attempt+RequestThreadLocal.getSession().getUserId()
				,AttemptAssessmentListEntity.class);
		return attemptAssessmentListEntity;
	}

	@Override
	public void saveAssessmentAttempt(
			AttemptAssessmentListEntity attemptAssessmentListEntity) {
		super.set(Attempt+attemptAssessmentListEntity.getUserId(), attemptAssessmentListEntity);
		
	}

	@Override
	public void saveAssessment(AssessmentEntity assessmentEntity) {
		super.set(Assessment+assessmentEntity.getAttemptId(), assessmentEntity);
		
	}

	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity) {
		AssessmentEntity assessmentDataEntity =  super.get(Assessment+assessmentEntity.getAttemptId()
				,AssessmentEntity.class);
		return assessmentDataEntity;
	}

}
