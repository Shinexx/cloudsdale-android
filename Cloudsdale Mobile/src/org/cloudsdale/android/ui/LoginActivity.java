package org.cloudsdale.android.ui;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.bugsense.trace.BugSenseHandler;
import com.facebook.android.AsyncFacebookRunner;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.google.gson.Gson;
import com.zubhium.ZubhiumSDK;

import oauth.signpost.OAuth;
import oauth.signpost.OAuthProviderListener;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthProvider;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import oauth.signpost.exception.OAuthNotAuthorizedException;
import oauth.signpost.http.HttpRequest;
import oauth.signpost.http.HttpResponse;

import org.cloudsdale.android.PersistentData;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.authentication.CloudsdaleAsyncAuth;
import org.cloudsdale.android.models.authentication.LoginBundle;
import org.cloudsdale.android.models.authentication.OAuthBundle;
import org.cloudsdale.android.models.authentication.Provider;
import org.cloudsdale.android.models.network_models.FacebookResponse;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Controller for the login view
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com) Copyright (c) 2012
 *         Cloudsdale.org
 */
public class LoginActivity extends SherlockActivity {
    /**
     * Extension of CloudsdaleAsyncAuth to allow UI functions on execution
     * 
     * @author Jamison Greeley (atomicrat2552@gmail.com) code Copyright (c) 2012
     *         Cloudsdale.org
     */
    private class CdAuth extends CloudsdaleAsyncAuth {

        @Override
        protected void onCancelled(LoggedUser result) {
            // Close the dialog and inform the user of the error
            super.onCancelled(result);
            cancelDialog();
            Toast.makeText(LoginActivity.this,
                    "There was an error logging in, please try again",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        protected void onPostExecute(LoggedUser result) {
            // Close the dialog
            cancelDialog();

            // If a user was returned, store it and move on to the next activity
            // Else, inform the user that there was an error
            if (result != null) {
                // Store the user
                PersistentData.storeLoggedUser(result);

                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                finish();
            } else {
                if (++sFaileureCount < 5) {
                    Toast.makeText(LoginActivity.this,
                            "There was an error, please try again",
                            Toast.LENGTH_LONG).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(
                            LoginActivity.this);
                    builder.setMessage(
                            "You seem to be having issues logging in. Would you like to try loading Cloudsdale in your web browser instead?")
                            .setCancelable(true)
                            .setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            startActivity(new Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse("http://www.cloudsdale.org")));
                                            LoginActivity.this.finish();
                                        }
                                    })
                            .setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {
                                            // Do nothing, dialog dismisses
                                        }
                                    }).show();
                }
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        super.onConfigurationChanged(newConfig);
    }

    /**
     * Listener for Async queries
     * 
     * @author Jamison Greeley (atomicrat2552@gmail.com) code Copyright (c) 2012
     *         Cloudsdale.org
     */
    private class FbAsyncListener implements
            AsyncFacebookRunner.RequestListener {

        @Override
        public void onComplete(String response, Object state) {
            Looper.prepare();

            // Deserialize the response
            Gson gson = new Gson();
            FacebookResponse fbr = gson.fromJson(response,
                    FacebookResponse.class);

            // Set the fields
            LoginActivity.this.mFbId = fbr.getId();
            LoginActivity.this.mFbRunnerWorking = false;
        }

        @Override
        public void onFacebookError(FacebookError e, Object state) {

        }

        @Override
        public void onFileNotFoundException(FileNotFoundException e,
                Object state) {

        }

        @Override
        public void onIOException(IOException e, Object state) {

        }

        @Override
        public void onMalformedURLException(MalformedURLException e,
                Object state) {

        }

    }

    /**
     * Asychronus Facebook query runner
     * 
     * @author Jamison Greeley (atomicrat2552@gmail.com) code Copyright (c) 2012
     *         Cloudsdale.org
     */
    private class FbAsyncRunner extends AsyncFacebookRunner {

        public FbAsyncRunner(Facebook fb) {
            super(fb);
        }

    }

    /**
     * DialogListener listener for Facebook authentication
     * 
     * @author Jamison Greeley (atomicrat2552@gmail.com) code Copyright (c) 2012
     *         Cloudsdale.org
     */
    private class FbListener implements Facebook.DialogListener {

        @Override
        public void onCancel() {
            cancelDialog();
        }

        @Override
        public void onComplete(Bundle values) {
            SharedPreferences.Editor editor = LoginActivity.this.mPrefs.edit();
            editor.putString("access_token",
                    LoginActivity.this.mFacebook.getAccessToken());
            editor.putLong("access_expires",
                    LoginActivity.this.mFacebook.getAccessExpires());
            editor.commit();
            LoginActivity.this.mFbRunnerWorking = false;
        }

        @Override
        public void onError(DialogError e) {
            cancelDialog();
            Toast.makeText(LoginActivity.this,
                    "Facebook encountered an error,  please try again",
                    Toast.LENGTH_LONG).show();
        }

        @Override
        public void onFacebookError(FacebookError e) {
            BugSenseHandler.log(LoginActivity.TAG, e);
            cancelDialog();
            Toast.makeText(
                    LoginActivity.this,
                    "There was an error communicating with Facebook, please try again",
                    Toast.LENGTH_LONG).show();
        }

    }

    public static final String              TAG                    = "Cloudsdale LoginViewActivity";
    public static final int                 FACEBOOK_ACTIVITY_CODE = 10298;
    @SuppressWarnings("unused")
    private static final String             FILENAME               = "AndroidSSO_data";
    private static int                      sFaileureCount         = 0;
    private static boolean                  sProgressShowing;
    
    // Internal fields
    private EditText                        mEmailField;
    private EditText                        mPasswordField;
    private Button                          mCdButton;
    private Button                          mFbButton;
    private Button                          mTwitterButton;
    private ProgressDialog                  mProgress;

    // Fields required by the Facebook SDK
    private Facebook                        mFacebook;
    private SharedPreferences               mPrefs;

    // Fields for Facebook login
    private boolean                         mFbRunnerWorking;
    private String                          mFbId;

    // Fields for Twitter login
    private static String                   sTwitterConsumerKey;
    private static String                   sTwitterConsumerSecret;
    private static CommonsHttpOAuthProvider sTwitterProvider;
    private static CommonsHttpOAuthConsumer sTwitterConsumer;
    private static Twitter                  sTwitter;

    /**
     * Hides the login dialog if it's displayed
     */
    public void cancelDialog() {
        if (this.mProgress != null && this.mProgress.isShowing()) {
            this.mProgress.dismiss();
        }
    }

    /**
     * Gets the user's id from Facebook
     */
    private void getFbUserId() {
        // Get the user's uid from FB
        this.mFbRunnerWorking = true;
        FbAsyncRunner runner = new FbAsyncRunner(this.mFacebook);
        runner.request("me", new Bundle(), new FbAsyncListener());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Callback to Facebook
        this.mFacebook.authorizeCallback(requestCode, resultCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // SET THEMES HERE

        // Setup BugSense
        BugSenseHandler.setup(this, "2bccbeb2");
        // Setup Zubhium
        ZubhiumSDK sdk = ZubhiumSDK.getZubhiumSDKInstance(
                getApplicationContext(), "65de0ea209fa414beee8518bd08b05");
        sdk.enableCrashReporting(true);

        // Hide the Cloudsdale text and icon in the ActionBar
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);

        super.onCreate(savedInstanceState);

        // Set the layout
        setContentView(R.layout.activity_login);

        // Instantiate the Facebook client
        this.mFacebook = new Facebook(getString(R.string.facebook_api_token));

        // Grab the Twitter fields
        LoginActivity.sTwitterConsumerKey = getString(R.string.twitter_consumer_key);
        LoginActivity.sTwitterConsumerSecret = getString(R.string.twitter_consumer_secret);

        // Bind the data entities
        this.mEmailField = (EditText) findViewById(R.id.login_email_field);
        this.mPasswordField = (EditText) findViewById(R.id.login_password_field);
        this.mCdButton = (Button) findViewById(R.id.login_view_cd_button);
        this.mFbButton = (Button) findViewById(R.id.login_view_fb_button);
        this.mTwitterButton = (Button) findViewById(R.id.login_view_twitter_button);

        // Set the CD button listener
        this.mCdButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendCdCredentials();
            }
        });

        // Set the FB button listener
        this.mFbButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startFbAuthFlow();
            }
        });
        mFbButton.setEnabled(false);

        // Set the Twitter button listener
        this.mTwitterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTwitterAuthFlow();
            }
        });
        mTwitterButton.setEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Create the register menu option
        menu.add("Register")
                .setIcon(R.drawable.ic_register_user)
                .setShowAsAction(
                        MenuItem.SHOW_AS_ACTION_IF_ROOM
                                | MenuItem.SHOW_AS_ACTION_WITH_TEXT);

        return super.onCreateOptionsMenu(menu);
    }
    
    @Override
    protected void onStart() {
        if(sProgressShowing) {
            showDialog();
        }
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        cancelDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Uri uri = getIntent().getData();
        if (uri != null
                && uri.toString().startsWith(
                        getString(R.string.twitter_callback_url))) {

            // Reshow the login dialog
            showDialog();

            // Get Twitter authorized
            final String verifier = uri.getQueryParameter(OAuth.OAUTH_VERIFIER);
            new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        Looper.prepare();

                        // Setup Twitter
                        LoginActivity.sTwitterProvider.retrieveAccessToken(
                                LoginActivity.sTwitterConsumer, verifier);
                        AccessToken accessToken = new AccessToken(
                                LoginActivity.sTwitterConsumer.getToken(),
                                LoginActivity.sTwitterConsumer.getTokenSecret());
                        LoginActivity.sTwitter = new TwitterFactory()
                                .getInstance();
                        LoginActivity.sTwitter.setOAuthConsumer(
                                LoginActivity.sTwitterConsumer.getConsumerKey(),
                                LoginActivity.sTwitterConsumer
                                        .getConsumerSecret());
                        LoginActivity.sTwitter.setOAuthAccessToken(accessToken);
                        String twId = String.valueOf(LoginActivity.sTwitter
                                .verifyCredentials().getId());
                        Log.d(LoginActivity.TAG, twId);

                        // Get the Twitter ID and send off to Cloudsdale
                        sendTwitterCredentials(twId);
                    } catch (OAuthMessageSignerException e) {
                        cancelDialog();
                        e.printStackTrace();
                    } catch (OAuthNotAuthorizedException e) {
                        cancelDialog();
                        e.printStackTrace();
                    } catch (OAuthExpectationFailedException e) {
                        cancelDialog();
                        e.printStackTrace();
                    } catch (OAuthCommunicationException e) {
                        cancelDialog();
                        e.printStackTrace();
                    } catch (TwitterException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    /**
     * Sends Cloudsdale credentials to Cloudsdale
     */
    private void sendCdCredentials() {
        // Get inputted email and password
        String email = this.mEmailField.getText().toString();
        String pass = this.mPasswordField.getText().toString();

        // Check that neither is null
        if (email != null && !email.equals("") && pass != null
                && !pass.equals("")) {
            showDialog();

            // Get the api resources
            String apiUrl = getString(R.string.cloudsdale_api_base)
                    + getString(R.string.cloudsdale_sessions_endpoint);

            // Build the login bundle and execute auth
            LoginBundle login = new LoginBundle(email, pass, apiUrl, null, null);
            CdAuth auth = new CdAuth();
            auth.execute(login);
        } else {
            Toast.makeText(
                    this,
                    "You must fill out both the email and password fields to log in",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Sends Facebook credentials to Cloudsdale
     */
    private void sendFbCredentials() {
        // Create the oAuth bundle
        OAuthBundle oAuth = new OAuthBundle(Provider.FACEBOOK, this.mFbId,
                getString(R.string.cloudsdale_auth_token));

        // Build the login bundle with the oAuth bundle
        LoginBundle bundle = new LoginBundle(null, null,
                getString(R.string.cloudsdale_api_base)
                        + getString(R.string.cloudsdale_sessions_endpoint),
                getString(R.string.cloudsdale_auth_token), oAuth);

        // Send the credentials to Cloudsdale
        CdAuth auth = new CdAuth();
        auth.execute(bundle);
    }

    /**
     * Sends Twitter credentials to Cloudsdale
     * 
     * @param id
     *            The user's Twitter ID
     */
    private void sendTwitterCredentials(String id) {
        // Create the oAuth Bundle
        OAuthBundle oAuth = new OAuthBundle(Provider.TWITTER, id,
                getString(R.string.cloudsdale_auth_token));

        // Build the login bundle
        LoginBundle bundle = new LoginBundle(null, null,
                getString(R.string.cloudsdale_api_base)
                        + getString(R.string.cloudsdale_sessions_endpoint),
                getString(R.string.cloudsdale_auth_token), oAuth);

        // Send credentials to Cloudsdale
        CdAuth auth = new CdAuth();
        auth.execute(bundle);
    }

    /**
     * Sets up the Twitter authentication objects if necessary
     */
    private void SetupTwitterObjects() {
        // OAuth objects
        LoginActivity.sTwitterConsumer = new CommonsHttpOAuthConsumer(
                LoginActivity.sTwitterConsumerKey,
                LoginActivity.sTwitterConsumerSecret);
        LoginActivity.sTwitterProvider = new CommonsHttpOAuthProvider(
                getString(R.string.twitter_request_url),
                getString(R.string.twitter_access_token_url),
                getString(R.string.twitter_auth_url));
        LoginActivity.sTwitterProvider.setListener(new OAuthProviderListener() {

            @Override
            public boolean onResponseReceived(HttpRequest arg0,
                    HttpResponse arg1) throws Exception {
                // TODO Auto-generated method stub
                return false;
            }

            @Override
            public void prepareRequest(HttpRequest arg0) throws Exception {
                // TODO Auto-generated method stub

            }

            @Override
            public void prepareSubmission(HttpRequest arg0) throws Exception {
                // TODO Auto-generated method stub

            }
        });
    }

    /**
     * Shows the login dialog
     */
    public void showDialog() {
        this.mProgress = ProgressDialog.show(this, "",
                getString(R.string.login_dialog_wait_string));
        sProgressShowing = true;
    }

    /**
     * Starts the authentication flow using Facebook credentials
     */
    private void startFbAuthFlow() {
        // Show the dialog
        showDialog();

        // Get existing token if it exists
        this.mPrefs = getPreferences(Context.MODE_PRIVATE);
        String access_token = this.mPrefs.getString("access_token", null);
        long expires = this.mPrefs.getLong("access_expires", 0);

        // Set items
        if (access_token != null) {
            this.mFacebook.setAccessToken(access_token);
        }
        if (expires != 0) {
            this.mFacebook.setAccessExpires(expires);
        }

        // Only call auth if access has expired
        if (!this.mFacebook.isSessionValid()) {
            this.mFacebook.authorize(LoginActivity.this, new String[] {},
                    new FbListener());
            this.mFbRunnerWorking = true;
        }

        Log.d(TAG, "Facebook is " + (mFacebook == null ? "null" : "not null"));

        while (this.mFbRunnerWorking) {
            continue;
        }

        // Start CD Auth flow
        getFbUserId();

        // Chill while FB does its thing
        while (this.mFbRunnerWorking) {
            continue;
        }

        // Send the credentials to Cloudsdale
        sendFbCredentials();

    }

    /**
     * Starts the authentication flow using Twitter credentials
     */
    private void startTwitterAuthFlow() {
        // Show the dialog
        showDialog();

        SetupTwitterObjects();

        // Get the auth url
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    String authUrl = LoginActivity.sTwitterProvider
                            .retrieveRequestToken(
                                    LoginActivity.sTwitterConsumer,
                                    getString(R.string.twitter_callback_url));

                    // Call out to the browser for authorization
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri
                            .parse(authUrl)));
                } catch (OAuthMessageSignerException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (OAuthNotAuthorizedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (OAuthExpectationFailedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (OAuthCommunicationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            };

        }).start();

    }
}