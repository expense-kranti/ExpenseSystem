package com.boilerplate.asyncWork;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IRedisAssessment;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.AssessmentEntity;
import com.boilerplate.java.entities.ScoreEntity;

/**
 * This class calculate user total score and update the details into data store
 * 
 * @author shiva
 *
 */
public class CalculateTotalScoreObserver implements IAsyncWorkObserver {

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
	 * @see IAsyncWorkObserver.observe
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		AssessmentEntity assessmentEntity = (AssessmentEntity) asyncWorkItem.getPayload();
		this.calculateTotalScore(assessmentEntity);
	}

	/**
	 * This method is used to calculate the user total score
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data ,assessment data
	 *            like assessment id, assessment section,assessment
	 *            questions,assessment score,obtained score etc.
	 */
	public void calculateTotalScore(AssessmentEntity assessmentEntity) {
		// Get total score
		ScoreEntity scoreEntity = redisAssessment.getTotalScore(assessmentEntity.getUserId());
		// If score is not null
		if (scoreEntity != null) {
			this.updateTotalScore(scoreEntity, assessmentEntity);
		} else {
			this.createNewTotalScore(assessmentEntity);
		}
	}

	/**
	 * This method is used to update the user total score
	 * 
	 * @param scoreEntity
	 *            this parameter contains the user total score
	 * @param assessmentEntity
	 *            this parameter contains the assessment data ,assessment data
	 *            like assessment id, assessment section,assessment
	 *            questions,assessment score,obtained score etc.
	 */
	private void updateTotalScore(ScoreEntity scoreEntity, AssessmentEntity assessmentEntity) {
		// Calculate max score
		Float maxScore = Float.valueOf(scoreEntity.getMaxScore()) + Float.valueOf(assessmentEntity.getMaxScore());
		// Calculate total score
		Float obtainedScore = Float.valueOf(scoreEntity.getObtainedScore())
				+ Float.valueOf(assessmentEntity.getObtainedScore());
		// Set max score
		scoreEntity.setMaxScore(String.valueOf(maxScore));
		// Set the obtained score
		scoreEntity.setObtainedScore(String.valueOf(obtainedScore));
		// set rank
		scoreEntity.setRank(calculateRank(obtainedScore));
		// Save total score
		redisAssessment.saveTotalScore(scoreEntity);
	}

	/**
	 * This method is used to create a new user total score detail
	 * 
	 * @param assessmentEntity
	 *            this parameter contains the assessment data ,assessment data
	 *            like assessment id, assessment section,assessment
	 *            questions,assessment score,obtained score etc.
	 */
	private void createNewTotalScore(AssessmentEntity assessmentEntity) {
		// New instance of score entity
		ScoreEntity scoreEntity = new ScoreEntity();
		// Set max score
		scoreEntity.setMaxScore(assessmentEntity.getMaxScore());
		// Set the obtained score
		scoreEntity.setObtainedScore(assessmentEntity.getObtainedScore());
		// Set user id
		scoreEntity.setUserId(assessmentEntity.getUserId());
		// set rank
		scoreEntity.setRank(calculateRank(Float.parseFloat(assessmentEntity.getObtainedScore())));
		// Save total score
		redisAssessment.saveTotalScore(scoreEntity);
	}
	/**
	 * This method set the rank of the user.
	 * @param score This is the current user score.
	 * @return The rank of the user.
	 */
	private String calculateRank(Float score){
		String rank = "";
		if(score>0 && score<50){
			rank = "First Steps";
		}else if(score>=50 && score<100){
			rank = "Stepping Up";
		}else if(score>=100 && score<500){
			rank = "Walker";
		}else if(score>=500 && score<1000){
			rank = "Jogger";
		}else if(score>=1000 && score<1500){
			rank = "Runner";
		}else if(score>=1500 && score<2000){
			rank = "Sprinter";
		}else if(score>=2000 && score<2500){
			rank = "Pace Setter";
		}else if(score>=2500 && score<3000){
			rank = "Cross Country Racer";
		}else if(score>=3000){
			rank = "Marathon Racer";
		}
		return rank;
	}
}