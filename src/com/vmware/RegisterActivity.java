package com.vmware;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a registration screen to the user, offering registration as
 * well.
 */
public class RegisterActivity extends Activity {

	private static final String PREFRENCES_NAME = "myPrefs";
	private UserLoginTask mAuthTask = null;
	Button signUp;
	Button signIn;
	DataHandler dbHandler;
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private static boolean userRegistered = false;
	public static boolean isUserRegistered() {
		return userRegistered;
	}

	public static void setUserRegistered(boolean userRegistered) {
		RegisterActivity.userRegistered = userRegistered;
	}

	private String mUserName;
	private String mPassword;
	private String mManagerEmail;
	private String mRole;
	private String mTeam;
	private int atCount;
	private int dotCount;
	private int managerAtCount;
	private int managerDotCount;
	// UI references.
	private EditText mEmailView;
	private EditText mUserNameView;
	private EditText mPasswordView;
	private EditText mManagerEmailView;
	private Spinner mRoleSpinner;
	private Spinner mTeamSpinner;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;

	private void nextActivity() {
		Intent intent = new Intent(this, JobLandActivity.class);
		startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		setupActionBar();
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);
		signUp = (Button) findViewById(R.id.sign_up_button);
		mPasswordView = (EditText) findViewById(R.id.password);
		mUserNameView = (EditText) findViewById(R.id.username);
		mManagerEmailView = (EditText) findViewById(R.id.manageremail);
		addListenerOnSpinnerItemSelection();
		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

	}

	public void signUp(View view) {
		attemptRegister();
	}

	public void reset(View view) {
		mEmailView.setText("");
		mUserNameView.setText("");
		mPasswordView.setText("");
		mManagerEmailView.setText("");
		mEmailView.setError(null);
		mUserNameView.setError(null);
		mPasswordView.setError(null);
		mManagerEmailView.setError(null);
		mRoleSpinner.setSelection(0);
		mTeamSpinner.setSelection(0);
		
	}
	
	public void resetForm(){
		mEmailView.setText("");
		mUserNameView.setText("");
		mPasswordView.setText("");
		mManagerEmailView.setText("");
		mEmailView.setError(null);
		mUserNameView.setError(null);
		mPasswordView.setError(null);
		mManagerEmailView.setError(null);
		mRoleSpinner.setSelection(0);
		mTeamSpinner.setSelection(0);
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


	
	public void addListenerOnSpinnerItemSelection() {
		mRoleSpinner = (Spinner) findViewById(R.id.user_role);
		mRoleSpinner.setOnItemSelectedListener(new OnItemSelectedListener());
		mTeamSpinner = (Spinner) findViewById(R.id.user_team);
		mTeamSpinner.setOnItemSelectedListener(new OnItemSelectedListener());
	  }
	
	
	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptRegister() {
		// Reset errors.
		
		mEmailView.setError(null);
		mPasswordView.setError(null);
		mUserNameView.setError(null);
		mManagerEmailView.setError(null);
		
		mEmail = null;
		mPassword = null;
		mUserName = null;
		mManagerEmail = null;
		mRole = null;
		mTeam = null;
		atCount = 0;
		dotCount = 0;
		managerAtCount=0;
		managerDotCount=0;
		if (mAuthTask != null) {
			return;
		}
		mEmail =  mEmailView.getText().toString();
		mEmail = mEmail.toLowerCase();
		mPassword = mPasswordView.getText().toString();
		mUserName = mUserNameView.getText().toString();
		mManagerEmail= mManagerEmailView.getText().toString();
		mRole = String.valueOf(mRoleSpinner.getSelectedItem());
		mTeam = String.valueOf(mTeamSpinner.getSelectedItem());
		/*System.out.println(" User Inputs in attemptRegister");
		System.out.println("Email entered : "+mEmail);
		System.out.println("username entered : "+mUserName);
		System.out.println("Password entered : "+mPassword);
		System.out.println("Manager EmailId entered : "+mManagerEmail);
		System.out.println("Role entered : "+mRole);
		System.out.println("Team entered : "+mTeam);
		*/mRole = mRoleSpinner.getSelectedItem().toString();
		mTeam = mTeamSpinner.getSelectedItem().toString();
		
		dbHandler = new DataHandler(getBaseContext());
		dbHandler.open();
		boolean userRegistered = dbHandler.isUserRegistered(mEmail);
		boolean cancel = false;
		View focusView = null;
		
		if (TextUtils.isEmpty(mEmail)) { // check if the email is empty or not 
			mEmailView.setError(getString(R.string.error_email_required));
			focusView = mEmailView;
			cancel = true;
		}else if (null == mUserName || mUserName.length() == 0) { // check if the username is null or not 
			mUserNameView.setError(getString(R.string.error_invalid_UserName));
			focusView = mUserNameView;
			cancel = true;
		}else if (TextUtils.isEmpty(mPassword)) { // check if the password is empty or not  
			mPasswordView.setError(getString(R.string.error_pwd_required));
			focusView = mPasswordView;
			cancel = true;
		}else if(mRole.equalsIgnoreCase("-SELECT USER ROLE-")){
			Toast.makeText(RegisterActivity.this, "Please Select the User Role !!", Toast.LENGTH_LONG).show();
			return;
		}else if(mTeam.equalsIgnoreCase("-SELECT YOUR TEAM-")){
			Toast.makeText(RegisterActivity.this, "Please Select Your Team  !!", Toast.LENGTH_LONG).show();
			return;
		}else if(userRegistered){ // Check for a valid email address.
			//System.out.println("User is already registered");
			mEmailView.setError(getString(R.string.error_email_registered));
			focusView = mEmailView;
			cancel = true;
		} else {
			
			// Check for only one occurence of '@' and '.'
			for (int i = 0; i < mEmail.length(); i++) {
				if (mEmail.charAt(i) == '@') {
					atCount++;
				}
				if (mEmail.charAt(i) == '.') {
					dotCount++;
				}
			}
			for (int i = 0; i < mManagerEmail.length(); i++) {
				if (mManagerEmail.charAt(i) == '@') {
					managerAtCount++;
				}
				if (mManagerEmail.charAt(i) == '.') {
					managerDotCount++;
				}
			}
			//System.out.println("managerDotCount : "+managerDotCount);
			//System.out.println("managerAtCount : "+managerAtCount);
			if (mEmail.length() > 100) {
				mEmailView
						.setError(getString(R.string.error_field_email_length));
				focusView = mEmailView;
				cancel = true;
			}else if (atCount == 0) {
				mEmailView.setError(getString(R.string.error_noAtTheRate));
				focusView = mEmailView;
				cancel = true;
			}else if (dotCount == 0) {
				mEmailView.setError(getString(R.string.error_noDot));
				focusView = mEmailView;
				cancel = true;
			}else if (atCount > 1) {
				mEmailView.setError(getString(R.string.error_field_at_rate));
				focusView = mEmailView;
				cancel = true;
			}else if(mEmail.length() == 2){
				if(atCount == 1 && dotCount == 1){
					//System.out.println("email has just @.");
					mEmailView.setError(getString(R.string.error_justdotatrate));
					focusView = mEmailView;
					cancel = true;
				}
			}else if (!(mEmail.contains("com") || mEmail.contains(".edu"))){
				mEmailView.setError(getString(R.string.error_dotcom));
				focusView = mEmailView;
				cancel = true;
			}
			 else if (mPassword.length() < 4) {
				mPasswordView.setError(getString(R.string.error_invalid_password));
				focusView = mPasswordView;
				cancel = true;
			} else if (mPassword.length() > 50) {
				mPasswordView.setError(getString(R.string.error_field_pwd_length));
				focusView = mPasswordView;
				cancel = true;
			} 
			/*else if (mManagerEmail== null ||  mManagerEmail.length() == 0){
				mManagerEmailView.setError(getString(R.string.error_email_required));
				focusView = mManagerEmailView;
				cancel = true;
			}*/else if(!mRole.equalsIgnoreCase("CEO")){
				if(mManagerEmail == null || mManagerEmail.length() == 0){
					mManagerEmailView.setError(getString(R.string.error_invalid_manageremail));
					focusView = mManagerEmailView;
					cancel = true;
				}else{
					boolean isValidManager = dbHandler.isUserRegistered(mManagerEmail);
					if(isValidManager == false){
						mManagerEmailView.setError(getString(R.string.error_invalid_managermail));
						focusView = mManagerEmailView;
						cancel = true;
					}
					else if (mManagerEmail.length() > 100) {
						mManagerEmailView.setError(getString(R.string.error_field_email_length));
						focusView = mManagerEmailView;
						cancel = true;
					}else if (managerAtCount == 0) {
						//System.out.println("Manager email does not have @");
						mManagerEmailView.setError(getString(R.string.error_noAtTheRate));
						focusView = mManagerEmailView;
						cancel = true;
					}else if (managerDotCount == 0) {
						//System.out.println("Manager email does not have .");
						mManagerEmailView.setError(getString(R.string.error_noDot));
						focusView = mManagerEmailView;
						cancel = true;
					}else if (managerAtCount > 1) {
						mManagerEmailView.setError(getString(R.string.error_field_at_rate));
						focusView = mManagerEmailView;
						cancel = true;
					}else if(mManagerEmail.length() == 2){
						if(atCount == 1 && dotCount == 1){
							mManagerEmailView.setError(getString(R.string.error_justdotatrate));
							focusView = mManagerEmailView;
							cancel = true;
						}
					}else if (!(mManagerEmail.contains("com") || mManagerEmail.contains(".edu"))){
						mManagerEmailView.setError(getString(R.string.error_dotcom));
						focusView = mManagerEmailView;
						cancel = true;
					}
				}
			}
		}
		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_up);
			showProgress(true);
			
			long insertOutput = dbHandler.registerUser(mEmail, mUserName, mPassword, mManagerEmail, mRole, mTeam);
			//System.out.println("no of records inserted : "+insertOutput);
			
			if(insertOutput > 0){
				SharedPreferences settings = getSharedPreferences(PREFRENCES_NAME, Context.MODE_PRIVATE);
				SharedPreferences.Editor editor = settings.edit();
				editor.putString("userName", mUserName);
				editor.putBoolean("isRegistered", true);
				editor.putBoolean("isLoggedin", true);
				editor.commit();
				Toast.makeText(getBaseContext(), "Registered Successfully", Toast.LENGTH_LONG).show();
			}
			dbHandler.close();
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
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
			showProgress(false);

			if (success) {
				// finish(); should go to next page
				nextActivity();
			} else {
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}

}
