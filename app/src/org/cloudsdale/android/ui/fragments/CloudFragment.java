package org.cloudsdale.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.googlecode.androidannotations.annotations.App;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.res.StringRes;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;

@EFragment
public class CloudFragment extends Fragment {

	public static final String	BUNDLE_CLOUD_ID	= "id";

	@App
	Cloudsdale					app;
	@ViewById(R.id.cloud_pager)
	ViewPager					viewPager;
	@StringRes(R.string.chat_fragment_header)
	String						chatTitle;
	@StringRes(R.string.drop_fragment_header)
	String						dropTitle;

	private ChatFragment		chatFragment;
	private DropFragment		dropFragment;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		chatFragment = ChatFragment.newInstance("chat_frag_1", "chat_frag_2");
		dropFragment = DropFragment.newInstance("drop_frag_1", "drop_frag_2");

		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		String cloudId = getArguments().getString(BUNDLE_CLOUD_ID);
		Cloud c = app.getCloudDataStore().get(cloudId);
		String displayString = "Got cloud: ".concat(c == null ? cloudId : c.getName());
		Crouton.showText(getActivity(), displayString, Style.INFO);
		return null;
	}

	public class CloudPagerAdapter extends FragmentStatePagerAdapter {

		public CloudPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			switch (position) {
				case 1:
					return dropFragment;
				case 0:
				default:
					return chatFragment;
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			switch (position) {
				case 0:
					return chatTitle;
				case 1:
					return dropTitle;
				default:
					return "Error";
			}
		}
	}

}
