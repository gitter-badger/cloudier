package net.kyouko.cloudier.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.fragment.UserListFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class FriendsActivity extends FixedTabsActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.tabs)
    TabLayout tabLayout;
    @Bind(R.id.view_pager)
    ViewPager viewPager;

    UserListFragment fragmentFollowing;
    UserListFragment fragmentFollowers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends);

        ButterKnife.bind(this);

        initToolbar();
        initTabs();
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initTabs() {
        FixedTabsPagerAdapter adapter = new FixedTabsPagerAdapter(getSupportFragmentManager());

        fragmentFollowing = new UserListFragment();
        fragmentFollowers = new UserListFragment();

        // TODO: Prepare data for fragments.

        adapter.addTab(getString(R.string.title_tab_following), fragmentFollowing);
        adapter.addTab(getString(R.string.title_tab_followers), fragmentFollowers);

        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
    }

}
