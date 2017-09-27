package com.boilerplate.service.implemetations;

import org.springframework.beans.factory.annotation.Autowired;
import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.exceptions.rest.BadRequestException;

import java.util.ArrayList;
import java.util.List;
import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentStatus;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.service.interfaces.IAssessmentService;

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
	 * @throws BadRequestException
	 * @see IAssessmentService.getAssessment
	 */
	@Override
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity) throws BadRequestException {
		/**
		 * TODO get current user attemp if null create
		 * AttemptAssessmentListEntity with current assessment and create your
		 * xml and save into redis if not null then check your assessment is
		 * exist or not if exist then return xml from redis if not exist then
		 * create your xml and return it
		 */

		try {
			AttemptAssessmentListEntity attemptAssessmentList = this.getAssessmentAttempt();

			if (attemptAssessmentList.getAttemptAssessmentList().stream()
					.anyMatch(assessment -> ((BaseEntity) assessment).getId() == assessmentEntity.getId())) {
				// TODO get assessment xml from redis
			} else {
				// TODO save the assessment xml to data base
				// Get the assessment data regarding assessment id
				AssessmentEntity newAssessment = assessment.getAssessment(assessmentEntity);
				this.saveAssessmentAttempt(assessmentEntity, attemptAssessmentList);
			}

		} catch (NotFoundException ex) {
			// Declare new instance of attempt assessment list
			AttemptAssessmentListEntity attemptAssessment = new AttemptAssessmentListEntity();
			// Set the user id to list
			attemptAssessment.setUserId(RequestThreadLocal.getSession().getUserId());
			this.saveAssessmentAttempt(assessmentEntity, attemptAssessment);
		}
		// Get the assessment data from data base
		return assessment.getAssessment(assessmentEntity);
	}

	/**
	 * This method is used to save the assessment attempt data to redis
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the information about the assessment
	 * @param attemptAssessment
	 *            this parameter contains the information about the assessment
	 *            attempt by user
	 */
	private void saveAssessmentAttempt(AssessmentEntity assessmentEntity,
			AttemptAssessmentListEntity attemptAssessment) {
		// Declare new list of assessment
		BoilerplateList<AssessmentEntity> assessmentList = new BoilerplateList<>();
		// Add new assessment to list
		assessmentList.add(new AssessmentEntity(assessmentEntity.getId(), 
								AssessmentStatus.Inprogress));
		// Set the assessemnt list to attemp assessment list entity
		attemptAssessment.setAttemptAssessmentList(assessmentList);
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
