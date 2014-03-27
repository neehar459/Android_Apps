package com.vmware;

// This class is equivalent representation of Job Details table in Database
public class JobDetails {
	
	private String jobID;
	private String userID;
	private String creationDate;
	private String endDate;
	private String selfTeam;
	private String targetTeam;
	private String priority;
	private String targetDuration;
	private String actualDuration;
	private String description;
	private String statusID;
	private String cpuUsage;
	private String memoryUsage;
	private String url;
	public String getJobID() {
		return jobID;
	}
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getCreationDate() {
		return creationDate;
	}
	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}
	public String getSelfTeam() {
		return selfTeam;
	}
	public void setSelfTeam(String selfTeam) {
		this.selfTeam = selfTeam;
	}
	public String getTargetTeam() {
		return targetTeam;
	}
	public void setTargetTeam(String targetTeam) {
		this.targetTeam = targetTeam;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public String getTargetDuration() {
		return targetDuration;
	}
	public void setTargetDuration(String targetDuration) {
		this.targetDuration = targetDuration;
	}
	public String getActualDuration() {
		return actualDuration;
	}
	public void setActualDuration(String actualDuration) {
		this.actualDuration = actualDuration;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getStatusID() {
		return statusID;
	}
	public void setStatusID(String statusID) {
		this.statusID = statusID;
	}
	public String getCpuUsage() {
		return cpuUsage;
	}
	public void setCpuUsage(String cpuUsage) {
		this.cpuUsage = cpuUsage;
	}
	public String getMemoryUsage() {
		return memoryUsage;
	}
	public void setMemoryUsage(String memoryUsage) {
		this.memoryUsage = memoryUsage;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
}
