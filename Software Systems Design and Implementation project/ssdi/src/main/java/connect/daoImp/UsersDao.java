package connect.daoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import connect.database_connections.CommonDataSource;
import connect.pojo.RegistrationDetails;

@Repository
public class UsersDao {

	private static String insertRegistrationDetails = " insert into Users (student_id, first_name, middle_name,last_name,"
			+ " personal_email_id,contact_number,password,linked_in_id,graduation_year,major,student_type,profile_pic)"
			+ " values (?,?,?,?,?,?,?,?,?,?,?,?);";

	private static String checkLoginCredentials = "select student_id from Users where student_id =? and password =? ";

	private static String searchForProfile = "select first_name, student_id,ifnull(middle_name,'NA') middle_name, last_name, graduation_year, profile_pic,linked_in_id,major, "
			+ " contact_number,password,personal_email_id,student_type from users where ";

	private static String updateUserProfile = "update users set first_name =?, middle_name =?,last_name=?,personal_email_id=?,contact_number=?,"
			+ " password=?,linked_in_id =?,graduation_year =?,major=?,student_type=?,profile_pic=? where student_id = ?";

	/**
	 * The function inserts the student details entered during registration process
	 * into the DB.
	 * 
	 * <pre>
	 * Table Used : Users
	 * </pre>
	 * 
	 * @param registrationDetails
	 * @return
	 */
	public int insertRegistrationDetails(RegistrationDetails registrationDetails) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CommonDataSource dataSource = null;
		int successfullyUpdated = -1;
		try {
			dataSource = CommonDataSource.getInstance();
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(insertRegistrationDetails);
			pstmt.setLong(1, registrationDetails.getStudentId());
			pstmt.setString(2, registrationDetails.getFirstName());
			pstmt.setString(3, registrationDetails.getMiddleName());
			pstmt.setString(4, registrationDetails.getLastName());
			pstmt.setString(5, registrationDetails.getPersonalEmailId());
			pstmt.setString(6, registrationDetails.getContactNumber());
			pstmt.setString(7, registrationDetails.getPassword());
			pstmt.setString(8, registrationDetails.getLinkedInId());
			pstmt.setInt(9, registrationDetails.getGraduationYear());
			pstmt.setString(10, registrationDetails.getMajor());
			pstmt.setString(11, registrationDetails.getStudentType());
			pstmt.setString(12, registrationDetails.getProfileImage());
			successfullyUpdated = pstmt.executeUpdate();
		} catch (SQLIntegrityConstraintViolationException e) {
			System.out.println(e.getMessage());
			if(e.getMessage().contains("Duplicate entry")) {
				return -2;	
			}
			return -1;
		} catch (Exception e) {
			System.out.println("Exception occurred while registering " + e.getMessage());
		} finally {
			dataSource.closeResources(rs, pstmt, conn);
		}
		return successfullyUpdated;
	}

	/**
	 * The function validates the credentials specified with the details stored int
	 * the DB.
	 * 
	 * @param studentId
	 * @param password
	 * @return
	 */
	public boolean checkLoginCredentials(long studentId, String password) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CommonDataSource dataSource = null;
		int successfullyUpdated = -1;
		try {
			dataSource = CommonDataSource.getInstance();
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(checkLoginCredentials);
			pstmt.setLong(1, studentId);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				successfullyUpdated = rs.getInt("student_id");
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while validating credentials " + e.getMessage());
		} finally {
			dataSource.closeResources(rs, pstmt, conn);
		}
		return successfullyUpdated == studentId;
	}

	/**
	 * The function returns the profile details based on the search string which can
	 * be either student's name or id.
	 * 
	 * @param searchString
	 * @return
	 */
	public List<RegistrationDetails> searchForProfile(String searchString) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CommonDataSource dataSource = null;
		List<RegistrationDetails> userSearchList = new ArrayList<RegistrationDetails>();
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			StringBuilder query = new StringBuilder(searchForProfile);
			if (!(searchString.trim().isEmpty()) && searchString != null) {
				query.append(" (first_name like ? OR middle_name like ? OR last_name like ? OR student_id = ?)");
			}
			pstmt = conn.prepareStatement(query.toString());
			if (!(searchString.trim().isEmpty()) && searchString != null) {
				pstmt.setString(1, "%" + searchString + "%");
				pstmt.setString(2, "%" + searchString + "%");
				pstmt.setString(3, "%" + searchString + "%");
				try {
					pstmt.setInt(4, Integer.valueOf(searchString));
				} catch (NumberFormatException e) {
					pstmt.setInt(4, 0);
				}
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				RegistrationDetails details = new RegistrationDetails();
				details.setFirstName(rs.getString("first_name"));
				details.setMiddleName(rs.getString("middle_name"));
				details.setLastName(rs.getString("last_name"));
				details.setGraduationYear(rs.getInt("graduation_year"));
				details.setProfileImage(rs.getString("profile_pic"));
				details.setMajor(rs.getString("major"));
				details.setLinkedInId(rs.getString("linked_in_id"));
				details.setStudentId(rs.getInt("student_id"));
				details.setContactNumber(rs.getString("contact_number"));
				details.setPersonalEmailId(rs.getString("personal_email_id"));
				details.setPassword(rs.getString("password"));
				details.setStudentType(rs.getString("student_type"));
				userSearchList.add(details);
			}
		} catch (Exception e) {
			System.out.println("Exception occurred while fetching users " + e.getMessage());
		} finally {
			dataSource.closeResources(rs, pstmt, conn);
		}
		return userSearchList;
	}

	/**
	 * The function updates the user profile in DB with the details specified.
	 * 
	 * <pre>
	 * Table used: Users
	 * </pre>
	 * 
	 * @param registrationDetails
	 * @return
	 */
	public boolean updateUserProfile(RegistrationDetails registrationDetails) {
		Connection conn = null;
		ResultSet rs = null;
		PreparedStatement pstmt = null;
		CommonDataSource dataSource = null;
		int successfullyUpdated = -1;
		try {
			dataSource = CommonDataSource.getInstance();
			conn = dataSource.getConnection();
			pstmt = conn.prepareStatement(updateUserProfile);
			pstmt.setString(1, registrationDetails.getFirstName());
			pstmt.setString(2, registrationDetails.getMiddleName());
			pstmt.setString(3, registrationDetails.getLastName());
			pstmt.setString(4, registrationDetails.getPersonalEmailId());
			pstmt.setString(5, registrationDetails.getContactNumber());
			pstmt.setString(6, registrationDetails.getPassword());
			pstmt.setString(7, registrationDetails.getLinkedInId());
			pstmt.setInt(8, registrationDetails.getGraduationYear());
			pstmt.setString(9, registrationDetails.getMajor());
			pstmt.setString(10, registrationDetails.getStudentType());
			pstmt.setString(11, registrationDetails.getProfileImage());
			pstmt.setInt(12, registrationDetails.getStudentId());
			successfullyUpdated = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("Exception occurred while updating " + e.getMessage());
		} finally {
			dataSource.closeResources(rs, pstmt, conn);
		}
		return successfullyUpdated == 1;
	}

}
