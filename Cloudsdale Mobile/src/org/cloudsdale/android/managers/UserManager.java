package org.cloudsdale.android.managers;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.os.AsyncTask;

import org.cloudsdale.android.Cloudsdale;
import org.cloudsdale.android.R;
import org.cloudsdale.android.models.LoggedUser;
import org.cloudsdale.android.models.api.User;
import org.cloudsdale.android.models.exceptions.QueryException;
import org.cloudsdale.android.models.queries.UserGetQuery;

import java.util.concurrent.ExecutionException;

public class UserManager {

	private static User	sLoggedUser;

	public static User getLoggedInUser() {
		if (sLoggedUser == null) {
			Context context = Cloudsdale.getContext();
			AccountManager am = AccountManager.get(context);
			Account account = UserAccountManager.getAccount();
			User user = DatabaseManager.readUser(am.getUserData(account,
					UserAccountManager.KEY_ID));
			if (user != null) {
				sLoggedUser = user;
			} else {
				String id = am.getUserData(account, UserAccountManager.KEY_ID);
				String url = context.getString(R.string.cloudsdale_api_base)
						+ context.getString(R.string.cloudsdale_user_endpoint,
								id);
				String[] strings = new String[2];
				strings[UserFetchTask.KEY_URL] = url;
				strings[UserFetchTask.KEY_AUTH_TOKEN] = am.getPassword(account);
				UserFetchTask task = new UserFetchTask();
				task.execute(strings);
				try {
					sLoggedUser = task.get();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return sLoggedUser;
	}
	
	public static boolean storeLoggedInUser(LoggedUser user) {
		sLoggedUser = user;
		return storeUser(user);
	}
	
	public static User getUserById(String id) {
		return DatabaseManager.readUser(id);
	}
	
	public static boolean storeUser(User user) {
		return DatabaseManager.storeUser(user);
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
