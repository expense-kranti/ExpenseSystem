package com.boilerplate.service.interfaces;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseHistoryEntity;
import com.boilerplate.java.entities.FileMappingEntity;

/**
 * This interface has methods for CRUD operations related to file mapping and
 * file saving
 * 
 * @author ruchi
 *
 */
public interface IFileService {

	/**
	 * this method is used to save file onto disk
	 * 
	 * @param fileMasterTag
	 *            This is the file name given by user
	 * @param file
	 *            This is the file uploaded by user
	 * @return Attachment id or file name on disk
	 * @throws UpdateFailedException
	 *             Throw this exception if file upload fails
	 * @throws Exception
	 *             throw this exception exception occurs while saving file
	 */
	public FileMappingEntity saveFileOnLocal(String fileMasterTag, MultipartFile file)
			throws UpdateFailedException, Exception;

	/**
	 * this method is used to save file mapping in MySQL
	 * 
	 * @param expenseEntity
	 *            this is the expense entity or which attachments need to be
	 *            saved
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             file mapping
	 */
	public boolean saveFileMapping(ExpenseEntity expenseEntity) throws Exception;

	/**
	 * This method is used to update file mappings while updating expense
	 * 
	 * @param expenseEntity
	 *            This is the updated expense entity
	 * @param expenseHistoryEntity
	 *            This is the previous expense
	 * @return List of attachments
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             file mapping
	 */
	public List<FileMappingEntity> updateFileMapping(ExpenseEntity expenseEntity,
			ExpenseHistoryEntity expenseHistoryEntity) throws BadRequestException, Exception;

	/**
	 * Thi method is used to download a file
	 * 
	 * @param request
	 *            This is the http request
	 * @param response
	 *            This is the response
	 * @param fileName
	 *            This is the file name
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 * @throws NotFoundException
	 *             throw this exception if file maping is not found
	 */
	public void downloadFile(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("fileName") String fileName) throws BadRequestException, NotFoundException;

}
