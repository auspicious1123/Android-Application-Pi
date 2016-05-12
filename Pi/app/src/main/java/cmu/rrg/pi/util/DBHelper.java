package cmu.rrg.pi.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cmu.rrg.pi.model.User;

/**
 * Created by Yang on 5/2/16.
 */
public class DBHelper extends SQLiteOpenHelper {
    private final static String TAG = "DBHelper";
    private final static String DBNAME = "pi.db";
    private final static String TABLENAME = "session";
    private final static int DBVERSION = 1;

    private final static String KEY_ID = "id";
    private final static String KEY_USERNAME = "username";
    private final static String KEY_PASSWORD = "password";

    private final static String TABLESQL = "CREATE TABLE IF NOT EXISTS "
            + TABLENAME + "(" + KEY_ID + " INTEGER PRIMARY KEY, " + KEY_USERNAME
            + " TEXT NOT NULL UNIQUE, " + KEY_PASSWORD + " TEXT NOT NULL);";



    public DBHelper(Context context){
        super(context, DBNAME, null, DBVERSION);
    }


    /**
     * Run only once, used to create table
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLESQL);
        Log.i(TAG, "Create Table: " + TABLESQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void reset(SQLiteDatabase db){
        Log.i(TAG, "Reset database");
        db.execSQL("DELETE FROM session");
    }
}

