package org.cloudsdale.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MessageDbAdapter {
	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, MessageDbAdapter.DATABASE_NAME, null,
					MessageDbAdapter.DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(MessageDbAdapter.DATABASE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(MessageDbAdapter.TAG, "Upgrading message db from version "
					+ oldVersion + " to " + newVersion
					+ ", which will destroy the old data");
			db.execSQL("DROP TABLE IF EXISTS messages");
			onCreate(db);
		}
	}

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

	public MessageDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}

	public void close() {
		this.mDbHelper.close();
	}

	public long createMessage(String id, String content, String user_id,
			String user_avatar, String user_name) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(MessageDbAdapter.KEY_ID, id);
		initialValues.put(MessageDbAdapter.KEY_CONTENT, content);
		initialValues.put(MessageDbAdapter.KEY_USERID, user_id);
		initialValues.put(MessageDbAdapter.KEY_USERAVATAR, user_avatar);
		initialValues.put(MessageDbAdapter.KEY_USERNAME, user_name);

		return this.mDb
				.insert(MessageDbAdapter.TABLE_NAME, null, initialValues);
	}

	public boolean deleteMessage(String id) {
		return this.mDb.delete(MessageDbAdapter.TABLE_NAME,
				MessageDbAdapter.KEY_ID + "=" + id, null) > 0;
	}

	public Cursor fetchAllMessages() {
		return this.mDb.query(MessageDbAdapter.TABLE_NAME,
				new String[] { MessageDbAdapter.KEY_ID }, null, null, null,
				null, null);
	}

	public Cursor fetchUser(String id) {
		Cursor mCursor = this.mDb.query(true, MessageDbAdapter.TABLE_NAME,
				new String[] { MessageDbAdapter.KEY_ID },
				MessageDbAdapter.KEY_ID + "=" + id, null, null, null, null,
				null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}

		return mCursor;
	}

	public MessageDbAdapter open() throws SQLException {
		this.mDbHelper = new DatabaseHelper(this.mCtx);
		this.mDb = this.mDbHelper.getWritableDatabase();
		return this;
	}

	public boolean updateMessage(String id, String content, String user_id,
			String user_avatar, String user_name) {
		ContentValues args = new ContentValues();
		args.put(MessageDbAdapter.KEY_ID, id);
		args.put(MessageDbAdapter.KEY_CONTENT, content);
		args.put(MessageDbAdapter.KEY_USERID, user_id);
		args.put(MessageDbAdapter.KEY_USERAVATAR, user_avatar);
		args.put(MessageDbAdapter.KEY_USERNAME, user_name);

		return this.mDb.update(MessageDbAdapter.TABLE_NAME, args,
				MessageDbAdapter.KEY_ID + "=" + id, null) > 0;
	}
}
