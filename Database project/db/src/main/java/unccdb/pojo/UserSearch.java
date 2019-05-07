package unccdb.pojo;

public class UserSearch {

	private String firstName;

	private String lastName;

	private String subjectName;

	private int subjectId;

	private int topicId;

	private String topicName;

	private int tutorAvailabilityId;

	private String zipCode;

	private int serviceId;

	//private int registerFlag;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int subjectId) {
		this.subjectId = subjectId;
	}

	public int getTopicId() {
		return topicId;
	}

	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}

	public String getTopicName() {
		return topicName;
	}

	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}

	public String getZipCode() {
		return zipCode;
	}

	public int getTutorAvailabilityId() {
		return tutorAvailabilityId;
	}

	public void setTutorAvailabilityId(int tutorAvailabilityId) {
		this.tutorAvailabilityId = tutorAvailabilityId;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

//	public int getRegisterFlag() {
//		return registerFlag;
//	}
//
//	public void setRegisterFlag(int registerFlag) {
//		this.registerFlag = registerFlag;
//	}

}
