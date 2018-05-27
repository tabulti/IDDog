package com.example.jpribeiro.idwall.view;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.example.jpribeiro.idwall.R;
import com.example.jpribeiro.idwall.RemoteDataSource.SignUpResponse;
import com.example.jpribeiro.idwall.RemoteDataSource.retrofit.DogsService;
import com.example.jpribeiro.idwall.RemoteDataSource.retrofit.RetrofitImplementation;
import com.example.jpribeiro.idwall.viewModel.SignUpViewModel;

public class SignUpActivity extends AppCompatActivity {

    private SignUpViewModel mViewModel;
    public static final String INTENT_USER_TOKEN = "user_extra";
    private RelativeLayout mLoadingView;

    private DogsService.SignUpUser mSignUpUserHandler = new DogsService.SignUpUser() {
        @Override
        public void onUserSignUp(SignUpResponse response, Error error) {
            if (mViewModel.isValidSignUpResponse(response, error)) {


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mLoadingView.setVisibility(View.GONE);
                    }
                });

                Intent intent = new Intent(getContext(), FeedActivity.class);
                intent.putExtra(INTENT_USER_TOKEN, response.user.token);
                startActivity(intent);
            }
        }
    };

    private Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mViewModel = ViewModelProviders
                .of(this)
                .get(SignUpViewModel.class);

        bindViews();
    }

    private void bindViews() {
        mLoadingView = findViewById(R.id.progress_view);

        RetrofitImplementation.getInstance().initRetrofit();

        final EditText mSignUpEdt = findViewById(R.id.enter_email_edt);

        findViewById(R.id.sign_up_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mViewModel.isValidEmail(mSignUpEdt.getText().toString())) {

                    mLoadingView.setVisibility(View.VISIBLE);
                    RetrofitImplementation.getInstance().signupUser(mSignUpEdt.getText().toString(),
                            mSignUpUserHandler);

                } else {
                    mSignUpEdt.setError(getString(R.string.invalid_email));
                }

            }
        });
    }
}
