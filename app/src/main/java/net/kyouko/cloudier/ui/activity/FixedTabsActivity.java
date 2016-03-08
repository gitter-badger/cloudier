package net.kyouko.cloudier.ui.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public abstract class FixedTabsActivity extends AppCompatActivity {

    protected class FixedTabsPagerAdapter extends FragmentPagerAdapter {

        private List<String> titles;
        private List<Fragment> fragments;


        public FixedTabsPagerAdapter(FragmentManager manager) {
            super(manager);
        }


        public FixedTabsPagerAdapter(FragmentManager manager, List<String> titles, List<Fragment> fragments) {
            super(manager);
            this.titles = titles;
            this.fragments = fragments;
        }


        public void addTab(String title, Fragment fragment) {
            titles = (titles == null) ? new ArrayList<String>() : titles;
            fragments = (fragments == null) ? new ArrayList<Fragment>() : fragments;

            titles.add(title);
            fragments.add(fragment);
        }


        public void setTabs(List<String> titles, List<Fragment> fragments) {
            this.titles = titles;
            this.fragments = fragments;
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }


        public CharSequence getPageTitle(int position) {
            return titles.get(position);
        }


        @Override
        public int getCount() {
            return fragments.size();
        }

    }

}
