package com.vmware;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;



public class JobLandActivity extends Activity {
	private static final String PREFRENCES_NAME = "myPrefs";
	DataHandler dbHandler;
	//private UserLoginTask mAuthTask = null;
	//private EditText mSyncView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//System.out.println("Landing Page");
		setContentView(R.layout.activity_joblanding);
		setupActionBar();
		
	}
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	public void createJob(View view) {
		
		//setContentView(R.layout.activity_jobcreate);
		Intent intent = new Intent(this, JobCreateActivity.class);
		startActivity(intent);	
			
	}
	
	public void logoutLand(View view) {
		SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean("isRegistered", true);
		editor.putBoolean("isLoggedin", false);
		editor.commit();
		
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		
	}

	public void jobSearch(View view) {
		
		//setContentView(R.layout.activity_jobsearch);
		Intent intent = new Intent(this, JobSearchActivity.class);
		startActivity(intent);
		
	}
	
	

	/*public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.

			try {
				// Simulate network access.
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				return false;
			}

		// TODO: register the new account here.
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			//mAuthTask = null;

			if (success) {
				// finish(); should go to next page
				// nextActivity();
			} else {
				mSyncView.setError(getString(R.string.error_error_sync));
				mSyncView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			//mAuthTask = null;

		}
	}*/
}
