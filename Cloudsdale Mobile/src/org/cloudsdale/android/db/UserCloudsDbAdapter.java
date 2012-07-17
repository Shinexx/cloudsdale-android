package org.cloudsdale.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserCloudsDbAdapter {
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, UserCloudsDbAdapter.DATABASE_NAME, null,
					UserCloudsDbAdapter.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(UserCloudsDbAdapter.DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(UserCloudsDbAdapter.TAG, "Upgrading message db from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy the old data");
			db.execSQL("DROP TABLE IF EXISTS messages");
			onCreate(db);
		}
	}

	public static final String	KEY_USERID			= "user_id";

	public static final String	KEY_CLOUDID			= "cloud_id";
	private static final String	TAG					= "UserCloudDbAdapter";
	private DatabaseHelper		mDbHelper;

	private SQLiteDatabase		mDb;

	private static final String	DATABASE_CREATE		= "create table user_clouds "
															+ "(user_id text references user(user_id), "
															+ "cloud_id text references cloud(cloud_id), primary key(user_id, cloud_id))";
	private static final String	DATABASE_NAME		= "cloudsdale_data";
	private static final String	TABLE_NAME			= "user_clouds";

	private static final int	DATABASE_VERSION	= 0;

	private final Context		mCtx;

	public UserCloudsDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public void close() {
		this.mDbHelper.close();
	}

	public long createUserCloud(String user_id, String cloud_id) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(UserCloudsDbAdapter.KEY_USERID, user_id);
		initialValues.put(UserCloudsDbAdapter.KEY_CLOUDID, cloud_id);

		return this.mDb.insert(UserCloudsDbAdapter.TABLE_NAME, null,
				initialValues);
	}

	public boolean deleteUserCloud(String user_id) {
		return this.mDb.delete(UserCloudsDbAdapter.TABLE_NAME,
				UserCloudsDbAdapter.KEY_USERID + "=" + user_id, null) > 0;
	}

	public Cursor fetchAllUserClouds() {
		return this.mDb.query(UserCloudsDbAdapter.TABLE_NAME,
				new String[] { UserCloudsDbAdapter.KEY_USERID,
						UserCloudsDbAdapter.KEY_CLOUDID }, null, null, null,
				null, null);
	}

	public Cursor fetchUserClouds(String user_id) {
		Cursor mCursor = this.mDb.query(true, UserCloudsDbAdapter.TABLE_NAME,
				new String[] { UserCloudsDbAdapter.KEY_USERID,
						UserCloudsDbAdapter.KEY_CLOUDID },
				UserCloudsDbAdapter.KEY_USERID + "=" + user_id, null, null,
				null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public UserCloudsDbAdapter open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}

	public boolean updateUserCloud(String user_id, String cloud_id) {
		ContentValues args = new ContentValues();
		args.put(UserCloudsDbAdapter.KEY_USERID, user_id);
		args.put(UserCloudsDbAdapter.KEY_CLOUDID, cloud_id);

		return this.mDb.update(UserCloudsDbAdapter.TABLE_NAME, args,
				UserCloudsDbAdapter.KEY_USERID + "=" + user_id, null) > 0;
	}
}
