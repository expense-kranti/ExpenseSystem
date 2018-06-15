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
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
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
	 * @see IExpressService.validateAndRegisterUser
	 */
	@Override
	public void validateName(ExpressEntity expressEntity) throws PreconditionFailedException {

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

	@Override
	public ExpressEntity getNamesByMobileNumber(ExpressEntity expressEntity)
			throws ValidationFailedException, IOException {

		// check the expressEntity having mobile number or not
		if (expressEntity.validate() != true) {
			throw new ValidationFailedException("ExpressEntity", "mobile number can not be null or empty", null);
		}
		String requestData = createRequestBodyForGettingName(expressEntity);

		// prepare request headers and request body for our experian request
		// Mediator(named Java)
		BoilerplateMap<String, BoilerplateList<String>> requestHeadersJava = getRequestHeaders("Content-Type",
				"application/json;charset=UTF-8");

		HttpResponse httpResponse = HttpUtility.makeHttpRequest(configurationManager.get("GET_NAMES_BY_MOBILE_NUMBER"),
				requestHeadersJava, null, requestData, "POST");
		// throw exception if status is not 200
		if (httpResponse.getHttpStatus() != 200) {
			// Throw exception
		}
		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Object> responseBodyMap = objectMapper.readValue(httpResponse.getResponseBody(), Map.class);
		List<String> names = (List<String>) responseBodyMap.get("nameList");

		List<String> randomNames = new ArrayList<>();
		// Check if random names are required, if list of names fetched is less
		// than 4
		if (names.size() < 4) {
			// get random names
			randomNames = getRandomNames(4 - names.size());
		}
		names.addAll(randomNames);
		Collections.shuffle(names);
		expressEntity.setFullNameList(names);
		expressDataAccess.saveUserExpressDetails(expressEntity);
		return expressEntity;
	}

	/**
	 * This method generates request headers
	 * 
	 * @return
	 */
	public BoilerplateMap<String, BoilerplateList<String>> getRequestHeaders(String headerName, String headerValue) {
		BoilerplateMap<String, BoilerplateList<String>> requestHeadersJava = new BoilerplateMap();
		BoilerplateList<String> contentTypeHeaderValueJava = new BoilerplateList<String>();
		contentTypeHeaderValueJava.add(headerValue);
		requestHeadersJava.put(headerName, contentTypeHeaderValueJava);
		return requestHeadersJava;
	}

	/**
	 * This method creates the request body for experian request.
	 * 
	 * @param reportInputEntiity
	 *            The report input entity containing required data for making
	 *            request body for request
	 * @return The required request body
	 */
	private String createRequestBodyForGettingName(ExpressEntity expressEntity) {
		String requestBody = configurationManager.get("GET_NAME_FOR_MOBILE_NUMBER_REQUEST_BODY");
		requestBody = requestBody.replace("@mobileNumber", expressEntity.getMobileNumber());
		return requestBody;
	}

	/**
	 * This method return a list of random names
	 * 
	 * @param count
	 *            number of names to be returned
	 * @return namesReturned list of names
	 */
	private List<String> getRandomNames(int count) {

		List<String> namesReturned = new ArrayList();
		List<String> names = new ArrayList();
		names.add("Shiva Gupta");
		names.add("Ankita Trivedi");
		names.add("Amit");
		names.add("Ashima");
		names.add("Urvij kumar Singh");
		names.add("Love Singhal");
		names.add("Pawan");
		names.add("Ankur Singh");
		names.add("Aman Bindal");
		names.add("Madu");

		// generate a list of random names
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
