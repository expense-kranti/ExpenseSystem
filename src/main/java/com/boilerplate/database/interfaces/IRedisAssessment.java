package com.boilerplate.database.interfaces;


import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;

public interface IRedisAssessment {

	public AttemptAssessmentListEntity getAssessmentAttempt();

}
