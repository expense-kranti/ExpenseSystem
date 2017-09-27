package com.boilerplate.service.implemetations;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.service.interfaces.IAssessmentService;

public class AssesmentService implements IAssessmentService {

	/**
	 * This gets the method permissions
	 */
	@Autowired
	IAssessment assessment;

	/**
	 * This method is used to set the assessment
	 * 
	 * @param assessment
	 */
	public void setAssessment(IAssessment assessment) {
		this.assessment = assessment;
	}

	@Autowired
	IRedisAssessment redisAssessment;

	/**
	 * @param redisAssessment
	 *            the redisAssessment to set
	 */
	public void setRedisAssessment(IRedisAssessment redisAssessment) {
		this.redisAssessment = redisAssessment;
	}

	/**
	 * @see IAssessmentService.getAssessment
	 */
	@Override
	public List<AssessmentEntity> getAssessment(
			AssessmentEntity assessmentEntity) {
		/**
		 * TODO get current user attemp
		 * if null create  AttemptAssessmentListEntity with current assessment and create your xml and save into redis
		 * if not null then check your assessment is exist or not
		 * if exist then return xml from redis
		 * if not exist then create your xml and return it
		 */
		List<AssessmentEntity> assessmentData = assessment
				.getAssessment(assessmentEntity);
		return assessmentData;
	}

	@Override
	public List<AssessmentEntity> getAssessment() {
		List<AssessmentEntity> assessmentList = assessment.getAssessment();
		return assessmentList;
	}

	@Override
	public AttemptAssessmentListEntity getAssessmentAttempt()
			throws NotFoundException {
		AttemptAssessmentListEntity attemptAssessmentListEntity = redisAssessment
				.getAssessmentAttempt();
		if (attemptAssessmentListEntity == null)
			throw new NotFoundException("AttemptAssessmentListEntity",
					"No attempt was found for this user.", null);
		return attemptAssessmentListEntity;
	}

}
