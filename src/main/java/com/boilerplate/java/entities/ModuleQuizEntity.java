package com.boilerplate.java.entities;

import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class defines the attributes of the quiz entity for modules
 * 
 * @author ruchi
 *
 */
public class ModuleQuizEntity extends BaseEntity {

	/**
	 * This is the id of the module to which this quiz belongs
	 */
	private String moduleId;

	/**
	 * This is the list of questions in this quiz
	 */
	private List<ModuleQuestionEntity> questionList;

	/**
	 * This indicate whether this quiz is active or not
	 */
	private boolean isActive;

	/**
	 * This method is used get the module id
	 * 
	 * @return
	 */
	public String getModuleId() {
		return moduleId;
	}

	/**
	 * This method is used to set the module id
	 * 
	 * @param moduleId
	 */
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * This method is used to get the list of questions
	 * 
	 * @return
	 */
	public List<ModuleQuestionEntity> getQuestionList() {
		return questionList;
	}

	/**
	 * This method is used to set question list
	 * 
	 * @param questionList
	 */
	public void setQuestionList(List<ModuleQuestionEntity> questionList) {
		this.questionList = questionList;
	}

	/**
	 * This method is used to get is active
	 * 
	 * @return
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * This method is used to set is active
	 * 
	 * @return
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
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
