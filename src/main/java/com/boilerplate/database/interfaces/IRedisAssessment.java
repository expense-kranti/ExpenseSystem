package com.boilerplate.database.interfaces;


import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;

public interface IRedisAssessment {

	public AttemptAssessmentListEntity getAssessmentAttempt();
	
	public void saveAssessmentAttempt(AttemptAssessmentListEntity attemptAssessmentListEntity);

	public void saveAssessment(AssessmentEntity assessmentEntity);
	
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity);
}
