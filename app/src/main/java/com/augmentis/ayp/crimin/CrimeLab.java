package com.augmentis.ayp.crimin;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.augmentis.ayp.crimin.CrimeDbSchema.CrimeTable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Amita on 7/18/2016.
 */
public class CrimeLab {
    private Context context;
    private SQLiteDatabase database;

    private static CrimeLab instance;
    private static final String TAG = "CrimeLab";

    public static CrimeLab getInstance(Context context) {
        if (instance == null) {
            instance = new CrimeLab(context);
        }
        return instance;
    }

    private static ContentValues getContentValues(Crime crime) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(CrimeTable.Cols.UUID, crime.getId().toString());
        contentValues.put(CrimeTable.Cols.TITLE, crime.getTitle());
        contentValues.put(CrimeTable.Cols.DATE, crime.getCrimedate().getTime());
        contentValues.put(CrimeTable.Cols.SOLVED, (crime.isSolved()) ? 1 : 0);
        contentValues.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());
        return contentValues;
    }

    private CrimeLab(Context context) {
        this.context = context.getApplicationContext();

        CrimeBaseHelper crimeBaseHelper = new CrimeBaseHelper(context);
        database = crimeBaseHelper.getWritableDatabase();

    }

    public Crime getCrimesById(UUID uuid) {
        CrimeCursorWrapper cursor = queryCrimes(CrimeTable.Cols.UUID + " = ? ",
                new String[]{uuid.toString()});

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getCrime();
        } finally {
            cursor.close();
        }
    }

/*    //set position
    public int getCrimesPositionById(UUID uuid) {
        return -1;
    }*/

    public CrimeCursorWrapper queryCrimes(String whereCause, String[] whereArgs) {
        Cursor cursor = database.query(CrimeTable.NAME,
                null,
                whereCause,
                whereArgs,
                null,
                null,
                null,
                null);
        return new CrimeCursorWrapper(cursor);
    }

    public List<Crime> getCrime() {
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper cursor = queryCrimes(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                crimes.add(cursor.getCrime());

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }
        return crimes;
    }

    public static void main(String[] args) {
        CrimeLab crimeLab = CrimeLab.getInstance(null);
        List<Crime> crimeList = crimeLab.getCrime();
        int size = crimeList.size();
        for (int i = 0; i < size; i++) {
            System.out.println(i);

//            System.out.println(crimeLab.toString());
//            System.out.println(CrimeLab.getInstance(null));
        }
    }

    public void addCrime(Crime crime) {
        Log.d(TAG, "Add Crime" + crime.toString());

        ContentValues contentValues = getContentValues(crime);
        database.insert(CrimeTable.NAME, null, contentValues);
    }

    public void deleteCrime(UUID uuid) {
        database.delete(CrimeTable.NAME,
                CrimeTable.Cols.UUID + " = ? ",
                new String[] {uuid.toString()});
    }

    public void updateCrime(Crime crime) {
        Log.d(TAG, "Update Crime" + crime.toString());

        String uuidStr = crime.getId().toString();
        ContentValues contentValues = getContentValues(crime);

        database.update(CrimeTable.NAME, contentValues,
                CrimeTable.Cols.UUID + " = ?", new String[]{uuidStr});
    }

    public File getPhotoFile(Crime crime) {
        File externalFilesDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        if (externalFilesDir == null) {
            return null;
        }

        return new File(externalFilesDir, crime.getPhotoFilename());
    }
}
