package com.vmware;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DataHandler {
	public String userNameGlobal;
	public static final String USER_DETAILS_TABLE_NAME = "USER_DETAILS";
	public static final String EMAIL = "EMAIL";
	public static final String USER_NAME = "USER_NAME";
	public static final String PWD = "PWD";
	public static final String USER_ID_USER_DETAILS = "USER_ID";
	public static final String MANAGER_EMAIL = "MANAGER_EMAIL";
	public static final String ROLE = "ROLE";
	public static final String TEAM = "TEAM";
	
	public static final String JOB_DETAILS_TABLE_NAME = "JOB_DETAILS";
	public static final String JOB_ID = "JOB_ID";
	public static final String USER_ID_JOB_DETAILS = "USER_ID";
	public static final String CREATION_DATE = "CREATION_DATE";
	public static final String END_DATE = "END_DATE";
	public static final String SELF_TEAM = "SELF_TEAM";
	public static final String TARGET_TEAM = "TARGET_TEAM";
	public static final String PRIORITY = "PRIORITY";
	public static final String TARGET_DURATION = "TARGET_DURATION";
	public static final String ACTUAL_DURATION = "ACTUAL_DURATION";
	public static final String DESCRIPTION = "DESCRIPTION";
	public static final String STATUS_ID = "STATUS_ID";
	public static final String CPU_USAGE = "CPU_USAGE";
	public static final String MEMORY_USAGE = "MEMORY_USAGE";
	public static final String URL = "URL";
	
	public static final String STATUS_DETAILS_TABLE_NAME = "STATUS_DETAILS";
	public static final String STATUS_DETAILS_STATUS_ID = "STATUS_ID";
	public static final String STATUS_DESC = "STATUS_DESC";
	
	public static final String DB_NAME = "VMTest.db";
	public static final int DB_VERSION = 1;
	public static final String JOB_DETAILS_CREATE = " CREATE TABLE "
			+ JOB_DETAILS_TABLE_NAME
			+ "( "
			+ " JOB_ID REAL NOT NULL, USER_ID LONG NOT NULL, CREATION_DATE DATETIME NOT NULL,END_DATE DATETIME, SELF_TEAM TEXT NOT NULL, TARGET_TEAM TEXT NOT NULL,PRIORITY TEXT DEFAULT LOW, TARGET_DURATION REAL,"
			+ "ACTUAL_DURATION REAL, DESCRIPTION NOT NULL, STATUS_ID INT, CPU_USAGE INT, MEMORY_USAGE TEXT, URL TEXT, PRIMARY KEY(JOB_ID));";
	public static final String USER_DETAILS_CREATE = " CREATE TABLE "
			+ USER_DETAILS_TABLE_NAME
			+ "( "
			+ " EMAIL TEXT UNIQUE NOT NULL,USER_NAME TEXT UNIQUE NOT NULL, PWD TEXT NOT NULL, USER_ID LONG PRIMARY KEY, MANAGER_EMAIL TEXT, ROLE TEXT NOT NULL, TEAM NOT NULL);";

	public static final String STATUS_DETAILS_CREATE = " CREATE TABLE "
			+ STATUS_DETAILS_TABLE_NAME
			+ "( "
			+ " STATUS_ID INT PRIMARY KEY, STATUS_DESC TEXT);";

	
	DataBaseHelper dbHelper;
	Context ctx;
	SQLiteDatabase db;

	public DataHandler(Context ctx) {
		this.ctx = ctx;
		dbHelper = new DataBaseHelper(ctx);
	}

	private static class DataBaseHelper extends SQLiteOpenHelper {

		public DataBaseHelper(Context ctx) {
			super(ctx, DB_NAME, null, DB_VERSION);
			
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			//System.out.println("On Create method called");
			// TODO Auto-generated method1 stub
			try {
				//System.out.println("query to create USER_DETAILS table : "+ USER_DETAILS_CREATE);
				db.execSQL(USER_DETAILS_CREATE);
				//System.out.println("query to create job_details table : " + JOB_DETAILS_CREATE);
				db.execSQL(JOB_DETAILS_CREATE);
				//System.out.println("query to create STATUS_DETAILS table : " + STATUS_DETAILS_CREATE);
				db.execSQL(STATUS_DETAILS_CREATE);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			 //db.execSQL("DROP TABLE IF EXISTS"+JOB_DETAILS_CREATE);
			 //db.execSQL("DROP TABLE IF EXISTS "+USER_DETAILS_CREATE);
			onCreate(db);
		}

	}

	public DataHandler open() {
		//System.out.println("In Database open() ");
		db = dbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		dbHelper.close();
	}

	public long getMaxUserID() {
		SQLiteDatabase sqlDB = dbHelper.getReadableDatabase();
		if (sqlDB != null) {
			String maxQuery = "SELECT MAX(USER_ID) FROM " + USER_DETAILS_TABLE_NAME;
			Long maxUserId = -1L;
			Cursor c = sqlDB.rawQuery(maxQuery, null);
			if (null != c && c.getCount() > 0) {
				c.moveToFirst();
				maxUserId = Long.valueOf(c.getLong(0));
			}
			//System.out.println("User ID max from db : " + maxUserId);
			if (maxUserId == null || maxUserId == -1) {
				maxUserId = 0L;
			}
			return maxUserId;
		}
		return -1;
	}
	
	public boolean isUserRegistered(String email) {
		if (email == null || email.length() == 0) {
			return false;
		} else {
			boolean result = false;
			Long noOfRows = -1L;
			SQLiteDatabase sqlDB1 = dbHelper.getReadableDatabase();
			String userIDQuery = "SELECT COUNT(USER_ID) FROM "+ USER_DETAILS_TABLE_NAME + " WHERE EMAIL = '"+ email + "';";
			//System.out.println("Query to get userid : " + userIDQuery);
			Cursor c = sqlDB1.rawQuery(userIDQuery, null);
			if (null != c && c.getCount() > 0) {
				//System.out.println("cursor is not null");
				c.moveToFirst();
				noOfRows = Long.valueOf(c.getLong(0));
			}
			//System.out.println("Number of rows returned  from db : " + noOfRows + "for email : " + email);
			if (noOfRows == 0) {
				result = false;
			}else{
				result = true;
			}
			return result;
		}
	}
	
	
	public long getMaxJobID() {
		SQLiteDatabase sqlDB = dbHelper.getReadableDatabase();
		if (sqlDB != null) {
			String maxQuery = "SELECT MAX(JOB_ID) FROM "+ JOB_DETAILS_TABLE_NAME;
			Long maxJobId = -1L;
			Cursor c = sqlDB.rawQuery(maxQuery, null);
			if (null != c && c.getCount() >0) {
				c.moveToFirst();
				maxJobId = Long.valueOf(c.getLong(0));
			}
			//System.out.println("Job Id max from db : " + maxJobId);
			if (maxJobId == null || maxJobId == -1) {
				maxJobId = 0L;
			}
			return maxJobId;
		}
		return -1;
	}
	

	public long registerUser(String email, String userName,
			String password, String managerEmail, String role, String team) {
		ContentValues content = new ContentValues();
		content.put(EMAIL, email);
		content.put(USER_NAME, userName);
		content.put(PWD, password);
		Long userIdLong = getMaxUserID();
		if(userIdLong == -1){
			return -1;
		}
		userIdLong++;
		content.put(USER_ID_USER_DETAILS, String.valueOf(userIdLong));
		content.put(MANAGER_EMAIL, managerEmail);
		content.put(ROLE, role);
		content.put(TEAM, team);
		if (db != null) {
			long rowIdInserted = db.insert(USER_DETAILS_TABLE_NAME, null, content);
			return rowIdInserted;
		} else {
			return -1;
		}
	}

	public long getUserID(String userName) {
		//System.out.println("Entered getUSerId");
		//System.out.println("username sent : " + userName);
		if (userName == null || userName.length() == 0) {
			return -1;
		} else {
			SQLiteDatabase sqlDB1 = dbHelper.getReadableDatabase();
			Long maxNumber = -1L;
			String userIDQuery = "SELECT USER_ID FROM "
					+ USER_DETAILS_TABLE_NAME + " WHERE USER_NAME = '"
					+ userName + "'";
			//System.out.println("Query to get userid : " + userIDQuery);
			Cursor c = sqlDB1.rawQuery(userIDQuery, null);
			if (null != c && c.getCount() > 0) {
				c.moveToFirst();
				maxNumber = Long.valueOf(c.getLong(0));
			}
			/*System.out.println("User ID returned  from db : " + maxNumber
					+ "for username : " + userName);
			*/if (maxNumber == null || maxNumber == -1) {
				maxNumber = 0L;
			}
			return maxNumber;
		}
	}

	public long createJob(String userID, String creationDate, String selfTeam,  String priority, String targetTeam, String targetDuration,
			String description) {
		
		ContentValues content = new ContentValues();
		long maxJobID = getMaxJobID();
		if(maxJobID == -1){
			return -1;
		}
		maxJobID++;
		content.put(JOB_ID, String.valueOf(maxJobID));
		content.put(USER_ID_JOB_DETAILS, userID);
		content.put(CREATION_DATE, creationDate);
		content.put(END_DATE, new String());
		content.put(SELF_TEAM, selfTeam);
		content.put(TARGET_TEAM, targetTeam);
		content.put(PRIORITY, priority);
		content.put(TARGET_DURATION, targetDuration);
		content.put(ACTUAL_DURATION, new String());
		content.put(END_DATE, new String());
		content.put(DESCRIPTION, description);
		content.put(STATUS_ID, 1);
		content.put(CPU_USAGE, 0);
		content.put(MEMORY_USAGE, 0);
		content.put(URL, "www.vmware.com");
		
		if (db != null) {
			long rowIndexInserted = db.insert(JOB_DETAILS_TABLE_NAME, null, content);
			return rowIndexInserted;
		} else {
			return -1;
		}

	}

	public List<UserDetails> fetchUserDetails(String userName) {
		//System.out.println("username : "+userName);
		if (userName == null || userName.length() == 0) {
			return null;
		} else {
			String userIDDetailsQuery = "SELECT * FROM "+ USER_DETAILS_TABLE_NAME + " WHERE USER_NAME = '"+ userName + "';";
			//System.out.println("Query to get userdetails : " + userIDDetailsQuery);
			Cursor userCursor = db.rawQuery(userIDDetailsQuery, null);
			List<UserDetails> userDetailsList = new ArrayList<UserDetails>();
			if(userCursor.getCount() <= 0){
				return null;
			}else{
				if(userCursor.moveToFirst()){
					do{
						UserDetails user = new UserDetails();
						user.setEmail(userCursor.getString(0));
						user.setUserName(userCursor.getString(1));
						user.setPassword(userCursor.getString(2));
						user.setUserID(userCursor.getLong(3));
						user.setManagerEmail(userCursor.getString(4));
						user.setRole(userCursor.getString(5));
						user.setTeam(userCursor.getString(6));
						userDetailsList.add(user);
					}while(userCursor.moveToNext());
				}
				return userDetailsList;
			}
			
		}
		
	}

	public String[] fetchJobDetails(String jobID,String userName) {
		Long userIDLong = getUserID(userName);
		String jobIDDetailsQuery = "SELECT * FROM "+JOB_DETAILS_TABLE_NAME+" WHERE JOB_ID = '"+jobID+"' ;";
			String userId = String.valueOf(userIDLong);
			/*System.out.println("userId : "+userId);
			System.out.println("Query to get jobdetails : " + jobIDDetailsQuery);
			*/Cursor jobCursor = db.rawQuery(jobIDDetailsQuery, null);
			List<String> job = new ArrayList<String>();
			if(jobCursor.moveToFirst()){
					job.add(jobCursor.getString(0));
					job.add(jobCursor.getString(1));
					job.add(jobCursor.getString(2));
					job.add(jobCursor.getString(3));
					job.add(jobCursor.getString(4));
					job.add(jobCursor.getString(5));
					job.add(jobCursor.getString(6));
					job.add(jobCursor.getString(7));
					job.add(jobCursor.getString(8));
					job.add(jobCursor.getString(9));
					job.add(jobCursor.getString(10));
					job.add(jobCursor.getString(11));
					job.add(jobCursor.getString(12));
					job.add(jobCursor.getString(13));
			}
			String[] toReturn = new String[job.size()];
			toReturn = job.toArray(toReturn);
			return  toReturn;
		
}

	public List<String> fetchJobList(String jobId, String priority, String statusId, String startDate, String endDate, String userName) {
		if(priority.equalsIgnoreCase("-SELECT PRIORITY-") ){
			priority = new String("");
		}
		String jobIDDetailsQuery = "SELECT JOB_ID FROM "+JOB_DETAILS_TABLE_NAME+" WHERE ";
		Long userIDLong = getUserID(userName);
		if(userIDLong < 0){
			return null;
		}else{
			
			String userId = String.valueOf(userIDLong);
			boolean userFlag = false;
			boolean priorityFlag = false;
			boolean statusFlag = false;
			if(jobId.length()> 0){
				userFlag = true;
			}
			if(priority.length()> 0){
				priorityFlag = true;
			}
			if(statusId.length()> 0){
				statusFlag= true;
			}
			boolean mainFlag = false;
			if(userFlag){
				jobIDDetailsQuery += " JOB_ID = '"+jobId+"' "+"AND USER_ID = '"+userId+"' ";
				if(priorityFlag){
					jobIDDetailsQuery += " AND PRIORITY = '"+priority+"' ";
				}
				if(statusFlag){
					jobIDDetailsQuery += " AND STATUS_ID = '"+statusId+"' ";
				}
				mainFlag = true;
			}
			if(priorityFlag && (mainFlag == false)){
				jobIDDetailsQuery += " PRIORITY = '"+priority+"' "+"AND USER_ID = '"+userId+"' ";
					if(statusFlag){
						jobIDDetailsQuery += " AND STATUS_ID = '"+statusId+"' ";
					}
				mainFlag = true;	
			}
			if(statusFlag && (mainFlag == false)){
				jobIDDetailsQuery += " STATUS_ID = '"+statusId+"' "+"AND USER_ID = '"+userId+"' ";
			}
			
			
			/*if(jobId.length() > 0 && (priority.length() == 0)&&(statusId.length() == 0)&&(startDate.length() == 0)&&(endDate.length() == 0)){
				System.out.println("case 1 if");
				jobIDDetailsQuery += " JOB_ID = '"+jobId+"' ";
			}else{
				System.out.println("case 1 else");
				jobIDDetailsQuery += " JOB_ID = '"+jobId+"' AND ";
			}
			if(priority.length() > 0 && (statusId.length() == 0)&&(startDate.length() == 0)&&(endDate.length() == 0)){
				if(userId == null){
					System.out.println("case 2 if");
					jobIDDetailsQuery += " PRIORITY = '"+priority+"' ";
				}else{
					System.out.println("case 2 else");
					jobIDDetailsQuery += " PRIORITY = '"+priority+"' AND USER_ID = '"+userId+"' ";
				}
			}
			if(statusId.length() > 0 && (priority.length()==0)&&(startDate.length() == 0)&&(endDate.length() == 0)&& (jobId.length() == 0)){
				if(userId == null){
					System.out.println("case 3 if");
					jobIDDetailsQuery += " STATUS_ID = '"+statusId+"' ";
				}else{
					System.out.println("case 3 else");
					jobIDDetailsQuery += " STATUS_ID = '"+statusId+"' AND USER_ID = '"+userId+"' ";
				}
			}
			*/// To modify dates condition
			/*if(priority.length() > 0 && (statusId == null)&&(startDate == null)&&(endDate == null)&& (jobId == null)){
				if(userId == null){
					jobIDDetailsQuery += " PRIORITY = '"+priority+"' ";
				}else{
					jobIDDetailsQuery += " PRIORITY = '"+priority+"' AND USER_ID = '"+userId+"' ";
				}
			}*/
			jobIDDetailsQuery+= " ; ";
			/*System.out.println("Job Id in fetchJobDetails : ************************************* "+jobId);
			System.out.println("Priority in fetchJobDetails : *************************************** "+priority);
			System.out.println("Status ID in fetchJobDetails : *************************************** "+statusId);
			System.out.println("StartDate in fetchJobDetails : *************************************** "+startDate);
			System.out.println("End Date in fetchJobDetails : *************************************** "+endDate);
			System.out.println("End Date in fetchJobDetails : *************************************** "+userName);
			
			System.out.println("Query to get jobdetails : " + jobIDDetailsQuery);
			*/
			Cursor jobCursor = db.rawQuery(jobIDDetailsQuery, null);
			//user.
			List<String> jobIdList = new ArrayList<String>();
			if(jobCursor!=null && jobCursor.getCount() > 0){
				if(jobCursor.moveToFirst()){
					do{
						jobIdList.add(jobCursor.getString(0));
					}while(jobCursor.moveToNext());
				}
			}else{
				jobIdList = null;
			}
			
			return  jobIdList;
		}
}
	
	
	public String validateUser(String email, String password) {
		if (email == null || email.length() == 0) {
			return null;
		} else {
			Long emailRowCount = null;
			SQLiteDatabase sqlDB1 = dbHelper.getReadableDatabase();
			String userIDQuery = "SELECT USER_ID,PWD FROM "
					+ USER_DETAILS_TABLE_NAME + " WHERE EMAIL = '" + email
					+ "';";
			//System.out.println("Query to get userid : " + userIDQuery);
			Cursor c = sqlDB1.rawQuery(userIDQuery, null);
			//System.out.println("Cursor.getCount : "+c.getCount());
			if (null != c && c.getCount() > 0) {
				c.moveToFirst();
				emailRowCount = Long.valueOf(c.getLong(0));
			}
			
			//System.out.println("No of rows returned from the db  for email : "
					//+ emailRowCount + "for email : " + email);
			if (emailRowCount < 0) {
				return "Email is not present.";
			} else {
				String pwdFromDB = c.getString(1);
				if (pwdFromDB.equals(password)) {
					return "Success";
				} else {
					return "Failure";
				}
			}
		}
	}

	public String validateUserName(String userName) {
		/*System.out.println("Entered validateUser");
		System.out.println("userName entered : " + userName);
		*/if (userName == null || userName.length() == 0) {
			return null;
		} else {
			SQLiteDatabase sqlDB1 = dbHelper.getReadableDatabase();
			Long emailRowCount = -1L;
			String userNameQuery = "SELECT COUNT(*) FROM "
					+ USER_DETAILS_TABLE_NAME + " WHERE USER_NAME = '"
					+ userName + "';";
			//System.out.println("Query to validate userName : " + userNameQuery);
			Cursor c = sqlDB1.rawQuery(userNameQuery, null);
			if (null != c && c.getCount() > 0) {
				c.moveToFirst();
				emailRowCount = Long.valueOf(c.getLong(0));
			}
			if (emailRowCount > 0) {
				return "User Present";
			} else {
				return "User not present";
			}
		}
	}
}