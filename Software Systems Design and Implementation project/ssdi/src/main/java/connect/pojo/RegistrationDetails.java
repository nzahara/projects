package connect.pojo;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * Used to store registration details of a user.
 * 
 * @author Noor Zahara
 *
 */
public class RegistrationDetails {

	@NotNull
	@JsonProperty(value = "student_id")
	private int studentId;

	@NotBlank
	@NotEmpty
	@JsonProperty(value = "first_name")
	private String firstName;

	@JsonProperty(value = "middle_name")
	private String middleName;

	@NotBlank
	@JsonProperty(value = "last_name")
	private String lastName;

	@NotBlank
	@JsonProperty(value = "personal_email_id")
	private String personalEmailId;

	@NotBlank
	@JsonProperty(value = "contact_number")
	@Pattern(regexp = "[0-9]{10}", message = "Kindly enter a valid contact number!")
	private String contactNumber;

	@NotBlank
	@JsonProperty(value = "password")
	private String password;

	@NotBlank
	@JsonProperty(value = "linked_in_id")
	private String linkedInId;

	@NotNull
	@JsonProperty(value = "graduation_year")
	private int graduationYear;

	@NotBlank
	@JsonProperty(value = "major")
	private String major;

	@NotBlank
	@JsonProperty(value = "student_type")
	private String studentType;

	private String profileImage;

	public int getStudentId() {
		return studentId;
	}

	public void setStudentId(int studentId) {
		this.studentId = studentId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPersonalEmailId() {
		return personalEmailId;
	}

	public void setPersonalEmailId(String personalEmailId) {
		this.personalEmailId = personalEmailId;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLinkedInId() {
		return linkedInId;
	}

	public void setLinkedInId(String linkedInId) {
		this.linkedInId = linkedInId;
	}

	public int getGraduationYear() {
		return graduationYear;
	}

	public void setGraduationYear(int graduationYear) {
		this.graduationYear = graduationYear;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public String getStudentType() {
		return studentType;
	}

	public void setStudentType(String studentType) {
		this.studentType = studentType;
	}

	public String getProfileImage() {
		return profileImage;
	}

	public void setProfileImage(String profileImage) {
		this.profileImage = profileImage;
	}

}
