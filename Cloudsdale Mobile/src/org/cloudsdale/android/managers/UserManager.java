package org.cloudsdale.android.managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.AsyncTask;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.UserGetQuery;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class UserManager {

	private static ArrayList<User>	mStoredUsers;

	static {
		mStoredUsers = new ArrayList<User>();
	}

	public static User getLoggedInUser() {
		Account userAccount = UserAccountManager.getAccount();
		AccountManager am = AccountManager.get(Cloudsdale.getContext());
		return getUserById(am.getUserData(userAccount,
				UserAccountManager.KEY_ID));
	}

	public static User getUserById(String id) {
		if (mStoredUsers != null && !mStoredUsers.isEmpty()) {
			UserLoadTask task = new UserLoadTask();
			task.execute(id);
			try {
				return task.get();
			} catch (InterruptedException e) {
				e.printStackTrace();
				return null;
			} catch (ExecutionException e) {
				e.printStackTrace();
				return null;
			}
		} else return null;
	}

	public static void storeUser(User user) {
		synchronized (mStoredUsers) {
			mStoredUsers.add(user);
		}
	}

	static class UserLoadTask extends AsyncTask<String, Void, User> {
		@Override
		protected User doInBackground(String... params) {
			ArrayList<User> users;
			synchronized (mStoredUsers) {
				users = new ArrayList<User>(mStoredUsers);
			}
			for (User u : users) {
				if (u.getId().equals(params[0])) { return u; }
			}
			return null;
		}
	}

	static class UserFetchTask extends AsyncTask<String, Void, User> {

		public static final int	KEY_URL			= 0;
		public static final int	KEY_AUTH_TOKEN	= 1;

		@Override
		protected User doInBackground(String... params) {
			UserGetQuery query = new UserGetQuery(params[KEY_URL]);
			query.addHeader("X-AUTH-TOKEN", params[KEY_AUTH_TOKEN]);
			try {
				return query.execute(null, null);
			} catch (QueryException e) {
				return null;
			}
		}

	}

}
