package org.cloudsdale.android.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.api.Cloud;

import java.sql.SQLException;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * OrmLite database helper for the Clouds database
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 *         Copyright (c) 2012 Cloudsdale.org
 * 
 */
public class CloudDatabaseHelper extends OrmLiteSqliteOpenHelper {

	private static final String			TAG					= "Cloud Database Helper";

	// Database info
	private static final String			DATABASE_NAME		= Cloudsdale.CLOUD_DATABASE_NAME;
	private static final int			DATABASE_VERSION	= Cloudsdale.DATABASE_VERSION;

	// The DAO we use to interact with the table
	private Dao<Cloud, String>			mCloudDao			= null;
	private static final AtomicInteger	mUsageCounter		= new AtomicInteger(
																	0);

	// This is so we don't have more than none helper
	private static CloudDatabaseHelper	mHelper				= null;

	private CloudDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	/**
	 * Get the database helper, constructing it as necessary. For each call to
	 * this method, there should only be one call to {@link #close()}.
	 * 
	 * @param context
	 * @return The database helper
	 */
	public static synchronized CloudDatabaseHelper getHelper(Context context) {
		if (mHelper == null) {
			mHelper = new CloudDatabaseHelper(context);
		}
		mUsageCounter.incrementAndGet();
		return mHelper;
	}

	/**
	 * Called when the DB is first created
	 */
	@Override
	public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
		try {
			Log.i(TAG, "onCreate");
			TableUtils.createTable(connectionSource, Cloud.class);
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * Called when the application is upgraded and a higher DB version number is
	 * encountered
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource,
			int oldVersion, int newVersion) {
		try {
			Log.i(TAG, "onUpgrade");
			TableUtils.dropTable(connectionSource, Cloud.class, true);
			onCreate(db, connectionSource);
		} catch (SQLException e) {
			Log.e(TAG, "Couldn't upgrade the database", e);
			throw new RuntimeException(e);
		}

	}

	/**
	 * Returns our Database Access Object (DAO)
	 * 
	 * @return The cloud DAO
	 * @throws SQLException
	 */
	public Dao<Cloud, String> getCloudDao() throws SQLException {
		if (mCloudDao == null) {
			mCloudDao = getDao(Cloud.class);
		}
		return mCloudDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs/helpers
	 */
	@Override
	public void close() {
		if (mUsageCounter.decrementAndGet() == 0) {
			super.close();
			mCloudDao = null;
			mHelper = null;
		}
	}
}