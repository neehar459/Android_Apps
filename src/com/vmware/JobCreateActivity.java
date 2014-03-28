package com.vmware;

import java.util.Date;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;



public class JobCreateActivity extends Activity {
	private static final String PREFRENCES_NAME = "myPrefs";
	DataHandler dbHandler;
	Context context = this;
	//private EditText mSyncView;
	private String mPriority;
	private String mTeam;
	private Spinner mPrioritySpinner;
	private Spinner mTeamSpinner;
	private String mDescription;
	private String mDuration;
	private EditText mDescView;
	private EditText mDurationView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//System.out.println("In Create Activity");
		setContentView(R.layout.activity_jobcreate);
		setupActionBar();
		mDescView = (EditText) findViewById(R.id.description);
		mDurationView = (EditText) findViewById(R.id.duration);
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
		mPrioritySpinner = (Spinner) findViewById(R.id.priority);
		mPrioritySpinner.setOnItemSelectedListener(new OnItemSelectedListener());
		mTeamSpinner = (Spinner) findViewById(R.id.target_team_type);
		mTeamSpinner.setOnItemSelectedListener(new OnItemSelectedListener());
	  }
	
	
	public void createJob(View view) {
		try{
		mDescription = mDescView.getText().toString();
		mDuration = mDurationView.getText().toString();
		mTeam = mTeamSpinner.getSelectedItem().toString();
		mPriority = mPrioritySpinner.getSelectedItem().toString();
		
		
		
			if(mDescription == null || mDescription.length() == 0){
				mDescView.setError(getString(R.string.error_empty_description));
				mDescView.requestFocus();
			}else if(mPriority.equalsIgnoreCase("-SELECT PRIORITY-")){
				Toast.makeText(JobCreateActivity.this, "Please Select the Priority", Toast.LENGTH_LONG).show();
				return;
			}else if(mTeam.equalsIgnoreCase("-SELECT TARGET TEAM-")){
				Toast.makeText(JobCreateActivity.this, "Please Select the Target Team type", Toast.LENGTH_LONG).show();
				return;
			}
			else if (mDuration == null || mDuration.length() == 0){
				mDurationView.setError(getString(R.string.error_empty_duration));
				mDurationView.requestFocus();
			}else{
				SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
				String userName = sharedPref.getString("userName", "");	
				dbHandler = new DataHandler(getBaseContext());
				dbHandler.open();
				List<UserDetails> userList = dbHandler.fetchUserDetails(userName); 
				if(userList != null && userList.size() !=0){
					String userID = String.valueOf(userList.get(0).getUserID());
					Date todayDate = new Date();
					String selfTeam = userList.get(0).getTeam();
					long noOfInsertedRecords = dbHandler.createJob(userID, todayDate.toString(), selfTeam, mPriority, mTeam, mDuration, mDescription);
					if(noOfInsertedRecords > -1){
						Toast.makeText(getBaseContext(), "Task Created", Toast.LENGTH_LONG).show();
						AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
						alertDialogBuilder.setTitle("Create Job");
						alertDialogBuilder.setMessage("Do you want to create another job");
						alertDialogBuilder.setCancelable(false);
						alertDialogBuilder.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								resetBack();
							}
						  });
						alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,int id) {
								dialog.cancel();
							}
						});
						AlertDialog alertDialog = alertDialogBuilder.create();
						alertDialog.show();
					}else{
						mDurationView.setError(getString(R.string.error_jobfailure));
						mDurationView.requestFocus();
					}
				}
				
			}
		}catch(Exception exception){
			Log.e("JobCreateACTIVITY", exception.getMessage().toString()) ;
		}
		finally{
			if(dbHandler!=null ){
				dbHandler.close();
			}
		}
	}
	
	public void logout(View view) {
		SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean("isRegistered", true);
		editor.putBoolean("isLoggedin", false);
		editor.commit();
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	public void back(View view) {
		Intent intent = new Intent(this, JobLandActivity.class);
		startActivity(intent);
	}
	

	public void reset(View view) {
		mPrioritySpinner.setSelection(0);
		mTeamSpinner.setSelection(0);
		mDescView.setText("");
		mDurationView.setText("");
		mDescView.setError(null);
		mDurationView.setError(null);
		
	}
	
	public void resetBack(){
		Intent intent = new Intent(this, JobCreateActivity.class);
		startActivity(intent);
	}
}
