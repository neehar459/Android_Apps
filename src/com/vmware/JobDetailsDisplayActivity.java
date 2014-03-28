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
		setContentView(R.layout.activity_joblist_modified);
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
		TextView c0 = ((TextView) findViewById(R.id.jobID_dm));
		c0.setText(jobArray[0]);
		TextView c1 = ((TextView) findViewById(R.id.userID_dm));
		c1.setText(userName);
		TextView c2 = ((TextView) findViewById(R.id.creationDate_dm));
		c2.setText(jobArray[2]);
		TextView c3 = ((TextView) findViewById(R.id.endDate_dm));
		if(jobArray[3].length() == 0){
			c3.setText(" ---- ");
		}else{
			c3.setText(jobArray[3]);
		}
		TextView c4 = ((TextView) findViewById(R.id.selfTeam_dm));
		c4.setText(jobArray[4]);
		TextView c5 = ((TextView) findViewById(R.id.targetTeam_dm));
		c5.setText(jobArray[5]);
		TextView c6 = ((TextView) findViewById(R.id.priority_dm));
		c6.setText(jobArray[6]);
		TextView c7 = ((TextView) findViewById(R.id.targetDuration_dm));
		c7.setText(jobArray[7]+" Hours ");
		TextView c8 = ((TextView) findViewById(R.id.actualDuration_dm));
		if(jobArray[8].length() == 0){
			c8.setText(" ---- ");
		}else{
			c8.setText(jobArray[8]+" Hours ");
		}
		
		TextView c9 = ((TextView) findViewById(R.id.description_dm));
		c9.setText(jobArray[9]);
		TextView c10 = ((TextView) findViewById(R.id.statusID_dm));
		c10.setText(jobArray[10]);
		TextView c11 = ((TextView) findViewById(R.id.cpuUsage_dm));
		c11.setText(jobArray[11]);
		TextView c12 = ((TextView) findViewById(R.id.memoryUsage_dm));
		c12.setText(jobArray[12]);
		TextView c13 = ((TextView) findViewById(R.id.url_dm));
		c13.setText(jobArray[13]);
		
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
