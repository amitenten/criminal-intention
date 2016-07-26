package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CompoundButton;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Amita on 7/18/2016.
 */
public class CrimeFragment extends Fragment {

    private static final String CRIME_ID = "CrimeFragment.CRIME_ID";
    private static final String CRIME_POSITION = "CrimeFragment.CRIME_POS";

    private Crime crime;
    private int position;
    private EditText editText;
    private Button crimeDateBtt;
    private CheckBox crimeSlovedCheckbox;

    public CrimeFragment() {}

    public static CrimeFragment newInstance(UUID crimeId, int position) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, crimeId);
        args.putInt(CRIME_POSITION, position);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID);
        position = (int) getArguments().getInt(CRIME_POSITION);
        crime = CrimeLab.getInstance().getCrimesById(crimeId);
        Log.d(CrimeListFragment.TAG, "crime.getId()= " + crime.getId());
        Log.d(CrimeListFragment.TAG, "crime.getId()= " + crime.getTitle());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        editText = (EditText) v.findViewById(R.id.crime_title);
        editText.setText(crime.getTitle());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            crime.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        crimeDateBtt = (Button) v.findViewById(R.id.crime_date);
        crimeDateBtt.setText(getFormattedDate(crime.getCrimedate()));
        crimeDateBtt.setEnabled(false);

        crimeSlovedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        crimeSlovedCheckbox.setChecked(crime.isSolved());
        crimeSlovedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                Log.d(CrimeListFragment.TAG, "Crime: " + crime.toString());
            }
        });

//        Button newButton = (Button) v.findViewById(R.id.newButton);
//        if(newButton != null){}

        Intent intent = new Intent();
        intent.putExtra("position", position);
        Log.d(CrimeListFragment.TAG, "Send position back : " + position);
        getActivity().setResult(Activity.RESULT_OK, intent);

        return v;
    }


    private String getFormattedDate(Date date) {
        return new SimpleDateFormat("dd MMMM yyyy").format(date);
    }
}
