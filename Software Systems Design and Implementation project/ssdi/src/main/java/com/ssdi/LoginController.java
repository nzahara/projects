package com.ssdi;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class LoginController {

@Autowired
LoginComponent loginComponent;


	@RequestMapping(value = "/find")
	public List<Employee> findAll() throws SQLException, ClassNotFoundException{
		return loginComponent.findAll();
	}
	
}
