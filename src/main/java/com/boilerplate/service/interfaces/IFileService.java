package com.boilerplate.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.FileEntity;

public interface IFileService {
	/**
	 * This method saves a file to the file service provider
	 * @param fileMasterTag The file master tag
	 * @param file The file data
	 * @return the file entity
	 */
	public FileEntity saveFile(String fileMasterTag, MultipartFile file) throws UpdateFailedException;
	
	public BoilerplateList<FileEntity> getAllFileListOnMasterTag(
			String fileMasterTag);

	public String getPreSignedS3URL(String id);

	public FileEntity getFile(String id) throws NotFoundException;
	
}
