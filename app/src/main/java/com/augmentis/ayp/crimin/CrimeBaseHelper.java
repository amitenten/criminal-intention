package com.augmentis.ayp.crimin;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.augmentis.ayp.crimin.CrimeDbSchema.CrimeTable;

/**
 * Created by Amita on 8/1/2016.
 */
public class CrimeBaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 2;

    private static final String DATABASE_NAME = "crimeBase.db";
    protected static final String TAG = "CrimeBaseHelper";

    public CrimeBaseHelper(Context context){
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "Create Database");

        db.execSQL("create table " + CrimeTable.NAME
                + "("
                + "_id integer primary key autoincrement"
                + ","
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED + ","
                + CrimeTable.Cols.SUSPECT + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.d(TAG,"Running upgrade db...");
        //1. rename tablt to _(oldversion)
        db.execSQL("alter table " + CrimeTable.NAME + " rename to " + CrimeTable.NAME + "_" + oldVersion);

        Log.d(TAG,"Drop table already...");
        //2. creat new table
        onCreate(db);

        //3.insert data from temp table
        db.execSQL("insert into " + CrimeTable.NAME
                + " ("
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED
                + ")"
                + " select "
                + CrimeTable.Cols.UUID + ","
                + CrimeTable.Cols.TITLE + ","
                + CrimeTable.Cols.DATE + ","
                + CrimeTable.Cols.SOLVED
                + " from "
                + CrimeTable.NAME + "_" + oldVersion
        );
        Log.d(TAG,"Insert data from temp table already...");
        //4.drop temp table
        db.execSQL("drop table if exists " + CrimeTable.NAME + "_" + oldVersion);
    }
}
