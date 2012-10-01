package org.cloudsdale.android.db;

import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

public class AvatarDatabaseHelper extends OrmLiteSqliteOpenHelper {
    
    private static final String TAG = "Avatar Database Helper";

    @Override
    public void onCreate(SQLiteDatabase arg0, ConnectionSource arg1) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, ConnectionSource arg1, int arg2,
            int arg3) {
        // TODO Auto-generated method stub

    }

}
