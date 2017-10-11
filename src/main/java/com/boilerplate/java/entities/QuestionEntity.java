package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * This is provide the question related information
 * 
 * @author shiva
 *
 */
public class QuestionEntity extends BaseEntity implements Serializable {

	/**
	 * This is the question type
	 */
	private QuestionTypeEntity questionType;

	// TODO need to discuss with GV
	/**
	 * This is the question text
	 */
	private MultipleChoiceQuestionEntity questionData;

	/**
	 * This is the answer of question by user
	 */
	private String answer;

	/**
	 * This is the question explanation
	 */
	@JsonIgnore
	private String explanation;

	/**
	 * This method is used to get the question data
	 * 
	 * @return the questionData
	 */
	public MultipleChoiceQuestionEntity getQuestionData() {
		return questionData;
	}

	/**
	 * This method is used to set the question text
	 * 
	 * @param questionData
	 *            the questionData to set
	 */
	public void setQuestionData(MultipleChoiceQuestionEntity questionData) {
		this.questionData = questionData;
	}

	/**
	 * This method is used to get the question type
	 * 
	 * @return the questionType
	 */
	public QuestionTypeEntity getQuestionType() {
		return questionType;
	}

	/**
	 * This method is used to set the question type
	 * 
	 * @param questionType
	 *            the questionType to set
	 */
	public void setQuestionType(QuestionTypeEntity questionType) {
		this.questionType = questionType;
	}

	/**
	 * @see BaseEntity.ValidationFailedException
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * This method is used to get the answer
	 * 
	 * @return the answer
	 */
	public String getAnswer() {
		return answer;
	}

	/**
	 * This method is used to set the answer
	 * 
	 * @param answer
	 *            the answer to set
	 */
	public void setAnswer(String answer) {
		this.answer = answer;
	}

	/**
	 * This method is used to get the explanation
	 * 
	 * @return the explanation
	 */
	public String getExplanation() {
		return explanation;
	}

	/**
	 * This method is used to set the explanation
	 * 
	 * @param explanation
	 *            the explanation to set
	 */
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
}
