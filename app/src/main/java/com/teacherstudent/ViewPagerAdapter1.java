package com.teacherstudent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class ViewPagerAdapter1 extends FragmentPagerAdapter {
    Fragment[] fragments;

    public ViewPagerAdapter1(FragmentManager fm) {

        super(fm);
        fragments = new Fragment[]{
                new MONDAY1(),
                new TUESDAY1(),
                new WEDNESDAY1(),
                new THRUSDAY1(),
                new FRIDAY1()
        };
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MONDAY1();

            case 1:
                return new TUESDAY1();

            case 2:
                return new WEDNESDAY1();

            case 3:
                return new THRUSDAY1();

            case 4:
                return new FRIDAY1();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return 5;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title=getItem(position).getClass().getName();
        return title.subSequence(title.lastIndexOf(".") + 1, title.length());

        //  return super.getPageTitle(position);
    }
}
