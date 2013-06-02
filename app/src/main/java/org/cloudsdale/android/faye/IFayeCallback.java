package org.cloudsdale.android.faye;

import android.app.Application;

public interface IFayeCallback {

	public void connected();

	public void disconnected();

	public void connecting();

	public boolean getDebugStatus();

	public Application getServiceApplication();

}
