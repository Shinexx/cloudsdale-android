package org.cloudsdale.android.ui.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.managers.UserManager;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api.Message;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.ChatMessageGetQuery;
import org.cloudsdale.android.models.queries.MessagePostQuery;
import org.cloudsdale.android.ui.adapters.CloudMessageAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatFragment extends SherlockFragment {

    private static final String        TAG = "Chat Fragment";

    private static View                sChatView;
    private static ListView            sMessageList;
    private static CloudMessageAdapter sMessageAdapter;
    private static String              sCloudUrl;
    private static EditText            sInputField;
    private static ImageButton         sSendButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        sChatView = inflater.inflate(R.layout.chat_view, null);
        return sChatView;
    }

    @Override
    public void onStart() {
        attachViews();
        super.onStart();
    }

    @Override
    public void onResume() {
        attachViews();
        super.onResume();
    }

    private void attachViews() {
        sMessageList = (ListView) sChatView
                .findViewById(R.id.chat_message_list);
        sMessageAdapter = new CloudMessageAdapter(getActivity(),
                new ArrayList<Message>());
        sMessageList.setAdapter(sMessageAdapter);

        sInputField = (EditText) sChatView.findViewById(R.id.chat_input_field);
        sSendButton = (ImageButton) sChatView
                .findViewById(R.id.chat_send_button);

        sSendButton.setClickable(true);
        sSendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                sendChatMessage();
            }
        });

        populateChat();
    }

    @Override
    public void onPause() {
        detachViews();
        super.onPause();
    }

    // @Override
    // public void onStop() {
    // detachViews();
    // super.onStop();
    // }

    private void detachViews() {
        sMessageList.setAdapter(null);
        // sMessageList = null;
        // sMessageAdapter.clearMessages();
        // sMessageAdapter = null;
        // sInputField = null;
        // sSendButton = null;
    }

    public void populateChat() {
        sCloudUrl = getString(R.string.cloudsdale_api_base)
                + getString(R.string.cloudsdale_cloud_chat_messages_endpoint,
                        Cloudsdale.getShowingCloud());
        MessageAsyncQuery query = new MessageAsyncQuery();
        query.execute();
    }

    public void addMessage(Message message) {
        if (!message.getAuthorId().equals(UserManager.getLoggedInUser().getId())
                || !message.getDevice().equals("mobile")) {
            sMessageAdapter.addMessage(message);
        }
    }

    private void sendChatMessage() {
        String content = sInputField.getText().toString();
        if (content.length() > 0) {
            new MessageAsyncSend().execute(content);
        } else {
            return;
        }
    }

    class MessageAsyncQuery extends AsyncTask<Void, Void, Message[]> {

        @Override
        protected Message[] doInBackground(Void... params) {
            QueryData data = new QueryData();
            String url = sCloudUrl;
            if (Cloudsdale.DEBUG) {
                Log.d("Cloudsdale Chat Fragment", "Message url: " + url);
            }
            data.setUrl(url);

            ChatMessageGetQuery query = new ChatMessageGetQuery(url);
            try {
                return query.executeForCollection(data, getActivity());
            } catch (QueryException e) {
                // TODO error handling
                return null;
            }
        }

        @Override
        protected void onPostExecute(Message[] result) {
            super.onPostExecute(result);
            if (Cloudsdale.DEBUG) {
                Log.d(TAG, "There are " + result.length + " messages");
            }
            if (result != null) {
                for (Message m : result) {
                    // Message previous = getPreviousMessage();
                    // if (previous != null
                    // && m.getAuthor().getName()
                    // .equals(previous.getAuthor().getName())) {
                    // LinearLayout layout = (LinearLayout) mMessageList
                    // .getChildAt(mMessageAdapter.getCount() - 1)
                    // .findViewById(R.id.message_root);
                    // TextView tv = new TextView(getActivity());
                    // tv.setText(m.getContent());
                    // layout.addView(tv);
                    // } else {
                    ((CloudMessageAdapter) sMessageList.getAdapter())
                            .addMessage(m);
                    // }
                }
            }
        }

        private Message getPreviousMessage() {
            if (sMessageAdapter.getCount() > 0) {
                return (Message) sMessageAdapter.getItem(sMessageAdapter
                        .getCount() - 1);
            } else {
                return null;
            }
        }
    }

    class MessageAsyncSend extends AsyncTask<String, Void, Message> {

        @Override
        protected Message doInBackground(String... params) {
            JSONObject body = new JSONObject();
            JSONObject message = new JSONObject();
            try {
                body.put("device", "mobile");
                body.put("content", sInputField.getText().toString());
                body.put("client_id", UserManager.getLoggedInUser().getId());
                message.put("message", body);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (Cloudsdale.DEBUG) {
                Log.d("Chat Send Message", "Attempting to send message");
                Log.d("Chat Send Message", message.toString());
            }

            QueryData qd = new QueryData();
            qd.setJson(message.toString());
            MessagePostQuery q = new MessagePostQuery(sCloudUrl);
            q.addHeader("X-AUTH-TOKEN", UserManager.getLoggedInUser().getAuthToken());
            try {
                return q.execute(qd, getActivity());
            } catch (QueryException e) {
                // TODO error handling
                return null;
            }
        }

        @Override
        protected void onPostExecute(Message result) {
            if (Cloudsdale.DEBUG) {
                Log.d("Chat Send Message", "Results received");
            }
            sInputField.setText("");
            if (result != null) {
                ((CloudMessageAdapter) sMessageList.getAdapter())
                        .addMessage(result);
            }
            super.onPostExecute(result);
        }
    }
}
