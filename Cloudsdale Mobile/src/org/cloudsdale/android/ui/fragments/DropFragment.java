package org.cloudsdale.android.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import org.cloudsdale.android.R;
import org.cloudsdale.android.models.QueryData;
import org.cloudsdale.android.models.api.Drop;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.DropGetQuery;
import org.cloudsdale.android.ui.CloudActivity;
import org.cloudsdale.android.ui.adapters.CloudDropAdapter;

public class DropFragment extends SherlockFragment {

	private View				mDropView;
	private ListView			mDropList;
	private CloudDropAdapter	mDropAdapter;
	private String				mCloudId;
	private String				mCloudUrl;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Bundle args = getArguments();
		mCloudId = args.getString(CloudActivity.CLOUD_ID);
		mDropView = inflater.inflate(R.layout.fragment_drop_list, null);
		return mDropView;
	}

	@Override
	public void onResume() {
		attachViews();
		super.onResume();
	}

	public void addDrop(Drop drop) {
		mDropAdapter.addDrop(drop);
	}

	private void attachViews() {
		mDropList = (ListView) mDropView.findViewById(R.id.drop_list);
		mDropAdapter = new CloudDropAdapter(getActivity(), null);
		mDropList.setAdapter(mDropAdapter);
		populateDrops();
		mDropList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String url = ((Drop) mDropList.getItemAtPosition(position))
						.getUrl();
				Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
				startActivity(intent);
			}
		});
	}

	private void populateDrops() {
		if (mCloudUrl == null) {
			mCloudUrl = getString(R.string.cloudsdale_api_base)
					+ getString(R.string.cloudsdale_cloud_drop_endpoint,
							mCloudId);
		}
		DropAsyncQuery query = new DropAsyncQuery();
		query.execute();
	}

	class DropAsyncQuery extends AsyncTask<Void, Void, Drop[]> {

		@Override
		protected Drop[] doInBackground(Void... params) {
			QueryData data = new QueryData();
			data.setUrl(mCloudUrl);

			DropGetQuery query = new DropGetQuery(mCloudUrl);
			try {
				return query.executeForCollection(data, getActivity());
			} catch (QueryException e) {
				// TODO Stop fucking up and get the drops goddamn it
				e.printStackTrace();
				return null;
			}
		}

		@Override
		protected void onPostExecute(Drop[] result) {
			super.onPostExecute(result);

			if (result != null) {
				for (int i = result.length - 1; i >= 0; i--) {
					mDropAdapter.addDrop(result[i]);
				}
				mDropList.smoothScrollToPosition(0);
			}
		}

	}

	public static interface DropFragmentCallbacks {

		public long getCurruntCloudId();

	}

}
