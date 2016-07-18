package com.augmentis.ayp.crimin;

import android.support.v4.app.Fragment;

public class CrimeActivity extends SingleFragmentActivity {
    protected static final String TAG = "com.augmentis.ayp.CRIME"; //เอาไว้แท็กดูล็อค

    @Override
    protected Fragment onCreateFragment() {
        return new CrimeFragment();
    }
}
