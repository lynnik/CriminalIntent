package com.lynnik.criminalintent;

import android.app.Activity;
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

  private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
  private static final int REQUEST_CRIME = 1;

  private int mCrimePosition;
  private RecyclerView mCrimeRecyclerView;
  private TextView mTextViewListEmpty;
  private CrimeAdapter mAdapter;
  private boolean mSubtitleVisible;
  private Callbacks mCallbacks;

  public interface Callbacks {
    void onCrimeSelected(Crime crime);
  }

  @Override
  public void onAttach(Activity activity) {
    super.onAttach(activity);
    mCallbacks = (Callbacks) activity;
  }

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
    mCrimeRecyclerView = (RecyclerView)
        v.findViewById(R.id.crime_recycler_view);
    mTextViewListEmpty = (TextView)
        v.findViewById(R.id.crime_recycler_view_empty);
    mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

    if (savedInstanceState != null) {
      mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
    }

    updateUI();

    return v;
  }

  @Override
  public void onResume() {
    super.onResume();
    updateUI();
  }

  @Override
  public void onSaveInstanceState(Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(SAVED_SUBTITLE_VISIBLE, mSubtitleVisible);
  }

  @Override
  public void onDetach() {
    super.onDetach();
    mCallbacks = null;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);
    inflater.inflate(R.menu.fragment_crime_list, menu);

    MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
    if (mSubtitleVisible)
      subtitleItem.setTitle(R.string.hide_subtitle);
    else
      subtitleItem.setTitle(R.string.show_subtitle);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.menu_item_new_crime:
        Crime crime = new Crime();
        CrimeLab.getInstance(getActivity()).addCrime(crime);
        updateUI();
        mCallbacks.onCrimeSelected(crime);
        return true;
      case R.id.menu_item_show_subtitle:
        mSubtitleVisible = !mSubtitleVisible;
        getActivity().invalidateOptionsMenu();
        updateSubtitle();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void updateSubtitle() {
    CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
    int crimeCount = crimeLab.getCrimes().size();
    String subtitle = getResources()
        .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

    if (!mSubtitleVisible)
      subtitle = null;

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
      mAdapter.setCrimes(crimes);
      mAdapter.notifyItemChanged(mCrimePosition);
    }

    updateSubtitle();
    listFullness();
  }

  private void listFullness() {
    if (CrimeLab.getInstance(getActivity()).isEmpty()) {
      mCrimeRecyclerView.setVisibility(View.GONE);
      mTextViewListEmpty.setVisibility(View.VISIBLE);
    } else {
      mTextViewListEmpty.setVisibility(View.GONE);
      mCrimeRecyclerView.setVisibility(View.VISIBLE);
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
      mCallbacks.onCrimeSelected(mCrime);
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

    public void setCrimes(List<Crime> crimes) {
      mCrimes = crimes;
    }
  }
}