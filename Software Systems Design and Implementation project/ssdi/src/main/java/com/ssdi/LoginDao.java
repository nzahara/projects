package com.ssdi;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.database.CommonDataSource;


@Repository
public class LoginDao {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(LoginDao.class);
	
	
	private static String findAll = "select * from employee";
	
	
	public List<Employee> findAll() throws SQLException, ClassNotFoundException {
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<Employee> emp = new ArrayList<Employee>();
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			
			conn = connection.getConnection();
			LOGGER.debug("Connection:{}",conn);
			Statement createStatement = conn.createStatement();
			createStatement.execute(findAll);
			rs = createStatement.getResultSet();
			while(rs.next()) {
				Employee employee = new Employee();
				LOGGER.debug("Empl:" + rs.getString("emp_id"));
				employee.setContactNumber(rs.getString("contact_number"));
				employee.setEmplId(rs.getString("emp_id"));
				employee.setEmployeeName(rs.getString("employee_name"));
				employee.setSalary(rs.getFloat("salary"));
				emp.add(employee);
			}
		}catch (Exception e) {
			System.out.println("An exception has occured :" + e);
		}finally {
			CommonDataSource.closeResources(rs, stmt, conn);
		}
		return emp;
	}

}
