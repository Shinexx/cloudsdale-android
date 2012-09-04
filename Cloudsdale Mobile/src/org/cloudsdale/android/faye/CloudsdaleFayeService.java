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

import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.b3rwynmobile.fayeclient.FayeService;

import org.cloudsdale.android.Cloudsdale;

/**
 * Service class to run Faye. Provides a singleton method to get the running
 * instance.
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class CloudsdaleFayeService extends FayeService {

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

        // Create the binder
        mFayeBinder = new CloudsdaleFayeBinder(this, this.mFayeClient);
    }

    /**
     * Starts the Faye client
     */
    @Override
    public void startFaye() {
        if (Cloudsdale.DEBUG) {
            Toast.makeText(getApplicationContext(), "Faye Started",
                    Toast.LENGTH_SHORT).show();
        }
        mFayeClient.connect();
    }

    /**
     * Stops the Faye client
     */
    @Override
    public void stopFaye() {
        mFayeClient.disconnect();
    }
}
