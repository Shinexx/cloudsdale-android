package org.cloudsdale.android.test;

import org.cloudsdale.android.ui.HomeActivity;

import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;

import com.jayway.android.robotium.solo.Solo;

public class HomeActivityTests extends
		ActivityInstrumentationTestCase2<HomeActivity> {

	private Solo	mRobotiumTester;

	public HomeActivityTests() {
		super(HomeActivity.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mRobotiumTester = new Solo(getInstrumentation(), getActivity());
	}

	@UiThreadTest
	public void testSlidingMenuShow() {
		boolean viewLoaded = mRobotiumTester
				.waitForText("Loading your profile...");
		if (viewLoaded) {
			mRobotiumTester.clickOnActionBarHomeButton();
			// Check that the behind is showing
		} else {
			fail("View didn't load during the timemout");
		}
	}

}
