package com.example.atlas.viewpagerdemo;

import android.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by atlas on 2016/8/31.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    private int numOfTab;
    public PagerAdapter(FragmentManager fm, int numOfTab) {
        super(fm);
        this.numOfTab = numOfTab;
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        switch (position) {
            case 0:
                FirstFragment tab1 = new FirstFragment();
                return tab1;
            case 1:
                SecondFragment tab2 = new SecondFragment();
                return tab2;
            case 2:
                ThirdFragment tab3 = new ThirdFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTab;
    }
}
