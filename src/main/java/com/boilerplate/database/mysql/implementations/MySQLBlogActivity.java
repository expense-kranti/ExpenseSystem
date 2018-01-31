package com.boilerplate.database.mysql.implementations;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Set;

import com.boilerplate.database.interfaces.IBlogActivity;
import com.boilerplate.framework.RequestThreadLocal;
import com.boilerplate.java.entities.BlogActivityEntity;

public class MySQLBlogActivity extends MySQLBaseDataAccessLayer implements IBlogActivity {
	
	@Override
	public void saveActivity(BlogActivityEntity blogActivityEntity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteActivity(String userId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Set<String> getAllBlogUserKeys(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void mySqlSaveBlogActivity(BlogActivityEntity blogActivityEntity) {
				// Save the user article
				super.create(blogActivityEntity);
		
	}

	@Override
	public void addInRedisSet(BlogActivityEntity blogActivity) {
		// TODO Auto-generated method stub
		
	}


}
