package com.example.jpribeiro.idwall.RemoteDataSource.retrofit;

import android.support.annotation.NonNull;

import com.example.jpribeiro.idwall.RemoteDataSource.GetFeedResponse;
import com.example.jpribeiro.idwall.RemoteDataSource.SignUpBody;
import com.example.jpribeiro.idwall.RemoteDataSource.SignUpResponse;

import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitImplementation {

    private DogsService service;

    private static RetrofitImplementation instance = null;

    public static RetrofitImplementation getInstance() {
        if (instance == null) {
            instance = new RetrofitImplementation();
        }
        return instance;
    }

    public void initRetrofit() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();

        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        String RETROFIT_BASE_URL = "https://iddog-api.now.sh";
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RETROFIT_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .callbackExecutor(Executors.newSingleThreadExecutor())
                .client(client)
                .build();

        service = retrofit.create(DogsService.class);
    }

    public void signupUser(@NonNull String email,
                           @NonNull final DogsService.SignUpUser handler) {
        if (service != null) {
            Call<SignUpResponse> res = service.signUpUser(new SignUpBody(email));

            res.enqueue(new Callback<SignUpResponse>() {
                @Override
                public void onResponse(@NonNull Call<SignUpResponse> call,
                                       @NonNull Response<SignUpResponse> response) {
                    if (response.errorBody() == null &&
                            response.body() != null) {
                        handler.onUserSignUp(response.body(), null);
                    } else {
                        String errorMsg = "Code: " + response.raw().code() + "\n" +
                                "Message: " + response.raw().message();
                        handler.onUserSignUp(null, new Error(errorMsg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SignUpResponse> call, @NonNull Throwable t) {
                    handler.onUserSignUp(null, new Error(t.toString()));
                }
            });
        }
    }

    public void getFeed(@NonNull final String userToken,
                        @NonNull final DogsService.GetFeed handler){
        if (service != null) {
            final Call<GetFeedResponse> res = service.getFeed(userToken);

            res.enqueue(new Callback<GetFeedResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetFeedResponse> call, @NonNull Response<GetFeedResponse> response) {
                    if (response.errorBody() == null &&
                            response.body() != null) {
                        handler.onGetFeed(response.body(), null);
                    } else {
                        String errorMsg = "Code: " + response.raw().code() + "\n" +
                                "Message: " + response.raw().message();
                        handler.onGetFeed(null, new Error(errorMsg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetFeedResponse> call, @NonNull Throwable t) {
                    handler.onGetFeed(null, new Error(t.toString()));
                }
            });
        }
    }

    public void getFeedByCategory(@NonNull final String userToken,
                                  @NonNull String category,
                                  @NonNull final DogsService.GetFeed handler){
        if (service != null) {
            final Call<GetFeedResponse> res = service.getFeedByCategory(userToken, category);

            res.enqueue(new Callback<GetFeedResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetFeedResponse> call, @NonNull Response<GetFeedResponse> response) {
                    if (response.errorBody() == null &&
                            response.body() != null) {
                        handler.onGetFeed(response.body(), null);
                    } else {
                        String errorMsg = "Code: " + response.raw().code() + "\n" +
                                "Message: " + response.raw().message();
                        handler.onGetFeed(null, new Error(errorMsg));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GetFeedResponse> call, @NonNull Throwable t) {
                    handler.onGetFeed(null, new Error(t.toString()));
                }
            });
        }
    }
}
