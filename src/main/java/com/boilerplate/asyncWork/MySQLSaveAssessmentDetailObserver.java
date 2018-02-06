package com.boilerplate.asyncWork;

import java.util.List;
import java.util.Set;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IAssessment;
import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.framework.HibernateUtility;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.AssessmentQuestionSectionEntity;
import com.boilerplate.java.entities.AssessmentSectionEntity;
import com.boilerplate.java.entities.MultipleChoiceQuestionEntity;
import com.boilerplate.java.entities.UserAssessmentDetailEntity;

/**
 * This class is used to read the queue
 * 
 * @author urvij
 *
 */
public class MySQLSaveAssessmentDetailObserver implements IAsyncWorkObserver {

	/**
	 * This is the new instance of redis assessment
	 */
	@Autowired
	IRedisAssessment redisAssessment;

	/**
	 * This method set the redisAssessment
	 * 
	 * @param redisAssessment
	 *            the redisAssessment to set
	 */
	public void setRedisAssessment(IRedisAssessment redisAssessment) {
		this.redisAssessment = redisAssessment;
	}

	/**
	 * This is the instance of IAssessment
	 */
	IAssessment assessment;

	/**
	 * Sets the assessment
	 * 
	 * @param assessment
	 *            the assessment to set
	 */
	public void setAssessment(IAssessment assessment) {
		this.assessment = assessment;
	}

	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		// get the assessment from Redis data store and save it in MySQL data
		// base
		saveOrUpdateAssessmentInMySQL();
	}

	/**
	 * This method is used to fetch assessment and user ids from Redis Set and
	 * fetch assessments data against userId and save the assessment detail in
	 * MySQL
	 * 
	 * @throws Exception
	 *             throws exception in case of any error while saving or
	 *             updating assessment in the database
	 */
	public void saveOrUpdateAssessmentInMySQL() throws Exception {
		AssessmentEntity assessmentEntity = new AssessmentEntity();

		Set<String> elements = redisAssessment.fetchAssessmentIdsFromRedisSet();
		for (String element : elements) {

			String[] ids = element.split(",");
			// set assessment id to get the assessment from Redis data store
			assessmentEntity.setId(ids[1]);
			// get the assessment entity from Redis Store here ids[0] contains
			// userId
			assessmentEntity = redisAssessment.getAssessment(assessmentEntity, ids[0]);

			populateUserAssessmentDetailEntityAndSaveInMySQL(ids[0], assessmentEntity);
		}
	}

	/**
	 * This method is used to populate user assessment detail entity for each
	 * attempted question from assessment entity and save it in MySQL Database
	 * 
	 * @param assessmentEntity
	 *            this contains the user attempted assessment
	 * @throws Exception
	 *             throws exception in case of any error while saving or
	 *             updating assessment in the database
	 */
	private void populateUserAssessmentDetailEntityAndSaveInMySQL(String userId, AssessmentEntity assessmentEntity)
			throws Exception {
		// get all sections in attempted assessment and fetch per section
		// data
		for (AssessmentSectionEntity section : assessmentEntity.getSections()) {
			UserAssessmentDetailEntity userAssessmentDetailEntity = null;

			// get all assessment question entity and fetch per question
			// section entity
			for (AssessmentQuestionSectionEntity questionSection : section.getQuestions()) {
				userAssessmentDetailEntity = new UserAssessmentDetailEntity();
				// set user id of the user whose assessments are being saved
				userAssessmentDetailEntity.setUserId(userId);
				// set section id of current section
				userAssessmentDetailEntity.setSectionId(Integer.parseInt(section.getId()));
				userAssessmentDetailEntity.setStatus(assessmentEntity.getStatus().toString());
				userAssessmentDetailEntity.setAssessmentId(Integer.parseInt(assessmentEntity.getId()));
				if (userAssessmentDetailEntity.getStatus().toString().equals("Submit")) {
					// set total correct answer
					userAssessmentDetailEntity.setTotalCorrectAnswer(assessmentEntity.getTotalCorrectAnswer());
				}
				MultipleChoiceQuestionEntity question = questionSection.getQuestion().getQuestionData();
				// set the question id
				userAssessmentDetailEntity.setQuestionId(Integer.parseInt(question.getQuestionId()));
				// set the answerid of the answer given by user for the
				// current question
				if (questionSection.getQuestion().getAnswer() != null
						&& !(questionSection.getQuestion().getAnswer().isEmpty())) {
					userAssessmentDetailEntity.setAnswerId(Integer.parseInt(questionSection.getQuestion().getAnswer()));
					// save the user assessment detail Entity in Mysql
					assessment.saveUserAssessmentData(userAssessmentDetailEntity);
				}

			}
			// delete the id related to assessment after saving the
			// assessment in MySQL with the id present in Redis set
			redisAssessment.deleteRedisAssessmentIdFromSet(
					userAssessmentDetailEntity.getUserId() + "," + userAssessmentDetailEntity.getAssessmentId());

		}
	}

}
