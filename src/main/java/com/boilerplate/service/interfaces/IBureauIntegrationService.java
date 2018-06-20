package com.boilerplate.service.interfaces;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ReportInputEntity;

/**
 * This interface has methods for starting experian integration like starting
 * communication with experian server and fetching experian report against a
 * user.
 * 
 * @author love
 *
 */
public interface IBureauIntegrationService {

	/**
	 * This method is used to start experian integration
	 * 
	 * @param reportInputEntiity
	 *            it contains the data required to send request to server for
	 *            experian integration
	 * @return the report input entity
	 * @throws ValidationFailedException
	 *             thrown when any required value is not present
	 * @throws ConflictException
	 *             thrown when user already exists
	 * @throws NotFoundException
	 *             thrown when user not found with given userId(logged in
	 *             userId)
	 * @throws IOException
	 *             thrown if IOException occurs in while making http requests to
	 *             experian server
	 * @throws PreconditionFailedException
	 *             thrown when successful response of http request to experian
	 *             server is not received
	 * @throws BadRequestException
	 *             when userId is not found
	 * @throws UnauthorizedException
	 * @throws Exception
	 *             thrown when any exception occurs
	 */
	public ReportInputEntity start(ReportInputEntity reportInputEntiity)
			throws ValidationFailedException, ConflictException, NotFoundException, IOException,
			PreconditionFailedException, BadRequestException, UnauthorizedException, Exception;

	/**
	 * This method is used to start question answer session with the user for
	 * getting authentication for getting experian report of the authenticated
	 * user
	 * 
	 * @param questionId
	 *            the question id of the question
	 * @param answerPart1
	 *            answer part 1 of the answer of the question
	 * @param answerPart2
	 *            answer part 2 of the answer of the question
	 * @return the reportInputEntity that contains data produced during this
	 *         process of experian integration like question data for request,
	 *         report data got in response
	 * @throws ConflictException
	 *             thrown when there is an error in updating the user
	 * @throws NotFoundException
	 *             when the user is not found in database against whom experian
	 *             report to be generated
	 * @throws IOException
	 *             thrown if IOException occurs in while making http requests to
	 *             experian server
	 * @throws BadRequestException
	 *             if proper required input is not provided
	 * @throws ParserConfigurationException
	 *             thrown if a DocumentBuilder cannot be created which satisfies
	 *             the configuration requested.
	 * @throws SAXException
	 *             thrown when exception occurs in parsing the xml document
	 * @throws UpdateFailedException
	 *             thrown when saving of experian report data fails
	 * @throws PreconditionFailedException
	 *             thrown when successful response of http request to experian
	 *             server is not received
	 * @throws Exception
	 */
	public ReportInputEntity fetchNextItem(String questionId, String answerPart1, String answerPart2)
			throws ConflictException, NotFoundException, IOException, BadRequestException, SAXException,
			UpdateFailedException, ParserConfigurationException, PreconditionFailedException, Exception;

	
}
