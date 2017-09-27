package com.boilerplate.service.implemetations;

import org.apache.commons.lang.NotImplementedException;
import org.springframework.beans.factory.annotation.Autowired;
import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.exceptions.rest.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentQuestionSectionEntity;
import com.boilerplate.java.entities.AssessmentSectionEntity;
import com.boilerplate.java.entities.AssessmentStatus;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionEntity;
import com.boilerplate.java.entities.QuestionType;
import com.boilerplate.service.interfaces.IAssessmentService;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * This class implements the IAssessment service class
 * 
 * @author shiva
 *
 */
public class AssesmentService implements IAssessmentService {

	/**
	 * this is the new instance of assessment class of data layer
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
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity) throws BadRequestException {
		AssessmentEntity assessmentData = new AssessmentEntity();
		try {
			AttemptAssessmentListEntity attemptAssessmentList = this.getAssessmentAttempt();
			// Set attempt id
			assessmentEntity.setAttemptId(RequestThreadLocal.getSession().getUserId() + assessmentEntity.getId());
			if (this.isAssessmentExist(attemptAssessmentList, assessmentEntity)) {
				// Get assessment data from redis
				assessmentData = redisAssessment.getAssessment(assessmentEntity);
			} else {
				// Save the assessment data to redis
				this.saveAssessmentAttemptWithAppendAssessmentDetail(assessmentEntity, attemptAssessmentList);
				// Get the assessment data regarding assessment id
				assessmentData = assessment.getAssessment(assessmentEntity);
				// Set attempt id
				assessmentData.setAttemptId(RequestThreadLocal.getSession().getUserId() + assessmentData.getId());
				// Save assessment data to redis
				redisAssessment.saveAssessment(assessmentData);
			}
		} catch (NotFoundException ex) {
			// Declare new instance of attempt assessment list
			AttemptAssessmentListEntity attemptAssessment = new AttemptAssessmentListEntity();
			// Set the user id to list
			attemptAssessment.setUserId(RequestThreadLocal.getSession().getUserId());
			// Save the assessment data to redis
			this.saveAssessmentAttemptWithAppendAssessmentDetail(assessmentEntity, attemptAssessment);
			// Get the assessment data regarding assessment id
			assessmentData = assessment.getAssessment(assessmentEntity);
			// Set attempt id
			assessmentData.setAttemptId(RequestThreadLocal.getSession().getUserId() + assessmentData.getId());
			// Save assessment data to redis
			redisAssessment.saveAssessment(assessmentData);
		}
		// Get the assessment data from data base
		return assessmentData;
	}

	

	/**
	 * This method is used to check is assessment is exist in a list or not if
	 * exist return true else false
	 * 
	 * @param attemptAssessmentList
	 *            this parameter contains the list of assessment list
	 * @param assessmentEntity
	 *            this parameter define the new assessment data this method
	 *            check this assessment is already exist or not
	 * @return true if assessment exist else return false
	 */
	private boolean isAssessmentExist(AttemptAssessmentListEntity attemptAssessmentList,
			AssessmentEntity assessmentEntity) {
		// Get the assessment list
		List<AssessmentEntity> assessmentList = attemptAssessmentList.getAssessmentList();
		// Run for loop to check is exist or not
		for (AssessmentEntity assessment : assessmentList) {
			// Check is assessment id equal to or not
			if ((assessment.getId()).equals(assessmentEntity.getId())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * This method is used to save the assessment attempt data to redis with
	 * append the list of assessment
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the information about the assessment
	 * @param attemptAssessment
	 *            this parameter contains the information about the assessment
	 *            attempt by user
	 */
	private void saveAssessmentAttemptWithAppendAssessmentDetail(AssessmentEntity assessmentEntity,
			AttemptAssessmentListEntity attemptAssessment) {
		// Declare new list of assessment
		BoilerplateList<AssessmentEntity> assessmentList = new BoilerplateList<>();
		// Check is size of assessment listk
		if (attemptAssessment.getAssessmentList().size() > 0) {
			// Declare new list of assessment
			assessmentList = attemptAssessment.getAssessmentList();
		}
		// Add new assessment to list
		assessmentList.add(new AssessmentEntity(assessmentEntity.getId(), AssessmentStatus.Inprogress));
		// Set the assessment list
		attemptAssessment.setAssessmentList(assessmentList);
		// Save the assessment details
		redisAssessment.saveAssessmentAttempt(attemptAssessment);
	}

	@Override
	public List<AssessmentEntity> getAssessment() {
		List<AssessmentEntity> assessmentList = assessment.getAssessment();
		return assessmentList;
	}

	@Override
	public AttemptAssessmentListEntity getAssessmentAttempt() throws NotFoundException {
		AttemptAssessmentListEntity attemptAssessmentListEntity = redisAssessment.getAssessmentAttempt();
		if (attemptAssessmentListEntity == null) {
			throw new NotFoundException("AttemptAssessmentListEntity", "No attempt was found for this user.", null);
		}
		return attemptAssessmentListEntity;
	}

	@Override
	public void saveAssesment(AssessmentEntity assessmentEntity) {
		redisAssessment.saveAssessment(assessmentEntity);
		
	}

}
