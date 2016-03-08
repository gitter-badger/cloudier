package net.kyouko.cloudier.ui.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import net.kyouko.cloudier.R;
import net.kyouko.cloudier.application.Config;
import net.kyouko.cloudier.model.Account;
import net.kyouko.cloudier.util.AccountUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AuthActivity extends AppCompatActivity {

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webview_auth)
    WebView authWebView;


    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        context = this;

        initView();
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
        authWebView.setWebViewClient(new AuthWebViewClient());
        authWebView.getSettings().setJavaScriptEnabled(true);

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


    private class AuthWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(Config.TENCENT_REDIRECT_URL)) {
                Account account = AccountUtil.parseAccountFromUrl(url);
                AccountUtil.saveAccount(context, account);

                setResult(RESULT_OK);
                finish();

                return true;
            } else if (url.startsWith("http")) {
                authWebView.loadUrl(url);
                return true;
            } else {
                return false;
            }
        }
    }

}
