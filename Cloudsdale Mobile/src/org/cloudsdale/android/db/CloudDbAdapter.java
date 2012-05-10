package org.cloudsdale.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Database adapter for long-term cloud storage
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 * 
 */
public class CloudDbAdapter {
	// Column names
	public static final String	KEY_ID				= "cloud_id";
	public static final String	KEY_NAME			= "name";
	public static final String	KEY_DESCRIPTION		= "description";
	public static final String	KEY_CREATEDAT		= "created_at";
	public static final String	KEY_CHATID			= "chat_id";
	public static final String	KEY_AVATARN			= "avatar_normal";
	public static final String	KEY_AVATARM			= "avatar_mini";
	public static final String	KEY_AVATART			= "avatar_thumb";
	public static final String	KEY_AVATARP			= "avatar_preview";

	// Helper objects
	private static final String	TAG					= "CloudDbAdapter";
	private DatabaseHelper		mDbHelper;
	private SQLiteDatabase		mDb;

	// Query to create the table as necessary
	private static final String	DATABASE_CREATE		= "create table clouds "
															+ "(cloud_id text primary key, name text not null, description text not null, "
															+ "created_at text not null, chat_id text, avatar_normal text, avatar_mini text, "
															+ "avatar_thumb text, avatar_preview text";

	// Database identifiers
	private static final String	DATABASE_NAME		= "cloudsdale_data";
	private static final String	TABLE_NAME			= "clouds";
	private static final int	DATABASE_VERSION	= 0;

	private final Context		mCtx;

	/**
	 * Helper class for database basic executions
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 * 
	 */
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
			Log.w(TAG, "Upgrading cloud db from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS clouds");
			onCreate(db);
		}
	}

	/**
	 * Constructor
	 * 
	 * @param ctx
	 *            Context for the application
	 */
	public CloudDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	/**
	 * Open the connection to the Database
	 * 
	 * @return The Adapter with the open connection
	 * @throws SQLException
	 *             Failure to open the connection
	 */
	public CloudDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	/**
	 * Close the connection
	 */
	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a user in the database
	 * 
	 * @param id
	 * @param name
	 * @param description
	 * @param created_at
	 * @param avatar_n
	 * @param avatar_m
	 * @param avatar_t
	 * @param avatar_p
	 * @return Status of the insert
	 */
	public long createUser(String id, String name, String description,
			String created_at, String avatar_n, String avatar_m,
			String avatar_t, String avatar_p) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ID, id);
		initialValues.put(KEY_NAME, name);
		initialValues.put(KEY_DESCRIPTION, description);
		initialValues.put(KEY_CREATEDAT, created_at);
		initialValues.put(KEY_AVATARN, avatar_n);
		initialValues.put(KEY_AVATARM, avatar_m);
		initialValues.put(KEY_AVATART, avatar_t);
		initialValues.put(KEY_AVATARP, avatar_p);

		return mDb.insert(TABLE_NAME, null, initialValues);
	}

	/**
	 * Delete the user in the database
	 * 
	 * @param id
	 *            The id of the user
	 * @return Whether the user was deleted or not
	 */
	public boolean deleteUser(String id) {
		return mDb.delete(TABLE_NAME, KEY_ID + "=" + id, null) > 0;
	}

	/**
	 * Get all the users in the database
	 * 
	 * @return A cursor with access to all the users
	 */
	public Cursor fetchAllUsers() {
		return mDb.query(TABLE_NAME, new String[] { KEY_ID, KEY_NAME,
				KEY_DESCRIPTION }, null, null, null, null, null);
	}

	/**
	 * Get a user from the database
	 * 
	 * @param id
	 *            the id of the user to find
	 * @return A Cursor pointing to the database results
	 */
	public Cursor fetchUser(String id) {
		Cursor mCursor = mDb.query(true, TABLE_NAME, new String[] { KEY_ID,
				KEY_NAME, KEY_DESCRIPTION }, KEY_ID + "=" + id, null, null,
				null, null, null);

		if (mCursor != null)
			mCursor.moveToFirst();

		return mCursor;
	}

	/**
	 * Update a user in the database
	 * 
	 * @param id
	 * @param email
	 * @param name
	 * @param time_zone
	 * @param member_since
	 * @param avatar_n
	 * @param avatar_m
	 * @param avatar_t
	 * @param avatar_c
	 * @return Boolean stating whether the user has been updated or not
	 */
	public boolean updateUser(String id, String email, String name,
			String time_zone, String member_since, String avatar_n,
			String avatar_m, String avatar_t, String avatar_c) {
		ContentValues args = new ContentValues();
		args.put(KEY_ID, id);
		args.put(KEY_NAME, email);
		args.put(KEY_DESCRIPTION, time_zone);
		args.put(KEY_CREATEDAT, member_since);
		args.put(KEY_AVATARN, avatar_n);
		args.put(KEY_AVATARM, avatar_m);
		args.put(KEY_AVATART, avatar_t);
		args.put(KEY_AVATARP, avatar_c);

		return mDb.update(TABLE_NAME, args, KEY_ID + "=" + id, null) > 0;
	}
}
