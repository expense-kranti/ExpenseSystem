package com.boilerplate.database.interfaces;

import java.util.List;
import java.util.Map;

import com.boilerplate.java.entities.AssessmentEntity;

public interface IAssessment {

	/**
	 * This method is used to get the assessment data
	 * @param assessmentEntity this parameter contains the assessment id
	 * @return the assessment data
	 */
	public List<AssessmentEntity> getAssessment(AssessmentEntity assessmentEntity);

	public List<AssessmentEntity> getAssessment(); 
}
