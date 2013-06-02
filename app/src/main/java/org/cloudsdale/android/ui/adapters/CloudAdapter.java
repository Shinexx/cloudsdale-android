package org.cloudsdale.android.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.widget.TextView;
import com.androidquery.AQuery;
import com.koushikdutta.urlimageviewhelper.UrlImageViewHelper;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CloudAdapter extends BaseAdapter {

	private List<Cloud>	clouds;
	private Context		context;
	private AQuery		aquery;

	public CloudAdapter(List<Cloud> clouds, Context context) {
		this.context = context;
		if (clouds != null) {
			this.clouds = clouds;
		} else {
			this.clouds = new ArrayList<Cloud>();
		}
	}

	@Override
	public int getCount() {
		if (clouds != null) {
			return clouds.size();
		} else {
			return 0;
		}
	}

	@Override
	public Cloud getItem(int position) {
		return clouds.get(position);
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getId().hashCode();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (aquery == null) {
			aquery = new AQuery(context);
		}
		View view = aquery.inflate(convertView,
				R.layout.widget_sliding_menu_item, parent);

		TextView tv = (TextView) view
				.findViewById(R.id.sliding_menu_item_lable);
		ImageView iv = (ImageView) view
				.findViewById(R.id.sliding_menu_item_icon);

		tv.setText(getItem(position).getName());
		UrlImageViewHelper.setUrlDrawable(iv, getItem(position).getAvatar()
				.getNormal(), R.drawable.ic_unknown_user,
				Cloudsdale.CLOUD_EXPIRATION);

		return view;
	}

	public void addCloud(Cloud... cloud) {
		if (cloud != null) {
			this.clouds.addAll(Arrays.asList(cloud));
		}
		notifyDataSetChanged();
	}

}
