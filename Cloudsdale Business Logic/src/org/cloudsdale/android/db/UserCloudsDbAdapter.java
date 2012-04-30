      package org.cloudsdale.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserCloudsDbAdapter {
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

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading message db from version " + oldVersion
					+ " to " + newVersion + ", which will destroy the old data");
			db.execSQL("DROP TABLE IF EXISTS messages");
			onCreate(db);
		}
	}

	public UserCloudsDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public UserCloudsDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createUserCloud(String user_id, String cloud_id) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USERID, user_id);
		initialValues.put(KEY_CLOUDID, cloud_id);

		return mDb.insert(TABLE_NAME, null, initialValues);
	}

	public boolean deleteUserCloud(String user_id) {
		return mDb.delete(TABLE_NAME, KEY_USERID + "=" + user_id, null) > 0;
	}

	public Cursor fetchAllUserClouds() {
		return mDb.query(TABLE_NAME, new String[] { KEY_USERID, KEY_CLOUDID },
				null, null, null, null, null);
	}

	public Cursor fetchUserClouds(String user_id) {
		Cursor mCursor = mDb.query(true, TABLE_NAME, new String[] { KEY_USERID,
				KEY_CLOUDID }, KEY_USERID + "=" + user_id, null, null, null,
				null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public boolean updateUserCloud(String user_id, String cloud_id) {
		ContentValues args = new ContentValues();
		args.put(KEY_USERID, user_id);
		args.put(KEY_CLOUDID, cloud_id);

		return mDb.update(TABLE_NAME, args, KEY_USERID + "=" + user_id, null) > 0;
	}
}
