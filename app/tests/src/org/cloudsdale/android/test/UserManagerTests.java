package org.cloudsdale.android.test;

import android.test.ApplicationTestCase;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.exceptions.QueryException;

public class UserManagerTests extends ApplicationTestCase<Cloudsdale> {

	public UserManagerTests() {
		super(Cloudsdale.class);
	}
	
	@Override
	protected void setUp() throws Exception {
		createApplication();
		super.setUp();
	}

	public void testUserGet() {
		try {
			User u = getApplication().getUserManager().getUserById(
					Constants.DUMMY_ID);
			assertNotNull(u);
			assertEquals(Constants.DUMMY_EMAIL, u.getEmail());
		} catch (QueryException e) {
			fail();
		}
	}

}
