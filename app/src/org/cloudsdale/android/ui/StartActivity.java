package org.cloudsdale.android.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.actionbarsherlock.app.SherlockActivity;
import com.bugsense.trace.BugSenseHandler;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;

import lombok.val;

public class StartActivity extends SherlockActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getSupportActionBar().hide();
		setContentView(R.layout.activity_start);
		BugSenseHandler.initAndStartSession(this, "2bccbeb2");
	}

	@Override
	protected void onStart() {
		super.onStart();

		val handler = new AsyncHttpResponseHandler() {

			@Override
			public void onFailure(Throwable error, String json) {
				new AlertDialog.Builder(StartActivity.this)
						.setMessage(getString(R.string.config_error_message))
						.setTitle(getString(R.string.config_error_title))
						.setPositiveButton(android.R.string.ok,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										StartActivity.this.finish();
									}
								}).create();
				super.onFailure(error, json);
			}

			@Override
			public void onSuccess(String json) {
				// TODO Auto-generated method stub
				super.onSuccess(json);
			}

		};
		((Cloudsdale) getApplication()).configureFromRemote(handler);

		Account[] accounts = ((Cloudsdale) getApplication())
				.getSessionManager().getAccounts();

		Intent intent = new Intent();
		if (accounts.length > 0) {
			intent.setClass(this, CloudsdaleActivity.class);
		} else {
			intent.setClass(this, LoginActivity.class);
		}
		startActivity(intent);
	}

	@Override
	protected void onPause() {
		super.onPause();
		finish();
	}

}
