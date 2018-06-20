package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This entity maintains the Q&A for the report.
 * 
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value = "The question/answer asked for a user", description = "The question/answer asked for a user")
public class ExperianQuestionAnswer extends BaseEntity implements Serializable {

	/**
	 * This is the id of the question
	 */
	@ApiModelProperty(value = "This is the id of the question")
	private String questionId;

	/**
	 * This is the text of the question
	 */
	@ApiModelProperty(value = "This is the text of the question")
	private String questionText;

	/**
	 * This is the list of answers from option set 1
	 */
	@ApiModelProperty(value = "This is the list of option set 1 questions")
	private BoilerplateList<String> optionSet1 = new BoilerplateList<>();

	/**
	 * This is the list of answers from option set 2
	 */
	@ApiModelProperty(value = "This is the list of option set 2 questions")
	private BoilerplateList<String> optionSet2 = new BoilerplateList<>();

	/**
	 * This is the answer for option set 1
	 */
	@ApiModelProperty(value = "This is the option 1 answer")
	private String optionSet1Answer;

	/**
	 * This is the list of answers for option set 2
	 */
	@ApiModelProperty(value = "This is the option 2 answer")
	private String optionSet2Answer;

	/**
	 * This gets the question id
	 * 
	 * @return The question id
	 */
	public String getQuestionId() {
		return questionId;
	}

	/**
	 * This method sets the question id
	 * 
	 * @param questionId
	 *            The question id
	 */
	public void setQuestionId(String questionId) {
		this.questionId = questionId;
	}

	/**
	 * This gets the question text
	 * 
	 * @return The question text
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * This sets the question text
	 * 
	 * @param questionText
	 *            The question text
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/**
	 * Gets the option set 1
	 * 
	 * @return option set 1
	 */
	public BoilerplateList<String> getOptionSet1() {
		return optionSet1;
	}

	/**
	 * Sets the option set 1
	 * 
	 * @param optionSet1
	 *            The option set 1
	 */
	public void setOptionSet1(BoilerplateList<String> optionSet1) {
		this.optionSet1 = optionSet1;
	}

	/**
	 * Gets option set 2
	 * 
	 * @return The option set 2
	 */
	public BoilerplateList<String> getOptionSet2() {
		return optionSet2;
	}

	/**
	 * Sets the option set 2
	 * 
	 * @param optionSet2
	 *            The option set 2
	 */
	public void setOptionSet2(BoilerplateList<String> optionSet2) {
		this.optionSet2 = optionSet2;
	}

	/**
	 * Gets the option set 1 answer
	 * 
	 * @return The option set 1 answer
	 */
	public String getOptionSet1Answer() {
		return optionSet1Answer;
	}

	/**
	 * Sets the option set 1 answer
	 * 
	 * @param optionSet1Answer
	 *            The option set 1 answer
	 */
	public void setOptionSet1Answer(String optionSet1Answer) {
		this.optionSet1Answer = optionSet1Answer;
	}

	/**
	 * Gets the option set 2 answer
	 * 
	 * @return The option set 2 answer
	 */
	public String getOptionSet2Answer() {
		return optionSet2Answer;
	}

	/**
	 * Sets the option set 2 answer
	 * 
	 * @param optionSet2Answer
	 *            The option set 2 answer
	 */
	public void setOptionSet2Answer(String optionSet2Answer) {
		this.optionSet2Answer = optionSet2Answer;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public BaseEntity transformToInternal() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BaseEntity transformToExternal() {
		// TODO Auto-generated method stub
		return null;
	}

}
