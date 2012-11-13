package org.cloudsdale.android.ui.adapters;

import org.cloudsdale.android.Contributers;
import org.cloudsdale.android.R;

import com.WazaBe.HoloEverywhere.widget.TextView;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class ContributorGridAdapter extends BaseAdapter {

	private LayoutInflater	mInflater;

	public ContributorGridAdapter(Context context) {
		mInflater = (LayoutInflater) context
		        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return Contributers.values().length;
	}

	@Override
	public Contributers getItem(int position) {
		return Contributers.values()[position];
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getDisplayName().hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View contributorView = convertView;
		if (contributorView == null) {
			contributorView = mInflater.inflate(R.layout.about_contributor_tile, null);
		}

		ImageView avatar = (ImageView) contributorView
		        .findViewById(R.id.contributor_icon);
		TextView name = (TextView) contributorView
		        .findViewById(R.id.contributor_username_label);
		TextView title = (TextView) contributorView
		        .findViewById(R.id.contributor_title_label);

		Contributers contributor = getItem(position);

		if (contributor.getImageUrl() != null) {
			UrlImageViewHelper.setUrlDrawable(avatar,
			        contributor.getImageUrl(), R.drawable.ic_unknown_user,
			        Contributers.IMAGE_EXPIRATION_MILLIS);
		} else {
			avatar.setVisibility(View.INVISIBLE);
		}
		name.setText(contributor.getDisplayName());
		title.setText(contributor.getTitle());

		return contributorView;
	}

}
