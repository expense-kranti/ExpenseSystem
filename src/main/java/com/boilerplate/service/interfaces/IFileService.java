package com.boilerplate.service.interfaces;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.java.entities.AttachmentEntity;
import com.boilerplate.java.entities.ExpenseEntity;
import com.boilerplate.java.entities.ExpenseHistoryEntity;

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
	public AttachmentEntity saveFileOnLocal(String fileMasterTag, MultipartFile file)
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
	public void saveFileMapping(ExpenseEntity expenseEntity) throws Exception;

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
	public List<AttachmentEntity> updateFileMapping(ExpenseEntity expenseEntity,
			ExpenseHistoryEntity expenseHistoryEntity) throws BadRequestException, Exception;

	public void getFile(HttpServletResponse attachmentId)
			throws FileNotFoundException, MalformedURLException, IOException;
}
