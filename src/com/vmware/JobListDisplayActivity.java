package com.vmware;

import android.annotation.TargetApi;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;



public class JobListDisplayActivity extends ListActivity {
	private static final String PREFRENCES_NAME = "myPrefs";
	DataHandler dbHandler;
	 ListView listView;
	 String jobIdForward = null;
	//private UserLoginTask mAuthTask = null;
	private EditText mSyncView;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//setContentView(R.layout.activity_joblist);
		//listView = (ListView) findViewById(R.id.job_list);
		listView = getListView();
		int listSize = getIntent().getIntExtra("ListSize", 0);
		String[] jobArray = new String[listSize];
		jobArray = (String[])getIntent().getStringArrayExtra("JobList");
		//ArrayAdapter<String> adapter =new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,android.R.id.text1, jobArray);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,R.layout.activity_joblist, android.R.id.text1, jobArray);
		listView.setAdapter(adapter);
	   listView.setOnItemClickListener(new OnItemClickListener() {
	                  @Override
	                  public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
	                   String  itemValue    = (String) listView.getItemAtPosition(position);
	                   //Toast.makeText(getApplicationContext(), ((TextView) view).getText(), Toast.LENGTH_SHORT).show();
	                    Toast.makeText(getApplicationContext(),
	                      " You have selected JobID : " +itemValue , Toast.LENGTH_LONG)
	                      .show();
	                   //System.out.println("in toast ////////////////////////////////////////////////////");
	                    jobIdForward = itemValue;
	    				forward();
	                  }
	             });
		/*setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_joblist,jobArray));
		ListView listView = getListView();
		listView.setTextFilterEnabled(true);
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
				// When clicked, show a toast with the TextView text
				Toast.makeText(getApplicationContext(),((TextView) view).getText(), Toast.LENGTH_SHORT).show();
				System.out.println(" in toast //////////////////////////////////////////////////////////////////////////");
				forward();
			}
		});*/
		//setContentView(R.layout.activity_joblist);
		setupActionBar();
		
	}
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	public void forward(){
		//System.out.println("Forward method called");
		Intent intentForward = new Intent(this,JobDetailsDisplayActivity.class);
		//System.out.println("JobId to forward : "+jobIdForward);
		intentForward.putExtra("JobIDForwarded", jobIdForward);
		startActivity(intentForward);
	}
	
	public void createJob(View view) {
		
		setContentView(R.layout.activity_jobcreate);
		Intent intent = new Intent(this, JobCreateActivity.class);
		startActivity(intent);	
			
	}
	
	public void logoutList(View view) {
		SharedPreferences sharedPref= getSharedPreferences(PREFRENCES_NAME, 0);
		SharedPreferences.Editor editor = sharedPref.edit();
		editor.putBoolean("isRegistered", true);
		editor.putBoolean("isLoggedin", false);
		editor.commit();
		
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
		
	}

	
	
	public void backlanding(View view) {
		Intent intent = new Intent(this, JobSearchActivity.class);
		startActivity(intent);
		
	}

	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
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
	}
}
