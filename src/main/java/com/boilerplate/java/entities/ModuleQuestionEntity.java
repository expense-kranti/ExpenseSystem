package com.boilerplate.java.entities;

import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class defines the attributes for question entity which will be used in
 * module quiz and also for scenarios
 * 
 * @author ruchi
 *
 */
public class ModuleQuestionEntity extends BaseEntity {

	/**
	 * This is the question text
	 */
	private String questionText;

	/**
	 * This indicates whether this question is scenario
	 */
	private boolean isScenario;

	/**
	 * This is the order id
	 */
	private int orderId;

	/**
	 * This is the list of options
	 */
	private List<ModuleOptionEntity> optionList;

	/**
	 * This method is used to get question text
	 * 
	 * @return
	 */
	public String getQuestionText() {
		return questionText;
	}

	/**
	 * This method is used to set question text
	 * 
	 * @return
	 */
	public void setQuestionText(String questionText) {
		this.questionText = questionText;
	}

	/**
	 * This method is used to get is scenario
	 * 
	 * @return
	 */
	public boolean getIsScenario() {
		return isScenario;
	}

	/**
	 * This method is used to set is scenario
	 * 
	 * @return
	 */
	public void setIsScenario(boolean isScenario) {
		this.isScenario = isScenario;
	}

	/**
	 * This method is used to get order id
	 * 
	 * @return
	 */
	public int getOrderId() {
		return orderId;
	}

	/**
	 * This method is used to set order id
	 * 
	 * @param orderId
	 */
	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	/**
	 * This method is used to get option list
	 * 
	 * @return
	 */
	public List<ModuleOptionEntity> getOptionList() {
		return optionList;
	}

	/**
	 * This method is used to set option list
	 * 
	 * @param optionList
	 */
	public void setOptionList(List<ModuleOptionEntity> optionList) {
		this.optionList = optionList;
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
