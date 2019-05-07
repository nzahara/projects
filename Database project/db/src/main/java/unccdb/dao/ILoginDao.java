package unccdb.dao;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

import unccdb.pojo.TutorRegisterDetails;
import unccdb.pojo.TutorScheduleDetails;
import unccdb.pojo.TutorServices;
import unccdb.pojo.User;
import unccdb.pojo.UserSearch;

public interface ILoginDao {

	int insertUserRegisterData(User userData) throws IOException, PropertyVetoException;

	boolean insertUserRoleData(int userId, String userType, Connection conn);

	boolean insertTutorRole(int userId, Connection conn, boolean isConn);

	boolean insertStudentRole(int userId, Connection conn, boolean isConn);

	List<UserSearch> getAvailableServicesDetails(String searchString);

	List<TutorScheduleDetails> fetchTutorScheduleDetails(int tutorAvailabilityId, int userId);

	boolean insertStudentServiceDetails(int userId, int serviceId);

	boolean payForServiceAvailed(int userId, int serviceId);

	String getUserRole(int userId, String password);

	List<TutorServices> getTutorServiceDetails(int userId);

	boolean changeTutorServiceStatus(int status, int userId);

	List<TutorRegisterDetails> getTutorRegisterDetails();

	List<TutorRegisterDetails> getCategoryList();

	boolean insertTutorSchedule(int userId, int topicId, int classSize, String weekDay, String fromDate, String toDate,
			int serviceType);
}
