package org.cloudsdale.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MessageDbAdapter {
	public static final String	KEY_ID				= "message_id";
	public static final String	KEY_CONTENT			= "content";
	public static final String	KEY_USERID			= "user_id";
	public static final String	KEY_USERAVATAR		= "user_avatar";
	public static final String	KEY_USERNAME		= "user_name";

	private static final String	TAG					= "MessageDbAdapter";
	private DatabaseHelper		mDbHelper;
	private SQLiteDatabase		mDb;

	private static final String	DATABASE_CREATE		= "create table messages"
															+ "(message_id text primary key, content text not null, "
															+ "user_id text references user(user_id), user_avatar text "
															+ "references user(avatar_chat), user_name text references user(username))";

	private static final String	DATABASE_NAME		= "cloudsdale_data";
	private static final String	TABLE_NAME			= "messages";
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

	public MessageDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public MessageDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	public long createMessage(String id, String content, String user_id,
			String user_avatar, String user_name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_ID, id);
		initialValues.put(KEY_CONTENT, content);
		initialValues.put(KEY_USERID, user_id);
		initialValues.put(KEY_USERAVATAR, user_avatar);
		initialValues.put(KEY_USERNAME, user_name);

		return mDb.insert(TABLE_NAME, null, initialValues);
	}
	
	public boolean deleteMessage(String id) {
		return mDb.delete(TABLE_NAME, KEY_ID + "=" + id,  null) > 0;
	}
	
	public Cursor fetchAllMessages() {
		return mDb.query(TABLE_NAME, new String[] { KEY_ID }, null, null, null, null, null);
	}
	
	public Cursor fetchUser(String id) {
		Cursor mCursor = mDb.query(true, TABLE_NAME, new String[] { KEY_ID }, KEY_ID + "=" + id, null, null, null, null, null);
		
		if(mCursor != null) {
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	public boolean updateMessage(String id, String content, String user_id, String user_avatar, String user_name) {
		ContentValues args = new ContentValues();
		args.put(KEY_ID, id);
		args.put(KEY_CONTENT, content);
		args.put(KEY_USERID, user_id);
		args.put(KEY_USERAVATAR, user_avatar);
		args.put(KEY_USERNAME, user_name);
		
		return mDb.update(TABLE_NAME, args, KEY_ID + "=" + id, null) > 0;
	}
}
