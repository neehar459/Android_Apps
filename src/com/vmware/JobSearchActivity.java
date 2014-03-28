package com.vmware;

import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



public class JobSearchActivity extends Activity {
	private static final String PREFRENCES_NAME = "myPrefs";
	DataHandler dbHandler;
	private String mJobID;
	private String mPriority;
	private String mStatus;
	private String mStartDate;
	private String mEndDate;
	private EditText mJobIDView;
	private EditText mStatusView;
	private EditText mStartDateView;
	private EditText mEndDateView;
	private Spinner mPrioritySpinner;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_jobsearch);
		setupActionBar();
		mJobIDView = (EditText) findViewById(R.id.jobID);
		mStatusView = (EditText) findViewById(R.id.status);
		mStartDateView = (EditText) findViewById(R.id.startdate);
		mEndDateView = (EditText) findViewById(R.id.enddate);
		addListenerOnSpinnerItemSelection();
	}
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}
	
	public void addListenerOnSpinnerItemSelection() {
		mPrioritySpinner = (Spinner) findViewById(R.id.priority_jobsearch);
		mPrioritySpinner.setOnItemSelectedListener(new OnItemSelectedListener());
	  }
	
	
	public void searchJob(View view) {
		try{
		mJobID = mJobIDView.getText().toString();
		mPriority = mPrioritySpinner.getSelectedItem().toString();
		mStatus = mStatusView.getText().toString();
		mStartDate = mStartDateView.getText().toString();
		mEndDate = mEndDateView.getText().toString();
		mJobIDView.setError(null);
		mStatusView.setError(null);
		mStartDateView.setError(null);
		mEndDateView.setError(null);
		
			SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
			String userName = sharedPref.getString("userName", "");
			if((mJobID.length() == 0)&& (mPriority.length() == 0)&& (mStatus.length() == 0)
					&& (mStartDate.length() == 0) && (mEndDate.length() == 0)){
				mJobIDView.setError(getString(R.string.error_empty_allfields));
				mJobIDView.requestFocus();
				
			}else if ((mStartDate.length()>0) && (mEndDate.length()==0)){
				mEndDateView.setError(getString(R.string.error_enddate_required));
				mEndDateView.requestFocus();
			}else if ((mStartDate.length()==0) && (mEndDate.length()>0)){
				mStartDateView.setError(getString(R.string.error_startdate_required));
				mStartDateView.requestFocus();
			}else if(mPriority.equalsIgnoreCase("-SELECT PRIORITY-") & (mStartDate.length()==0)& (mEndDate.length()==0)& (mJobID.length()==0)
					& (mStatus.length()==0)){
				Toast.makeText(this, "Please Select atleast Priority", Toast.LENGTH_LONG).show();
				return;
			}
			else{	
				dbHandler = new DataHandler(getBaseContext());
				dbHandler.open();
				List<String> jobList = dbHandler.fetchJobList(mJobID, mPriority, mStatus, mStartDate, mEndDate, userName);
				if(jobList == null){
					return;
				}
				if(jobList!=null && jobList.size() > 0){
					// forward it to next page
					Toast.makeText(getBaseContext(), "Success", Toast.LENGTH_LONG).show();
					String[] jobArray = new String[jobList.size()];
					jobArray = jobList.toArray(jobArray);
					int jobArrayLength = jobList.size();
					Intent resultIntent = new Intent(this, JobListDisplayActivity.class);
					  resultIntent.putExtra("JobList",jobArray);
					  resultIntent.putExtra("ListSize",jobArrayLength);
					  startActivity(resultIntent);
				}else{
					// no records found
					Toast.makeText(getBaseContext(), "No Records Found", Toast.LENGTH_LONG).show();
					
				}
				
			}
		}catch(Exception exception){
			Log.e("JOBSEARCHACTIVITY", exception.getMessage().toString()) ;
		}finally{
			if(dbHandler!=null ){
				dbHandler.close();
			}
		}
		
	}
	
	public void logoutLanding(View view) {
		SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean("isRegistered", true);
		editor.putBoolean("isLoggedin", false);
		editor.commit();
		
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		
	}

	public void resetLanding(View view) {
		mJobIDView.setText("");
		mPrioritySpinner.setSelection(0);
		mStatusView.setText("");
		mStartDateView.setText("");
		mEndDateView.setText("");
		mJobIDView.setError(null);
		mStatusView.setError(null);
		mStartDateView.setError(null);
		mEndDateView.setError(null);
	}
	public void backlanding(View view) {
		Intent intent = new Intent(this, JobLandActivity.class);
		startActivity(intent);
		
	}
}
