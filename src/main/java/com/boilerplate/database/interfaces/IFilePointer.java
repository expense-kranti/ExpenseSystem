package com.boilerplate.database.interfaces;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.java.entities.ReferalEntity;

/**
 * This is for file data integration
 * 
 * @author gaurav.verma.icloud
 *
 */
public interface IFilePointer {
	/**
	 * Saves the file
	 * 
	 * @param fileEntity
	 *            The file entity
	 * @return The file entity
	 * @throws Exception
	 */
	public FileEntity save(FileEntity fileEntity) throws Exception;

	/**
	 * This method gets a file pointer based on id
	 * 
	 * @param id
	 *            The id of the file
	 * @return The file entity
	 * @throws NotFoundException
	 *             When the file is not found
	 */
	public FileEntity getFilePointerById(String id) throws NotFoundException;

	/**
	 * This method updates the file metadata
	 * 
	 * @param id
	 *            The id of the file
	 * @param metaDataMap
	 *            The metadata map
	 * @return The updated metadata
	 * @throws NotFoundException
	 *             If the file is not found.
	 */
	public FileEntity updateMetaData(String id, BoilerplateMap<String, String> metaDataMap) throws NotFoundException;

	/**
	 * This method gets all the files for the given user
	 * 
	 * @param userId
	 *            The user id
	 * @param organizationId
	 *            The organization id of the user
	 * @return The file entity
	 */
	public BoilerplateList<FileEntity> getAllFiles(String userId, String organizationId);

	/**
	 * Gets all the files for the organization
	 * 
	 * @param organizationId
	 *            The id of the organizaion
	 * @return The list of file entities
	 */
	public BoilerplateList<FileEntity> getFiles(String organizationId);

	/**
	 * 
	 * @param id
	 * @param fileMasterTag
	 * @return
	 */
	public BoilerplateList<FileEntity> getAllFilesOnMasterTag(String id, String fileMasterTag);

	/**
	 * This method is used to add key in Redis
	 * 
	 * @param referalEntity
	 */
	void addInRedisSet(FileEntity fileEntity);

	/**
	 * This method is used to fetch UserFile key which is stored in redis
	 * 
	 * @return
	 */
	public Set<String> fetchUserFileAndAddInQueue();

	/**
	 * This method is used to delete this UserFile Name in User file Name
	 * 
	 * @param fileName
	 */
	void deleteItemFromRedisUserFileSet(String fileName);

	/**
	 * This method is used to set file name in Redis Set
	 * 
	 * @param fileName
	 *            the filename to be added in Redis Set
	 */
	public void addInRedisSet(String fileName);

	/**
	 * This method is used to get all file keys
	 * 
	 * @return the set of all file keys
	 */
	public Set<String> getAllFileKeys();

	List<String> getAllUserFileKeysForUser(String userId);

}
