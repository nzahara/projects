package unccdb.controllers;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.models.auth.In;
import unccdb.componentImp.LoginComponent;
import unccdb.pojo.TutorRegisterDetails;
import unccdb.pojo.TutorScheduleDetails;
import unccdb.pojo.TutorServices;
import unccdb.pojo.User;
import unccdb.pojo.UserSearch;

@RestController
@CrossOrigin
public class LoginController {

	@Autowired
	LoginComponent loginComponent;

	@PostMapping(value = "/register")
	public int registerUser(@RequestBody User userData) throws IOException, PropertyVetoException {
		return loginComponent.insertRegisterData(userData);
	}

	@GetMapping(value = "/search")
	public List<UserSearch> getAvailableServices(@RequestParam(value = "search_string") String searchString) {
		return loginComponent.getAvailableServicesDetails(searchString);
	}

	@GetMapping(value = "/fetch/tutorschedule")
	public List<TutorScheduleDetails> getTutorScheduleDetails(
			@RequestParam(value = "tutor_schedule_id") String tutorScheduleId,
			@RequestParam(value = "user_id") int userId) {
		return loginComponent.getTutorScheduleDetails(Integer.parseInt(tutorScheduleId), userId);
	}

	@PostMapping(value = "/student/register/{user_id}/{service_id}")
	public boolean insertStudentServiceDetails(@PathVariable("user_id") int userId,
			@PathVariable("service_id") int serviceId) {
		return loginComponent.insertStudentServiceDetails(userId, serviceId);
	}

	@PostMapping(value = "/student/pay/{user_id}/{service_id}")
	public boolean payForServiceAvailed(@PathVariable("user_id") int userId,
			@PathVariable("service_id") int serviceId) {
		return loginComponent.payForServiceAvailed(userId, serviceId);
	}

	@GetMapping(value = "/login")
	public String userLogin(@RequestParam(value = "user_name") int userId,
			@RequestParam(value = "password") String password) {
		return loginComponent.getUserRole(userId, password);
	}

	@PostMapping(value = "/register/{user_id}")
	public boolean tutorRegister(@PathVariable(value = "user_id") int userId) {
		return loginComponent.tutorRegister(userId);
	}

	@PostMapping(value = "/student/register/{user_id}")
	public boolean studentRegister(@PathVariable(value = "user_id") int userId) {
		return loginComponent.studentRegister(userId);
	}

	@GetMapping(value = "/tutordetails/{user_id}")
	public List<TutorServices> getTutorServiceDetails(@PathVariable(value = "user_id") int userId) {
		return loginComponent.getTutorServiceDetails(userId);

	}

	@PostMapping(value = "/change/service/status")
	public boolean changeTutorServiceStatus(@RequestParam(value = "status") int status,
			@RequestParam(value = "service_id") int serviceId) {
		return loginComponent.changeTutorServiceStatus(status, serviceId);
	}

	@GetMapping(value = "/tutor/register/details")
	
	public List<TutorRegisterDetails> getTutorRegisterDetails() {
		return loginComponent.getTutorRegisterDetails();
	}

	@GetMapping(value = "/category")
	public List<TutorRegisterDetails> getCategoryList() {
		return loginComponent.getCategoryList();
	}

	@PostMapping(value = "/tutor/service/register")
	public boolean insertTutorServiceDetails(@RequestParam(value = "userId") int userId,
			@RequestParam(value = "topicId") int topicId, @RequestParam(value = "classSize") int classSize,
			@RequestParam(value = "weekDay") String weekDay, @RequestParam(value = "fromDate") String fromDate,
			@RequestParam(value = "toDate") String toDate, @RequestParam(value = "serviceType") int serviceType) {
		return loginComponent.insertTutorRegisterDetails(userId, topicId, classSize, weekDay, fromDate, toDate,
				serviceType);
	}

}
