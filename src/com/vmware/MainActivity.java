package com.vmware;


import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {
	private static final String PREFRENCES_NAME = "myPrefs";
	
		
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
		if(sharedPref!= null){
			//String userName = sharedPref.getString("userName", "");
			boolean isRegistered = sharedPref.getBoolean("isRegistered", false);
			boolean isLoggedin = sharedPref.getBoolean("isLoggedin", false);
			if (isRegistered == false){
				setContentView(R.layout.activity_main);
			}else if (isRegistered == true && isLoggedin == true){ // if the user is already registered and already loggedin, the landing page 
				// should be loaded
				
				Intent intent = new Intent(this, JobLandActivity.class);
				startActivity(intent);
			}else if (isRegistered == true && isLoggedin == false){ // if the user is already registered and not logged in, the login page
				// should be loaded
				
				Intent intent = new Intent(this, LoginActivity.class);
				startActivity(intent);
			}
		}else{
			// by default main activity should run if an exception occurs
			setContentView(R.layout.activity_main);
		}
		
		
	}
	


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	// Method for the button to go to next page
	public void enter(View view) {
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}
}
