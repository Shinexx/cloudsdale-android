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
import org.cloudsdale.android.models.api_models.Drop;

import java.util.ArrayList;

public class CloudDropAdapter extends BaseAdapter {

    private ArrayList<Drop>  mDrops;
    private Activity         mViewRoot;
    private LayoutInflater   mInflater;

    private static final int PREVIEW_REFRESH_INTERVAL = 1000 * 60 * 10;

    public CloudDropAdapter(Activity activity, ArrayList<Drop> drops) {
        this.mViewRoot = activity;
        this.mInflater = (LayoutInflater) this.mViewRoot
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (drops != null && drops.size() > 0) {
            mDrops = drops;
        } else {
            clearDrops();
        }
    }

    public void addDrop(Drop drop) {
        mDrops.add(drop);
        notifyDataSetChanged();
    }
    
    public void clearDrops() {
        mDrops = new ArrayList<Drop>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mDrops.size();
    }

    @Override
    public Drop getItem(int index) {
        return mDrops.get(index);
    }

    @Override
    public long getItemId(int index) {
        return mDrops.get(index).getId().hashCode();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Drop drop = (Drop) getItem(position);

        View dropView = convertView;

        if (dropView == null) {
            dropView = mInflater.inflate(R.layout.drop_layout, null);
        }

        ImageView preview = (ImageView) dropView
                .findViewById(R.id.drop_preview);
        TextView title = (TextView) dropView.findViewById(R.id.drop_title);

        title.setText(drop.getTitle());
        title.setSelected(true);
        UrlImageViewHelper.setUrlDrawable(preview, drop.getPreview(),
                R.drawable.unknown_drop, PREVIEW_REFRESH_INTERVAL);
        
        return dropView;
    }
}
