package com.lynnik.criminalintent;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.lynnik.criminalintent.database.CrimeBaseHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeLab {

  private static CrimeLab sCrimeLab;
  private List<Crime> mCrimes;
  private Context mContext;
  private SQLiteDatabase mDatabase;

  private CrimeLab(Context context) {
    mContext = context.getApplicationContext();
    mDatabase = new CrimeBaseHelper(mContext).getWritableDatabase();
    mCrimes = new ArrayList<>();
  }

  public static CrimeLab getInstance(Context context) {
    if (sCrimeLab == null)
      sCrimeLab = new CrimeLab(context);
    return sCrimeLab;
  }

  public void addCrime(Crime c) {
    mCrimes.add(c);
  }

  public void removeCrime(Crime c) {
    mCrimes.remove(c);
  }

  public List<Crime> getCrimes() {
    return mCrimes;
  }

  public Crime getCrime(UUID id) {
    for (Crime crime : mCrimes) {
      if (crime.getId().equals(id))
        return crime;
    }
    return null;
  }

  public boolean isEmpty() {
    return mCrimes.size() == 0;
  }
}