package connect.pojo;

import javax.validation.constraints.NotNull;

/**
 * Used to store messages sent by a user to another.
 * 
 * @author Noor Zahara
 *
 */
public class Notification {
	@NotNull
	private String message;

	@NotNull
	private int fromStudentId;

	@NotNull
	private int toStudentId;

	private String fileName;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getFromStudentId() {
		return fromStudentId;
	}

	public void setFromStudentId(int fromStudentId) {
		this.fromStudentId = fromStudentId;
	}

	public int getToStudentId() {
		return toStudentId;
	}

	public void setToStudentId(int toStudentId) {
		this.toStudentId = toStudentId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String toString() {
		return "{ \"message\" : \"" + getMessage() + "\",\n" + "\"fromStudentId\" : \"" + getFromStudentId() + "\",\n"
				+ "\"toStudentId\" : \"" + getToStudentId() + "\",\n" + "\"fileName\" : \"" + getFileName() + "\"\n"
				+ "}";

	}

}
