package net.kyouko.cloudier.ui.activity;

import android.app.ProgressDialog;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.api.RequestError;
import net.kyouko.cloudier.api.RequestErrorListener;
import net.kyouko.cloudier.api.RequestSuccessListener;
import net.kyouko.cloudier.api.timeline.HomeTimelineRequest;
import net.kyouko.cloudier.api.tweet.SendTweetRequest;
import net.kyouko.cloudier.model.Account;
import net.kyouko.cloudier.model.Timeline;
import net.kyouko.cloudier.model.TimelineEntry;
import net.kyouko.cloudier.model.TweetId;
import net.kyouko.cloudier.ui.adapter.TimelineAdapter;
import net.kyouko.cloudier.ui.dialog.EditTweetDialog;
import net.kyouko.cloudier.util.AccountUtil;
import net.kyouko.cloudier.util.RequestUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements RequestActivity {

    private final static int ACTION_USER_AUTH = 0;
    private final static String TAG_REQUESTS = "REQUEST_MAIN";
    private final static String TAG_EDIT_TWEET_DIALOG = "EDIT_TWEET_DIALOG";


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

    private RequestQueue requestQueue;
    private Account currentAccount;
    private TimelineAdapter adapter;
    private Timeline timeline = new Timeline();
    private List<TimelineEntry> timelineEntries = new ArrayList<>();
    private LinkedHashMap<String, String> timelineUserList = new LinkedHashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        initRequestQueue();

        if (!hasAuthorized()) {
            startAuth();
        } else {
            fetchHomeTimeline();
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
                fetchHomeTimeline();
            }
        });
    }


    private void initRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        adapter = new TimelineAdapter(this, timelineEntries, timelineUserList);
        recyclerView.setAdapter(adapter);
    }


    private void initFab() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditTweetDialog editTweetDialog = new EditTweetDialog();
                editTweetDialog.setOnEditFinishedListener(new EditTweetDialog.OnEditFinishedListener() {
                    @Override
                    public void onEditFinished(String content) {
                        if (content.length() != 0) {
                            editTweetDialog.dismiss();
                            sendTweet(content);
                        } else {
                            Toast.makeText(getApplicationContext(),
                                    getString(R.string.text_info_empty_content),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                editTweetDialog.show(getSupportFragmentManager(), TAG_EDIT_TWEET_DIALOG);
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


    private void initRequestQueue() {
        requestQueue = RequestUtil.getRequestQueue(this);
        requestQueue.start();
    }


    private void fetchHomeTimeline() {
        swipeRefreshLayout.setRefreshing(true);
        new HomeTimelineRequest(this,
                new RequestSuccessListener<Timeline>() {
                    @Override
                    public void onRequestSuccess(Timeline result) {
                        swipeRefreshLayout.setRefreshing(false);

                        timeline = result;
                        timelineEntries.clear();
                        timelineEntries.addAll(timeline.tweets);

                        sortUserListDesc(timeline.userList);

                        adapter.notifyDataSetChanged();
                    }
                },
                new RequestErrorListener() {
                    @Override
                    public void onRequestError(RequestError error) {
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }
        ).execute();
    }


    private void sortUserListDesc(Map<String, String> originalUserList) {
        List<String> usernames = new ArrayList<>();
        usernames.addAll(originalUserList.keySet());
        Collections.sort(usernames, new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                int minLength = Math.min(s1.length(), s2.length());
                for (int i = 0; i < minLength; i += 1) {
                    if (s1.charAt(i) != s2.charAt(i)) {
                        return (s2.charAt(i) - s1.charAt(i));
                    }
                }
                return (s2.length() - s1.length());
            }
        });

        timelineUserList.clear();
        for (String username : usernames) {
            timelineUserList.put(username, originalUserList.get(username));
        }
    }


    private void sendTweet(String content) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "",
                getString(R.string.text_progress_sending_tweet), true);

        new SendTweetRequest(this, content, new RequestSuccessListener<TweetId>() {
            @Override
            public void onRequestSuccess(TweetId result) {
                progressDialog.dismiss();
                Snackbar.make(coordinatorLayout, getString(R.string.text_info_tweet_send_success),
                        Snackbar.LENGTH_SHORT).show();
            }
        }, new RequestErrorListener() {
            @Override
            public void onRequestError(RequestError error) {
                progressDialog.dismiss();
                Log.e("Error", error.getLocalizedMessage());
                Snackbar.make(coordinatorLayout, getString(R.string.text_info_tweet_send_fail),
                        Snackbar.LENGTH_SHORT)
                        .setAction(getString(R.string.title_action_resend),
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        // TODO: resend after fail
                                        Toast.makeText(getApplicationContext(), "Coming soon", Toast.LENGTH_SHORT).show();
                                    }
                                }
                        ).show();
            }
        }).execute();
    }


    @Override
    public void executeRequest(Request request) {
        request.setTag(TAG_REQUESTS);
        requestQueue.add(request);
    }


    @Override
    protected void onStop() {
        super.onStop();
        requestQueue.cancelAll(TAG_REQUESTS);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ACTION_USER_AUTH) {
            if (resultCode == RESULT_OK) {
                loadAuthorizedAccount();
                fetchHomeTimeline();
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
