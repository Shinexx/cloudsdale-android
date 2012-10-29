package org.cloudsdale.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.api.User;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OrmLite database helper for the Users database
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 *         Copyright (c) 2012 Cloudsdale.org
 * 
 */
public class UserDatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String			TAG					= "User Database Helper";

	// Database info
	private static final String			DATABASE_NAME		= Cloudsdale.USER_DATABASE_NAME;
	private static final int			DATABASE_VERSION	= Cloudsdale.DATABASE_VERSION;

	// The DAO we use to interact with the table
	private Dao<User, String>			mUserDao			= null;
	private static final AtomicInteger	mUsageCounter		= new AtomicInteger(
																	0);

	// This is so we don't ever have more than one helper
	private static UserDatabaseHelper	mHelper				= null;

	private UserDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Get the database helper, constructing it as necessary. For each call to
	 * this method, there should only be one call to {@link #close()}.
	 * 
	 * @param context
	 *            The context of the caller
	 * @return The database helper
	 */
	public static synchronized UserDatabaseHelper getHelper(Context context) {
		if (mHelper == null) {
			mHelper = new UserDatabaseHelper(context);
		}
		mUsageCounter.incrementAndGet();
		return mHelper;
	}

	/**
	 * This is called when the database is first called
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(TAG, "onCreate");
			TableUtils.createTable(connectionSource, User.class);
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when the application is upgraded and a higher database version
	 * number is encountered
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(TAG, "onUpgrade");
			TableUtils.dropTable(connectionSource, User.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't upgrade the database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Returns our Database Access Object (DAO)
	 * 
	 * @return The user DAO
	 * @throws SQLException
	 */
	public Dao<User, String> getUserDao() throws SQLException {
		if (mUserDao == null) {
			mUserDao = getDao(User.class);
		}
		return mUserDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs/helpers
	 */
	@Override
	public void close() {
		if (mUsageCounter.decrementAndGet() == 0) {
			super.close();
			mUserDao = null;
			mHelper = null;
		}
	}

}