package com.lynnik.criminalintent;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lynnik.criminalintent.database.CrimeBaseHelper;

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

    return values;
  }

  public void addCrime(Crime c) {
    ContentValues values = getContentValues(c);
    mDatabase.insert(CrimeTable.NAME, null, values);
  }

  public void removeCrime(Crime c) {
  }

  public List<Crime> getCrimes() {
    return null;
  }

  public Crime getCrime(UUID id) {
    return null;
  }

  public void updateCrime(Crime c) {
    String uuidString = c.getId().toString();
    ContentValues values = getContentValues(c);

    mDatabase.update(CrimeTable.NAME, values,
        CrimeTable.Cols.UUID + " = ?", new String[]{uuidString});
  }

  public boolean isEmpty() {
    return false;
  }
}