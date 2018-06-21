package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

public class KycDocumentsInformation extends BaseEntity implements Serializable{
	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		return true;
	}
	/**
	 * @see BaseEntity.transformToInternal
	 */
	@Override
	public BaseEntity transformToInternal() {
		return this;
	}

	/**
	 * @see BaseEntity.transformToExternal
	 */
	@Override
	public BaseEntity transformToExternal() {
		return this;
	}
	
	/**
	 * This is the KycDocument Id
	 */
	private String kycDocumentId;
	
	/**
	 * This method gets the KycDocument Id
	 * @return The KycDocument Id
	 */
	public String getKycDocumentId() {
		return kycDocumentId;
	}
	
	/**
	 * This method sets the KycDocument Id
	 * @param kycDocumentId The KycDocument Id
	 */
	public void setKycDocumentId(String kycDocumentId) {
		this.kycDocumentId = kycDocumentId;
	}
	
	/**
	 * This method gets the KycDocument Type
	 * @return The KycDocument Type
	 */
	public String getKycDocumentType() {
		return kycDocumentType;
	}
	
	/**
	 * This method sets the KycDocument Type
	 * @param kycDocumentType The KycDocument Type
	 */
	public void setKycDocumentType(String kycDocumentType) {
		this.kycDocumentType = kycDocumentType;
	}
	
	/**
	 * This method gets the KycDocument Name
	 * @return The KycDocument Name
	 */
	public String getKycDocumentName() {
		return kycDocumentName;
	}
	
	/**
	 * This method sets the KycDocument Name
	 * @param kcDocumentName
	 */
	public void setKycDocumentName(String kcDocumentName) {
		this.kycDocumentName = kcDocumentName;
	}

	/**
	 * This is the KycDocument Type
	 */
	private String kycDocumentType;
	
	/**
	 * This is the KycDocument Name
	 */
	private String kycDocumentName;

}
