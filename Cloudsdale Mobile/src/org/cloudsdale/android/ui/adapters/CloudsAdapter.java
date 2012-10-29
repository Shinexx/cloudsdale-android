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
import org.cloudsdale.android.models.api.Cloud;

import java.util.ArrayList;

public class CloudsAdapter extends BaseAdapter {

    private Activity         mViewRoot;
    private ArrayList<Cloud> mCloudArray;
    private LayoutInflater   mInflater;

    public CloudsAdapter(Activity activity, ArrayList<Cloud> clouds) {
        this.mViewRoot = activity;
        this.mInflater = (LayoutInflater) this.mViewRoot
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        
        if (clouds != null && clouds.size() > 0) {
            this.mCloudArray = clouds;
        } else {
            this.mCloudArray = new ArrayList<Cloud>();
        }
    }

    public void addCloud(Cloud cloud) {
        this.mCloudArray.add(cloud);
    }

    @Override
    public int getCount() {
        return this.mCloudArray.size();
    }

    @Override
    public Cloud getItem(int position) {
        return this.mCloudArray.get(position);
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
            cloudView = this.mInflater.inflate(R.layout.cloud_list_item, null);
        }

        // Get the view objects
        ImageView icon = (ImageView) cloudView.findViewById(R.id.cloud_icon);
        TextView nameView = (TextView) cloudView.findViewById(R.id.cloud_name);
        TextView unreadCount = (TextView) cloudView
                .findViewById(R.id.cloud_unread);
        TextView hiddenId = (TextView) cloudView
                .findViewById(R.id.cloud_hidden_id);

        // Set view properties
        UrlImageViewHelper.setUrlDrawable(icon, cloud.getAvatar().getPreview(),
                R.drawable.unknown_cloud, 60000 * 60);
        nameView.setText(cloud.getName());
        nameView.setSelected(true);
        unreadCount.setText("0");
        hiddenId.setText(cloud.getId());

        // Set the view's id to the cloud's id
        cloudView.setId(cloud.getId().hashCode());
        return cloudView;
    }

}
