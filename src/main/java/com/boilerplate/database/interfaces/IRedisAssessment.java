package com.boilerplate.database.interfaces;

import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AttemptAssessmentListEntity;

/**
 * This class provide the mechanism to perform assessment related operations
 * regarding redis
 * 
 * @author shiva
 *
 */
public interface IRedisAssessment {

	/**
	 * This method is used to get the user attempted assessment details means
	 * all those assessment which is attempted by user in past
	 * 
	 * @return the user attempted assessment details means all those assessment
	 *         which was attempt by user in past
	 */
	public AttemptAssessmentListEntity getAssessmentAttempt();

	/**
	 * This method is used to save the user attempt assessment details to Redis
	 * ,attempt assessment details like user id and list of assessments
	 * 
	 * @param attemptAssessmentListEntity
	 *            this parameter contains the user attempt assessment details
	 *            which we want to save to data base details like the user id
	 *            its attempted assessment list
	 */
	public void saveAssessmentAttempt(AttemptAssessmentListEntity attemptAssessmentListEntity);

	/**
	 * This method is used to save the assessment data to Redis ,assessment data
	 * like assessment id, assessment section,assessment questions etc.
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data which we need to
	 *            save to data redis ,assessment data like assessment id,
	 *            assessment section,assessment questions etc.
	 */
	public void saveAssessment(AssessmentEntity assessmentEntity);

	/**
	 * This method is used to get the assessment data regarding the assessment
	 * id from the data store
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment details like assessment
	 *            id
	 * @return the assessment data like assessment id, assessment
	 *         section,assessment questions etc.
	 */
	public AssessmentEntity getAssessment(AssessmentEntity assessmentEntity);
}
