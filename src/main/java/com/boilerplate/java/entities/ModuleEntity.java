package com.boilerplate.java.entities;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * This class defines the module entity for lessons
 * 
 * @author ruchi
 *
 */
public class ModuleEntity extends BaseEntity {

	/**
	 * This is the name of module
	 */
	private String name;

	/**
	 * This is the category for the module, will be used for displaying module
	 * as per user knowledge level
	 */
	private CategoryTagType category;

	/**
	 * This is the summary for module
	 */
	private String summary;

	/**
	 * This enum tells whether the module ends with quiz or scenario
	 */
	private ModuleEndsWithType endsWith;

	/**
	 * This is the id of the quiz/scenario that will follow after this module
	 */
	private String endingItemId;

	/**
	 * This indicates whether module is deleted or not
	 */
	private boolean isActive;

	/**
	 * This method is used to get the name of the module
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	/**
	 * This method is used to set the module name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * This method is used to get the category
	 * 
	 * @return
	 */
	public CategoryTagType getCategory() {
		return category;
	}

	/**
	 * This method is used to set the category
	 */
	public void setCategory(CategoryTagType category) {
		this.category = category;
	}

	/**
	 * This method is used to get the summary
	 * 
	 * @return
	 */
	public String getSummary() {
		return summary;
	}

	/**
	 * This method is used to set the summary
	 */
	public void setSummary(String summary) {
		this.summary = summary;
	}

	/**
	 * This method is used to get the ends with
	 * 
	 * @return
	 */
	public ModuleEndsWithType getEndsWith() {
		return endsWith;
	}

	/**
	 * This method is used to set ends with
	 */
	public void setEndsWith(ModuleEndsWithType endsWith) {
		this.endsWith = endsWith;
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
	 */
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}

	/**
	 * this method is used to get the quiz/scenario id
	 * 
	 * @return
	 */
	public String getEndingItemId() {
		return endingItemId;
	}

	/**
	 * this method is used to set the quiz/scenario id
	 */
	public void setEndingItemId(String endingItemId) {
		this.endingItemId = endingItemId;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		// Check if name and category are not null
		if (BaseEntity.isNullOrEmpty(this.getName()))
			throw new ValidationFailedException("ModuleEntity", "Module name is null or empty", null);
		// check if module has value for ends with then ends with id should not
		// be null
		if (this.endsWith != null && this.endingItemId == null)
			throw new ValidationFailedException("ModuleEntity",
					"Module should be followed by a scenario/question if ends with is checked", null);
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
