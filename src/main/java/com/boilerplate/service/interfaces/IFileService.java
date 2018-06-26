package com.boilerplate.service.interfaces;

import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.FileEntity;

public interface IFileService {
	/**
	 * This method saves a file to the file service provider
	 * 
	 * @param fileMasterTag
	 *            The file master tag
	 * @param file
	 *            The file data
	 * @return the file entity
	 * @throws Exception
	 */
	public FileEntity saveFile(String fileMasterTag, MultipartFile file) throws UpdateFailedException, Exception;

	/**
	 * This method is used to the list of files for given master tag
	 * 
	 * @param fileMasterTag
	 *            the fileMasterTag against whom files are to be found
	 * @return the list of files
	 */
	public BoilerplateList<FileEntity> getAllFileListOnMasterTag(String fileMasterTag);

	/**
	 * This method is used to get pre signed s3url
	 * 
	 * @param id
	 *            the id of the file
	 * @return the pre signed s3url
	 */
	public String getPreSignedS3URL(String id);

	/**
	 * This method is used to getFile
	 * 
	 * @param id
	 *            the file id
	 * @return the file found
	 * @throws NotFoundException
	 *             thrown if the file is not found
	 */
	public FileEntity getFile(String id) throws NotFoundException;

	/**
	 * This method is used to get the list of all files against a user
	 * 
	 * @param userId
	 *            the userId against whom files are to be found and get
	 * @return the list of files against a user
	 * @throws UnauthorizedException
	 *             thrown if the user getting the files is not authorized
	 */
	public BoilerplateMap<String, FileEntity> getAllFileList(String userId) throws UnauthorizedException;

	/**
	 * This method is used to update the file entity
	 * 
	 * @param fileEntity
	 *            contains the data to be updated
	 */
	public void updateFileEntity(FileEntity fileEntity);

}
