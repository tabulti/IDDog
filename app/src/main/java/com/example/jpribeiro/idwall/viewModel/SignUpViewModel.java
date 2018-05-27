package com.example.jpribeiro.idwall.viewModel;

import android.arch.lifecycle.ViewModel;

import com.example.jpribeiro.idwall.RemoteDataSource.SignUpResponse;

public class SignUpViewModel extends ViewModel {

    public boolean isValidSignUpResponse(SignUpResponse response, Error error) {
        return error == null &&
                response != null &&
                response.user != null &&
                response.user._id != null &&
                response.user.token != null &&
                response.user.email != null;
    }

    public Boolean isValidEmail(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
