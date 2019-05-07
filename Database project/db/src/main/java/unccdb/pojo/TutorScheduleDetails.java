package unccdb.pojo;

public class TutorScheduleDetails {

	private String weekDay;

	private String fromTime;

	private String toTime;

	private String serviceType;

	private int registerFlag;

	public String getWeekDay() {
		return weekDay;
	}

	public void setWeekDay(String weekDay) {
		this.weekDay = weekDay;
	}

	public String getFromTime() {
		return fromTime;
	}

	public void setFromTime(String fromTime) {
		this.fromTime = fromTime;
	}

	public String getToTime() {
		return toTime;
	}

	public void setToTime(String toTime) {
		this.toTime = toTime;
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public int getRegisterFlag() {
		return registerFlag;
	}

	public void setRegisterFlag(int registerFlag) {
		this.registerFlag = registerFlag;
	}

}
