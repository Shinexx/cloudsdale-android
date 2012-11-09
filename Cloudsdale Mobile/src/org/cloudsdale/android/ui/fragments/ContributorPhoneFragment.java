package org.cloudsdale.android.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.WazaBe.HoloEverywhere.widget.LinearLayout;
import com.WazaBe.HoloEverywhere.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.cloudsdale.android.Contributers;
import org.cloudsdale.android.R;

public class ContributorPhoneFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contributor_phone_layout,
				null);
		LinearLayout list = (LinearLayout) rootView
				.findViewById(android.R.id.content);

		for (Contributers c : Contributers.values()) {
			View contributorView = inflater.inflate(
					R.layout.about_contributor_tile, null);
			ImageView avatar = (ImageView) contributorView
					.findViewById(R.id.contributor_icon);
			TextView name = (TextView) contributorView
					.findViewById(R.id.contributor_username_label);
			TextView title = (TextView) contributorView
					.findViewById(R.id.contributor_title_label);

			UrlImageViewHelper.setUrlDrawable(avatar, c.getImageUrl(),
					R.drawable.ic_unknown_user,
					Contributers.IMAGE_EXPIRATION_MILLIS);
			name.setText(c.getDisplayName());
			title.setText(c.getTitle());
			list.addView(contributorView);
		}

		return rootView;
	}

}
