package com.vmware;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class JobDetailsDisplayActivity extends Activity {
	private static final String PREFRENCES_NAME = "myPrefs";
	DataHandler dbHandler;
	String jobIDToQuery = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		jobIDToQuery = getIntent().getStringExtra("JobIDForwarded");
		setContentView(R.layout.activity_jobdetails);
		displayJobDetails(jobIDToQuery);
	}
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	public void displayJobDetails(String jobID) {
		try {
		SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
		String userName = sharedPref.getString("userName", "");
			if (jobID.length() != 0 & jobID != null) {

				dbHandler = new DataHandler(getBaseContext());
				dbHandler.open();
				String[] jobDetails = dbHandler
						.fetchJobDetails(jobID, userName);
				if (jobDetails == null) {
					return;
				}
				if (jobDetails != null && jobDetails.length > 0) {

					display(jobDetails, userName);
				} else {
					// no records found
					Toast.makeText(getBaseContext(), "No Records Found",
							Toast.LENGTH_LONG).show();

				}

			}
		}catch(Exception exception){
			Log.e("JOBDETAILSDISPLAYACTIVITY", exception.getMessage().toString()) ;
		} finally {
			if(dbHandler!=null ){
				dbHandler.close();
			}
		}
	}
	
	
	private void display(String[] jobArray, String userName) {
		TextView c0 = ((TextView) findViewById(R.id.jobID_d));
		c0.setText("Job ID : "+jobArray[0]);
		TextView c1 = ((TextView) findViewById(R.id.userID_d));
		c1.setText("Job Created By : "+userName);
		TextView c2 = ((TextView) findViewById(R.id.creationDate_d));
		c2.setText("Job Creation Date : "+jobArray[2]);
		TextView c3 = ((TextView) findViewById(R.id.endDate_d));
		if(jobArray[3].length() == 0){
			c3.setText("Job End Date : ---- ");
		}else{
			c3.setText("Job End Date : "+jobArray[3]+" Hours ");
		}
		TextView c4 = ((TextView) findViewById(R.id.selfTeam_d));
		c4.setText("Job created Team : "+jobArray[4]);
		TextView c5 = ((TextView) findViewById(R.id.targetTeam_d));
		c5.setText("Job targetted Team :"+jobArray[5]);
		TextView c6 = ((TextView) findViewById(R.id.priority_d));
		c6.setText("Priority : "+jobArray[6]);
		TextView c7 = ((TextView) findViewById(R.id.targetDuration_d));
		c7.setText("Expected Duration : "+jobArray[7]+" Hours ");
		TextView c8 = ((TextView) findViewById(R.id.actualDuration_d));
		if(jobArray[8].length() == 0){
			c8.setText("Actual Duration : ---- Hours ");
		}else{
			c8.setText("Actual Duration : "+jobArray[8]+" Hours ");
		}
		
		TextView c9 = ((TextView) findViewById(R.id.description_d));
		c9.setText("Description of job : "+jobArray[9]);
		TextView c10 = ((TextView) findViewById(R.id.statusID_d));
		c10.setText("Status of job : "+jobArray[10]);
		TextView c11 = ((TextView) findViewById(R.id.cpuUsage_d));
		c11.setText("CPU Usage : "+jobArray[11]);
		TextView c12 = ((TextView) findViewById(R.id.memoryUsage_d));
		c12.setText("Memory Usage : "+jobArray[12]);
		TextView c13 = ((TextView) findViewById(R.id.url_d));
		c13.setText("Results URL : "+jobArray[13]);
		
	}
	
	public void logoutDisplayJob(View view) {
		SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean("isRegistered", true);
		editor.putBoolean("isLoggedin", false);
		editor.commit();
		
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		
	}
	
	public void editJob(View view) {
		SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean("isRegistered", true);
		editor.putBoolean("isLoggedin", false);
		editor.commit();
		
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		
	}
	
	public void saveJob(View view) {
		SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean("isRegistered", true);
		editor.putBoolean("isLoggedin", false);
		editor.commit();
		
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		
	}
	
	
	public void backDisplayJob(View view) {
		Intent intent = new Intent(this, JobListDisplayActivity.class);
		startActivity(intent);
		
	}
}
