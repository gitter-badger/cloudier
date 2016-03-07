package net.kyouko.cloudier.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.model.Account;
import net.kyouko.cloudier.util.AccountUtil;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private final static int ACTION_USER_AUTH = 0;


    @Bind(R.id.coordinator)
    CoordinatorLayout coordinatorLayout;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.srl)
    SwipeRefreshLayout swipeRefreshLayout;
    @Bind(R.id.recycler)
    RecyclerView recyclerView;
    @Bind(R.id.fab)
    FloatingActionButton fab;

    RequestQueue requestQueue;
    private Account currentAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        requestQueue = Volley.newRequestQueue(this);

        initView();
        if (!hasAuthorized()) {
            startAuth();
        }
    }


    private void initView() {
        ButterKnife.bind(this);

        initToolbar();
        initSrl();
        initRecyclerView();
        initFab();
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
    }


    private void initSrl() {
        swipeRefreshLayout.setColorSchemeResources(R.color.light_blue_500, R.color.light_blue_700);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Snackbar.make(coordinatorLayout, "Refreshing", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    }


    private void initFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    private void loadAuthorizedAccount() {
        currentAccount = AccountUtil.readAccount(this);
    }


    private boolean hasAuthorized() {
        loadAuthorizedAccount();

        return (currentAccount.accessToken != null && currentAccount.accessToken.length() > 0);
    }


    private void startAuth() {
        startActivityForResult(new Intent(this, AuthActivity.class), ACTION_USER_AUTH);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_USER_AUTH) {
            if (resultCode == RESULT_OK) {
                loadAuthorizedAccount();
            } else if (resultCode == RESULT_CANCELED) {
                finish();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
            return true;
        }

        if (id == R.id.action_friends) {
            startActivity(new Intent(this, FriendsActivity.class));
            return true;
        }

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
