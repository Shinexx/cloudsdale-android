package org.cloudsdale.android.ui;

import java.util.ArrayList;

import org.cloudsdale.android.R;
import org.cloudsdale.android.models.Cloud;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CloudListAdapter extends BaseAdapter {

	private Activity				root;
	private ArrayList<Cloud>		clouds;
	private static LayoutInflater	inflate;

	public CloudListAdapter(Activity activity, ArrayList<Cloud> clouds) {
		this.root = activity;
		this.clouds = clouds;
		inflate = (LayoutInflater) root
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		return clouds.size();
	}

	@Override
	public Object getItem(int position) {
		return clouds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the cloud
		Cloud cloud = (Cloud) getItem(position);

		// Inflate the view if neccessary
		View cloudView = convertView;

		if (convertView == null) {
			cloudView = inflate.inflate(R.layout.cloud_list_item, null);
		}

		// Get the view objects
		ImageView icon = (ImageView) cloudView.findViewById(R.id.cloud_icon);
		TextView tv = (TextView) cloudView.findViewById(R.id.cloud_name);

		// Set view properties
		UrlImageViewHelper.setUrlDrawable(icon, cloud.getAvatar().getPreview(),
				R.drawable.unknown_cloud, 60000 * 60);
		tv.setText(cloud.getName());
		return cloudView;
	}

}
