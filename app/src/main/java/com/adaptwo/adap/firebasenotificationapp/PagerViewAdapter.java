package com.adaptwo.adap.firebasenotificationapp;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by AkshayeJH on 01/01/18.
 */

class PagerViewAdapter extends FragmentPagerAdapter{

    public PagerViewAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                com.adaptwo.adap.firebasenotificationapp.ProfileFragment profileFragment = new com.adaptwo.adap.firebasenotificationapp.ProfileFragment();
                return profileFragment;

            case 1:
                com.adaptwo.adap.firebasenotificationapp.UsersFragment usersFragment = new com.adaptwo.adap.firebasenotificationapp.UsersFragment();
                return usersFragment;

            case 2:
                NotificationFragment notificationFragment = new NotificationFragment();
                return  notificationFragment;

            default:
                return null;

        }

    }

    @Override
    public int getCount() {
        // 3 pages
        return 3;
    }

}
