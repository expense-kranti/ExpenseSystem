package com.boilerplate.service.implemetations;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.CurrencyConversionEntity;
import com.boilerplate.service.interfaces.ICurrencyConversionService;

/**
 * This class implements the ICurrencyConversionService interface
 * 
 * @author urvij
 *
 */
public class CurrencyConversionService implements ICurrencyConversionService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(CurrencyConversionService.class);
	/**
	 * formatting the currency to two decimal places
	 */
	private static DecimalFormat df = new DecimalFormat("#.00");
	/**
	 * The instance of configuration manager
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * Sets the configuration manager
	 * 
	 * @param configurationManager
	 *            The configuration manager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @see ICurrencyConversionService.findExchangeRateAndConvertCurrency
	 */
	@Override
	public CurrencyConversionEntity findExchangeRateAndConvertCurrency(
			CurrencyConversionEntity currencyConversionEntity) throws ValidationFailedException, IOException {
		currencyConversionEntity.validate();

		// prepare GET request URL
		String requestUrl = configurationManager.get("CurrencyConversionAPI_URL");
		// replace {currencyFromCurrencyCode} with input value
		requestUrl = requestUrl.replace("{currencyFromsCurrencyCode}",
				currencyConversionEntity.getConvertFromsCurrencyCode().toUpperCase());
		// replace {currencyToCurrencyCode} with input value
		requestUrl = requestUrl.replace("{currencyTosCurrencyCode}",
				currencyConversionEntity.getConvertTosCurrencyCode().toUpperCase());
		// replace {amount} with input value
		requestUrl = requestUrl.replace("{amount}", String.valueOf(currencyConversionEntity.getCurrencyToConvert()));
		// make GET request to third party api to get latest exchange rate
		HttpResponse httpResponse = HttpUtility.makeHttpRequest(requestUrl, null, null, null, "GET");

		try {
			if (httpResponse.getHttpStatus() == 200) {
				ObjectMapper objectMapper = new ObjectMapper();
				// parse json response by mapping the response
				Map<String, Object> responseBodyMap = objectMapper.readValue(httpResponse.getResponseBody(), Map.class);
				if (Integer.valueOf((Integer) responseBodyMap.get("error")) == 0) {
					if (responseBodyMap.get("amount") != null) {
						// logic of converting currency
						currencyConversionEntity
								.setConvertedCurrency(new Double(df.format((responseBodyMap.get("amount")))));
					}
				} else {
					// TODO do whatever needed to do if no exchange rates
					// got from response
					currencyConversionEntity.setCurrencyToConvert(0);
					currencyConversionEntity.setConvertedCurrency(0);
				}

			}
		} catch (Exception ex) {
			// log the exception
			logger.logException("CurrencyConversionService", "findExchangeRateAndConvertCurrency", null, ex.toString(),
					ex);
			throw ex;
		}
		return currencyConversionEntity;
	}

}
