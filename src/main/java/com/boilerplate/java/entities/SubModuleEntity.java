package com.boilerplate.java.entities;

import java.util.List;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This entity defines the attributes of sub modules, which are contained by
 * modules
 * 
 * @author ruchi
 *
 */
public class SubModuleEntity extends BaseEntity {

	/**
	 * This is the name of the sub module
	 */
	private String name;

	/**
	 * This is the id of the module to which this sub module belongs to
	 */
	private String moduleId;

	/**
	 * this is the list of info snippets for this sub module
	 */
	private List<InfoSnippetEntity> infoSnippetsList;

	/**
	 * This indicates whether this sub module is active or not
	 */
	private boolean isActive;

	/**
	 * This method is used to get the name
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * this method is used to set the name
	 * 
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This method is used to get the module id
	 * 
	 * @return
	 */
	public String getModuleId() {
		return moduleId;
	}

	/**
	 * This method is used to set the module id
	 * 
	 * @return
	 */
	public void setModuleId(String moduleId) {
		this.moduleId = moduleId;
	}

	/**
	 * This method is used to get the list of info snippets
	 * 
	 * @return
	 */
	public List<InfoSnippetEntity> getInfoSnippetsList() {
		return infoSnippetsList;
	}

	/**
	 * This method is used to set the list of info snippets
	 * 
	 * @param infoSnippetsList
	 */
	public void setInfoSnippetsList(List<InfoSnippetEntity> infoSnippetsList) {
		this.infoSnippetsList = infoSnippetsList;
	}

	/**
	 * This method is used to get isActive
	 * 
	 * @return
	 */
	public boolean getIsActive() {
		return isActive;
	}

	/**
	 * This method is used to set isActive
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
