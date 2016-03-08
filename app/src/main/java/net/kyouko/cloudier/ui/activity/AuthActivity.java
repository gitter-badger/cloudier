package net.kyouko.cloudier.ui.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.api.RequestError;
import net.kyouko.cloudier.api.RequestErrorListener;
import net.kyouko.cloudier.api.RequestSuccessListener;
import net.kyouko.cloudier.api.user.UserInfoRequest;
import net.kyouko.cloudier.application.Config;
import net.kyouko.cloudier.model.Account;
import net.kyouko.cloudier.model.User;
import net.kyouko.cloudier.util.AccountUtil;
import net.kyouko.cloudier.util.RequestUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity implements RequestActivity {

    private final static String TAG_REQUESTS = "REQUEST_AUTH";


    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webview_auth)
    WebView authWebView;


    private Context context;
    private RequestQueue requestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        context = this;

        initView();
        initRequestQueue();
    }


    private void initView() {
        ButterKnife.bind(this);

        initToolbar();
        initWebView();
    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
    }


    private void initWebView() {
        authWebView.getSettings().setJavaScriptEnabled(true);
        authWebView.setWebViewClient(new AuthWebViewClient(new AuthWebViewClient.OnAuthSuccessListener() {
            @Override
            public void onAuthSuccess(Account account) {
                AccountUtil.saveAccount(context, account);
                fetchUserAvatar(account.username);
            }
        }));

        loadAuthPage();
    }


    private void loadAuthPage() {
        try {
            authWebView.loadUrl("https://open.t.qq.com/cgi-bin/oauth2/authorize?client_id="
                    + Config.TENCENT_APP_KEY + "&response_type=token&redirect_uri="
                    + URLEncoder.encode(Config.TENCENT_REDIRECT_URL, "UTF-8"));
        } catch (UnsupportedEncodingException ex) {
            // Ignore.
        }
    }


    private void initRequestQueue() {
        requestQueue = RequestUtil.getRequestQueue(this);
        requestQueue.start();
    }


    private void fetchUserAvatar(String username) {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "",
                getString(R.string.text_progress_fetching_user_info), true);

        new UserInfoRequest(this, username,
                new RequestSuccessListener<User>() {
                    @Override
                    public void onRequestSuccess(User user) {
                        progressDialog.dismiss();

                        Account account = AccountUtil.readAccount(context);
                        account.avatarUrl = user.avatarUrl;
                        AccountUtil.saveAccount(context, account);

                        Toast.makeText(getApplicationContext(),
                                getString(R.string.text_toast_auth_succeeded),
                                Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);
                        finish();
                    }
                },
                new RequestErrorListener() {
                    @Override
                    public void onRequestError(RequestError error) {
                        progressDialog.dismiss();

                        Toast.makeText(getApplicationContext(),
                                getString(R.string.text_toast_auth_succeeded),
                                Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);
                        finish();
                    }
                }
        ).execute();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG_REQUESTS);
        }
    }


    @Override
    public void onBackPressed() {
        setResult(RESULT_CANCELED);
        super.onBackPressed();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_auth, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            loadAuthPage();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void executeRequest(Request request) {
        request.setTag(TAG_REQUESTS);
        requestQueue.add(request);
    }


    private static class AuthWebViewClient extends WebViewClient {

        public interface OnAuthSuccessListener {
            void onAuthSuccess(Account account);
        }


        private OnAuthSuccessListener onAuthSuccessListener;


        public AuthWebViewClient(OnAuthSuccessListener onAuthSuccessListener) {
            this.onAuthSuccessListener = onAuthSuccessListener;
        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(Config.TENCENT_REDIRECT_URL)) {
                Account account = AccountUtil.parseAccountFromUrl(url);

                if (onAuthSuccessListener != null) {
                    onAuthSuccessListener.onAuthSuccess(account);
                }

                return true;
            } else if (url.startsWith("http")) {
                view.loadUrl(url);
                return true;
            } else {
                return false;
            }
        }
    }

}
