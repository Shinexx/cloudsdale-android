package org.cloudsdale.android.db;

public class UserCloudsDbAdapter {
	public static final String	KEY_USERID		= "user_id";
	public static final String	KEY_CLOUDID		= "cloud_id";

	private static final String	TAG				= "UserCloudDbAdapter";
	private DatabaseHelper		mDbHelper;
	private SQLiteDatabase		mDb;

	private static final String	DATABASE_CREATE	= "create table user_clouds "
														+ "(user_id text references user(user_id), "
														+ "cloud_id text references cloud(cloud_id), primary key(user_id, cloud_id))";
}
