package qbai22.com.criminalintent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
    public static final String SAVED_SUBTITLE_VISIBLE = "subtitle";
    public static final String CRIME_INDEX_KEY = "qbai22.com.criminalintent.CRIME_INDEX_KEY";
    public static final int REQUEST_CRIME = 1;
    private RecyclerView mCrimeRecycleView;
    private CrimeAdapter mAdapter;
    private int changedItemPosition;
    private boolean mSubtitleVisible;
    private TextView mGreetTextView;
    private Callbacks mCallbacks;

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Activity a;
        if(context instanceof Activity){
            a = (Activity)context;
            mCallbacks = (Callbacks) context; //host activity has to realize Callbacks interface
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (savedInstanceState != null) {
            mSubtitleVisible = savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }

        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);
        mCrimeRecycleView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);
        mCrimeRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mGreetTextView = (TextView) v.findViewById(R.id.greet_text_view);
        mGreetTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createNewCrime();
            }
        });
        updateUI();
        return v;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_crime_list, menu);

        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if (mSubtitleVisible) {
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_new_crime) {
            createNewCrime();
            return true;
        }
        if (item.getItemId() == R.id.menu_item_show_subtitle) {
            mSubtitleVisible = !mSubtitleVisible;
            getActivity().invalidateOptionsMenu();
            updateSubtitle();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        updateUI();

    }

    private void updateUI() {
        CrimeLab crimeLab = CrimeLab.getCrimeLab(getActivity());
        List<Crime> crimes = crimeLab.getCrimes();
        String test = "";

        if (mAdapter == null) {
            mAdapter = new CrimeAdapter(crimes);
            mCrimeRecycleView.setAdapter(mAdapter);
        } else {
            mAdapter.setCrimes(crimes);
            mAdapter.notifyDataSetChanged();
        }

       if (CrimeLab.getCrimeLab(getActivity()).getCrimes().size() != 0) {
            mGreetTextView.setVisibility(View.INVISIBLE);
            mGreetTextView.setEnabled(false);
        } else {
            mGreetTextView.setVisibility(View.VISIBLE);
            mGreetTextView.setEnabled(true);
        }

        updateSubtitle();
    }

    private void updateSubtitle() {
        CrimeLab crimeLab = CrimeLab.getCrimeLab(getActivity());
        int crimeCount = crimeLab.getCrimes().size();
        String subtitle = getResources()
                .getQuantityString(R.plurals.subtitle_plural, crimeCount, crimeCount);

        if (!mSubtitleVisible) {
            subtitle = null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
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

    public void createNewCrime() {
        Crime crime = new Crime();
        CrimeLab.getCrimeLab(getActivity()).addCrime(crime);
        updateUI();
        /*Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
        startActivity(intent);*/
        mCallbacks.onCrimeSelected(crime);
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;

        private Crime mCrime;

        public CrimeHolder(View itemView) {
            super(itemView);
            mTitleTextView = (TextView) itemView.findViewById(R.id.crime_title_item_text_view);
            mDateTextView = (TextView) itemView.findViewById(R.id.crime_date_item_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.check_box_item_crime_solved);

            itemView.setOnClickListener(this);
        }

        public void bindCrime(Crime crime) {
            mCrime = crime;
            mTitleTextView.setText(mCrime.getTitle());
            mDateTextView.setText(mCrime.getFormattedDate());
            mSolvedCheckBox.setChecked(mCrime.isSolved());
        }

        @Override
        public void onClick(View view) {
            /*Intent intent = CrimePagerActivity.newIntent(getActivity(), mCrime.getId());

            startActivity(intent);*/
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
            View v = layoutInflater.inflate(R.layout.crime_list_item, parent, false);
            return new CrimeHolder(v);
        }


        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Crime crime = mCrimes.get(position);
            holder.bindCrime(crime);
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
