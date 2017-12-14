package com.ituition.ituition;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.ituition.ituition.Tabs.LatestReviewsTab;
import com.ituition.ituition.Tabs.PopularTutorsTab;
import com.ituition.ituition.Tabs.TutorsNearYouTab;

/**
 * Created by mushfiq on 12/14/17.
 */

public class HomePager extends FragmentStatePagerAdapter {
    private int tabCount;
    public HomePager(FragmentManager fm, int tabCount) {
        super(fm);

        this.tabCount = tabCount;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                TutorsNearYouTab tab1 = new TutorsNearYouTab();
                return tab1;
            case 1:
                PopularTutorsTab tab2 = new PopularTutorsTab();
                return tab2;
            case 2:
                LatestReviewsTab tab3 = new LatestReviewsTab();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}
