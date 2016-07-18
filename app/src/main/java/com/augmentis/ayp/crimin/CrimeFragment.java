package com.augmentis.ayp.crimin;

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

/**
 * Created by Amita on 7/18/2016.
 */
public class CrimeFragment extends Fragment {

    private Crime crime;
    private EditText editText;
    private Button crimeDateBtt;
    private CheckBox crimeSlovedCheckbox;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        crime = new Crime();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_crime, container, false);

        editText = (EditText) v.findViewById(R.id.crime_title);

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
        Date date = new Date();
        date.getDate();
        crimeDateBtt.setText(getFormattedDate(crime.getCrimedate()));
        crimeDateBtt.setEnabled(false);

        crimeSlovedCheckbox = (CheckBox) v.findViewById(R.id.crime_solved);
        crimeSlovedCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                crime.setSolved(isChecked);
                Log.d(CrimeActivity.TAG, "Crime: " + crime.toString());
            }
        });

//        Button newButton = (Button) v.findViewById(R.id.newButton);
//        if(newButton != null){}

        return v;
    }

    private String getFormattedDate(Date date) {
        return new SimpleDateFormat("dd MMMM yyyy").format(date);
    }
}
