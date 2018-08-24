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

	public FileMappingEntity getFileMapping(String attachmentId) throws BadRequestException;

	public void updateFileMapping(FileMappingEntity fileMappingEntity);

	public List<FileMappingEntity> getFileMappingByExpenseId(String expenseId) throws BadRequestException;

}
