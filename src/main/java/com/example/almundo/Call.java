package com.example.almundo;

public class Call {
	private String callID;
	private Integer duration;
	private String employeeName;
	
	Call(String callID){
		this.callID = callID;
	}

	public String getCallID() {
		return callID;
	}

	public Integer getDuration() {
		return duration;
	}

	public void setDuration(Integer duration) {
		this.duration = duration;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setCallID(String callID){
		this.callID = callID;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

}
