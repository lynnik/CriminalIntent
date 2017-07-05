package com.lynnik.criminalintent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class CrimeListFragment extends Fragment {

  private RecyclerView mCrimeRecyclerView;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
    mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);
    mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    return v;
  }

  private class CrimeHolder extends RecyclerView.ViewHolder {

    public TextView mTitleTextView;

    public CrimeHolder(View itemView) {
      super(itemView);

      mTitleTextView = (TextView) itemView;
    }
  }
}