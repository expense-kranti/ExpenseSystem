package com.boilerplate.service.interfaces;

import java.io.IOException;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.CurrencyConversionEntity;

/**
 * This interface provides the services to convert an amount in one currency to
 * another
 * 
 * @author urvij
 *
 */
public interface ICurrencyConversionService {

	/**
	 * This method is used to convert an amount in one currency to another
	 * 
	 * @param currencyConversionEntity
	 *            The currency conversion entity contains all inputs required
	 *            for currency conversion
	 * @return The currency conversion entity It contains the converted currency
	 * @throws ValidationFailedException Thrown when one of the currency codes is null/empty
	 * @throws IOException If there is an error in creating the http request
	 */
	public CurrencyConversionEntity findExchangeRateAndConvertCurrency(
			CurrencyConversionEntity currencyConversionEntity) throws ValidationFailedException, IOException;

}
