package connect.pojo;

/**
 * Used to store message details fetched by the DB.
 * 
 * @author Noor Zahara
 * 
 *
 */
public class Message {

	private String message;

	private String attachment;

	private String firstName;

	private String middleName;

	private String lastName;

	private int gradationYear;

	private String linkedInId;

	private String major;

	private int fromStudentId;

	private String profilePic;

	private int messageId;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
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

	public int getGradationYear() {
		return gradationYear;
	}

	public void setGradationYear(int gradationYear) {
		this.gradationYear = gradationYear;
	}

	public String getLinkedInId() {
		return linkedInId;
	}

	public void setLinkedInId(String linkedInId) {
		this.linkedInId = linkedInId;
	}

	public String getMajor() {
		return major;
	}

	public void setMajor(String major) {
		this.major = major;
	}

	public int getFromStudentId() {
		return fromStudentId;
	}

	public void setFromStudentId(int fromStudentId) {
		this.fromStudentId = fromStudentId;
	}

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public int getMessageId() {
		return messageId;
	}

	public void setMessageId(int messageId) {
		this.messageId = messageId;
	}

}
