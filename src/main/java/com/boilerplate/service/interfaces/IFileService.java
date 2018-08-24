package com.boilerplate.service.interfaces;

import java.util.List;

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

	public List<AttachmentEntity> updateFileMapping(ExpenseEntity expenseEntity, ExpenseHistoryEntity expenseHistoryEntity)
			throws BadRequestException, Exception;
}
