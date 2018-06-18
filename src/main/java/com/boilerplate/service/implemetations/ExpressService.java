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
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.BaseEntity;
import com.boilerplate.java.entities.ExpressEntity;
import com.boilerplate.java.entities.ReportInputEntity;
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
			throws ValidationFailedException, IOException, PreconditionFailedException {

		// validate express entity
		expressEntity.validate();
		String requestData = createRequestBodyForGettingName(expressEntity);

		// prepare request headers and request body for our request
		// Mediator(named Java)
		BoilerplateMap<String, BoilerplateList<String>> requestHeadersJava = getRequestHeaders("Content-Type",
				"application/json;charset=UTF-8");
		// Making the http call for generating the response
		HttpResponse httpResponse = HttpUtility.makeHttpRequest(configurationManager.get("GET_NAMES_BY_MOBILE_NUMBER"),
				requestHeadersJava, null, requestData, "POST");
		// Throw exception if status is not 200
		if (httpResponse.getHttpStatus() != 200) {
			// Throw exception
			throw new PreconditionFailedException("ExpressEntity", "No name found for this mobile number", null);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		// Converting the response into the map
		Map<String, Object> responseBodyMap = objectMapper.readValue(httpResponse.getResponseBody(), Map.class);
		// Get the list of names from the response
		List<String> names = (List<String>) responseBodyMap.get("nameList");
		// Creating the list for holding the random name which will be added to
		// the
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
		// creating the map for containing the value of required header
		BoilerplateMap<String, BoilerplateList<String>> requestHeadersJava = new BoilerplateMap();
		// This contains the value for header
		BoilerplateList<String> contentTypeHeaderValueJava = new BoilerplateList<String>();
		// Adding the value of content type of the required header
		contentTypeHeaderValueJava.add(headerValue);
		requestHeadersJava.put(headerName, contentTypeHeaderValueJava);
		return requestHeadersJava;
	}

	/**
	 * This method creates the request body for getting the list of names
	 * request.
	 * 
	 * @param expressEntity
	 *            The expressEntity containing required mobile number for making
	 *            request body for request
	 * @return The required request body
	 */
	private String createRequestBodyForGettingName(ExpressEntity expressEntity) {
		// getting the required request body from the configuration
		String requestBody = configurationManager.get("GET_NAME_FOR_MOBILE_NUMBER_REQUEST_BODY");
		// replacing the @mobile number with mobile number provided by user
		requestBody = requestBody.replace("@mobileNumber", expressEntity.getMobileNumber());
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
		names.add("Akash Trivedi");
		names.add("Amit");
		names.add("Ashima");
		names.add("Urvij pratap Singh");
		names.add("Love Singhal");
		names.add("Pawan");
		names.add("Ankur Singh");
		names.add("Aman Bindal");
		names.add("Madu");
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

	/**
	 * @throws IOException
	 * @throws PreconditionFailedException
	 * @throws ValidationFailedException
	 * @see IExpressService.getUserDetails
	 */
	@Override
	public ReportInputEntity getUserDetails()
			throws IOException, PreconditionFailedException, ValidationFailedException {

		// get list name from express entity from db for given phone number
		ExpressEntity expressEntityFromDB = expressDataAccess
				.getUserExpressDetails(RequestThreadLocal.getSession().getExternalFacingUser().getUserId());

		if (BaseEntity.isNullOrEmpty(expressEntityFromDB.getFullName())) {
			throw new ValidationFailedException("ExpressEntity", "User full Name not present for user", null);
		}
		// get request body
		String requestBody = createRequestBodyForUserDetails(expressEntityFromDB);
		// prepare request headers and request body for our request
		// Mediator(named Java)
		BoilerplateMap<String, BoilerplateList<String>> requestHeadersJava = getRequestHeaders("Content-Type",
				"application/json;charset=UTF-8");
		// Making the http call for generating the response
		HttpResponse httpResponse = HttpUtility.makeHttpRequest(
				configurationManager.get("GET_USER_DETAILS_FOR_MOBILE_NUMBER_AND_NAME_REQUEST_URL"), requestHeadersJava,
				null, requestBody, "POST");
		// Throw exception if status is not 200
		if (httpResponse.getHttpStatus() != 200) {
			// Throw exception
			throw new PreconditionFailedException("ExpressEntity", "No details found for logged in user mobile number",
					null);
		}
		ObjectMapper objectMapper = new ObjectMapper();
		// Converting the response into the map
		Map<String, Object> responseBodyMap = objectMapper.readValue(httpResponse.getResponseBody(), Map.class);

		if (responseBodyMap != null && !responseBodyMap.isEmpty()) {
			// set report input entity with the response details
		}
		return null;
	}

	/**
	 * This method is used to create request body for getting user details of
	 * logged in user using mobile number and full name
	 * 
	 * @param expressEntityFromDB
	 *            contains the mobile number and full name required to prepare
	 *            request body
	 * @return the prepared request body
	 */
	private String createRequestBodyForUserDetails(ExpressEntity expressEntityFromDB) {
		// prepare request body from the configuration
		String requestBody = configurationManager.get("GET_USER_DETAILS_REQUEST_BODY");
		// replace request body parts with values
		requestBody = requestBody.replace("@mobileNumber", expressEntityFromDB.getMobileNumber());
		requestBody = requestBody.replace("@fullName", expressEntityFromDB.getFullName());
		return requestBody;
	}

}
