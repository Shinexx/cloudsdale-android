package org.cloudsdale.android.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api_models.Cloud;

import java.util.ArrayList;

public class CloudsAdapter extends BaseAdapter {

	private Activity			root;
	private ArrayList<Cloud>	clouds;
	private LayoutInflater		inflate;

	public CloudsAdapter(Activity activity, ArrayList<Cloud> clouds) {
		this.root = activity;
		this.clouds = clouds;
		this.inflate = (LayoutInflater) this.root
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public void addCloud(Cloud cloud) {
		this.clouds.add(cloud);
	}

	@Override
	public int getCount() {
		return this.clouds.size();
	}

	@Override
	public Object getItem(int position) {
		return this.clouds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the cloud
		Cloud cloud = (Cloud) getItem(position);

		// Inflate the view if necessary
		View cloudView = convertView;

		if (convertView == null) {
			cloudView = this.inflate.inflate(R.layout.cloud_list_item, null);
		}

		// Get the view objects
		ImageView icon = (ImageView) cloudView.findViewById(R.id.cloud_icon);
		TextView tv = (TextView) cloudView.findViewById(R.id.cloud_name);
		TextView uc = (TextView) cloudView.findViewById(R.id.cloud_unread);

		// Set view properties
		UrlImageViewHelper.setUrlDrawable(icon, cloud.getAvatar().getPreview(),
				R.drawable.unknown_cloud, 60000 * 60);
		tv.setText(cloud.getName());
		uc.setText("0");
		
		// Set the view's id to the cloud's id
		cloudView.setId(cloud.getId().hashCode());
		return cloudView;
	}

}
