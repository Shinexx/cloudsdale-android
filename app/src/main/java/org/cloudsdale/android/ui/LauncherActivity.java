package org.cloudsdale.android.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import org.cloudsdale.android.R;

import java.net.URI;

import butterknife.InjectView;
import butterknife.Views;

public class LauncherActivity extends Activity {

    @InjectView(R.id.loading_view)
    ImageView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        Views.inject(this);
        ((AnimationDrawable) loadingView.getDrawable()).start();
    }

    @Override
    protected void onResume() {
        super.onResume();

        AccountManager am = AccountManager.get(this);
        Account[] accounts = am.getAccountsByType(getString(R.string.account_type));
        if(accounts == null || accounts.length <= 0) {
            ((AnimationDrawable) loadingView.getDrawable()).stop();
            loadingView.setVisibility(View.GONE);
            // TODO Show the login form
        } else {
            // TODO Forward onto the main activity
        }
    }
}
