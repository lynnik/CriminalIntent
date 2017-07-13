package com.lynnik.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lynnik.criminalintent.database.CrimeBaseHelper;

import java.util.List;
import java.util.UUID;

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

  public void addCrime(Crime c) {
  }

  public void removeCrime(Crime c) {
  }

  public List<Crime> getCrimes() {
    return null;
  }

  public Crime getCrime(UUID id) {
    return null;
  }

  public boolean isEmpty() {
    return false;
  }
}