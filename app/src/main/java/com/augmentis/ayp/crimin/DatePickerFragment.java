package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.DatePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Amita on 7/28/2016.
 */
public class DatePickerFragment extends DialogFragment implements DialogInterface.OnClickListener {

    protected static final String EXTRA_DATE = "extra_date";

    //.1
    public static DatePickerFragment newInstance(Date date){
        DatePickerFragment df = new DatePickerFragment();
        Bundle args = new Bundle();
        args.putSerializable("ARG_DATE", date);
        df.setArguments(args);
        return df;
    }

    DatePicker _datePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //3.
        Date date = (Date) getArguments().getSerializable("ARG_DATE");

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_date, null);
        _datePicker = (DatePicker) v.findViewById(R.id.date_picker_in_dialog);
        _datePicker.init(year,month,day,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle(R.string.date_picker_title);
        builder.setPositiveButton(android.R.string.ok, this);
        //builder.setNeutralButton();
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int day = _datePicker.getDayOfMonth();
        int month = _datePicker.getMonth();
        int year = _datePicker.getYear();

        Date date = new GregorianCalendar(year,month,day).getTime();
        sendResult(Activity.RESULT_OK, date);
    }

    private void sendResult(int resultCode, Date date){
        if (getTargetFragment()== null){
            return;
        }else {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATE, date);

            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }
}
