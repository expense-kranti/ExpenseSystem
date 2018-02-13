package com.boilerplate.asyncWork;

import java.sql.Date;
import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;

import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.database.mysql.implementations.MySQLFile;
import com.boilerplate.exceptions.rest.ConflictException;
import com.boilerplate.framework.Logger;
import com.boilerplate.java.entities.BlogActivityEntity;
import com.boilerplate.java.entities.ExternalFacingUser;
import com.boilerplate.java.entities.FileEntity;

/**
 * This class is used to pop or read queue and process each element
 * 
 * @author Yash
 *
 */
public class MySQLCreateUserFileObserver implements IAsyncWorkObserver {

	/**
	 * This is the instance of logger MySQLCreateOrUpdateUserObserver logger
	 */
	private static Logger logger = Logger.getInstance(MySQLCreateOrUpdateUserObserver.class);
	/**
	 * This is the instance of IFilePointer
	 */
	IFilePointer mySqlUserFile;

	/**
	 * This method is set File
	 * 
	 * @param mySqlUserFile
	 */
	public void setMySqlUserFile(IFilePointer mySqlUserFile) {
		this.mySqlUserFile = mySqlUserFile;
	}

	/**
	 * This is the instance of IFilePointer
	 */
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
	 * @param fileEntity
	 *            This is the file entity
	 * @throws Exception
	 */
	private void saveOrUpdateUserFileInMySQL(FileEntity fileEntity) throws Exception {
		// Set file id to null to avoid conflict of File Table Id column and
		// Redis FileEntity Id Column
		// used file name as get and delete key from redis
		// file name and file id is same
		fileEntity.setId(null);

		try {
			// add/save file entity to the mysql database
			mySqlUserFile.save(fileEntity);
		} catch (Exception ex) {
			logger.logException("MySQLCreateUserFileObserver", "save", "try-catch block calling save method",
					"Exception cause is : " + ex.getCause().toString() + "- Exception message is : " + ex.getMessage(),
					ex);
			// check if exception cause is "Duplicate entry"
			if (ex.getCause().toString().contains("Duplicate entry")) {
				// then remove the file name from Redis Set by making it in
				// upper case(making in upper case is must)
				filePointer.deleteItemFromRedisUserFileSet(fileEntity.getFileName().toUpperCase());
			}
			throw ex;
		}

		// after getting work done by using file name delete that filename from
		// set
		filePointer.deleteItemFromRedisUserFileSet(fileEntity.getFileName().toUpperCase());

	}

}
