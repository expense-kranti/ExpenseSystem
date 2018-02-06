package com.boilerplate.asyncWork;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.database.mysql.implementations.MySQLFile;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.FileEntity;

public class MySQLCreateUserFileObserver implements IAsyncWorkObserver {

	IFilePointer mySqlUserFile;

	/**
	 * This method is set File
	 * 
	 * @param mySqlUserFile
	 */
	public void setMySqlUserFile(IFilePointer mySqlUserFile) {
		this.mySqlUserFile = mySqlUserFile;
	}

	@Autowired
	private IFilePointer filePointer;

	/**
	 * This method used to set file pointer
	 * 
	 * @param filePointer
	 */
	public void setFilePointer(IFilePointer filePointer) {
		this.filePointer = filePointer;
	}

	/**
	 * This method get the file from Redis data store using supplied fileName
	 * and save it in MySQL Database
	 */
	@Override
	public void observe(AsyncWorkItem asyncWorkItem) throws Exception {
		// get the userfile from Redis data store and save it in MySQL data base
		saveOrUpdateUserFileInMySQL((FileEntity) filePointer.getFilePointerById((String) asyncWorkItem.getPayload()));

	}

	/**
	 * This method is used to save File in MySQLdatabase
	 * 
	 * @param externalFacingUser
	 *            the file to save
	 * @throws ConflictException
	 *             thrown if the user already exists in the database for the
	 *             given provider.
	 */
	private void saveOrUpdateUserFileInMySQL(FileEntity fileEntity) throws ConflictException {
		// Set creation time to current time
		fileEntity.setCreationDate(Date.valueOf(LocalDate.now()));
		fileEntity.setId(null);
		// add file to the mysql database
		mySqlUserFile.save(fileEntity);

		// after getting work done by using userId delete that user id from set
		filePointer.deleteItemFromRedisUserFileSet(fileEntity.getFileName());

	}

}
