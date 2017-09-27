package com.boilerplate.database.redis.implementation;


import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;

public class RedisAssessment extends BaseRedisDataAccessLayer implements IRedisAssessment {
	
	private static final String Attempt = "Attempt:";
	
	@Override
	public AttemptAssessmentListEntity getAssessmentAttempt() {
		AttemptAssessmentListEntity attemptAssessmentListEntity =  super.get(Attempt+RequestThreadLocal.getSession().getUserId()
				,AttemptAssessmentListEntity.class);
		return attemptAssessmentListEntity;
	}

}
