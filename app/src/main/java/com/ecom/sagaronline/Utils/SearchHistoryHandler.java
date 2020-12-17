package com.ecom.sagaronline.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchHistoryHandler extends SQLiteOpenHelper {

    private static String DB_NAME = "search3";
    private static int DB_VERSION = 3;
    private SQLiteDatabase db;

    public static final String HISTORY_TABLE = "history";
    public static final String HISTORY_NAME = "searched";
    public static final String HISTORY_ID = "search_id";

    public SearchHistoryHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        this.db=db;
        String exe = "CREATE TABLE IF NOT EXISTS " + HISTORY_TABLE
                + "(" + HISTORY_ID + " TEXT primary key ,"
                + HISTORY_NAME + " integer NOT NULL"
                + ")";

        db.execSQL(exe);

    }
    public boolean setHistory(String name,String id) {
        db = getWritableDatabase();
        if (isInHistory(name)){
            db.execSQL("update " + HISTORY_TABLE + " set " + HISTORY_ID + " = '" + id + "' where " + HISTORY_NAME + " LIKE '" + name+"'");
            return false;
        }
        else {
            ContentValues values = new ContentValues();
            values.put(HISTORY_NAME, name);
            values.put(HISTORY_ID,id);
            db.insert(HISTORY_TABLE, null, values);

            return true;
        }
    }
    public boolean isInHistory(String name) {
        db = getReadableDatabase();
        String qry = "Select *  from " + HISTORY_TABLE + " WHERE " + HISTORY_NAME + " LIKE '" + name+"'";
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) return true;
        return false;
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
    public ArrayList<HashMap<String, String>> getHistoryAll() {
        ArrayList<HashMap<String, String>> list = new ArrayList<>();
        db = getReadableDatabase();
        String qry = "Select *  from " + HISTORY_TABLE + " Order by " + HISTORY_ID + " desc";
        Cursor cursor = db.rawQuery(qry, null);
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            HashMap<String, String> map = new HashMap<>();
            map.put(HISTORY_ID, cursor.getString(cursor.getColumnIndex(HISTORY_ID)));
            map.put(HISTORY_NAME, cursor.getString(cursor.getColumnIndex(HISTORY_NAME)));
            list.add(map);
            cursor.moveToNext();
        }
        return list;
    }

}
