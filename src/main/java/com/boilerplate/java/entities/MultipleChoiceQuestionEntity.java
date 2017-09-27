package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class is provide the data regarding Multiple Choice Question
 * 
 * @author shiva
 *
 */
public class MultipleChoiceQuestionEntity extends BaseEntity implements Serializable ,IQuestionEntity{

	/**
	 * This is the question type text
	 */
	private String text;

	/**
	 * This is the options
	 */
	private List<MultipleChoiceQuestionOptionEntity> options;

	/**
	 * This is the question id.
	 */
	private String questionId;

	/**
	 * This method is used to get the text
	 * 
	 * @return the text
	 */
	public String getText() {
		return text;
	}

	/**
	 * This method is used to set the text
	 * 
	 * @param text
	 *            the text to set
	 */
	public void setText(String text) {
		this.text = text;
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
	 * This method is used to get the options
	 * 
	 * @return the options
	 */
	public List<MultipleChoiceQuestionOptionEntity> getOptions() {
		return options;
	}

	/**
	 * This method is used to set the options
	 * 
	 * @param options
	 *            the options to set
	 */
	public void setOptions(List<MultipleChoiceQuestionOptionEntity> options) {
		this.options = options;
	}

	/**
	 * This method is used to get the question id
	 * 
	 * @return the questionId
	 */
	public String getQuestionId() {
		return questionId;
	}

	/**
	 * This method is used to set the question id
	 * 
	 * @param questionId
	 *            the questionId to set
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}
}
