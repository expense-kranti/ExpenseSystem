package com.boilerplate.java.entities;

import java.io.Serializable;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.wordnik.swagger.annotations.ApiModel;

/**
 * This is the currency conversion entity
 * 
 * @author urvij
 *
 */

public class CurrencyConversionEntity extends BaseEntity implements Serializable {

	/**
	 * This is the currency to convert
	 */
	private int currencyToConvert;
	/**
	 * This is the converted currency
	 */
	private double convertedCurrency;
	/**
	 * This is the currency code of currency to convert to
	 */
	private String convertTosCurrencyCode;
	/**
	 * This is the currency code of currency to convert from
	 */
	private String convertFromsCurrencyCode;

	/**
	 * Gets the currency to convert
	 * 
	 * @return The currency to convert
	 */
	public int getCurrencyToConvert() {
		return currencyToConvert;
	}

	/**
	 * Sets the currency to convert
	 * 
	 * @param currencyToConvert
	 *            The currency to convert
	 */
	public void setCurrencyToConvert(int currencyToConvert) {
		this.currencyToConvert = currencyToConvert;
	}

	/**
	 * Gets the converted currency
	 * 
	 * @return The converted currency
	 */
	public double getConvertedCurrency() {
		return convertedCurrency;
	}

	/**
	 * Sets the converted currency
	 * 
	 * @param convertedCurrency
	 *            The converted currency
	 */
	public void setConvertedCurrency(double convertedCurrency) {
		this.convertedCurrency = convertedCurrency;
	}

	/**
	 * Gets the currency code of currency to convert to
	 * 
	 * @return The converted currency's currency code
	 */
	public String getConvertTosCurrencyCode() {
		return convertTosCurrencyCode;
	}

	/**
	 * Sets the currency code of currency to convert to
	 * 
	 * @param convertToCurrencyCode
	 *            The converted currency's currency code
	 */
	public void setConvertTosCurrencyCode(String convertTosCurrencyCode) {
		this.convertTosCurrencyCode = convertTosCurrencyCode;
	}

	/**
	 * Gets the currency code of currency to convert from
	 * 
	 * @return The currency code of currency to convert
	 */
	public String getConvertFromsCurrencyCode() {
		return convertFromsCurrencyCode;
	}

	/**
	 * Sets the currency code of currency to convert from
	 * 
	 * @param convertFromCurrencyCode
	 *            The currency code of currency to convert
	 */
	public void setConvertFromsCurrencyCode(String convertFromsCurrencyCode) {
		this.convertFromsCurrencyCode = convertFromsCurrencyCode;
	}

	/**
	 * @see BaseEntity.validate
	 */
	@Override
	public boolean validate() throws ValidationFailedException {
		// if the currency code of currency to convert from, is null or empty
		// throw validationfailedexception
		if (this.isNullOrEmpty(this.getConvertFromsCurrencyCode())) {
			throw new ValidationFailedException("CurrencyConversionEntity",
					"Currency code of currency to convert from is null/Empty", null);
		}
		// if the currency code of currency to convert to, is null or empty
		// throw validationfailedexception
		if (this.isNullOrEmpty(this.getConvertTosCurrencyCode())) {
			throw new ValidationFailedException("CurrencyConversionEntity",
					"Currency code of currency to convert to is null/Empty", null);
		}
		if (this.getCurrencyToConvert() <= 0) {
			throw new ValidationFailedException("CurrencyConversionEntity",
					"Currency amount to be converted should be greater than 0", null);
		}
		return true;
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

}
