package org.cloudsdale.android.ui.fragments;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.support.v4.app.Fragment;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.EFragment;
import com.googlecode.androidannotations.annotations.ViewById;

import org.cloudsdale.android.R;

import java.io.IOException;
import java.io.InputStream;

@EFragment(R.layout.fragment_about)
public class AboutFragment extends Fragment {

	@ViewById(R.id.about_version_label)
	TextView	versionText;
	@ViewById(R.id.about_changelog_view)
	WebView		changelogText;
	@ViewById(R.id.about_license_view)
	WebView		licenseText;

	@AfterViews
	public void fillViews() {
		versionText.setText(getString(R.string.fragment_about_version_text,
				getVersionName()));

		try {
			InputStream stream = getActivity().getAssets().open(
					"changelog.html");
			int size = stream.available();
			byte[] buffer = new byte[size];
			stream.read(buffer);
			stream.close();
			String text = new String(buffer);
			changelogText.loadData(text, "text/html", "utf-8");
			changelogText.setBackgroundColor(getResources().getColor(
					android.R.color.transparent));
		} catch (IOException e) {
			changelogText.setVisibility(View.GONE);
		}

		try {
			InputStream stream = getActivity().getAssets()
					.open("licenses.html");
			int size = stream.available();
			byte[] buffer = new byte[size];
			stream.read(buffer);
			stream.close();
			String text = new String(buffer);
			licenseText.loadData(text, "text/html", "utf-8");
			licenseText.setBackgroundColor(getResources().getColor(
					android.R.color.transparent));
		} catch (IOException e) {
			licenseText.setVisibility(View.GONE);
		}
	}

	private String getVersionName() {
		try {
			Application app = getActivity().getApplication();
			PackageInfo manager = app.getPackageManager().getPackageInfo(
					app.getPackageName(), 0);
			return manager.versionName;
		} catch (NameNotFoundException e) {
			return "";
		}
	}

}
