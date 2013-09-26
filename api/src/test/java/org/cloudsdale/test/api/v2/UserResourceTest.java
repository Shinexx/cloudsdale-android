package org.cloudsdale.test.api.v2;

import junit.framework.TestCase;

import org.cloudsdale.api.v2.Cloudsdale;
import org.cloudsdale.model.v2.User;
import org.cloudsdale.response.v2.UserCollectionResponse;
import org.cloudsdale.response.v2.UserResponse;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by tyr on 25/09/2013.
 */
@RunWith(JUnit4.class)
public class UserResourceTest extends TestCase {

    private static final String FIXTURE_USERNAME = "berwyn";
    private static final String FIXTURE_ID = "4f2ad7371f5f9d3ab7004476";

    private static Cloudsdale api;
    private CountDownLatch lock;

    @BeforeClass
    public static void setup() {
        api = new Cloudsdale("cloudsdale-java/junit");
    }

    @Before
    public void before() {
        lock = new CountDownLatch(1);
    }

    @Test
    public void testGetUser() {
        final User[] user = new User[1];
        final RetrofitError[] error = new RetrofitError[1];

        api.getApi().getUser("berwyn", new Callback<UserResponse>() {
            @Override
            public void success(UserResponse userResponse, Response response) {
                user[0] = userResponse.getUser();
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                error[0] = retrofitError;
                lock.countDown();
            }
        });

        try {
            lock.await();
        } catch (InterruptedException e) {
            fail("Lock interrupted");
        }

        assertNotNull(user[0]);
        assertNull(error[0]);
        assertEquals(FIXTURE_USERNAME, user[0].getUsername());
        assertEquals(FIXTURE_ID, user[0].getId());
    }

    @Test
    public void testGetUsers() {
        final List<User> users = new ArrayList<User>();
        final RetrofitError[] error = new RetrofitError[1];

        api.getApi().getUsers(new Callback<UserCollectionResponse>() {
            @Override
            public void success(UserCollectionResponse userCollectionResponse, Response response) {
                users.addAll(Arrays.asList(userCollectionResponse.getUsers()));
                lock.countDown();
            }

            @Override
            public void failure(RetrofitError retrofitError) {
                System.out.println("Response had status:\n\t"
                        +retrofitError.getResponse().getStatus()
                        +"\nGot response:\n\t"
                        +retrofitError.getResponse().getBody()
                        +"\nRequested url:\n\t"
                        +retrofitError.getUrl()
                        +"\nReason:\n\t"
                        +retrofitError.getMessage());
                error[0] = retrofitError;
                lock.countDown();
            }
        });

        try {
            lock.await();
        } catch (InterruptedException e) {
            fail("Lock interrupted");
        }

        assertNotNull(users);
        assertNull(error[0]);
        assert users.size() > 0;
    }

}
