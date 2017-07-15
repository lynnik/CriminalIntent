package com.lynnik.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.lynnik.criminalintent.database.CrimeBaseHelper;
import com.lynnik.criminalintent.database.CrimeCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.lynnik.criminalintent.database.CrimeDbSchema.CrimeTable;

public class CrimeLab {

  private static CrimeLab sCrimeLab;
  private Context mContext;
  private SQLiteDatabase mDatabase;

  private CrimeLab(Context context) {
    mContext = context.getApplicationContext();
    mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
  }

  public static CrimeLab getInstance(Context context) {
    if (sCrimeLab == null)
      sCrimeLab = new CrimeLab(context);
    return sCrimeLab;
  }

  private static ContentValues getContentValues(Crime crime) {
    ContentValues values = new ContentValues();
    values.put(CrimeTable.Cols.UUID, crime.getId().toString());
    values.put(CrimeTable.Cols.TITLE, crime.getTitle());
    values.put(CrimeTable.Cols.DATE, crime.getDate().getTime());
    values.put(CrimeTable.Cols.SOLVED, crime.isSolved() ? 1 : 0);
    values.put(CrimeTable.Cols.SUSPECT, crime.getSuspect());

    return values;
  }

  private CrimeCursorWrapper queryCrimes(String whereClause, String[] whereArgs) {
    Cursor cursor = mDatabase.query(
        CrimeTable.NAME,
        null,
        whereClause,
        whereArgs,
        null,
        null,
        null
    );

    return new CrimeCursorWrapper(cursor);
  }

  public void addCrime(Crime c) {
    ContentValues values = getContentValues(c);
    mDatabase.insert(CrimeTable.NAME, null, values);
  }

  public void removeCrime(Crime c) {
    mDatabase.delete(CrimeTable.NAME,
        CrimeTable.Cols.UUID + " = ?", new String[]{c.getId().toString()});
  }

  public List<Crime> getCrimes() {
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

  public Crime getCrime(UUID id) {
    CrimeCursorWrapper cursor = queryCrimes(
        CrimeTable.Cols.UUID + " = ?", new String[]{id.toString()});

    try {
      if (cursor.getCount() == 0)
        return null;

      cursor.moveToFirst();
      return cursor.getCrime();
    } finally {
      cursor.close();
    }
  }

  public void updateCrime(Crime c) {
    String uuidString = c.getId().toString();
    ContentValues values = getContentValues(c);

    mDatabase.update(CrimeTable.NAME, values,
        CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
  }

  public boolean isEmpty() {
    CrimeCursorWrapper cursor = queryCrimes(null, null);
    return cursor.getCount() == 0;
  }
}