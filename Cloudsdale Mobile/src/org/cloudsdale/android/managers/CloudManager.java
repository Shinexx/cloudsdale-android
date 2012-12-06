package org.cloudsdale.android.managers;

import android.content.Context;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.api.Cloud;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.CloudGetQuery;

import java.util.HashMap;

import lombok.val;

public class CloudManager {

	private HashMap<String, Cloud>	mCachedClouds;

	public void storeCloud(Cloud cloud) {
		synchronized (mCachedClouds) {
			mCachedClouds.put(cloud.getId(), cloud);
		}
	}

	public synchronized Cloud getCloud(String id) throws QueryException {
		val isCached = mCachedClouds.containsKey(id);
		if (isCached) {
			synchronized (mCachedClouds) {
				return mCachedClouds.get(id);
			}
		} else {
			Context appContext = Cloudsdale.getContext();
			String url = appContext.getString(R.string.cloudsdale_api_base)
					+ appContext.getString(R.string.cloudsdale_cloud_endpoint,
							id);

			// Build and execute the query
			CloudGetQuery query = new CloudGetQuery(url);
			Cloud result = query.execute(null, null);
			if (result != null) storeCloud(result);
			return result;
		}

	}

}
