package com.boilerplate.service.implemetations;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.service.interfaces.IAssessmentService;

public class AssesmentService implements IAssessmentService {

	/**
	 * This gets the method permissions
	 */
	@Autowired
	IAssessment assessment;
	
	/**
	 * This method is used to set the assessment
	 * @param assessment
	 */
	public void setAssessment(IAssessment assessment) {
		this.assessment = assessment;
	}

	/**
	 * @see IAssessmentService.getAssessment
	 */
	@Override
	public List<AssessmentEntity> getAssessment(AssessmentEntity assessmentEntity) {
		List<AssessmentEntity> assessmentData = assessment.getAssessment(assessmentEntity);
		return assessmentData;
	}

}
