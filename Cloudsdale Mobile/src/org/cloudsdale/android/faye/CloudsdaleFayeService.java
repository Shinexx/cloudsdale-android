// @formatter:off
/******************************************************************************
 *
 *  Copyright 2011-2012 b3rwyn Mobile Solutions
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 ******************************************************************************/
// @formatter:on

package org.cloudsdale.android.faye;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.app.NotificationCompat.Builder;
import android.widget.Toast;

import com.b3rwynmobile.fayeclient.FayeService;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.ui.HomeActivity;

/**
 * Service class to run Faye. Provides a singleton method to get the running
 * instance.
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class CloudsdaleFayeService extends FayeService implements IFayeCallback {

	// Data objects
	protected CloudsdaleFayeClient			mFaye;
	protected CloudsdaleFayeBinder			mFayeBinder;
	protected PendingIntent					mPendingIntent;
	protected NotificationManager			mNotificationManager;
	protected NotificationCompat.Builder	mNotificationBuilder;

	/**
	 * Default constructor
	 */
	public CloudsdaleFayeService() {
		super();
	}

	/**
	 * Returns the Binder to interact with Faye. This is the prefered method to
	 * run the service, and starting from an Intent is not currently supported
	 */
	@Override
	public IBinder onBind(Intent intent) {
		setup();
		return this.mFayeBinder;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		setup();
	}

	/**
	 * Stops Faye when the Service is being destroyed by the OS
	 */
	@Override
	public void onDestroy() {
		stopFaye();
		super.onDestroy();
	}

	@Override
	protected void setup() {
		// Debug toast
		if (Cloudsdale.DEBUG) {
			Toast.makeText(getApplicationContext(), "Faye Service created",
					Toast.LENGTH_SHORT).show();
		}
		String fayeUrl = FayeService.FAYE_HOST + ":" + FayeService.FAYE_PORT
				+ FayeService.INITIAL_CHANNEL;

		// Create the client
		mFaye = new CloudsdaleFayeClient(fayeUrl);
		mFaye.setCallbacks(this);

		// Create the binder
		mFayeBinder = new CloudsdaleFayeBinder(this, this.mFaye);
	}

	/**
	 * Starts the Faye client
	 */
	@Override
	public void startFaye() {
		mFaye.connect();
	}

	/**
	 * Stops the Faye client
	 */
	@Override
	public void stopFaye() {
		mFaye.disconnect();
	}

	@Override
	public void connected() {
		mNotificationBuilder = new Builder(this).setContentTitle(
				"Cloudsdale is connected").setSmallIcon(R.drawable.color_icon);
		Intent resultIntent = new Intent(this, HomeActivity.class);
		mPendingIntent = PendingIntent.getActivity(this, 1337, resultIntent, 0);
		mNotificationBuilder.setContentIntent(mPendingIntent);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1337,
				mNotificationBuilder.getNotification());
	}

	@Override
	public void disconnected() {
		mNotificationBuilder = new Builder(this).setContentTitle(
				"Cloudsdale is disconnected").setSmallIcon(
				R.drawable.color_icon);
		Intent resultIntent = new Intent(this, HomeActivity.class);
		mPendingIntent = PendingIntent.getActivity(this, 1337, resultIntent, 0);
		mNotificationBuilder.setContentIntent(mPendingIntent);
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(1337,
				mNotificationBuilder.getNotification());
	}

	@Override
	public void connecting() {
		mNotificationBuilder = new Builder(this).setContentTitle(
				"Cloudsdale is connecting").setSmallIcon(R.drawable.color_icon);
		Intent resultIntent = new Intent(this, HomeActivity.class);
		mPendingIntent = PendingIntent.getActivity(this, 1337, resultIntent, 0);
		mNotificationBuilder.setContentIntent(mPendingIntent);

		startForeground(1337, mNotificationBuilder.getNotification());
	}
}
