package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.service.interfaces.IAssessmentService;

public class AssesmentService implements IAssessmentService {

	/**
	 * This gets the method permissions
	 */
	@Autowired
	com.boilerplate.service.interfaces.IAssessmentService assessment;

	/**
	 * @see IAssessmentService.getAssessment
	 */
	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity) {
		AssessmentEntity assessmentData = assessment.getAssessment(assessmentEntity);
		return assessmentEntity;
	}

}
