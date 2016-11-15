package qbai22.com.criminalintent;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class CrimeListActivity extends SingleFragmentActivity
        implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

    @Override
    protected Fragment createFragment() {
        return new CrimeListFragment();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_masterdetail;
    }

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId());
            startActivity(intent);
        } else {
            Fragment newDetail = CrimeFragment.newInstance(crime.getId());
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(R.id.detail_fragment_container, newDetail)
                    .commit();
        }
    }

    @Override
    public void onCrimeDeleted() {
        FragmentManager fm = getSupportFragmentManager();
        CrimeListFragment f = (CrimeListFragment)
                fm.findFragmentById(R.id.fragment_container);
        f.updateUI();

        CrimeFragment cf = (CrimeFragment)
                fm.findFragmentById(R.id.detail_fragment_container);
        fm.beginTransaction()
                .remove(cf)
                .commit();
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) != null){
            CrimeListFragment clf = (CrimeListFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.fragment_container);
            clf.updateUI();
        }

    }
}
