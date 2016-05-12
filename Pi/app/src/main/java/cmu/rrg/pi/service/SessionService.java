package cmu.rrg.pi.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import cmu.rrg.pi.model.User;
import cmu.rrg.pi.util.DBHelper;

/**
 * Created by Kid7st on 5/2/16.
 */
public class SessionService {
    private DBHelper dbHelper;

    public SessionService(Context context){
        dbHelper = new DBHelper(context);
    }

    public void addSession(String username, String password){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("INSERT INTO session(username, password) values(?, ?);",
                    new Object[]{username, password});
            db.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void deleteSession(){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM session");
        db.close();
    }

    public Cursor getSession(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM session", null);
        return cursor;
    }
}
