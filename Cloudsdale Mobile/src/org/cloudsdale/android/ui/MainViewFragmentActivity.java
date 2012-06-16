package org.cloudsdale.android.ui;

import org.cloudsdale.android.R;

import android.app.Fragment;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;

public class MainViewFragmentActivity extends SherlockFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main_view);
	}

	/**
	 * Secondary activity to show chat when the screen isn't large enough to
	 * show it with the sources list (e.g. phones)
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 */
	public static class ChatActivity extends SherlockActivity {

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

	/**
	 * Top level fragment to show the list of clouds
	 * 
	 * @author Jamison Greeley (atomicrat2552@gmail.com)
	 */
	public static class CloudsFragment extends SherlockListFragment {
		boolean mDualPane;
		int mSelectedCloud = 0;

		@Override
		public void onActivityCreated(Bundle savedInstanceState) {
			super.onActivityCreated(savedInstanceState);

			// TODO Set the list adapter

			// Check to see if we're in dual pane mode
			View chatFrame = getActivity().findViewById(R.id.chat_view);
			mDualPane = chatFrame != null
					&& chatFrame.getVisibility() == View.VISIBLE;

			// Restore saved state if it exists
			if (savedInstanceState != null) {
				mSelectedCloud = savedInstanceState.getInt("selectedCloud", 0);
			}

			// Make selection visible is dual pane
			if (mDualPane) {
				getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
				showChat(mSelectedCloud);
			}
		}

		@Override
		public void onSaveInstanceState(Bundle outState) {
			super.onSaveInstanceState(outState);

			// Save the selected cloud
			outState.putInt("selectedCloud", mSelectedCloud);
		}

		@Override
		public void onListItemClick(ListView l, View v, int position, long id) {
			showChat(position);
		}

		void showChat(int index) {
			mSelectedCloud = index;

			if (mDualPane) {
				// Set the selected cloud
				getListView().setItemChecked(index, true);

				// Check the fragment, replace as needed
				ChatFragment chatFrame = (ChatFragment) getFragmentManager()
						.findFragmentById(R.id.chat_view);
				if (chatFrame == null || chatFrame.getShownCloud() != index) {
					// Make the new fragment to show
					chatFrame = ChatFragment.newInstance(index);

					// Execute the transaction, replace whatever fragment is
					// showing
					FragmentTransaction ft = getFragmentManager()
							.beginTransaction();
					ft.replace(R.id.chat_view, chatFrame);
					ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
					ft.commit();
				} else {
					// Launch the activity
					Intent intent = new Intent();
					intent.setClass(getSherlockActivity(), ChatActivity.class);
					intent.putExtra("index", index);
				}
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
