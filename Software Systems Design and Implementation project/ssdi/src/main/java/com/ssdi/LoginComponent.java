package com.ssdi;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LoginComponent {
	
	@Autowired
	LoginDao loginDao;
	
	
	public List<Employee> findAll() throws SQLException, ClassNotFoundException {
		return loginDao.findAll();
	}

}
