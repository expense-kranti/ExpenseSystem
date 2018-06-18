package com.boilerplate.service.implemetations;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IExpress;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.framework.HttpResponse;
import com.boilerplate.framework.HttpUtility;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.ExpressEntity;
import com.boilerplate.service.interfaces.IExpressService;

/**
 * This
 * 
 * @author urvij
 *
 */
public class ExpressService implements IExpressService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(ExpressService.class);

	/**
	 * This is an instance of expressDataAccess
	 */
	IExpress expressDataAccess;

	/**
	 * Sets the expressDataAccess
	 * 
	 * @param expressDataAccess
	 *            the expressDataAccess to set
	 */
	public void setExpressDataAccess(IExpress expressDataAccess) {
		this.expressDataAccess = expressDataAccess;
	}

	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * @see IExpressService.validateName
	 */
	@Override
	public void validateName(ExpressEntity expressEntity)
			throws PreconditionFailedException, ValidationFailedException {
		// validating phonenumber
		expressEntity.validate();
		// check full name
		if (BaseEntity.isNullOrEmpty(expressEntity.getFullName())) {
			throw new ValidationFailedException("ExpressEntity", "Full name is null or empty", null);
		}
		// get list name from express entity from db for given phone number
		ExpressEntity expressEntityFromDB = expressDataAccess.getUserExpressDetails(expressEntity.getMobileNumber());
		if (expressEntityFromDB != null) {
			expressEntityFromDB.getFullNameList().contains(expressEntity.getFullName());
			expressEntityFromDB.setFullName(expressEntity.getFullName());
			// save express entity with the correct full name set
			expressDataAccess.saveUserExpressDetails(expressEntityFromDB);
		} else {
			throw new PreconditionFailedException("ExpressEntity", "No initial Express Attempt found", null);
		}
	}

	/**
	 * @see IExpressService.getNamesByMobileNumber
	 */
	@Override
	public ExpressEntity getNamesByMobileNumber(ExpressEntity expressEntity)
			throws ValidationFailedException, IOException {

		logger.logInfo("ExpressService", "getNamesByMobileNumber", "This is the express entity:" + expressEntity, null);

		// validate express entity
		expressEntity.validate();
		String requestData = createRequestBodyForGettingName(expressEntity);

		// prepare request headers and request body for our experian request
		// Mediator(named Java)
		BoilerplateMap<String, BoilerplateList<String>> requestHeadersJava = getRequestHeaders("Content-Type",
				"application/json;charset=UTF-8");

		logger.logInfo("ExpressService", "getNamesByMobileNumber",
				"Before making the http call the value of request header is:" + requestHeadersJava, null);
		// Making the http call for generating the response
		HttpResponse httpResponse = HttpUtility.makeHttpRequest(configurationManager.get("GET_NAMES_BY_MOBILE_NUMBER"),
				requestHeadersJava, null, requestData, "POST");
		logger.logInfo("ExpressService", "getNamesByMobileNumber",
				"after making the http call the http response is as:" + httpResponse, null);

		// Throw exception if status is not 200
		if (httpResponse.getHttpStatus() != 200) {
			// Throw exception
		}
		ObjectMapper objectMapper = new ObjectMapper();

		// Converting the response into the map
		Map<String, Object> responseBodyMap = objectMapper.readValue(httpResponse.getResponseBody(), Map.class);
		// Get the list of names from the response
		List<String> names = (List<String>) responseBodyMap.get("nameList");

		logger.logInfo("ExpressService", "getNamesByMobileNumber",
				"after getting the names for the response is as:" + names, null);
		// Creating the list for holding the random name which will be added to the
		// response names list
		List<String> randomNames = new ArrayList<>();
		// Check if random names are required, if list of names fetched is less
		// than 4
		if (names.size() < 4) {
			// get random names
			randomNames = getRandomNames(4 - names.size());
		}
		// Adding the list of random names to the response data names
		names.addAll(randomNames);
		// Shuffle the list of names
		Collections.shuffle(names);

		// Setting the list of required list of names
		expressEntity.setFullNameList(names);
		logger.logInfo("ExpressService", "getNamesByMobileNumber",
				"before saving the names into the redis entity is as:" + expressEntity, null);
		// Save this list of name into the redis database
		expressDataAccess.saveUserExpressDetails(expressEntity);
		return expressEntity;
	}

	/**
	 * This method is used to generates request headers
	 * 
	 * @param headerName
	 *            This is the key
	 * @param headerValue
	 *            This contains the value of key means content type
	 * 
	 * @return requestHeadersJava This is the required header value
	 */
	public BoilerplateMap<String, BoilerplateList<String>> getRequestHeaders(String headerName, String headerValue) {

		logger.logInfo("ExpressService", "getRequestHeaders",
				"the input data is as:" + headerName + " header value" + headerValue, null);

		// creating the map for containing the required header
		BoilerplateMap<String, BoilerplateList<String>> requestHeadersJava = new BoilerplateMap();
		// This contains the value for header
		BoilerplateList<String> contentTypeHeaderValueJava = new BoilerplateList<String>();
		// Adding the value of content type of the required header
		contentTypeHeaderValueJava.add(headerValue);
		requestHeadersJava.put(headerName, contentTypeHeaderValueJava);
		return requestHeadersJava;
	}

	/**
	 * This method creates the request body for getting the list of names request.
	 * 
	 * @param expressEntity
	 *            The expressEntity containing required mobile number for making
	 *            request body for request
	 * @return The required request body
	 */
	private String createRequestBodyForGettingName(ExpressEntity expressEntity) {
		logger.logInfo("ExpressService", "createRequestBodyForGettingName", "the express entity is as:" + expressEntity,
				null);

		// getting the required request body from the configuration
		String requestBody = configurationManager.get("GET_NAME_FOR_MOBILE_NUMBER_REQUEST_BODY");
		// replacing the @mobile number with mobile number provided by user
		requestBody = requestBody.replace("@mobileNumber", expressEntity.getMobileNumber());
		logger.logInfo("ExpressService", "createRequestBodyForGettingName", "requestBody is as:" + requestBody, null);
		return requestBody;
	}

	/**
	 * This method return a list of random names
	 * 
	 * @param count
	 *            number of names to be returned
	 * @return required list of names
	 */
	private List<String> getRandomNames(int count) {

		List<String> namesReturned = new ArrayList();
		List<String> names = new ArrayList();
		names.add("Shiva Gupta");
		names.add("Ankita Trivedi");
		names.add("Amit Verma");
		names.add("Ashima khan");
		names.add("Urvij kumar Singh");
		names.add("Love Singhal");
		names.add("Pawan goel");
		names.add("Ankur Singh");
		names.add("Aman Bindal");
		names.add("Madhurima singh");
		// generate a list of required size random names
		while (namesReturned.size() < count) {
			String randomName = names.get((int) (Math.random() * ((10 - 1))) + 1);
			if (namesReturned.contains(randomName))
				continue;
			else
				namesReturned.add(randomName);
		}
		// return the list of random names generated
		return namesReturned;
	}

}
