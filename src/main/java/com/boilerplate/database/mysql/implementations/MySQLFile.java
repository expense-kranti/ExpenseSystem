package com.boilerplate.database.mysql.implementations;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;

import org.joda.time.DateTime;

import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.FileEntity;

public class MySQLFile extends MySQLBaseDataAccessLayer implements IFilePointer  {

	
	/**
	 * @see IFilePointer.mySqlSaveFile
	 */
	@Override
	public void mySqlSaveFile(FileEntity file) throws IOException {
		// Set creation time to current time
		file.setCreationDate(Date.valueOf(LocalDate.now()));
		// save file in MySql Database
		super.create(file);
		
	}

	@Override
	public FileEntity save(FileEntity fileEntity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileEntity getFilePointerById(String id) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FileEntity updateMetaData(String id, BoilerplateMap<String, String> metaDataMap) throws NotFoundException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoilerplateList<FileEntity> getAllFiles(String userId, String organizationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoilerplateList<FileEntity> getFiles(String organizationId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public BoilerplateList<FileEntity> getAllFilesOnMasterTag(String id, String fileMasterTag) {
		// TODO Auto-generated method stub
		return null;
	}

}
