package org.cloudsdale.android.models.api;

import android.content.Context;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.Model;

public class Prosecution extends Model<Prosecution> {
	
	public Prosecution() {
		this(Cloudsdale.getContext());
	}

	public Prosecution(Context context) {
		super(context);
	}

}
