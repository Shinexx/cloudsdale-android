package org.cloudsdale.android.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import org.cloudsdale.android.R;

public class ChatFragment extends SherlockFragment {

	private EditText	inputField;
	private Button		sendButton;
	private ListView	messagePane;

	@Override
	public void onInflate(Activity activity, AttributeSet attrs,
			Bundle savedInstanceState) {
		super.onInflate(activity, attrs, savedInstanceState);
		
		inputField = (EditText) activity.findViewById(R.id.chat_input);
		sendButton = (Button) activity.findViewById(R.id.chat_send_button);
		messagePane = (ListView) activity.findViewById(R.id.chat_message_list);
	}

}
