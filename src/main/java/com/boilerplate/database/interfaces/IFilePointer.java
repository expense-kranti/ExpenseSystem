package com.boilerplate.database.interfaces;

import java.util.List;

import com.boilerplate.exceptions.rest.BadRequestException;
import com.boilerplate.java.entities.FileMappingEntity;

/**
 * This is for file data integration
 * 
 * @author gaurav.verma.icloud
 *
 */
public interface IFilePointer {

	/**
	 * This method is used to save file mapping in MySQL
	 * 
	 * @param fileMappingEntity
	 *            this is the file mapping entity
	 * @throws Exception
	 *             Throw this exception if any exception occurs while saving
	 *             file mapping
	 */
	public void saveFileMapping(FileMappingEntity fileMappingEntity) throws Exception;

	/**
	 * this method is used to get file mapping for the gien attachment id
	 * 
	 * @param attachmentId
	 *            his is the attachment id
	 * @return File mapping
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 */
	public FileMappingEntity getFileMapping(String attachmentId) throws BadRequestException;

	/**
	 * This method is used update file mapping
	 * 
	 * @param fileMappingEntity
	 *            this is the file mapping entity to be updated
	 */
	public void updateFileMapping(FileMappingEntity fileMappingEntity);

	/**
	 * This method is used to get file mapping by expense id
	 * 
	 * @param expenseId
	 *            this is the expense id for which list needs to be fetched
	 * @return List of file mappings
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 */
	public List<FileMappingEntity> getFileMappingByExpenseId(String expenseId) throws BadRequestException;

	/**
	 * This method is used to get file mapping by attachment id
	 * 
	 * @param attachmentId
	 *            This is the attachment id
	 * @return The file mapping entity
	 * @throws BadRequestException
	 *             Throw this exception if user sends a bad request
	 */
	public FileMappingEntity getFileMappingByAttachmentId(String attachmentId) throws BadRequestException;

}
