package unccdb.daoImp;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import unccdb.component.ILoginComponent;
import unccdb.dao.ILoginDao;
import unccdb.database_connections.CommonDataSource;
import unccdb.pojo.TutorRegisterDetails;
import unccdb.pojo.TutorScheduleDetails;
import unccdb.pojo.TutorServices;
import unccdb.pojo.User;
import unccdb.pojo.UserSearch;

@Repository
public class LoginDao implements ILoginDao {

	private static String insertRegisterData = " insert into users (first_name, last_name,email_id,"
			+ "password,address_line1,address_line2,city,state,country,zip_code) values (?,?,?,?,?,?,?,?,?,?)";

	private static String REGISTER_PROCEDURE = "{call REGISTER_USER(?,?,?,?,?,?,?,?,?,?,?,?)}";

	private static String insertUserRoleData = "insert into user_role (user_id,user_type) " + "values (?,?)";

	private static String insertStudentRoleData = "insert into student_role (user_id) values(?)";

	private static String insertTutorRoleData = "insert into tutor_role (user_id) values(?)";

	private static String getServiceAvailableDetails = "select usr.first_name, usr.last_name,sb.subject_name,sb.subject_id, "
			+ " tp.topic_id, tp.topic_name,sa.tutor_availability_id,usr.zip_code,sa.service_id "
			+ " from services_available sa join users usr on sa.user_id = usr.user_id "
			+ " inner join tutor_role ur on ur.user_id = usr.user_id "
			+ " join tutor_schedule ts on ts.user_id = usr.user_id  join topics tp on tp.topic_id = ts.topic_id "
			// + " left join student_services ss on ss.service_id = sa.service_id and
			// ss.user_id = usr.user_id "
			+ " join subjects sb on sb.subject_id = tp.subject_id  where sa.service_status = 1 ";

	private static String SERVICES_AVAILABLE_VIEW = "select first_name, last_name,subject_name,subject_id,"
			+ " topic_id, topic_name,tutor_availability_id,zip_code,service_id from searchServices where service_status = 1 ";

	private static String getTutorSchedule = " select av.*, st.service_type,CASE WHEN (ss.user_id is not null and ss.paid_status = 'NOT_PAID') "
			+ " THEN 1 WHEN (ss.user_id is not null and ss.paid_status = 'PAID') THEN 2 ELSE 0 END registerFlag from availability_timigs av "
			+ " join services_available sa on sa.tutor_availability_id = av.tutor_availability_id "
			+ " left join student_services ss on ss.service_id = sa.service_id and ss.user_id = ? "
			+ " join services_types st on st.service_type_id = av.service_type_id where av.tutor_availability_id = ? ";

	private static String insertStudentServicesDetails = "insert into student_services (user_id,service_id,paid_status)"
			+ "values (?,?,'NOT_PAID');";

	private static String updateServicesAvailableDetails = "update services_available set service_status = 0 "
			+ " where topic_id = ? and schedule_id = ? and user_id = ? ";

	private static String payForServiceAvailed = " update student_services set paid_status = 'PAID', "
			+ " availabilty_status = 1 where user_id = ? and service_id = ?";

	private static String GET_USER_ROLE = "select ifnull(tr.user_id,0) tutor, ifnull(sr.user_id,0) student from users usr "
			+ " left join student_role sr on sr.user_id = usr.user_id "
			+ " left join tutor_role tr on tr.user_id = usr.user_id where usr.user_id = ? and usr.password = ?";

	private static String GET_TUTOR_SCHEDULE_DETAILS = "select topic_name, subject_name,category_type,avail_time_from, avail_time_to, service_type,"
			+ "	class_size,at.tutor_availability_id,sa.service_status,sa.service_id from tutor_schedule ts join topics t on t.topic_id = ts.topic_id "
			+ " join subjects s on s.subject_id = t.subject_id join category c on c.category_id = s.category_id"
			+ " join availability_timigs at on at.schedule_id = ts.schedule_id join services_types st on "
			+ " st.service_type_id = at.service_type_id join services_available sa on sa.user_id = ts.user_id"
			+ " and at.tutor_availability_id = sa.tutor_availability_id where ts.user_id  = ?;";

	private static String GET_TUTOR_SCHEDULE_DETAILS_VIEW = "select topic_name, subject_name,category_type,avail_time_from, avail_time_to, service_type,"
			+ " class_size,tutor_availability_id,service_status,service_id from getTutorServiceDetails where user_id = ? ";

	private static String CHANGE_SERVICE_STATUS = "update services_available set service_status = ? where service_id = ? ";

	private static String GET_CATEGORY_SUBJECT_DETAILS = "select category_type,c.category_id,topic_name,t.topic_id,subject_name,s.subject_id"
			+ " from category c join subjects s on s.category_id = c.category_id join topics t on t.subject_id = s.subject_id ";

	private static String GET_CATEGORY_LIST = "select * from category ";

	private static String INSERT_TUTOR_SCHEDULE = "insert into tutor_schedule (user_id,topic_id,class_size)"
			+ "values (?,?,?)";

	private static String INSERT_AVAILABILITY_TIMINGS = "insert into availability_timigs(schedule_id,service_type_id,week_day,avail_time_from,avail_time_to)"
			+ " values(?,?,?,?,?) ";

	private static String INSERT_SERVICE_AVAILABLE = "insert into services_available(user_id,topic_id,tutor_availability_id) "
			+ "values (?,?,?)";

	private static String REGISTER_TUTOR_SERVICE_PROCEDURE = "{call REGISTER_TUTOR_SERVICE(?,?,?,?,?,?,?,?,?,?,?)}";

	/**
	 * The function inserts user data to user table and if it successfully inserts
	 * the data, it inserts user role details of corresponding user_id in user_role
	 * table
	 * 
	 * <pre>
	 * Table - users
	 * Table - user_role
	 * </pre>
	 * 
	 * @param userData
	 * @return
	 * @throws PropertyVetoException
	 * @throws IOException
	 */
	@Override
	public int insertUserRegisterData(User userData) throws IOException, PropertyVetoException {
		Connection conn = null;
		//CallableStatement pstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		int autoIncrementId = -1;
		boolean studentRoleRows = false;
		boolean tutorRoleRows = false;
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			connection.setAutoCommitPropetry(false, conn);
			pstmt = conn.prepareStatement(insertRegisterData, Statement.RETURN_GENERATED_KEYS);
			pstmt.setString(1, userData.getFirstName());
			pstmt.setString(2, userData.getLastName());
			pstmt.setString(3, userData.getEmailId());
			pstmt.setString(4, userData.getPassword());
			pstmt.setString(5, userData.getAddressLine1());
			pstmt.setString(6, userData.getAddressLine2());
			pstmt.setString(7, userData.getCity());
			pstmt.setString(8, userData.getState());
			pstmt.setString(9, userData.getCountry());
			pstmt.setString(10, userData.getZipCode());
			conn.commit();
			noOfRows = pstmt.executeUpdate();
			if (noOfRows < 1) {
				throw new SQLException("Unable to insert user details!");
			}
			rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				autoIncrementId = rs.getInt(1);
				System.out.println("Autoincrement id : " + autoIncrementId);
			}
//			userRoleRows = (autoIncrementId != -1) ? insertUserRoleData(autoIncrementId, userData.getUserType(), conn)
//					: false;
			String userType = userData.getUserType().trim();
			if (userType.equals("S") || userType.equals("B")) {
				studentRoleRows = insertStudentRole(autoIncrementId, conn, false);
				if (!studentRoleRows) {
					connection.rollbackConnection(conn);
				}
			}
			if (userType.equals("T") || userType.equals("B")) {
				tutorRoleRows = insertTutorRole(autoIncrementId, conn, false);
				if (!tutorRoleRows) {
					connection.rollbackConnection(conn);
				}
			}
		} catch (SQLIntegrityConstraintViolationException e) {
			return -2;
//			pstmt = conn.prepareCall(REGISTER_PROCEDURE);
//			pstmt.setString(1, userData.getFirstName());
//			pstmt.setString(2, userData.getLastName());
//			pstmt.setString(3, userData.getEmailId());
//			pstmt.setString(4, userData.getPassword());
//			pstmt.setString(5, userData.getAddressLine1());
//			pstmt.setString(6, userData.getAddressLine2());
//			pstmt.setString(7, userData.getCity());
//			pstmt.setString(8, userData.getState());
//			pstmt.setString(9, userData.getCountry());
//			pstmt.setString(10, userData.getZipCode());
//			pstmt.setString(11, userData.getUserType().toUpperCase());
//			pstmt.registerOutParameter(12, Types.INTEGER);
//			pstmt.executeUpdate();
//			autoIncrementId = pstmt.getInt(12);
		} catch (SQLException e) {
			System.out.println("An exception has occured while inserting" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return autoIncrementId;
	}

	/**
	 * The function inserts user role data i.e student (S) or tutor (T) .
	 * 
	 * <pre>
	 * Table : user_role
	 * </pre>
	 * 
	 * @param userId
	 * @param userType
	 * @return
	 */
	@Override
	public boolean insertUserRoleData(int userId, String userType, Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		try {
			pstmt = conn.prepareStatement(insertUserRoleData, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, userId);
			pstmt.setString(2, userType);
			noOfRows = pstmt.executeUpdate();
			if (noOfRows < 1) {
				throw new Exception("Unable to insert!");
			}
		} catch (Exception e) {
			System.out.println("An exception has occured while inserting data to user role table :" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return noOfRows == 1;

	}

	/**
	 * The function inserts user id to student_role table.
	 * 
	 * @param userId
	 * @param conn
	 * @return
	 */
	@Override
	public boolean insertStudentRole(int userId, Connection conn, boolean isConn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		try {
			if (isConn) {
				CommonDataSource connection = CommonDataSource.getInstance();
				conn = connection.getConnection();
			}
			pstmt = conn.prepareStatement(insertStudentRoleData);
			pstmt.setInt(1, userId);
			noOfRows = pstmt.executeUpdate();
			if (noOfRows < 1) {
				throw new Exception("Unable to insert!");
			}
		} catch (Exception e) {
			System.out
					.println("An exception has occured while inserting data to student role table :" + e.getMessage());
		} 
//		finally {
//			CommonDataSource.closeResources(rs, pstmt, conn);
//		}
		return noOfRows == 1;

	}

	/**
	 * The function inserts user id to student_role table.
	 * 
	 * @param userId
	 * @param conn
	 * @return
	 */
	@Override
	public boolean insertTutorRole(int userId, Connection conn, boolean isConn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		try {
			if (isConn) {
				CommonDataSource connection = CommonDataSource.getInstance();
				conn = connection.getConnection();
			}
			pstmt = conn.prepareStatement(insertTutorRoleData);
			pstmt.setInt(1, userId);
			noOfRows = pstmt.executeUpdate();
			if (noOfRows < 1) {
				throw new Exception("Unable to insert!");
			}
		} catch (Exception e) {
			System.out.println("An exception has occured while inserting data to tutor role table :" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return noOfRows == 1;

	}

	/**
	 * The function returns the available service details for the given search
	 * string i.e zipcode/subject name or none.
	 * 
	 * @param zipCode
	 * @param subjectName
	 * @return
	 */
	@Override
	public List<UserSearch> getAvailableServicesDetails(String searchString) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<UserSearch> userSearchList = new ArrayList<UserSearch>();
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			// StringBuilder query = new StringBuilder(getServiceAvailableDetails);
			StringBuilder query = new StringBuilder(SERVICES_AVAILABLE_VIEW);
			if (!(searchString.trim().isEmpty()) && searchString != null) {
				// query.append(" and (usr.zip_code = ? OR sb.subject_name like ?)");
				query.append(" and (zip_code = ? OR subject_name like ?)");
			}
			pstmt = conn.prepareStatement(query.toString());
			if (!(searchString.trim().isEmpty()) && searchString != null) {
				pstmt.setString(1, searchString);
				pstmt.setString(2, "%" + searchString + "%");
			}
			rs = pstmt.executeQuery();
			while (rs.next()) {
				UserSearch userSearchDetails = new UserSearch();
				userSearchDetails.setFirstName(rs.getString("first_name"));
				userSearchDetails.setLastName(rs.getString("last_name"));
				userSearchDetails.setSubjectName(rs.getString("subject_name"));
				userSearchDetails.setTopicName(rs.getString("topic_name"));
				userSearchDetails.setTopicId(rs.getInt("topic_id"));
				userSearchDetails.setSubjectId(rs.getInt("subject_id"));
				userSearchDetails.setTutorAvailabilityId(rs.getInt("tutor_availability_id"));
				userSearchDetails.setZipCode(rs.getString("zip_code"));
				userSearchDetails.setServiceId(rs.getInt("service_id"));
				// userSearchDetails.setRegisterFlag(rs.getInt("registerFlag"));
				userSearchList.add(userSearchDetails);
			}
		} catch (Exception e) {
			System.out.println("An exception has occured while inserting" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return userSearchList;
	}

	/**
	 * The function fetches the tutor schedule details for the given schedule id.
	 * 
	 * @param tutorAvailabilityId
	 * @return
	 */
	@Override
	public List<TutorScheduleDetails> fetchTutorScheduleDetails(int tutorAvailabilityId, int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TutorScheduleDetails> tutorScheduleDetailsList = new ArrayList<TutorScheduleDetails>();
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(getTutorSchedule);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, tutorAvailabilityId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TutorScheduleDetails tutorScheduleDetails = new TutorScheduleDetails();
				tutorScheduleDetails.setWeekDay(rs.getString("week_day"));
				tutorScheduleDetails.setFromTime(rs.getString("avail_time_from"));
				tutorScheduleDetails.setToTime(rs.getString("avail_time_to"));
				tutorScheduleDetails.setServiceType(rs.getString("service_type"));
				tutorScheduleDetails.setRegisterFlag(rs.getInt("registerFlag"));
				tutorScheduleDetailsList.add(tutorScheduleDetails);
			}
		} catch (Exception e) {
			System.out.println("An exception has occured while inserting" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return tutorScheduleDetailsList;
	}

	/**
	 * The function inserts the service details once the user registers for a
	 * service.
	 * 
	 * <pre>
	 * Table used : student_services
	 * </pre>
	 * 
	 * @param userId
	 * @param serviceId
	 * @return
	 */
	@Override
	public boolean insertStudentServiceDetails(int userId, int serviceId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(insertStudentServicesDetails);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, serviceId);
			noOfRows = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("An exception has occured while updating " + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return noOfRows == 1;
	}

	/**
	 * The function changes the paid status in student_services table to 'PAID' and
	 * also changes the status of service status to 1 i.e available.
	 * 
	 * @param userId
	 * @param serviceId
	 * @return
	 */
	@Override
	public boolean payForServiceAvailed(int userId, int serviceId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(payForServiceAvailed);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, serviceId);
			noOfRows = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("An exception has occured while updating " + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return noOfRows == 1;
	}

	/**
	 * The function changes the service status for the given service id accordingly.
	 * 
	 */
	@Override
	public boolean changeTutorServiceStatus(int status, int serviceId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(CHANGE_SERVICE_STATUS);
			pstmt.setInt(1, status);
			pstmt.setInt(2, serviceId);
			noOfRows = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("An exception has occured while updating " + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return noOfRows == 1;
	}

	public boolean deactivateAvailableService(int userId, int topicId, int scheduleId, int serviceId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		int autoIncrementId = -1;
		boolean userServices = false;
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
//			connection.setAutoCommitPropetry(false, conn);
//			pstmt = conn.prepareStatement(updateServicesAvailableDetails);
//			pstmt.setInt(1, topicId);
//			pstmt.setInt(2, scheduleId);
//			pstmt.setInt(3, userId);
//			conn.commit();
//			noOfRows = pstmt.executeUpdate();
//			if (noOfRows < 1) {
//				throw new Exception("Unable to update available services table!");
//			}
//			rs = pstmt.getGeneratedKeys();
//			while (rs.next()) {
//				autoIncrementId = rs.getInt(1);
//				System.out.println("Autoincrement id : " + autoIncrementId);
//			}
//			userServices = (autoIncrementId != -1) ? insertStudentServiceDetails(userId, autoIncrementId,conn)
//					: false;
//			if (!userServices) {
//				connection.rollbackConnection(conn);
//			}
			System.out.println("User role :" + userServices);
		} catch (Exception e) {
			System.out.println("An exception has occured while updating" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return noOfRows == 1 && userServices;
	}

	/**
	 * The function validates if the given userid and password and then returns 'S'
	 * - if student, 'B'- if both and 'T' - if tutor.
	 */
	@Override
	public String getUserRole(int userId, String password) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int tutorRole = 0;
		int studentRole = 0;
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(GET_USER_ROLE);
			pstmt.setInt(1, userId);
			pstmt.setString(2, password);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				tutorRole = rs.getInt("tutor");
				studentRole = rs.getInt("student");
			}
		} catch (Exception e) {
			System.out.println("An exception has occured while loggin " + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		if (tutorRole != 0 && studentRole != 0) {
			return "B";
		} else if (tutorRole != 0) {
			return "T";
		} else if (studentRole != 0) {
			return "S";
		}
		return "";
	}

	/**
	 * The function returns list of services that a given user (tutor) is offering.
	 */
	@Override
	public List<TutorServices> getTutorServiceDetails(int userId) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TutorServices> tutorServiceDetailsList = new ArrayList<TutorServices>();
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(GET_TUTOR_SCHEDULE_DETAILS_VIEW);
			pstmt.setInt(1, userId);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TutorServices tutorServiceDetails = new TutorServices();
				tutorServiceDetails.setSubjectName(rs.getString("subject_name"));
				tutorServiceDetails.setTopicName(rs.getString("topic_name"));
				tutorServiceDetails.setClassSize(rs.getInt("class_size"));
				tutorServiceDetails.setTutorAvailabilityId(rs.getInt("tutor_availability_id"));
				tutorServiceDetails.setFromTime(rs.getString("avail_time_from"));
				tutorServiceDetails.setToTime(rs.getString("avail_time_to"));
				tutorServiceDetails.setServiceType(rs.getString("service_type"));
				tutorServiceDetails.setCategoryType(rs.getString("category_type"));
				tutorServiceDetails.setServiceStatus(rs.getInt("service_status"));
				tutorServiceDetails.setServiceId(rs.getInt("service_id"));
				tutorServiceDetailsList.add(tutorServiceDetails);
			}
		} catch (Exception e) {
			System.out.println("An exception has occured while getting tutor service details" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return tutorServiceDetailsList;
	}

	@Override
	public List<TutorRegisterDetails> getTutorRegisterDetails() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TutorRegisterDetails> tutorRegisterDetailsList = new ArrayList<TutorRegisterDetails>();
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(GET_CATEGORY_SUBJECT_DETAILS);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TutorRegisterDetails tutorServiceDetails = new TutorRegisterDetails();
				tutorServiceDetails.setSubjectName(rs.getString("subject_name"));
				tutorServiceDetails.setTopicName(rs.getString("topic_name"));
				tutorServiceDetails.setCategoryType(rs.getString("category_type"));
				tutorServiceDetails.setSubjectId(rs.getInt("subject_id"));
				tutorServiceDetails.setCategoryId(rs.getInt("category_id"));
				tutorServiceDetails.setTopicId(rs.getInt("topic_id"));
				tutorRegisterDetailsList.add(tutorServiceDetails);
			}
		} catch (Exception e) {
			System.out.println("An exception has occured while getting tutor service details" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return tutorRegisterDetailsList;
	}

	@Override
	public List<TutorRegisterDetails> getCategoryList() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<TutorRegisterDetails> categoryList = new ArrayList<TutorRegisterDetails>();
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			pstmt = conn.prepareStatement(GET_CATEGORY_LIST);
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TutorRegisterDetails tutorServiceDetails = new TutorRegisterDetails();
				tutorServiceDetails.setCategoryType(rs.getString("category_type"));
				tutorServiceDetails.setCategoryId(rs.getInt("category_id"));
				categoryList.add(tutorServiceDetails);
			}
		} catch (Exception e) {
			System.out.println("An exception has occured while getting category list" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return categoryList;
	}

	/**
	 * 
	 * @param userId
	 * @param topicId
	 * @param classSize
	 * @param weekDay
	 * @param fromDate
	 * @param toDate
	 * @param serviceType
	 * @return
	 */
	@Override
	public boolean insertTutorSchedule(int userId, int topicId, int classSize, String weekDay, String fromDate,
			String toDate, int serviceType) {
		Connection conn = null;
		//CallableStatement pstmt = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		int scheduleId = -1;
		int tutorAvailabilityId = -1;
		try {
			CommonDataSource connection = CommonDataSource.getInstance();
			conn = connection.getConnection();
			connection.setAutoCommitPropetry(false, conn);
			pstmt = conn.prepareStatement(INSERT_TUTOR_SCHEDULE, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, topicId);
			pstmt.setInt(3, classSize);
			conn.commit();
			noOfRows = pstmt.executeUpdate();
			if (noOfRows < 1) {
				throw new SQLException("Unable to insert tutor schedule details!");
			}
			rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				scheduleId = rs.getInt(1);
				System.out.println("Schedule id : " + scheduleId);
			}
			tutorAvailabilityId = (scheduleId != -1)
					? insertAvailabilityTimings(scheduleId, serviceType, weekDay, fromDate, toDate, conn)
					: -1;
			if (tutorAvailabilityId == -1 || scheduleId == -1) {
				connection.rollbackConnection(conn);
				return false;
			}
			if (!insertAvailableServices(userId, topicId, tutorAvailabilityId, conn)) {
				connection.rollbackConnection(conn);
				return false;
			}
//			pstmt = conn.prepareCall(REGISTER_TUTOR_SERVICE_PROCEDURE);
//			pstmt.setInt(1, userId);
//			pstmt.setInt(2, topicId);
//			pstmt.setInt(3, classSize);
//			pstmt.setString(4, weekDay);
//			pstmt.setString(5, fromDate);
//			pstmt.setString(6, toDate);
//			pstmt.setInt(7, serviceType);
//			noOfRows = pstmt.executeUpdate();
		} catch (Exception e) {
			System.out.println("An exception has occured while inserting" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return noOfRows == 1;
	}

	public int insertAvailabilityTimings(int scheduleId, int serviceType, String weekDay, String fromDate,
			String toDate, Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		int tutorAvailabilityId = -1;
		try {
			pstmt = conn.prepareStatement(INSERT_AVAILABILITY_TIMINGS, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, scheduleId);
			pstmt.setInt(2, serviceType);
			pstmt.setString(3, weekDay);
			pstmt.setString(4, fromDate);
			pstmt.setString(5, toDate);
			noOfRows = pstmt.executeUpdate();
			if (noOfRows < 1) {
				throw new Exception("Unable to insert!");
			}
			rs = pstmt.getGeneratedKeys();
			while (rs.next()) {
				tutorAvailabilityId = rs.getInt(1);
				System.out.println("Tutor Schedule id : " + tutorAvailabilityId);
			}
		} catch (Exception e) {
			System.out.println(
					"An exception has occured while inserting data to availability timings table :" + e.getMessage());
		}
		return tutorAvailabilityId;

	}

	public boolean insertAvailableServices(int userId, int topicId, int tutorAvailabilityId, Connection conn) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int noOfRows = -1;
		try {
			pstmt = conn.prepareStatement(INSERT_SERVICE_AVAILABLE, Statement.RETURN_GENERATED_KEYS);
			pstmt.setInt(1, userId);
			pstmt.setInt(2, topicId);
			pstmt.setInt(3, tutorAvailabilityId);
			noOfRows = pstmt.executeUpdate();
			if (noOfRows < 1) {
				throw new Exception("Unable to insert!");
			}
		} catch (Exception e) {
			System.out.println(
					"An exception has occured while inserting data to services available table :" + e.getMessage());
		} finally {
			CommonDataSource.closeResources(rs, pstmt, conn);
		}
		return noOfRows == 1;

	}
}
