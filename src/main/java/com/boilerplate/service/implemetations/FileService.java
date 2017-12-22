package com.boilerplate.service.implemetations;

import java.io.IOException;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.database.interfaces.IFile;
import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.entities.FileEntity;
import com.boilerplate.java.entities.Role;
import com.boilerplate.service.interfaces.IFileService;

public class FileService implements IFileService {

	/**
	 * This is an instance of the logger
	 */
	Logger logger = Logger.getInstance(FileService.class);
	/**
	 * This is the instance of the configuration manager.
	 */
	@Autowired
	com.boilerplate.configurations.ConfigurationManager configurationManager;

	/**
	 * The setter to set the configuration manager
	 * 
	 * @param configurationManager
	 */
	public void setConfigurationManager(
			com.boilerplate.configurations.ConfigurationManager configurationManager) {
		this.configurationManager = configurationManager;
	}

	/**
	 * This is the file service provider on data layer
	 */
	@Autowired
	IFile file;

	/**
	 * Sets the file provider
	 * 
	 * @param file
	 *            The file provider
	 */
	public void setFile(IFile file) {
		this.file = file;
	}

	@Autowired
	IFilePointer filePointer;

	public void setFilePointer(IFilePointer filePointer) {
		this.filePointer = filePointer;
	}

	@Override
	public FileEntity saveFile(String fileMasterTag, MultipartFile file)
			throws UpdateFailedException {

		try {
			// validates the file size, If greater than Maximum_File_Upload_Size
			// then throws the IOException.
			if ((file.getSize() / (1024 * 1024)) > Long.parseLong(
					configurationManager.get("Maximum_File_Upload_Size"))) {
				throw new IOException("File size is greater than "
						+ configurationManager.get("Maximum_File_Upload_Size")
						+ "MB");
			}
			// save file to disk
			String fileNameOnDisk = this.file.saveFile(file);
			// save file data on DB

			FileEntity fileEntity = new FileEntity();
			// This will get content type of file
			fileEntity.setContentType(java.nio.file.Files.probeContentType(
					Paths.get(configurationManager.get("RootFileUploadLocation")
							+ fileNameOnDisk)));
			fileEntity.setFileMasterTag(fileMasterTag);
			fileEntity.setFileName(fileNameOnDisk);
			fileEntity.setFileNameOnDisk(fileNameOnDisk);
			fileEntity.setUserId(RequestThreadLocal.getSession()
					.getExternalFacingUser().getId());
			fileEntity.setOrganizationId(RequestThreadLocal.getSession()
					.getExternalFacingUser().getOrganizationId());
			fileEntity.setFullFileNameOnDisk(
					this.getPreSignedS3URL(fileNameOnDisk));
			fileEntity = filePointer.save(fileEntity);
			return fileEntity;
		} catch (IOException ex) {
			throw new UpdateFailedException("File", ex.toString(), ex);
		}
	}

	@Override
	public BoilerplateList<FileEntity> getAllFileListOnMasterTag(
			String fileMasterTag) {
	// set fullfile name on disk with pre signed url
	BoilerplateList<FileEntity> fileEntityList = new BoilerplateList<>();
	BoilerplateList<FileEntity> fileList = filePointer.getAllFilesOnMasterTag(
				RequestThreadLocal.getSession().getExternalFacingUser().getId(),
				fileMasterTag);
	for(Object fileObject:fileList){
		FileEntity fileEntity = (FileEntity) fileObject;
		fileEntity.setFullFileNameOnDisk(file.getPreSignedS3URL(fileEntity.getId()));
		fileEntityList.add(fileEntity);
	}
	return fileEntityList;
	}

	@Override
	public String getPreSignedS3URL(String id) {
		return file.getPreSignedS3URL(id);
	}
	
	/**
	 * @see IFileService.getFile
	 */
	@Override
	public FileEntity getFile(String id) throws NotFoundException {
		// check if the user has permission to read the file or if the file
		// exists
		FileEntity fileEntity = filePointer.getFilePointerById(id);
		if (fileEntity == null) {
			throw new NotFoundException("File", "Not found or unauthorized",
					null);
		}
		boolean userHasRightsOnFile = false;

		// validate that the user has the rights to read the file
		if (fileEntity.getUserId().equals(RequestThreadLocal.getSession()
				.getExternalFacingUser().getId())) {
			userHasRightsOnFile = true;
		}

		if (fileEntity.getOrganizationId() != null) {
			if (RequestThreadLocal.getSession().getExternalFacingUser()
					.getOrganizationId()
					.equals(fileEntity.getOrganizationId())) {
				userHasRightsOnFile = true;
			}
		}

		for (Role role : RequestThreadLocal.getSession().getExternalFacingUser()
				.getRoles()) {
			if (role.getRoleName().toUpperCase().equals("ADMIN") || role
					.getRoleName().toUpperCase().equals("BACKOFFICEUSER")) {
				userHasRightsOnFile = true;
				break;
			}
		}

		if (!userHasRightsOnFile) {
			throw new NotFoundException("File", "Not found or unauthorized",
					null);
		}
		return fileEntity;
	}
}
