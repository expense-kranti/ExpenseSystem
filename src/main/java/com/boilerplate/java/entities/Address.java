package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;

/**
 * 
 * @author love
 *
 */
public class Address extends BaseEntity implements Serializable {
	/**
	 * This is the first line of address.
	 */
	private String firstLineOfAddress;
	/**
	 * This is the second line of address.
	 */
	private String secondLineOfAddress;
	/**
	 * This is the third line of address.
	 */
	private String thirdLineOfAddress;
	/**
	 * This is the fifth line of address.
	 */
	private String fifthLineOfAddress;
	/**
	 * This is the city
	 */
	private String city;
	/**
	 * This is the state
	 */
	private String state;
	/**
	 * this is the pincode
	 */
	private String zipCode;
	/**
	 * this is the country code
	 */
	private String countryCode;
	/**
	 * This is the tradeline id
	 */
	private String tradelineId;
	/**
	 * This is the address type
	 */
	private String addressType;
	/**
	 * This is the account number
	 */
	private String accountNumber;
	/**
	 * This is the bankName
	 */
	private String bankName;
	/**
	 * This is the data source
	 */
	private String dataSource;

	/**
	 * @return the addressType
	 */
	public String getAddressType() {
		return addressType;
	}

	/**
	 * @param addressType
	 *            the addressType to set
	 */
	public void setAddressType(String addressType) {
		this.addressType = addressType;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *            the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the bankName
	 */
	public String getBankName() {
		return bankName;
	}

	/**
	 * @param bankName
	 *            the bankName to set
	 */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
	 * @return the dataSource
	 */
	public String getDataSource() {
		return dataSource;
	}

	/**
	 * @param dataSource
	 *            the dataSource to set
	 */
	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	/**
	 * This method is used to get the tradeline id
	 * 
	 * @return tradelineId
	 */
	public String getTradelineId() {
		return tradelineId;
	}

	/**
	 * This method is used to set the trade line id
	 * 
	 * @param tradelineId
	 */
	public void setTradelineId(String tradelineId) {
		this.tradelineId = tradelineId;
	}

	/**
	 * This gets the first line of address
	 * 
	 * @return first line of address
	 */
	public String getFirstLineOfAddress() {
		return firstLineOfAddress;
	}

	/**
	 * This sets the first line of address
	 * 
	 * @param firstLineOfAddress
	 *            the first line of ddress
	 */
	public void setFirstLineOfAddress(String firstLineOfAddress) {
		this.firstLineOfAddress = firstLineOfAddress;
	}

	/**
	 * This gets the second line of address
	 * 
	 * @return the second line of ddress
	 */
	public String getSecondLineOfAddress() {
		return secondLineOfAddress;
	}

	/**
	 * this sets the second line of address
	 * 
	 * @param secondLineOfAddress
	 *            the second line of address
	 */
	public void setSecondLineOfAddress(String secondLineOfAddress) {
		this.secondLineOfAddress = secondLineOfAddress;
	}

	/**
	 * This gets the third line of address
	 * 
	 * @return the third line of address
	 */
	public String getThirdLineOfAddress() {
		return thirdLineOfAddress;
	}

	/**
	 * This sets the third line of address
	 * 
	 * @param thirdLineOfAddress
	 *            c
	 */
	public void setThirdLineOfAddress(String thirdLineOfAddress) {
		this.thirdLineOfAddress = thirdLineOfAddress;
	}

	/**
	 * This gets the fifth line of address
	 * 
	 * @return the fifth line of address
	 */
	public String getFifthLineOfAddress() {
		return fifthLineOfAddress;
	}

	/**
	 * This sets the fifth line of address
	 * 
	 * @param fifthLineOfAddress
	 *            the fifth line of address
	 */
	public void setFifthLineOfAddress(String fifthLineOfAddress) {
		this.fifthLineOfAddress = fifthLineOfAddress;
	}

	/**
	 * this get the city
	 * 
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * this sets the city
	 * 
	 * @param city
	 *            the city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * this gets the state
	 * 
	 * @return the state
	 */
	public String getState() {
		return state;
	}

	/**
	 * this sets the state
	 * 
	 * @param state
	 *            the state
	 */
	public void setState(String state) {
		this.state = state;
	}

	/**
	 * this gets the zipcode
	 * 
	 * @return the zipcode
	 */
	public String getZipCode() {
		return zipCode;
	}

	/**
	 * this sets the zipcode
	 * 
	 * @param zipCode
	 *            the zipcode
	 */
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	/**
	 * this gets the country code
	 * 
	 * @return the country code
	 */
	public String getCountryCode() {
		return countryCode;
	}

	/**
	 * this sets the country code
	 * 
	 * @param countryCode
	 *            the country code
	 */
	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	@Override
	public boolean validate() throws ValidationFailedException {
		return false;
	}

	@Override
	public BaseEntity transformToInternal() {
		return null;
	}

	@Override
	public BaseEntity transformToExternal() {
		return null;
	}

}
