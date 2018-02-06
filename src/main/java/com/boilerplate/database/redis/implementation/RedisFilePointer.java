package com.boilerplate.database.redis.implementation;

import com.boilerplate.framework.Logger;

import java.io.IOException;
import java.util.Set;

import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.database.interfaces.IReferral;
import com.boilerplate.database.mysql.implementations.MySQLFile;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.java.Base;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.java.entities.ReferalEntity;

public class RedisFilePointer extends BaseRedisDataAccessLayer implements IFilePointer{
	Logger logger = Logger.getInstance(RedisFilePointer.class);

	private static final String File = "FILE:";

	private static final String UserFile = "USER_FILE:";

	private static final String OrganizationFile = "ORGANIZATION_FILE:";
	
	private static final String UserFileKeyForSet = "USERFILE_MYSQL:";
	@Override
	public FileEntity save(FileEntity fileEntity) {
		fileEntity.setId(fileEntity.getFileName());
		super.set(File+fileEntity.getFileName().toUpperCase(), fileEntity);
		
		//store file against user
		String userFilesAsXML =super.get(UserFile+fileEntity.getUserId().toUpperCase());
		
		BoilerplateList<String> userFilesAsList = null;
		if(userFilesAsXML == null){
			userFilesAsList = new BoilerplateList<>();
		}
		else{
			userFilesAsList = (BoilerplateList<String>)Base.fromXML(userFilesAsXML,BoilerplateList.class);
		}
		userFilesAsList.add(fileEntity.getId());
		super.set(UserFile+fileEntity.getUserId().toUpperCase(),Base.toXML(userFilesAsList));
		logger.logInfo("RedisFilePointer", "FileEntity.Save", "UserFileMapping", "Key=" + UserFile+fileEntity.getUserId().toUpperCase() + "Value=" + userFilesAsList.toString());
		//store file against organization
		if(fileEntity.getOrganizationId() != null){
			String organizationFilesAsXML =super.get(OrganizationFile+fileEntity.getOrganizationId().toUpperCase());
			
			BoilerplateList<String> organizationFilesAsList = null;
			if(organizationFilesAsXML == null){
				organizationFilesAsList = new BoilerplateList<>();
			}
			else{
				organizationFilesAsList = (BoilerplateList<String>)Base.fromXML(organizationFilesAsXML,BoilerplateList.class);
			}
			organizationFilesAsList.add(fileEntity.getId());
			super.set(OrganizationFile+fileEntity.getOrganizationId().toUpperCase(),Base.toXML(userFilesAsList));
		}
		
		return fileEntity;
	}

	@Override
	public FileEntity getFilePointerById(String id) throws NotFoundException {
		FileEntity fileEntity = super.get(File+id.toUpperCase(),FileEntity.class);
		if(fileEntity == null) throw new NotFoundException("File","File with Id = "+id+" not found", null);
		return fileEntity;
	}

	@Override
	public FileEntity updateMetaData(String id, BoilerplateMap<String, String> metaDataMap) throws NotFoundException {
		FileEntity fileEntity = this.getFilePointerById(id);
		if(fileEntity == null){
			throw new NotFoundException("File", "File with id = "+id+" not found", null);
		}
		fileEntity.getMetaData().putAll(metaDataMap);
		super.set(File+fileEntity.getFileName().toUpperCase(), fileEntity);
		return fileEntity;
	}

	@Override
	public BoilerplateList<FileEntity> getAllFiles(String userId, String organizationId) {
		
		BoilerplateList<String> fileIdsAsList = new BoilerplateList<>();
		
		//get files for the user
		String  fileIdsForUserXML = super.get(UserFile+userId.toUpperCase());
		if(fileIdsForUserXML != null){
			fileIdsAsList.addAll((BoilerplateList<String>)Base.fromXML(fileIdsForUserXML,BoilerplateList.class));
		}
		
		//get files for the organization
		if(organizationId !=null){
			String  fileIdsForOrganizationXML = super.get(OrganizationFile+organizationId.toUpperCase());
			if(fileIdsForOrganizationXML != null){
				fileIdsAsList.addAll((BoilerplateList<String>)Base.fromXML(fileIdsForOrganizationXML,BoilerplateList.class));
			}
		}
		BoilerplateList<FileEntity> files = new BoilerplateList<>();
		for(Object fileId :  fileIdsAsList){
			FileEntity  fileEntity = super.get(File+((String)fileId).toUpperCase(),FileEntity.class);
			if(fileEntity  != null){
				files.add(fileEntity);
			}
		}
		return files;
	}

	@Override
	public BoilerplateList<FileEntity> getFiles(String organizationId) {
		BoilerplateList<String> fileIdsAsList = new BoilerplateList<>();
		
		//get files for the organization
		String  fileIdsForOrganizationXML = super.get(OrganizationFile+organizationId.toUpperCase());
		if(fileIdsForOrganizationXML != null){
			fileIdsAsList.addAll((BoilerplateList<String>)Base.fromXML(fileIdsForOrganizationXML,BoilerplateList.class));
		}
		
		BoilerplateList<FileEntity> files = new BoilerplateList<>();
		for(Object fileId :  fileIdsAsList){
			FileEntity  fileEntity = super.get(File+((String)fileId).toUpperCase(),FileEntity.class);
			if(fileEntity  != null){
				files.add(fileEntity);
			}
		}
		return files;
	}
	@Override
	public BoilerplateList<FileEntity> getAllFilesOnMasterTag(String userId,
			String fileMasterTag) {
		BoilerplateList<String> fileIdsAsList = new BoilerplateList<>();
		
		//get files for the user
		String  fileIdsForUserXML = super.get(UserFile+userId.toUpperCase());
		if(fileIdsForUserXML != null){
			fileIdsAsList.addAll((BoilerplateList<String>)Base.fromXML(fileIdsForUserXML,BoilerplateList.class));
		}
		
		
		BoilerplateList<FileEntity> files = new BoilerplateList<>();
		for(Object fileId :  fileIdsAsList){
			FileEntity  fileEntity = super.get(File+((String)fileId).toUpperCase(),FileEntity.class);
			if(fileEntity.getFileMasterTag().equals(fileMasterTag)){
				files.add(fileEntity);
			}
		}
		return files;
	}

	@Override
	public void mySqlSaveFile(FileEntity file) throws IOException {
		// call MySQLFile method to save the in MySqlDatabase
		MySQLFile obj = new MySQLFile();
		obj.mySqlSaveFile(file);
		
	}
	
	/**
	 * @see IReferral.addInRedisSet
	 */
	@Override
	public void addInRedisSet(FileEntity fileEntity) {
		super.sadd(UserFileKeyForSet, fileEntity.getFileName());
	}

	/**
	 * @see IReferral.fetchUserIdsFromRedisSet
	 */
	@Override
	public Set<String> fetchUserFileAndAddInQueue() {
		return super.smembers(UserFileKeyForSet);
	}

	/**
	 * @see IReferral.deleteRedisUserIdSet
	 */
	@Override
	public void deleteItemFromRedisUserFileSet(String fileName) {
		super.srem(UserFileKeyForSet, fileName);
	}

}
