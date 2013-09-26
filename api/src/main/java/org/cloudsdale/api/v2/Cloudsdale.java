package org.cloudsdale.api.v2;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.cloudsdale.response.v2.CloudCollectionResponse;
import org.cloudsdale.response.v2.CloudResponse;
import org.cloudsdale.response.v2.MetaResponse;
import org.cloudsdale.response.v2.SpotlightCollectionResponse;
import org.cloudsdale.response.v2.UserCollectionResponse;
import org.cloudsdale.response.v2.UserResponse;
import org.cloudsdale.util.UnixTimestampDeserializer;

import retrofit.Callback;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

import java.util.Date;

/**
 * Created by tyr on 19/09/2013.
 */
public class Cloudsdale {

    private static final String BASE_URL = "http://api.cloudsdale.org/v2";
    private final Contract      api;

    public Cloudsdale(final String useragent) {
        Gson gson = new GsonBuilder().registerTypeAdapter(Date.class,
                new UnixTimestampDeserializer()).create();
        api = new RestAdapter.Builder()//
                .setConverter(new GsonConverter(gson))//
                .setServer(BASE_URL)//
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade requestFacade) {
                        requestFacade.addHeader("User-Agent", useragent);
                    }
                }).build().create(Contract.class);
    }

    public Contract getApi() {
        return api;
    }

    public interface Contract {

        @GET(".json")
        public void getMeta(Callback<MetaResponse> callback);

        @GET("/spotlights.json")
        public void getSpotlights(Callback<SpotlightCollectionResponse> callback);

        @GET("/clouds.json")
        public void getClouds(Callback<CloudCollectionResponse> callback);

        @GET("/clouds/{id}.json")
        public void getCloud(@Path("id") String id,
                Callback<CloudResponse> callback);

        @GET("/clouds/search.json")
        public void searchClouds(@Query("query") String query,
                Callback<CloudCollectionResponse> callback);

        @GET("/users.json")
        public void getUsers(Callback<UserCollectionResponse> callback);

        @GET("/users/{id}.json")
        public void getUser(@Path("id") String id,
                Callback<UserResponse> callback);

        @GET("/users/search.json")
        public void searchUsers(@Query("query") String query,
                Callback<UserResponse> callback);

        @GET("/me.json")
        public void getLoggedInUser(Callback<UserResponse> callback);

    }

}
