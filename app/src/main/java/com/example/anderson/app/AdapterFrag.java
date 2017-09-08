package com.example.anderson.app;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by anderson on 22/07/17.
 */

public class AdapterFrag extends FragmentPagerAdapter {

    public AdapterFrag(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new FragmentVelocidade();
            case 1:
                return new FragmentVelocidade();
            case 2:
                return new FragmentVelocidade();
            case 3:
                return new FragmentVelocidade();
            case 4:
                return new FragmentVelocidade();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 5;
    }
}
