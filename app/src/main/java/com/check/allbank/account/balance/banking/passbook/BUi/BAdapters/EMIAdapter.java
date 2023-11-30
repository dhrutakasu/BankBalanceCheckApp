package com.check.allbank.account.balance.banking.passbook.BUi.BAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.check.allbank.account.balance.banking.passbook.BUi.BActivities.Calculators.EMICalculatorActivity;
import com.check.allbank.account.balance.banking.passbook.BUi.BFragments.EMIFragment;

public class EMIAdapter extends FragmentPagerAdapter {
    private final int count;

    public EMIAdapter(FragmentManager supportFragmentManager, int i) {
        super(supportFragmentManager);
        this.count = i;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        EMICalculatorActivity.PosLevel = position;
        return new EMIFragment();
    }

    @Override
    public int getItemPosition(@NonNull Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return count;
    }
}
