package com.vmware;



import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

@SuppressLint("DefaultLocale")
public class LoginActivity extends Activity {

	private UserLoginTask mAuthTask = null;
	private static final String PREFRENCES_NAME = "myPrefs";
	DataHandler dbHandler;
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	private int atCount;
	private int dotCount;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		setupActionBar();

		// Set up the login form.
		mEmailView = (EditText) findViewById(R.id.email);
		mPasswordView = (EditText) findViewById(R.id.password);
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}, if the API is available.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private void setupActionBar() {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			// Show the Up button in the action bar.
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}

	public void validUser(View view) {
		// Reset errors.
		try{
		mEmailView.setError(null);
		mPasswordView.setError(null);
		atCount = 0;
		dotCount = 0;
		if (mAuthTask != null) {
			return;
		}
		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mEmail =mEmail.toLowerCase();
		mPassword = mPasswordView.getText().toString();
		
		
		
			boolean cancel = false;
			View focusView = null;
			dbHandler = new DataHandler(getBaseContext());
			dbHandler.open();
			boolean validEmail = dbHandler.isUserRegistered(mEmail);
			if (TextUtils.isEmpty(mEmail)) {
				mEmailView.setError(getString(R.string.error_email_required));
				focusView = mEmailView;
				cancel = true;
			} else {
				for (int i = 0; i < mEmail.length(); i++) {
					if (mEmail.charAt(i) == '@') {
						atCount++;
					}
					if (mEmail.charAt(i) == '.') {
						dotCount++;
					}
				}
				if (mEmail.length() > 100) {
					mEmailView
							.setError(getString(R.string.error_field_email_length));
					focusView = mEmailView;
					cancel = true;
				}  else if (atCount == 0) {
					mEmailView.setError(getString(R.string.error_noAtTheRate));
					focusView = mEmailView;
					cancel = true;
				} else if (dotCount == 0) { // Added code for . check
					mEmailView.setError(getString(R.string.error_noDot));
					focusView = mEmailView;
					cancel = true;
				}else if (atCount > 1) {
					mEmailView.setError(getString(R.string.error_field_at_rate));
					focusView = mEmailView;
					cancel = true;
				}else if(mEmail.length() == 2){
					if(atCount == 1 && dotCount == 1){
						mEmailView.setError(getString(R.string.error_justdotatrate));
						focusView = mEmailView;
						cancel = true;
					}
				}else if (!(mEmail.contains("com") || mEmail.contains(".edu"))){
					mEmailView.setError(getString(R.string.error_dotcom));
					focusView = mEmailView;
					cancel = true;
				}else if (TextUtils.isEmpty(mPassword)) {
					mPasswordView.setError(getString(R.string.error_pwd_required));
					focusView = mPasswordView;
					cancel = true;
				} else if (mPassword.length() < 4) {
					mPasswordView.setError(getString(R.string.error_invalid_password));
					focusView = mPasswordView;
					cancel = true;
				} else if (mPassword.length() > 50) {
					mPasswordView.setError(getString(R.string.error_field_pwd_length));
					focusView = mPasswordView;
					cancel = true;
				}else if(validEmail == false){
					mEmailView.setError(getString(R.string.error_incorrect_email));
					focusView = mEmailView;
					cancel = true;
				}

				
			}
			if (cancel) {

				focusView.requestFocus();
			} else {

				showProgress(true);
				
				String result = dbHandler.validateUser(mEmail, mPassword);
				if (result.equalsIgnoreCase("success")) {
					Toast.makeText(getBaseContext(), "Login Successful", Toast.LENGTH_LONG)
							.show();
					SharedPreferences settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
					SharedPreferences.Editor editor = settings.edit();
					editor.putBoolean("isLoggedin", true);
					editor.commit();
					nextActivity();
				} else {
					mPasswordView
							.setError(getString(R.string.error_incorrect_password));
					mPasswordView.requestFocus();
				}
			}
		}catch(Exception exception){
			
			Log.e("LOGINACTIVITY", "Exception Occured",exception) ;
		}finally{ // close connections
			if(dbHandler!=null ){
				dbHandler.close();
			}
		}
		

			
			
		
	}

	
	private void nextActivity() {
		Intent intent = new Intent(this, JobLandActivity.class);
		startActivity(intent);
	}
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {

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
			mAuthTask = null;
			if(success){
				nextActivity();
			}
			
			showProgress(false);
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}}
