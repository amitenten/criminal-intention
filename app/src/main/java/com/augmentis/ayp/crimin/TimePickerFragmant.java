package com.augmentis.ayp.crimin;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimePickerFragmant extends DialogFragment implements DialogInterface.OnClickListener {

    protected static final String EXTRA_TIME = "extra_time";
    protected Date date;

    //.1
    public static TimePickerFragmant newInstance(Date time){
        TimePickerFragmant tf = new TimePickerFragmant();
        Bundle args = new Bundle();
        args.putSerializable("ARG_TIME", time);
        tf.setArguments(args);
        return tf;
    }

    TimePicker _timePicker;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        //3.

        date = (Date) getArguments().getSerializable("ARG_TIME");
        Calendar calendar = Calendar.getInstance();

        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int min = calendar.get(Calendar.MINUTE);

        View v = LayoutInflater.from(getActivity()).inflate(R.layout.activity_time_picker_fregmant, null);
        _timePicker = (TimePicker) v.findViewById(R.id.time_picker_in_dialog);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            _timePicker.setHour(hour);
            _timePicker.setMinute(min);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v);
        builder.setTitle(R.string.date_picker_title);
        builder.setNeutralButton(android.R.string.ok, this);
        return builder.create();
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        int hour = 0;
        int minute = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            hour = _timePicker.getHour();
            minute = _timePicker.getMinute();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR,hour);
        calendar.set(Calendar.MINUTE,minute);

        sendResult(Activity.RESULT_OK, calendar.getTime());

    }

    private void sendResult(int resultCode, Date time){
        if (getTargetFragment()== null){
            return;
        }else {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_TIME, time);

            getTargetFragment().onActivityResult(getTargetRequestCode(), resultCode, intent);
        }
    }


}
