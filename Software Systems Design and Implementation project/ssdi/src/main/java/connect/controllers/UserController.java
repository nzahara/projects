package connect.controllers;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import connect.pojo.Message;
import connect.pojo.PostDetails;
import connect.pojo.RegistrationDetails;
import connect.serviceImp.PostService;
import connect.serviceImp.UserService;

@RestController
@CrossOrigin
public class UserController {

	@Autowired
	private UserService userService;

	@PostMapping("/register")
	public int registerStudentDetails(@RequestPart(value = "file") MultipartFile file,
		@RequestParam(value = "details") String registrationDetails) throws IOException {
		RegistrationDetails request = new ObjectMapper().readValue(registrationDetails, RegistrationDetails.class);
		return userService.registerStudentDetails(request, file);
	}

	@GetMapping("/login")
	public boolean checkLoginCredentials(@RequestParam(value = "student_id") long studentId,
			@RequestParam(value = "password") String password) {
		return userService.checkLoginCredentials(studentId, password);
	}

	@GetMapping("/search/{search_detail}")
	public List<RegistrationDetails> searchForProfile(@PathVariable(value = "search_detail") String searchString) {
		return userService.searchForProfile(searchString);
	}

	@PostMapping(value = "/update/profile")
	public boolean updateUserProfile(@RequestPart(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "details") String registrationDetails) throws IOException {
		//@Valid @RequestBody RegistrationDetails registrationDetails, @RequestPart) {
		RegistrationDetails request = new ObjectMapper().readValue(registrationDetails, RegistrationDetails.class);
		return userService.updateUserProfile(request, file);
	}

}
