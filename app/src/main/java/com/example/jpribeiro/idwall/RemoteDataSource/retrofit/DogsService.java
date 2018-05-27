package com.example.jpribeiro.idwall.RemoteDataSource.retrofit;

import com.example.jpribeiro.idwall.RemoteDataSource.GetFeedResponse;
import com.example.jpribeiro.idwall.RemoteDataSource.SignUpBody;
import com.example.jpribeiro.idwall.RemoteDataSource.SignUpResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface DogsService {

    interface SignUpUser {
        void onUserSignUp(SignUpResponse response, Error error);
    }

    interface GetFeed {
        void onGetFeed(GetFeedResponse response, Error error);
    }


    @POST("/signup")
    Call<SignUpResponse> signUpUser(@Body SignUpBody body);

    @GET("/feed")
    Call<GetFeedResponse> getFeed(@Header("Authorization") String userToken);

    @GET("/feed")
    Call<GetFeedResponse> getFeedByCategory(@Header("Authorization") String userToken,
                                  @Query("category") String category);
}
