package org.cloudsdale.android.ui.fragments;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.managers.FayeManager;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api.Message;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.ChatMessageGetQuery;
import org.cloudsdale.android.models.queries.MessagePostQuery;
import org.cloudsdale.android.ui.CloudActivity;
import org.cloudsdale.android.ui.adapters.CloudMessageAdapter;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChatFragment extends SherlockFragment {

	private static final String	TAG	= "Chat Fragment";

	private View				mChatView;
	private ListView			mMessageList;
	private CloudMessageAdapter	mMessageAdapter;
	private String				mCloudUrl;
	private EditText			mInputField;
	private ImageButton			mSendButton;
	private TextView			mFayeConnectedStatus;
	private View				mLoadingView;
	private String				mCloudId;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle args = getArguments();
		mCloudId = args.getString(CloudActivity.CLOUD_ID);
		mChatView = inflater.inflate(R.layout.fragment_chat, null);
		return mChatView;
	}

	@Override
	public void onResume() {
		attachViews();
		super.onResume();
	}

	@SuppressLint("NewApi")
	private void attachViews() {
		mMessageList = (ListView) mChatView
				.findViewById(R.id.chat_message_list);
		mLoadingView = mChatView.findViewById(R.id.chat_loading_view);
		mMessageAdapter = new CloudMessageAdapter(getActivity(),
				new ArrayList<Message>());
		mMessageList.setAdapter(mMessageAdapter);

		mInputField = (EditText) mChatView.findViewById(R.id.chat_input_field);
		mSendButton = (ImageButton) mChatView
				.findViewById(R.id.chat_send_button);

		mFayeConnectedStatus = (TextView) mChatView
				.findViewById(R.id.home_connection_status);
		if (FayeManager.isFayeConnected()) {
			mFayeConnectedStatus.setVisibility(View.GONE);
		} else {
			new ConnectionMonitorTask().execute();
		}

		mSendButton.setClickable(true);
		mSendButton.setOnClickListener(new View.OnClickListener() {

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

	@SuppressLint("NewApi")
	private void detachViews() {
		mMessageList.setAdapter(null);
	}

	public void populateChat() {
		mCloudUrl = getString(R.string.cloudsdale_api_base)
				+ getString(R.string.cloudsdale_cloud_chat_messages_endpoint,
						mCloudId);
		MessageAsyncQuery query = new MessageAsyncQuery();
		query.execute();
	}

	public void addMessage(Message message) {
		try {
			if (!message.getAuthorId().equals(
					Cloudsdale.getUserManager().getLoggedInUser().getId())
					|| !message.getDevice().equals("mobile")) {
				mMessageAdapter.addMessage(message);
			}
		} catch (QueryException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void sendChatMessage() {
		String content = mInputField.getText().toString();
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
			String url = mCloudUrl;
			if (Cloudsdale.isDebuggable()) {
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
			if (Cloudsdale.isDebuggable()) {
				Log.d(TAG, "There are " + result.length + " messages");
			}
			if (result != null && result.length > 0) {
				for (Message m : result) {
					if (m == null) continue;
					((CloudMessageAdapter) mMessageList.getAdapter())
							.addMessage(m);
				}
			}
			mLoadingView.setVisibility(View.GONE);
			mMessageList.setVisibility(View.VISIBLE);
		}
	}

	class MessageAsyncSend extends AsyncTask<String, Void, Message> {

		@Override
		protected Message doInBackground(String... params) {
			try {
				JSONObject body = new JSONObject();
				JSONObject message = new JSONObject();
				body.put("device", "mobile");
				body.put("content", mInputField.getText().toString());
				body.put("client_id", Cloudsdale.getUserManager()
						.getLoggedInUser().getId());
				message.put("message", body);

				if (Cloudsdale.isDebuggable()) {
					Log.d("Chat Send Message", "Attempting to send message");
					Log.d("Chat Send Message", message.toString());
				}

				QueryData qd = new QueryData();
				qd.setJson(message.toString());
				MessagePostQuery q = new MessagePostQuery(mCloudUrl);
				q.addHeader("X-AUTH-TOKEN", Cloudsdale.getUserManager()
						.getLoggedInUser().getAuthToken());
				return q.execute(qd, getActivity());
			} catch (QueryException e) {
				// TODO error handling
				return null;
			} catch (JSONException e) {
				// TODO error handling
				e.printStackTrace();
			}

			return null;
		}

		@Override
		protected void onPostExecute(Message result) {
			if (Cloudsdale.isDebuggable()) {
				Log.d("Chat Send Message", "Results received");
			}
			mInputField.setText("");
			if (result != null) {
				((CloudMessageAdapter) mMessageList.getAdapter())
						.addMessage(result);
			}
			super.onPostExecute(result);
		}
	}

	class ConnectionMonitorTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... arg0) {
			while (!FayeManager.isFayeConnected()) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			mFayeConnectedStatus.setVisibility(View.GONE);
			super.onPostExecute(result);
		}

	}
}
