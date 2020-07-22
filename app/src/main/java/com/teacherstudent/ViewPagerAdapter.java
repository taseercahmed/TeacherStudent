package com.teacherstudent;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class ViewPagerAdapter extends FragmentPagerAdapter {
    Fragment[] fragments;

    public ViewPagerAdapter(FragmentManager fm) {

        super(fm);
        fragments = new Fragment[]{
                new MONDAY(),
                new TUESDAY(),
                new WEDNESDAY(),
                new THRUSDAY(),
                new FRIDAY()
        };
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MONDAY();

            case 1:
                return new TUESDAY();

            case 2:
                return new WEDNESDAY();

            case 3:
                return new THRUSDAY();

            case 4:
                return new FRIDAY();

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
