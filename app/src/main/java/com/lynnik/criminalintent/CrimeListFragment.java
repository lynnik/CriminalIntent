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

import java.util.List;

public class CrimeListFragment extends Fragment {

  private RecyclerView mCrimeRecyclerView;
  private CrimeAdapter mAdapter;

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater,
      @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
    mCrimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);
    mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    updateUI();

    return v;
  }

  private void updateUI() {
    CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
    List<Crime> crimes = crimeLab.getCrimes();
    mAdapter = new CrimeAdapter(crimes);
    mCrimeRecyclerView.setAdapter(mAdapter);
  }

  private class CrimeHolder extends RecyclerView.ViewHolder {

    public TextView mTitleTextView;

    public CrimeHolder(View itemView) {
      super(itemView);

      mTitleTextView = (TextView) itemView;
    }
  }

  private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {

    private List<Crime> mCrimes;

    public CrimeAdapter(List<Crime> crimes) {
      mCrimes = crimes;
    }

    @Override
    public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
      View v = layoutInflater
          .inflate(android.R.layout.simple_list_item_1, parent, false);
      return new CrimeHolder(v);
    }

    @Override
    public void onBindViewHolder(CrimeHolder holder, int position) {
      Crime crime = mCrimes.get(position);
      holder.mTitleTextView.setText(crime.getTitle());
    }

    @Override
    public int getItemCount() {
      return mCrimes.size();
    }
  }
}