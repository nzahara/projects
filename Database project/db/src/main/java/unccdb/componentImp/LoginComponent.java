package unccdb.componentImp;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import unccdb.component.ILoginComponent;
import unccdb.daoImp.LoginDao;
import unccdb.pojo.TutorRegisterDetails;
import unccdb.pojo.TutorScheduleDetails;
import unccdb.pojo.TutorServices;
import unccdb.pojo.User;
import unccdb.pojo.UserSearch;

@Component
public class LoginComponent implements ILoginComponent {

	@Autowired
	LoginDao loginDao;

	@Override
	public int insertRegisterData(User userData) throws IOException, PropertyVetoException {
		return loginDao.insertUserRegisterData(userData);
	}

	@Override
	public List<UserSearch> getAvailableServicesDetails(String searchString) {
		return loginDao.getAvailableServicesDetails(searchString);
	}

	@Override
	public List<TutorScheduleDetails> getTutorScheduleDetails(int tutorScheduleId, int userId) {
		return loginDao.fetchTutorScheduleDetails(tutorScheduleId, userId);
	}

	@Override
	public boolean insertStudentServiceDetails(int userId, int serviceId) {
		return loginDao.insertStudentServiceDetails(userId, serviceId);
	}

	@Override
	public boolean payForServiceAvailed(int userId, int serviceId) {
		return loginDao.payForServiceAvailed(userId, serviceId);
	}

	@Override
	public String getUserRole(int userId, String password) {
		return loginDao.getUserRole(userId, password);
	}

	@Override
	public boolean tutorRegister(int userId) {
		return loginDao.insertTutorRole(userId, null, true);
	}

	@Override
	public boolean studentRegister(int userId) {
		return loginDao.insertStudentRole(userId, null, true);
	}

	@Override
	public List<TutorServices> getTutorServiceDetails(int userId) {
		return loginDao.getTutorServiceDetails(userId);
	}

	@Override
	public boolean changeTutorServiceStatus(int status, int serviceId) {
		return loginDao.changeTutorServiceStatus(status, serviceId);
	}

	@Override
	public List<TutorRegisterDetails> getTutorRegisterDetails() {
		return loginDao.getTutorRegisterDetails();
	}

	@Override
	public List<TutorRegisterDetails> getCategoryList() {
		return loginDao.getCategoryList();
	}
	
	@Override
	public boolean insertTutorRegisterDetails(int userId, int topicId,int classSize,String weekDay,String fromDate,String toDate,int serviceType) {
		return loginDao.insertTutorSchedule(userId, topicId, classSize, weekDay, fromDate, toDate, serviceType);
	}
}
