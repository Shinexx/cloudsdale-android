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
    protected CloudsdaleFayeClient mFayeClient;
    protected CloudsdaleFayeBinder mFayeBinder;

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
        mFayeClient = new CloudsdaleFayeClient(fayeUrl);
        mFayeClient.setCallbacks(this);

        // Create the binder
        mFayeBinder = new CloudsdaleFayeBinder(this, this.mFayeClient);
    }

    /**
     * Starts the Faye client
     */
    @Override
    public void startFaye() {
        mFayeClient.connect();
    }

    /**
     * Stops the Faye client
     */
    @Override
    public void stopFaye() {
        mFayeClient.disconnect();
    }

    @Override
    public void connected() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification note = makeNotification("Cloudsdale is connected",
                "Cloudsdale is now connected to the chat server");
        manager.notify(1337, note);
    }

    @Override
    public void disconnected() {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification note = makeNotification("Cloudsdale has disconnected",
                "Cloudsdale has disconnected from the chat server");
        manager.notify(1337, note);
    }

    @Override
    public void connecting() {
        Notification note = makeNotification("Cloudsdale is connecting",
                "Cloudsale is currently connecting to the chat server");
        startForeground(1337, note);
    }

    private Notification makeNotification(String title, String content) {
        Bitmap icon = BitmapFactory.decodeResource(getApplicationContext()
                .getResources(), R.drawable.color_icon);
        Notification note = new NotificationCompat.Builder(
                getApplicationContext()).setContentTitle(title)
                .setContentText(content).setSmallIcon(R.drawable.color_icon)
                .setLargeIcon(icon).getNotification();
        Intent i = new Intent(this, HomeActivity.class);
        PendingIntent pi = PendingIntent.getActivity(this, 0, i, 0);
        note.flags |= Notification.FLAG_NO_CLEAR;
        return note;
    }
}
