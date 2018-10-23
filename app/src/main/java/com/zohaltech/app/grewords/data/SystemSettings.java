package com.zohaltech.app.grewords.data;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.zohaltech.app.grewords.entities.SystemSetting;

import java.util.ArrayList;

public class SystemSettings {
    static final String TableName        = "SystemSettings";
    static final String Id               = "Id";
    static final String Installed        = "Installed";
    static final String RingingToneUri   = "RingingToneUri";
    static final String AlarmRingingTone = "AlarmRingingTone";
    static final String VibrateInAlarms  = "VibrateInAlarms";
    static final String SoundInAlarms    = "SoundInAlarms";
    
    
    static final String CreateTable = "CREATE TABLE " + TableName + " (" +
            Id + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
            Installed + " BOOLEAN NOT NULL, " +
            RingingToneUri + " VARCHAR(20)," +
            AlarmRingingTone + " VARCHAR(20)," +
            VibrateInAlarms + " BOOLEAN NOT NULL, " +
            SoundInAlarms + " BOOLEAN NOT NULL " +
            " ); ";
    static final String DropTable   = "Drop Table If Exists " + TableName;
    
    private static ArrayList<SystemSetting> select(Context context, String whereClause, String[] selectionArgs) {
        ArrayList<SystemSetting> settings = new ArrayList<>();
        DataAccess da = new DataAccess(context);
        SQLiteDatabase db = da.getReadableDB();
        Cursor cursor = null;
        
        try {
            String query = "Select * From " + TableName + " " + whereClause;
            cursor = db.rawQuery(query, selectionArgs);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SystemSetting systemSetting = new SystemSetting(cursor.getInt(cursor.getColumnIndex(Id)),
                            cursor.getInt(cursor.getColumnIndex(Installed)) == 1,
                            cursor.getString(cursor.getColumnIndex(RingingToneUri)),
                            cursor.getString(cursor.getColumnIndex(AlarmRingingTone)),
                            cursor.getInt(cursor.getColumnIndex(VibrateInAlarms)) == 1,
                            cursor.getInt(cursor.getColumnIndex(SoundInAlarms)) == 1);
                    settings.add(systemSetting);
                } while (cursor.moveToNext());
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
            if (db != null && db.isOpen())
                db.close();
        }
        return settings;
    }
    
    public static long update(Context context, SystemSetting setting) {
        ContentValues values = new ContentValues();
        
        values.put(Installed, setting.getInstalled() ? 1 : 0);
        values.put(RingingToneUri, setting.getRingingToneUri());
        values.put(AlarmRingingTone, setting.getAlarmRingingTone());
        values.put(VibrateInAlarms, setting.getVibrateInAlarms() ? 1 : 0);
        values.put(SoundInAlarms, setting.getSoundInAlarms() ? 1 : 0);
        
        DataAccess da = new DataAccess(context);
        return da.update(TableName, values, Id + " = ? ", new String[]{String.valueOf(setting.getId())});
    }
    
    //public static long delete(SystemSetting setting) {
    //    DataAccess db = new DataAccess();
    //    return db.delete(TableName, Id + " =? ", new String[]{String.valueOf(setting.getId())});
    //}
    
    public static SystemSetting getCurrentSettings(Context context) {
        ArrayList<SystemSetting> settings = select(context, "", null);
        int count = settings.size();
        
        return (count == 0) ? null : settings.get(0);
    }
}
