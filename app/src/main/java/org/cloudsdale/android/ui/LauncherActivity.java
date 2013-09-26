package org.cloudsdale.android.ui;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Views;

import org.cloudsdale.android.R;

public class LauncherActivity extends Activity {

    private enum State {
        LOGIN, REGISTER
    }

    private State state;

    @InjectView(R.id.loading_view)
    ImageView     loadingView;
    @InjectView(R.id.login_form)
    View          loginForm;
    @InjectView(R.id.login_submit)
    Button submitButton;
    @InjectView(R.id.login_register)
    Button        registerButton;

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
        Account[] accounts = am
                .getAccountsByType(getString(R.string.account_type));
        if (accounts == null || accounts.length <= 0) {
            ((AnimationDrawable) loadingView.getDrawable()).stop();
            loadingView.setVisibility(View.GONE);
            loginForm.setVisibility(View.VISIBLE);
        } else {
            // TODO Forward onto the main activity
        }
    }

    @Override
    public void onBackPressed() {
        if (state != State.REGISTER) {
            super.onBackPressed();
        } else {
            hideRegistrationFields();
        }
    }

    @OnClick(R.id.login_submit)
    void submitClicked() {
        if (state == State.LOGIN) {
            // TODO Login
        } else {
            // TODO Register
        }
    }

    @OnClick(R.id.login_register)
    void registerClicked() {
        displayRegistrationFields();
    }

    private void displayRegistrationFields() {
        state = State.REGISTER;
        registerButton.setVisibility(View.GONE);
        submitButton.setText("Register");
    }

    private void hideRegistrationFields() {
        state = State.LOGIN;
        registerButton.setVisibility(View.VISIBLE);
        submitButton.setText("Login");
    }
}
