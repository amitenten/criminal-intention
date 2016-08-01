package com.augmentis.ayp.crimin;

import android.app.Activity;
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
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Amita on 7/18/2016.
 */
public class CrimeListFragment extends Fragment {

    private static final int REQUEST_UPDATED_CRIME = 137;
    private static final java.lang.String SUBTITLE_VISIBLE_STATE = "SUBTITLE_VISIBLE";

    private RecyclerView _crimeRecyclerView;
    private CrimeAdapter _adapter;
    protected static final String TAG = "CRIME_LIST";
    private Integer[] crimePos;

    private boolean _subtitleVisible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime_list, container, false);

        _crimeRecyclerView = (RecyclerView) v.findViewById(R.id.crime_recycler_view);
        _crimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if(savedInstanceState != null){
            _subtitleVisible = savedInstanceState.getBoolean(SUBTITLE_VISIBLE_STATE);
        }

        updateUI();

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.crime_list_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.menu_item_show_subtitle);

        if (_subtitleVisible){
            menuItem.setIcon(R.drawable.ic_item_hide_subtitle);
            menuItem.setTitle(R.string.hide_subtitle);
        }else {
            menuItem.setIcon(R.drawable.ic_item_show_subtitle);
            menuItem.setTitle(R.string.show_subtitle);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                Crime crime = new Crime();
                CrimeLab.getInstance(getActivity()).addCrime(crime);
                Intent intent = CrimePagerActivity.newIntent(getActivity(), crime.getId());
                startActivity(intent);
                return true;

            case R.id.menu_item_show_subtitle:
                _subtitleVisible = !_subtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateSubtitle() {
        CrimeLab crimLab = CrimeLab.getInstance(getActivity());
        int crimeCount = crimLab.getCrime().size();
        String subtitle = getString(R.string.subtitle_format, crimeCount);

        if (!_subtitleVisible){
            subtitle = null;
        }
        AppCompatActivity appCompatActivity = (AppCompatActivity) getActivity();
        appCompatActivity.getSupportActionBar().setSubtitle(subtitle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(SUBTITLE_VISIBLE_STATE, _subtitleVisible);
    }

    /**
     * Update UI
     */
    public void updateUI() {
        CrimeLab crimeLab = CrimeLab.getInstance(getActivity());
        List<Crime> crimes = crimeLab.getCrime();

        if (_adapter == null) {
            _adapter = new CrimeAdapter(crimes);
            _crimeRecyclerView.setAdapter(_adapter);
        } else {
            _adapter.setCrimes(crimeLab.getCrime());
            _adapter.notifyDataSetChanged();
            /*if (crimePos != null) {
                for (Integer pos : crimePos) {
                    _adapter.notifyItemChanged(pos);
                    Log.d(TAG, "notify change at " + pos);
                }
            }*/
        }

        updateSubtitle();

    }


    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "Resume list");
        updateUI();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_UPDATED_CRIME) {
            if (resultCode == Activity.RESULT_OK) {
                crimePos = (Integer[]) data.getExtras().get("position");
                Log.d(TAG, "get crimePos = " + crimePos);
            }
            Log.d(TAG, "Return from CrimeFragment ");
        }
    }

    private class CrimeHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView _titleTextView;
        public TextView _dateTextView;
        public CheckBox _solvedCheckBox;

        Crime _crime;
        int _position;

        public CrimeHolder(View itemView) {
            super(itemView);

            _titleTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_title_text_view);
            _solvedCheckBox = (CheckBox)
                    itemView.findViewById(R.id.list_item_crime_solve_check_box);
            //_solvedCheckBox.setEnabled(false);
            _solvedCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    _crime.setSolved(isChecked);
                    CrimeLab.getInstance(getActivity()).updateCrime(_crime);
                }
            });

            _dateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_crime_date_text_view);

            itemView.setOnClickListener(this);
        }

        public void bind(Crime crime, int position) {

            _crime = crime;
            _position = position;
            _titleTextView.setText(_crime.getTitle());
            _dateTextView.setText(_crime.getCrimedate().toString());
            _solvedCheckBox.setChecked(_crime.isSolved());
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "send position : " + _position);
            //Intent intent = CrimeActivity.newIntent(getActivity(), _crime.getId(), _position);
            Intent intent = CrimePagerActivity.newIntent(getActivity(), _crime.getId());
            startActivityForResult(intent, REQUEST_UPDATED_CRIME);
        }
    }

    private class CrimeAdapter extends RecyclerView.Adapter<CrimeHolder> {
        protected List<Crime> _crimes;
        private int ViewCreatingCount;

        public CrimeAdapter(List<Crime> crimes) {
            this._crimes = crimes;
        }

        protected void setCrimes(List<Crime> crimes){
            _crimes = crimes;
        }

        @Override
        public CrimeHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            ViewCreatingCount++;
            Log.d(TAG, "Create view holder for CrimeList : creating time view" + ViewCreatingCount);

            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View v = layoutInflater.inflate(R.layout.list_item_crime, parent, false);

            return new CrimeHolder(v);
        }

        @Override
        public void onBindViewHolder(CrimeHolder holder, int position) {
            Log.d(TAG, "Blind view holder for CrimeList : position = " + position);

            Crime crime = _crimes.get(position);
            holder.bind(crime, position);
        }

        @Override
        public int getItemCount() {
            return _crimes.size();
        }


    }
}
