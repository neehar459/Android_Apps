package com.vmware;

import android.app.Application;

// This class saves all the variables needed to be saved at an application context
public class GlobalValues extends Application {
	private String userName;
	private boolean alreadyRegistered;
	private boolean loggedOut;
	public boolean isLoggedOut() {
		return loggedOut;
	}

	public void setLoggedOut(boolean loggedOut) {
		this.loggedOut = loggedOut;
	}
	
	public boolean isAlreadyRegistered() {
		return alreadyRegistered;
	}

	public void setAlreadyRegistered(boolean alreadyRegistered) {
		this.alreadyRegistered = alreadyRegistered;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}}
