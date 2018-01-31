package com.boilerplate.database.mysql.implementations;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.boilerplate.database.interfaces.IFile;

public class MySQLFile extends MySQLBaseDataAccessLayer implements IFile  {

	@Override
	public String saveFile(MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPreSignedS3URL(String id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mySqlSaveFile(MultipartFile file) throws IOException {
		// TODO Auto-generated method stub
		
	}

}
