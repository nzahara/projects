package unccdb.component;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.List;

import unccdb.pojo.TutorRegisterDetails;
import unccdb.pojo.TutorScheduleDetails;
import unccdb.pojo.TutorServices;
import unccdb.pojo.User;
import unccdb.pojo.UserSearch;

public interface ILoginComponent {

	int insertRegisterData(User userData) throws IOException, PropertyVetoException;

	List<UserSearch> getAvailableServicesDetails(String searchString);

	boolean insertStudentServiceDetails(int userId, int serviceId);

	boolean payForServiceAvailed(int userId, int serviceId);

	List<TutorScheduleDetails> getTutorScheduleDetails(int tutorScheduleId, int userId);

	String getUserRole(int userId, String password);

	boolean tutorRegister(int userId);

	List<TutorServices> getTutorServiceDetails(int userId);

	boolean studentRegister(int userId);

	boolean changeTutorServiceStatus(int status, int serviceId);

	List<TutorRegisterDetails> getTutorRegisterDetails();

	List<TutorRegisterDetails> getCategoryList();

	boolean insertTutorRegisterDetails(int userId, int topicId, int classSize, String weekDay, String fromDate,
			String toDate, int serviceType);

}
