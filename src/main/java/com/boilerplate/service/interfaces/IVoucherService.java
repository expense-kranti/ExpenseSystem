package com.boilerplate.service.interfaces;

import java.io.IOException;
import java.text.ParseException;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;

/**
 * This interface provides methods to operate on vouchers like saving creating
 * voucher
 * 
 * @author urvij
 *
 */
public interface IVoucherService {
	/**
	 * This method uploads Vouchers to DB from Csv File
	 * 
	 * @param fileId
	 *            The file Id
	 * @throws IOException
	 * @throws BadRequestException
	 * @throws NotFoundException
	 * @throws ParseException
	 * @throws UnauthorizedException
	 */
	public void voucherUploadFromCSV(String fileId)
			throws IOException, NotFoundException, BadRequestException, ParseException, UnauthorizedException;

}
