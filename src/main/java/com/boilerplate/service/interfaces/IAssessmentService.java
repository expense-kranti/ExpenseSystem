package com.boilerplate.service.interfaces;

import com.boilerplate.java.entities.AssessmentEntity;

public interface IAssessmentService {

	/**
	 * This method is used to get the assessment data
	 * @param assessmentEntity this parameter contains the assessment id
	 * @return the assessment data
	 */
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity);
}
