package org.cloudsdale.android.ui.fragments;

import org.cloudsdale.android.R;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

public class CloudListActivity extends SherlockFragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.cloud_list_fragment_view);
	}

	/**
	 * Secondary activity to show chat when the screen isn't large enough to
	 * show it with the sources list (e.g. phones)
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 */
	public static class ChatActivity extends SherlockFragmentActivity {

		@Override
		protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
				finish();
				return;
			}

			if (savedInstanceState == null) {
				ChatFragment chat = new ChatFragment();
				chat.setArguments(getIntent().getExtras());
				getSupportFragmentManager().beginTransaction()
						.add(android.R.id.content, chat).commit();
			}
		}

	}
	
	public static class ChatFragment extends SherlockFragment {

		/**
		 * Create a new instance of the fragment
		 * 
		 * @param index
		 *            Index of the selected cloud
		 * @return
		 */
		public static ChatFragment newInstance(int index) {
			ChatFragment cf = new ChatFragment();

			// Supply the index to the fragment
			Bundle args = new Bundle();
			args.putInt("index", index);
			cf.setArguments(args);

			return cf;
		}

		public int getShownCloud() {
			return getArguments().getInt("index", 0);
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			if (container == null) {
				return null;
			}

			// TODO implement layout logic here
			return null;
		}
	}
}
