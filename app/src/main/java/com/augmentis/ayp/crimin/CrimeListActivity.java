package com.augmentis.ayp.crimin;

import android.content.Intent;
import android.support.v4.app.Fragment;

public class CrimeListActivity extends SingleFragmentActivity implements CrimeListFragment.Callback
        , CrimeFragment.Callbacks {

    @Override
    protected Fragment onCreateFragment() {
        return new CrimeListFragment();
    }

    @Override
    public void onCrimeSelect(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            //single pane
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {

            CrimeFragment currentDetailFragment = (CrimeFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.detail_fragment_container);

            if (currentDetailFragment == null || !currentDetailFragment
                    .getCrimeId()
                    .equals(crime.getId())) {
                //two pane
                Fragment newDetailFragment = CrimeFragment.newInstance(crime.getId());

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.detail_fragment_container, newDetailFragment)
                        .commit();
            } else {
                currentDetailFragment.updateUI();
            }
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        listFragment.updateUI();
    }

    @Override
    public void onCrimeDelete() {
        CrimeListFragment listFragment = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);
        CrimeFragment detailFragment = (CrimeFragment) getSupportFragmentManager()
                .findFragmentById(R.id.detail_fragment_container);
        listFragment.updateUI();

        getSupportFragmentManager().beginTransaction().detach(detailFragment).commit();
    }
}
