package org.cloudsdale.android.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class CloudMessagesDbAdapter {
	public static final String	KEY_CLOUDID		= "cloud_id";
	public static final String	KEY_MESSAGEID		= "message_id";

	private static final String	TAG				= "CloudMessageDbAdapter";
	private DatabaseHelper		mDbHelper;
	private SQLiteDatabase		mDb;

	private static final String	DATABASE_CREATE	= "create table cloud_messages "
														+ "(cloud_id text references cloud(cloud_id, "
														+ "message_id text references message(message_id), primary key(cloud_id, message_id))";
	
	private static final String DATABASE_NAME = "cloudsdale_data";
	private static final String TABLE_NAME = "cloud_messages";
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
	
	public CloudMessagesDbAdapter(Context ctx) {
		this.mCtx = ctx;
	}
	
	public CloudMessagesDbAdapter open() throws SQLException {
		mDbHelper = new DatabaseHelper(mCtx);
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}
	
	public void close() {
		mDbHelper.close();
	}
	
	public long createCloudMessage(String cloud_id, String message_id) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_CLOUDID, cloud_id);
		initialValues.put(KEY_MESSAGEID, message_id);
		
		return mDb.insert(TABLE_NAME, null, initialValues);
	}
	
	public boolean deleteCloudMessage(String cloud_id, String message_id) {
		return mDb.delete(TABLE_NAME, KEY_CLOUDID + "=" + cloud_id + " and " + KEY_MESSAGEID + "=" + message_id, null) > 0;
	}
	
	public Cursor fetchAllCloudMessages() {
		return mDb.query(TABLE_NAME, new String[] { KEY_CLOUDID, KEY_MESSAGEID }, null, null, null, null, null);
	}
	
	public Cursor fetchCloudMessages(String cloud_id, String message_id) {
		Cursor mCursor = mDb.query(true, TABLE_NAME, new String[] { KEY_CLOUDID, KEY_MESSAGEID }, KEY_CLOUDID + "=" + cloud_id, null, null, null, null, null);
		
		if(mCursor != null) {
			mCursor.moveToFirst();
		}
		
		return mCursor;
	}
	
	public boolean updateCloudMessage(String cloud_id, String message_id) {
		ContentValues args = new ContentValues();
		args.put(KEY_CLOUDID, cloud_id);
		args.put(KEY_MESSAGEID, message_id);
		
		return mDb.update(TABLE_NAME, args, KEY_CLOUDID + "=" + cloud_id + "&" + KEY_MESSAGEID + "=" + message_id, null) > 0;
	}
}
