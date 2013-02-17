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

import android.os.Binder;

/**
 * Binder class to interact with the service
 * 
 * @author Jamison Greeley (atomicrat2552@gmail.com)
 */
public class CloudsdaleFayeBinder extends Binder {

    private CloudsdaleFayeService mFayeService;
    private CloudsdaleFayeClient  mFayeClient;

    public CloudsdaleFayeBinder() {
        this.mFayeService = null;
        this.mFayeClient = null;
    }

    public CloudsdaleFayeBinder(CloudsdaleFayeService service, CloudsdaleFayeClient client) {
        this.mFayeService = service;
        this.mFayeClient = client;
    }

    public CloudsdaleFayeClient getFayeClient() {
        return this.mFayeClient;
    }

    public CloudsdaleFayeService getFayeService() {
        return this.mFayeService;
    }

    public void setFayeClient(CloudsdaleFayeClient faye) {
        this.mFayeClient = faye;
    }

    public void setFayeService(CloudsdaleFayeService service) {
        this.mFayeService = service;
    }
}
