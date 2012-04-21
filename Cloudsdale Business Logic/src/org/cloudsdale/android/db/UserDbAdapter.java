package org.cloudsdale.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDbAdapter {
	public static final String	KEY_ID				= "user_id";
	public static final String	KEY_EMAIL			= "email";
	public static final String	KEY_USERNAME		= "username";
	public static final String	KEY_TIMEZONE		= "time_zone";
	public static final String	KEY_MEMBERSINCE		= "member_since";
	public static final String	KEY_AVATARN			= "avatar_normal";
	public static final String	KEY_AVATARM			= "avatar_mini";
	public static final String	KEY_AVATART			= "avatar_thumb";
	public static final String	KEY_AVATARC			= "avatar_chat";

	private static final String	TAG					= "UserDbAdapter";
	private DatabaseHelper		mDbHelper;
	private SQLiteDatabase		mDb;

	private static final String	DATABASE_CREATE		= "create table users "
															+ "(user_id text primary key, email text not null unique, "
															+ "name text unique, time_zone text, member_since text not null, "
															+ "avatar_normal text, avatar_mini text, avatar_thumb text, "
															+ "avatar_preview text, avatar_chat text)";

	private static final String	DATABASE_NAME		= "cloudsdale_data";
	private static final String	TABLE_NAME			= "users";
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
			Log.w(TAG, "Upgrading user db from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS users");
			onCreate(db);

		}
	}

	public UserDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public UserDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createUser(String id, String email, String name,
			String time_zone, String member_since, String avatar_n,
			String avatar_m, String avatar_t, String avatar_c) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ID, id);
		initialValues.put(KEY_EMAIL, email);
		initialValues.put(KEY_TIMEZONE, time_zone);
		initialValues.put(KEY_MEMBERSINCE, member_since);
		initialValues.put(KEY_AVATARN, avatar_n);
		initialValues.put(KEY_AVATARM, avatar_m);
		initialValues.put(KEY_AVATART, avatar_t);
		initialValues.put(KEY_AVATARC, avatar_c);

		return mDb.insert(TABLE_NAME, null, initialValues);
	}

	public boolean deleteUser(String id) {
		return mDb.delete(TABLE_NAME, KEY_ID + "=" + id, null) > 0;
	}

	public Cursor fetchAllUsers() {
		return mDb.query(TABLE_NAME, new String[] { KEY_ID, KEY_USERNAME,
				KEY_EMAIL }, null, null, null, null, null);
	}

	public Cursor fetchUser(String id) {
		Cursor mCursor = mDb.query(true, TABLE_NAME, new String[] { KEY_ID,
				KEY_EMAIL, KEY_USERNAME }, KEY_ID + "=" + id, null, null, null,
				null, null);

		if (mCursor != null)
			mCursor.moveToFirst();

		return mCursor;
	}

	public boolean updateUser(String id, String email, String name,
			String time_zone, String member_since, String avatar_n,
			String avatar_m, String avatar_t, String avatar_c) {
		ContentValues args = new ContentValues();
		args.put(KEY_ID, id);
		args.put(KEY_EMAIL, email);
		args.put(KEY_TIMEZONE, time_zone);
		args.put(KEY_MEMBERSINCE, member_since);
		args.put(KEY_AVATARN, avatar_n);
		args.put(KEY_AVATARM, avatar_m);
		args.put(KEY_AVATART, avatar_t);
		args.put(KEY_AVATARC, avatar_c);

		return mDb.update(TABLE_NAME, args, KEY_ID + "=" + id, null) > 0;
	}
}
