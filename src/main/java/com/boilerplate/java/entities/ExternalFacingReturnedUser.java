package com.boilerplate.java.entities;

import java.io.Serializable;
import java.util.List;

import org.mockito.ReturnValues;

import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * This is the user entity returned.
 * 
 * @author gaurav.verma.icloud
 *
 */
@ApiModel(value = "A User", description = "This is a user", parent = ExternalFacingUser.class)
public class ExternalFacingReturnedUser extends ExternalFacingUser
		implements Serializable, ICRMPublishDynamicURl, ICRMPublishEntity {

	public ExternalFacingReturnedUser() {

	}

	public ExternalFacingReturnedUser(ExternalFacingUser user) {
		super.setAuthenticationProvider(user.getAuthenticationProvider());
		super.setCreationDate(user.getCreationDate());
		super.setExternalSystemId(user.getExternalSystemId());
		super.setId(user.getId());
		super.setPassword(user.getPassword());
		super.setUpdationDate(user.getUpdationDate());
		super.setUserId(user.getUserId());
		super.setUserMetaData(user.getUserMetaData());
		super.setEmail(user.getEmail());
		super.setPhoneNumber(user.getPhoneNumber());
		super.setFirstName(user.getFirstName());
		super.setLastName(user.getLastName());
		super.setUserStatus(user.getUserStatus());
		super.setMiddleName(user.getMiddleName());
		super.setReferalSource(user.getReferalSource());
		super.setLocation(user.getLocation());
		super.setEmploymentStatus(user.getEmploymentStatus());
		super.setDateOfBirth(user.getDateOfBirth());
		super.setAlternateNumber(user.getAlternateNumber());
		super.setCrmid(user.getCrmid());
		super.setIsPasswordChanged(user.getIsPasswordChanged());

	}

	/**
	 * The roles of the user
	 */
	@ApiModelProperty(value = "This roles of the user")
	private List<Role> roles;

	/**
	 * Gets the roles of the user
	 * 
	 * @return The roles of the user
	 */
	public List<Role> getRoles() {
		return roles;
	}

	/**
	 * Sets the roles of the user
	 * 
	 * @param roles
	 *            The roles of the user
	 */
	public void setRoles(List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Gets the organization id of the user
	 * 
	 * @return The organization id of the user
	 */
	public String getOrganizationId() {
		return organizationId;
	}

	/**
	 * Sets the organization id of the user
	 * 
	 * @param organizationId
	 *            The organiation if
	 */
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}

	/**
	 * This is the organization id of the user
	 */
	@ApiModelProperty(value = "This organization id of the user")
	private String organizationId;

	/**
	 * This is the otpList
	 */

	private BoilerplateList<Integer> otpList;

	/**
	 * This method gets the otpList
	 * 
	 * @return The otpList
	 */
	public BoilerplateList<Integer> getOtpList() {
		return otpList;
	}

	/**
	 * This method sets the otpList
	 * 
	 * @param otpList
	 *            The otpList
	 */
	public void setOtpList(BoilerplateList<Integer> otpList) {
		this.otpList = otpList;
	}

	

	/**
	 * This method creates the user data for publishing
	 * 
	 * @return retrunValue The publish data string
	 */
	@Override
	public String createPublishJSON(String template) {
		String retrunValue = template;
		retrunValue = retrunValue.replace("@Id", this.getId() == null ? "" : this.getId());
		retrunValue = retrunValue.replace("@userMetaData",
				this.getUserMetaData() == null ? "" : Base.toJSON(this.getUserMetaData()));
		retrunValue = retrunValue.replace("@userId", this.getUserId() == null ? "" : this.getUserId());
		retrunValue = retrunValue.replace("@authenticationProvider",
				this.getAuthenticationProvider() == null ? "" : this.getAuthenticationProvider());
		retrunValue = retrunValue.replace("@email", this.getEmail() == null ? "" : this.getEmail());
		retrunValue = retrunValue.replace("@firstName", this.getFirstName() == null ? "" : this.getFirstName());
		retrunValue = retrunValue.replace("@lastName", this.getLastName() == null ? "" : this.getLastName());
		retrunValue = retrunValue.replace("@middleName", this.getMiddleName() == null ? "" : this.getMiddleName());
		retrunValue = retrunValue.replace("@phoneNumber", this.getPhoneNumber() == null ? "" : this.getPhoneNumber());
		retrunValue = retrunValue.replace("@referalSource",
				this.getReferalSource() == null ? "" : this.getReferalSource());
		return retrunValue;

	}

	/**
	 * This method creates the dynamic crm publish url.
	 * 
	 * @return returnUrl The publish url string
	 */
	@Override
	public String createPublishUrl(String url) {
		String returnUrl = url;
		returnUrl = returnUrl.replace("@crmRecordID", this.getCrmid() == null ? "" : this.getCrmid());
		return returnUrl;
	}
}
