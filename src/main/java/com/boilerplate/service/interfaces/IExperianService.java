package com.boilerplate.service.interfaces;

import java.io.IOException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.PreconditionFailedException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.exceptions.rest.ValidationFailedException;
import com.boilerplate.java.entities.ReportInputEntiity;

public interface IExperianService {

	public ReportInputEntiity startSingle(ReportInputEntiity reportInputEntiity)
			throws ConflictException, UnauthorizedException,
			ValidationFailedException, NotFoundException, BadRequestException,
			IOException, PreconditionFailedException;

	public ReportInputEntiity fetchNextItem(String questionId, String answerPart1,
			String answerPart2) throws ConflictException, NotFoundException,
			IOException, PreconditionFailedException, UpdateFailedException,
			BadRequestException, JAXBException, ParserConfigurationException,
			FactoryConfigurationError, SAXException, Exception;

}
