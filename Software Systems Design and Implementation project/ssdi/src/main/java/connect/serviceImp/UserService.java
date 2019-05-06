package connect.serviceImp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import connect.daoImp.UsersDao;
import connect.pojo.RegistrationDetails;

@Service
public class UserService {

	@Autowired
	UsersDao userDao;

	@Autowired
	AmazonClient amazonClient;

	/**
	 * @see UsersDao#insertRegistrationDetails(RegistrationDetails)
	 * 
	 * @param registrationDetails
	 * @param file
	 * @return
	 */
	public int registerStudentDetails(RegistrationDetails registrationDetails, MultipartFile file) {
		String fileUrl = amazonClient.uploadPost("profilePic", registrationDetails.getStudentId(), file,
				file.getName());
		registrationDetails.setProfileImage(fileUrl);
		return userDao.insertRegistrationDetails(registrationDetails);
	}

	/**
	 * @see UsersDao#checkLoginCredentials(long, String)
	 * 
	 * @param studentId
	 * @param password
	 * @return
	 */
	public boolean checkLoginCredentials(long studentId, String password) {
		return userDao.checkLoginCredentials(studentId, password);
	}

	/**
	 * @see UsersDao#searchForProfile(String)
	 * 
	 * @param searchString
	 * @return
	 */
	public List<RegistrationDetails> searchForProfile(String searchString) {
		return userDao.searchForProfile(searchString);
	}

	/**
	 * @see UsersDao#updateUserProfile(RegistrationDetails)
	 * 
	 * @param registrationDetails
	 * @param file
	 * @return
	 */
	public boolean updateUserProfile(RegistrationDetails registrationDetails, MultipartFile file) {
		if( file != null) {
			String fileUrl = amazonClient.uploadPost("profilePic", registrationDetails.getStudentId(), file,
					file.getName());
			registrationDetails.setProfileImage(fileUrl);
		}
		return userDao.updateUserProfile(registrationDetails);
	}

}
