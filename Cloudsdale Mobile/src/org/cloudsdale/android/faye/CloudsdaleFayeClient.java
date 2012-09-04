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

import android.util.Log;
import android.widget.Toast;

import com.b3rwynmobile.fayeclient.FayeClient;
import com.google.gson.Gson;

import de.tavendo.autobahn.WebSocketConnection;
import de.tavendo.autobahn.WebSocketException;
import de.tavendo.autobahn.WebSocketHandler;
import de.tavendo.autobahn.WebSocketOptions;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.CloudsdaleFayeMessage;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;

/**
 * This class handles interactions with the Faye server, such as connecting,
 * (un-)subscribing to channels and receiving push messages
 * 
 * @author Jamison Greeley (atomicrat552@gmail.com)
 */
public class CloudsdaleFayeClient extends FayeClient {

    protected CloudsdaleFayeListener mFayeListener;

    /**
     * Simplified constructor
     * 
     * @param fayeUrl
     *            Url of the Faye server
     * @param mAuthToken
     *            Token for Faye authentication
     */
    public CloudsdaleFayeClient(String fayeUrl) {
        super(fayeUrl, "");
    }

    /**
     * Simplified constructor
     * 
     * @param fayeUrl
     *            Url of they Faye server
     * @param mAuthToken
     *            Token for Faye authentication
     * @param channel
     *            Channel to subscribe to after handshake
     */
    public CloudsdaleFayeClient(String fayeUrl, String channel) {
        super(fayeUrl, channel, "");
    }

    /**
     * Full constructor
     * 
     * @param fayeUrl
     *            Url of they Faye server
     * @param authToken
     *            Token for Faye authentication
     * @param channel
     *            Channel to subscribe to after handshake
     * @param authToken
     *            Auth token for authenticated Faye hosts
     */
    public CloudsdaleFayeClient(String fayeUrl, String channel, String authToken) {
        super(fayeUrl, channel, authToken);
    }

    protected void openSocketConnection() {
        WebSocketOptions options = new WebSocketOptions();
        options.setReceiveTextMessagesRaw(true);
        mWebSocket = new WebSocketConnection();
        try {
            mWebSocket.connect(mFayeUrl, new WebSocketHandler() {

                public void onBinaryMessage(byte[] payload) {
                    onTextMessage(new String(payload));
                }

                public void onClose(int code, String reason) {
                    processClose(code);
                }

                public void onOpen() {
                    String handshakeString = "{\"supportedConnectionTypes\":[\"websocket\"],\"minimumVersion\":\"1.0beta\",\"version\":\"1.0\",\"channel\":\""
                            + CloudsdaleFayeClient.HANDSHAKE_CHANNEL + "\"}";
                    try {
                        mWebSocket.sendBinaryMessage(handshakeString
                                .getBytes("UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                public void onRawTextMessage(byte[] payload) {
                    onTextMessage(new String(payload));
                }

                public void onTextMessage(String payload) {
                    if (Cloudsdale.DEBUG) {
                        Log.d(CloudsdaleFayeClient.TAG,
                                "Text message payload: " + payload);
                    }
                    Gson gson = new Gson();
                    CloudsdaleFayeMessage[] messages = gson.fromJson(payload,
                            CloudsdaleFayeMessage[].class);
                    for (CloudsdaleFayeMessage message : messages) {
                        String channel = message.getChannel();
                        processTextMessage(message, channel);
                    }
                }
            }, options);
            if (DEBUG) {
                Log.d(CloudsdaleFayeClient.TAG,
                        "Service is opening the web socket");
            }
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }

    private void processTextMessage(CloudsdaleFayeMessage message,
            String channel) {
        if (channel.equals(CloudsdaleFayeClient.HANDSHAKE_CHANNEL)) {
            if (message.isSuccessful()) {
                mClientId = message.getClientId();
                openFayeConnection();
            } else {
                if (DEBUG) {
                    Log.e(CloudsdaleFayeClient.TAG, "Faye failed to handshake");
                }
            }
        } else if (channel.equals(CloudsdaleFayeClient.CONNECT_CHANNEL)) {
            if (message.isSuccessful()) {
                mFayeConnected = true;
                mFayeListener.connectedToServer(this);
                scheduleHeartbeat(message.getAdvice().getInterval());
                if (DEBUG) {
                    Log.d(TAG, "Faye connected");
                }
            } else {
                mFayeConnected = false;
                if (DEBUG) {
                    Log.e(CloudsdaleFayeClient.TAG, "Faye failed to connect");
                }
            }
        } else if (channel.equals(CloudsdaleFayeClient.DISCONNECT_CHANNEL)) {
            if (message.isSuccessful()) {
                mFayeConnected = false;
                mFayeListener.disconnectedFromServer(this);
                closeSocketConnection();
            } else {
                mFayeConnected = true;
                if (DEBUG) {
                    Log.e(CloudsdaleFayeClient.TAG, "Faye failed to disconnect");
                }
            }
        } else if (channel.equals(CloudsdaleFayeClient.SUBSCRIBE_CHANNEL)) {
            if (message.isSuccessful()) {
                if (DEBUG) {
                    Log.i(CloudsdaleFayeClient.TAG,
                            "Faye subscribed to channel"
                                    + message.getSubscription());
                }
                mActiveSubchannels.add(message.getSubscription());
            } else {
                if (DEBUG) {
                    Log.e(CloudsdaleFayeClient.TAG,
                            MessageFormat
                                    .format("Faye failed to connect to channel {0} with error {1}",
                                            message.getSubscription(),
                                            message.getError()));
                }
                // TODO Handle failed subscribe
            }
        } else if (channel.equals(CloudsdaleFayeClient.UNSUBSCRIBE_CHANNEL)) {
            if (DEBUG) {
                Log.i(CloudsdaleFayeClient.TAG,
                        "Faye unsubscribed from channel "
                                + message.getSubscription());
            }
        } else if (this.mActiveSubchannels.contains(channel)) {
            mFayeListener.messageReceived(this, message);
        } else {
            if (DEBUG) {
                Log.e(CloudsdaleFayeClient.TAG,
                        "Faye recieved a message with no subscription for channel "
                                + message.getSubscription());
            }
        }
    }

    /**
     * Sets the FayeListener attached to the client
     * 
     * @param mFayeListener
     *            The FayeListener to attach to the client
     */
    public void setFayeListener(CloudsdaleFayeListener fayeListener) {
        this.mFayeListener = fayeListener;
    }
}