package org.cloudsdale.android.test;

import org.cloudsdale.android.ui.HomeActivity;
import org.cloudsdale.android.ui.LoginActivity;

import android.test.ActivityInstrumentationTestCase2;

import com.jayway.android.robotium.solo.Solo;

public class LoginActivityTests extends ActivityInstrumentationTestCase2<LoginActivity> {
	
	private Solo mRobotiumTester;
	
	public LoginActivityTests() {
		super(LoginActivity.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		mRobotiumTester = new Solo(getInstrumentation(), getActivity());
	}
	
	public void testLogin() {
		mRobotiumTester.enterText(0, Constants.DUMMY_EMAIL);
		mRobotiumTester.enterText(1, Constants.DUMMY_PASSWORD);
		mRobotiumTester.clickOnText("Sign in");
		
		mRobotiumTester.waitForActivity("HomeActivity");
		mRobotiumTester.assertCurrentActivity("Login failed", HomeActivity.class);
	}

}
