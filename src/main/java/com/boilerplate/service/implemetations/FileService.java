package com.boilerplate.service.implemetations;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.database.interfaces.IFile;
import com.boilerplate.database.interfaces.IFilePointer;
import com.boilerplate.exceptions.rest.NotFoundException;
import com.boilerplate.exceptions.rest.UnauthorizedException;
import com.boilerplate.exceptions.rest.UpdateFailedException;
import com.boilerplate.framework.Logger;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.collections.BoilerplateList;
import com.boilerplate.java.collections.BoilerplateMap;
import com.boilerplate.java.entities.FileEntity;
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
	 *            to set
	 */
	public void setConfigurationManager(com.boilerplate.configurations.ConfigurationManager configurationManager) {
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

	/**
	 * This is the instance of filePointer
	 */
	@Autowired
	IFilePointer filePointer;

	/**
	 * Sets the file pointer
	 * 
	 * @param filePointer
	 *            to set
	 */
	public void setFilePointer(IFilePointer filePointer) {
		this.filePointer = filePointer;
	}

	/**
	 * @throws Exception
	 * @see IFileService.saveFile
	 */
	@Override
	public FileEntity saveFile(String fileMasterTag, MultipartFile file) throws Exception {

		try {
			// validates the file size, If greater than Maximum_File_Upload_Size
			// then throws the IOException.
			if ((file.getSize() / (1024 * 1024)) > Long
					.parseLong(configurationManager.get("Maximum_File_Upload_Size"))) {
				throw new IOException(
						"File size is greater than " + configurationManager.get("Maximum_File_Upload_Size") + "MB");
			}
			// save file to disk
			String fileNameOnDisk = this.file.saveFile(file);
			// save file data on DB

			FileEntity fileEntity = new FileEntity();
			// This will get content type of file
			fileEntity.setContentType(java.nio.file.Files
					.probeContentType(Paths.get(configurationManager.get("RootFileUploadLocation") + fileNameOnDisk)));
			fileEntity.setFileMasterTag(fileMasterTag);
			fileEntity.setFileName(fileNameOnDisk);
			fileEntity.setFileNameOnDisk(fileNameOnDisk);
			fileEntity.setUserId(RequestThreadLocal.getSession().getExternalFacingUser().getId());
			// fileEntity.setOrganizationId(RequestThreadLocal.getSession().getExternalFacingUser().getOrganizationId());
			fileEntity.setFullFileNameOnDisk(this.getPreSignedS3URL(fileNameOnDisk));

			// method to save file entity in redis database
			fileEntity = filePointer.save(fileEntity);

			// if true the add redis key in set for database migration
			if (Boolean.parseBoolean(configurationManager.get("IsMySQLPublishQueueEnabled"))) {
				filePointer.addInRedisSet(fileEntity.getFileName());
			}
			return fileEntity;
		} catch (IOException ex) {
			throw new UpdateFailedException("File", ex.toString(), ex);
		}
	}

	/**
	 * @see IFileService.getAllFileListOnMasterTag
	 */
	@Override
	public BoilerplateList<FileEntity> getAllFileListOnMasterTag(String fileMasterTag) {
		// set fullfile name on disk with pre signed url
		BoilerplateList<FileEntity> fileEntityList = new BoilerplateList<>();
		BoilerplateList<FileEntity> fileList = filePointer
				.getAllFilesOnMasterTag(RequestThreadLocal.getSession().getExternalFacingUser().getId(), fileMasterTag);
		for (Object fileObject : fileList) {
			FileEntity fileEntity = (FileEntity) fileObject;
			fileEntity.setFullFileNameOnDisk(file.getPreSignedS3URL(fileEntity.getId()));
			fileEntityList.add(fileEntity);
		}
		return fileEntityList;
	}

	/**
	 * @see IFileService.getPreSignedS3URL
	 */
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
			throw new NotFoundException("File", "Not found or unauthorized", null);
		}
		boolean userHasRightsOnFile = false;

		// validate that the user has the rights to read the file
		if (fileEntity.getUserId().equals(RequestThreadLocal.getSession().getExternalFacingUser().getId())) {
			userHasRightsOnFile = true;
		}

		if (fileEntity.getOrganizationId() != null) {
			// if
			// (RequestThreadLocal.getSession().getExternalFacingUser().getOrganizationId()
			// .equals(fileEntity.getOrganizationId())) {
			// userHasRightsOnFile = true;
			// }
		}

		// for (Role role :
		// RequestThreadLocal.getSession().getExternalFacingUser().getRoles()) {
		// if (role.getRoleName().toUpperCase().equals("ADMIN")
		// || role.getRoleName().toUpperCase().equals("BACKOFFICEUSER")) {
		// userHasRightsOnFile = true;
		// break;
		// }
		// }

		if (!userHasRightsOnFile) {
			throw new NotFoundException("File", "Not found or unauthorized", null);
		}
		return fileEntity;
	}

	/**
	 * @see IFileService.getAllFileList
	 */
	@Override
	public BoilerplateMap<String, FileEntity> getAllFileList(String userId) throws UnauthorizedException {
		boolean canExecute = false;
		// for (Role role :
		// RequestThreadLocal.getSession().getExternalFacingUser().getRoles()) {
		// if (role.getRoleName().toUpperCase().equals("ADMIN")
		// || role.getRoleName().toUpperCase().equals("BACKOFFICEUSER")) {
		// canExecute = true;
		// }
		// }
		if (RequestThreadLocal.getSession().getExternalFacingUser().getId().equals(userId)) {
			canExecute = true;
		}

		if (!canExecute)
			throw new UnauthorizedException("File", "User doesnt have permissions to get files", null);

		BoilerplateList<FileEntity> files = this.filePointer.getAllFiles(userId, null);
		BoilerplateMap<String, FileEntity> map = new BoilerplateMap<String, FileEntity>();

		for (Object file : files) {
			map.put(((FileEntity) file).getId(), (FileEntity) file);
		}
		return map;
	}

	/**
	 * @see IFileService.updateFileEntity
	 */
	@Override
	public void updateFileEntity(FileEntity fileEntity) {
		try {
			filePointer.save(fileEntity);
		} catch (Exception ex) {
			logger.logException("FileService", "updateFileEntity", "", "ExceptionUpdateFileEntity", ex);
		}
	}

	/**
	 * @see IFileService.saveFileOnLocal
	 */
	@Override
	public String saveFileOnLocal(String fileMasterTag, MultipartFile file)
			throws UpdateFailedException, Exception {
		//generate unique file name for saving on local disk
		String fileNameOnDisk = UUID.randomUUID().toString() + "_" + file.getOriginalFilename() + "_"
				+ RequestThreadLocal.getSession().getExternalFacingUser().getUserId();
		//get path for saving file from configuration
		String rootFileUploadLocation = configurationManager.get("DESTINATION_FOR_SAVING_FILE_ON_DISK");
		//generate path with file name
		String filePath = rootFileUploadLocation + "/" + fileNameOnDisk;
		//save file name onto disk
		file.transferTo(new java.io.File(filePath));
		return fileNameOnDisk;
	}
}
