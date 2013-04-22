package org.cloudsdale.android.ui.fragments;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import de.keyboardsurfer.android.widget.crouton.Crouton;
import de.keyboardsurfer.android.widget.crouton.Style;

import org.cloudsdale.android.R;
import org.holoeverywhere.LayoutInflater;
import org.holoeverywhere.app.Fragment;

public class CloudFragment extends Fragment {

	public static final String	BUNDLE_CLOUD_ID	= "id";

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Crouton.showText(getActivity(),
				"Got ID: " + getArguments().getString(BUNDLE_CLOUD_ID),
				Style.INFO);
		return inflater.inflate(R.layout.fragment_cloud, container, false);
	}

}
