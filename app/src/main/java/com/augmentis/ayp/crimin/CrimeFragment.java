package com.augmentis.ayp.crimin;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.text.format.DateFormat;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.StringTokenizer;
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
    private static final int REQUET_CONTACT_SUSPECT = 1307;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1209;

    private Crime crime;
    private EditText editText;
    private Button crimeDateBtt;
    private Button crimeTimeBtt;
    private Button crimeReport;
    private Button crimeSuspect;
    private Button crimeCallSuspect;
    private CheckBox crimeSlovedCheckbox;
    protected String phonenumber;

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

        setHasOptionsMenu(true);

        UUID crimeId = (UUID) getArguments().getSerializable(CRIME_ID);
        crime = CrimeLab.getInstance(getActivity()).getCrimesById(crimeId);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.crime_delete_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.menu_item_new_crime:
                CrimeLab.getInstance(getActivity()).deleteCrime(crime.getId());
                getActivity().finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void callSuspect(){
        Intent i = new Intent(Intent.ACTION_CALL);
        StringTokenizer tokenizer = new StringTokenizer(crime.getSuspect(), ":");
        String name = tokenizer.nextToken();
        String phone = tokenizer.nextToken();
        i.setData(Uri.parse("tel:" + phone));

        startActivity(i);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // Granted permission
                    callSuspect();

                } else {

                    // Denied permission
                    Toast.makeText(getActivity(),
                            R.string.denied_permission_to_call,
                            Toast.LENGTH_LONG)
                            .show();
                }
                return;
            }
        }
    }

    private boolean hasCallPermission() {

        // Check if permission is not granted
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{
                            Manifest.permission.CALL_PHONE
                    },
                    MY_PERMISSIONS_REQUEST_CALL_PHONE);

            return false; // checking -- wait for dialog
        }

        return true; // already has permission
    }


    private String getCrimeReport() {
        String solvedString = null;

        if (crime.isSolved()){
            solvedString = getString(R.string.crime_report_solve);
        } else {
            solvedString = getString(R.string.crime_report_unsolve);
        }

        String dateFormat = "EEE, MMM, dd";
        String dateString = DateFormat.format(dateFormat, crime.getCrimedate()).toString();

        String suspect = crime.getSuspect();

        if (suspect == null) {
            suspect = getString(R.string.crime_report_no_suspect);
        } else {
            suspect = getString(R.string.crime_report_with_suspect,suspect);
        }

        String report = getString(R.string.crime_report,
                crime.getTitle(), dateString, solvedString, suspect);

        return report;
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

        crimeReport = (Button) v.findViewById(R.id.crime_report);
        crimeReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_SEND);
                i.setType("text/plain"); //MIME Type
                i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
                i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));

                i = Intent.createChooser(i, getString(R.string.send_report));
                startActivity(i);
            }
        });

        final Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
//        pickContact.addCategory(Intent.CATEGORY_HOME);
        crimeSuspect = (Button) v.findViewById(R.id.crime_suspect);
        crimeSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(pickContact, REQUET_CONTACT_SUSPECT);
            }
        });

        if (crime.getSuspect() != null){
            crimeSuspect.setText(crime.getSuspect());
        }

        crimeCallSuspect = (Button) v.findViewById(R.id.crime_report);
        crimeCallSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentcall = new Intent();
                intentcall.setAction(Intent.ACTION_CALL);
                intentcall.setData(Uri.parse("tel:" + phonenumber)); // set the Uri
                startActivity(intentcall);
            }
        });

      /*  PackageManager packageManager = getActivity().getPackageManager();
        if (packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null){
            crimeSuspect.setEnabled(false);
        }*/
        if (crime.getSuspect() != null) {
            crimeSuspect.setText(crime.getSuspect());
        }

        PackageManager packageManager = getActivity().getPackageManager();
        if(packageManager.resolveActivity(pickContact, PackageManager.MATCH_DEFAULT_ONLY) == null ) {
            crimeSuspect.setEnabled(false);
        }

        crimeCallSuspect = (Button) v.findViewById(R.id.call_suspect);
        crimeCallSuspect.setEnabled( crime.getSuspect() != null );
        crimeCallSuspect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(hasCallPermission()) {
                    callSuspect();
                }
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
        if (requestCode == REQUET_CONTACT_SUSPECT) {
            if (data != null) {
                Uri contactUri = data.getData();
                String[] queryFields = new String[] {ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.NUMBER};

                Cursor c = getActivity().getContentResolver()
                        .query(contactUri,
                                queryFields,
                                null,
                                null,
                                null);
                try {
                    if(c.getCount() == 0) {
                        return ;
                    }

                    c.moveToFirst();
                    String suspect = c.getString(0);
                    suspect = suspect + ":" + c.getString(1);

                    crime.setSuspect(suspect);
                    crimeSuspect.setText(suspect);
                    crimeCallSuspect.setEnabled(suspect != null);
                } finally {
                    c.close();
                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        CrimeLab.getInstance(getActivity()).updateCrime(crime); //update crime in DB
    }
}
