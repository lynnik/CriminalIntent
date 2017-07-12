package com.lynnik.criminalintent;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

public class CrimeListFragment extends Fragment {

  private static final int REQUEST_CRIME = 1;

  private int mCrimePosition;
  private RecyclerView mCrimeRecyclerView;
  private CrimeAdapter mAdapter;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);
  }

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

  @Override
  public void onResume() {
    super.onResume();
    updateUI();
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_crime_list, menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_new_crime:
        Crime crime = new Crime();
        CrimeLab.getInstance(getActivity()).addCrime(crime);
        Intent intent = CrimePagerActivity
            .newIntent(getActivity(), crime.getId());
        startActivity(intent);
        return true;
      case R.id.menu_item_show_subtitle:
        updateSubtitle();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void updateSubtitle() {
    CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
    int crimeCount = crimeLab.getCrimes().size();
    String subtitle = getString(R.string.subtitle_format, crimeCount);

    AppCompatActivity activity = (AppCompatActivity) getActivity();
    activity.getSupportActionBar().setSubtitle(subtitle);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (REQUEST_CRIME == requestCode) {

    }
  }

  private void updateUI() {
    CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
    List<Crime> crimes = crimeLab.getCrimes();

    if (mAdapter == null) {
      mAdapter = new CrimeAdapter(crimes);
      mCrimeRecyclerView.setAdapter(mAdapter);
    } else {
      mAdapter.notifyItemChanged(mCrimePosition);
    }
  }

  private class CrimeHolder extends RecyclerView.ViewHolder
      implements View.OnClickListener {

    private int mPosition;
    private Crime mCrime;
    private TextView mTitleTextView;
    private TextView mDateTextView;
    private CheckBox mSolvedCheckBox;

    public CrimeHolder(View itemView) {
      super(itemView);
      itemView.setOnClickListener(this);

      mTitleTextView = (TextView)
          itemView.findViewById(R.id.list_item_crime_title_text_view);
      mDateTextView = (TextView)
          itemView.findViewById(R.id.list_item_crime_date_text_view);
      mSolvedCheckBox = (CheckBox)
          itemView.findViewById(R.id.list_item_crime_solved_check_box);
    }

    public void bindCrime(int position, Crime crime) {
      mPosition = position;
      mCrime = crime;
      mTitleTextView.setText(crime.getTitle());
      mDateTextView.setText(crime.getDate().toString());
      mSolvedCheckBox.setChecked(crime.isSolved());
    }

    @Override
    public void onClick(View view) {
      mCrimePosition = mPosition;
      Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());
      startActivity(intent);
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
      View v = layoutInflater.inflate(R.layout.list_item_crime, parent, false);
      return new CrimeHolder(v);
    }

    @Override
    public void onBindViewHolder(CrimeHolder holder, int position) {
      Crime crime = mCrimes.get(position);
      holder.bindCrime(position, crime);
    }

    @Override
    public int getItemCount() {
      return mCrimes.size();
    }
  }
}