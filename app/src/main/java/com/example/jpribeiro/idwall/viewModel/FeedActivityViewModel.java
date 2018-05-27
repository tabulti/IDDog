package com.example.jpribeiro.idwall.viewModel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.jpribeiro.idwall.RemoteDataSource.GetFeedResponse;

import java.util.List;

public class FeedActivityViewModel extends ViewModel {

    public MutableLiveData<List<String>> mImages = new MutableLiveData<>();

    public int lastTabSelected = -1;

    public boolean validateGetFeedResponse(GetFeedResponse response, Error error) {
        return error == null &&
                response != null &&
                response.list != null &&
                !response.list.isEmpty();
    }
}
