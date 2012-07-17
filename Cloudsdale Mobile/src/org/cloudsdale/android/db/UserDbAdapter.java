package org.cloudsdale.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UserDbAdapter {
	private static class DatabaseHelper extends SQLiteOpenHelper {

		DatabaseHelper(Context context) {
			super(context, UserDbAdapter.DATABASE_NAME, null,
					UserDbAdapter.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(UserDbAdapter.DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(UserDbAdapter.TAG, "Upgrading user db from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS users");
			onCreate(db);

		}
	}

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

	public UserDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public void close() {
		this.mDbHelper.close();
	}

	public long createUser(String id, String email, String name,
			String time_zone, String member_since, String avatar_n,
			String avatar_m, String avatar_t, String avatar_c) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(UserDbAdapter.KEY_ID, id);
		initialValues.put(UserDbAdapter.KEY_EMAIL, email);
		initialValues.put(UserDbAdapter.KEY_TIMEZONE, time_zone);
		initialValues.put(UserDbAdapter.KEY_MEMBERSINCE, member_since);
		initialValues.put(UserDbAdapter.KEY_AVATARN, avatar_n);
		initialValues.put(UserDbAdapter.KEY_AVATARM, avatar_m);
		initialValues.put(UserDbAdapter.KEY_AVATART, avatar_t);
		initialValues.put(UserDbAdapter.KEY_AVATARC, avatar_c);

		return this.mDb.insert(UserDbAdapter.TABLE_NAME, null, initialValues);
	}

	public boolean deleteUser(String id) {
		return this.mDb.delete(UserDbAdapter.TABLE_NAME, UserDbAdapter.KEY_ID
				+ "=" + id, null) > 0;
	}

	public Cursor fetchAllUsers() {
		return this.mDb.query(UserDbAdapter.TABLE_NAME, new String[] {
				UserDbAdapter.KEY_ID, UserDbAdapter.KEY_USERNAME,
				UserDbAdapter.KEY_EMAIL }, null, null, null, null, null);
	}

	public Cursor fetchUser(String id) {
		Cursor mCursor = this.mDb.query(true, UserDbAdapter.TABLE_NAME,
				new String[] { UserDbAdapter.KEY_ID, UserDbAdapter.KEY_EMAIL,
						UserDbAdapter.KEY_USERNAME }, UserDbAdapter.KEY_ID
						+ "=" + id, null, null, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public UserDbAdapter open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}

	public boolean updateUser(String id, String email, String name,
			String time_zone, String member_since, String avatar_n,
			String avatar_m, String avatar_t, String avatar_c) {
		ContentValues args = new ContentValues();
		args.put(UserDbAdapter.KEY_ID, id);
		args.put(UserDbAdapter.KEY_EMAIL, email);
		args.put(UserDbAdapter.KEY_TIMEZONE, time_zone);
		args.put(UserDbAdapter.KEY_MEMBERSINCE, member_since);
		args.put(UserDbAdapter.KEY_AVATARN, avatar_n);
		args.put(UserDbAdapter.KEY_AVATARM, avatar_m);
		args.put(UserDbAdapter.KEY_AVATART, avatar_t);
		args.put(UserDbAdapter.KEY_AVATARC, avatar_c);

		return this.mDb.update(UserDbAdapter.TABLE_NAME, args,
				UserDbAdapter.KEY_ID + "=" + id, null) > 0;
	}
}
