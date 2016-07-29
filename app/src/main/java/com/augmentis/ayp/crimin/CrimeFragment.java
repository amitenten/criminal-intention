package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
    private static final String DIALOG_DATE = "CrimeFragment.DIALOG_DATE";
    private static final String DIALOG_TIME = "CrimeFragment.DIALOG_TIME";
    private static final int REQUET_DATE = 2005;
    private static final int REQUET_TIME = 0713;

    private Crime crime;
    private EditText editText;
    private Button crimeDateBtt;
    private Button crimeTimeBtt;
    private Button crimeDeleteBtt;
    private CheckBox crimeSlovedCheckbox;

    public CrimeFragment() {
    }

    public static CrimeFragment newInstance(UUID crimeId) {
        Bundle args = new Bundle();
        args.putSerializable(CRIME_ID, crimeId);

        CrimeFragment crimeFragment = new CrimeFragment();
        crimeFragment.setArguments(args);
        return crimeFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID);
        crime = CrimeLab.getInstance(getActivity()).getCrimesById(crimeId);

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
        crimeDateBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                DatePickerFragment datePickerFragment =
                        DatePickerFragment.newInstance(crime.getCrimedate());
                datePickerFragment.setTargetFragment(CrimeFragment.this, REQUET_DATE);
                datePickerFragment.show(fm, DIALOG_DATE);
            }
        });
        crimeTimeBtt = (Button) v.findViewById(R.id.crime_time);
        crimeTimeBtt.setText(getFormattedTime(crime.getCrimedate()));
        crimeTimeBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentManager fm = getFragmentManager();
                TimePickerFragmant timePickerFragmant =
                        TimePickerFragmant.newInstance(crime.getCrimedate());
                timePickerFragmant.setTargetFragment(CrimeFragment.this, REQUET_TIME);
                timePickerFragmant.show(fm, DIALOG_TIME);
            }
        });

        crimeSlovedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        crimeSlovedCheckbox.setChecked(crime.isSolved());
        crimeSlovedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                Log.d(CrimeListFragment.TAG, "Crime: " + crime.toString());
            }
        });

        crimeDeleteBtt = (Button) v.findViewById(R.id.delete_button);
        crimeDeleteBtt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrimeLab.getInstance(getActivity()).deleteCrime(crime);
                getActivity().finish();
            }
        });

        return v;
    }

    private String getFormattedDate(Date date) {
        return new SimpleDateFormat("dd MMMM yyyy").format(date);
    }

    private String getFormattedTime(Date time) {
        return new SimpleDateFormat("HH:mm").format(time);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == REQUET_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);

            //set
            crime.setCrimedate(date);
            crimeDateBtt.setText(getFormattedDate(crime.getCrimedate()));
        }
        if (requestCode == REQUET_TIME) {
            Date date = (Date) data.getSerializableExtra(TimePickerFragmant.EXTRA_TIME);

            //set
            crime.setCrimedate(date);
            crimeTimeBtt.setText(getFormattedTime(crime.getCrimedate()));
        }
    }
}
