package com.example.jpribeiro.idwall.view;

import android.animation.Animator;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.jpribeiro.idwall.R;
import com.example.jpribeiro.idwall.RemoteDataSource.GetFeedResponse;
import com.example.jpribeiro.idwall.RemoteDataSource.retrofit.DogsService;
import com.example.jpribeiro.idwall.RemoteDataSource.retrofit.RetrofitImplementation;
import com.example.jpribeiro.idwall.viewModel.FeedActivityViewModel;
import com.example.jpribeiro.idwall.viewModel.FeedAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Objects;

public class FeedActivity extends AppCompatActivity implements FeedAdapter.ItemClickListener {

    private static final String HUSKY_CATEGORY = "husky";
    private static final String HOUND_CATEGORY = "hound";
    private static final String PUG_CATEGORY = "pug";
    private static final String LABRADOR_CATEGORY = "labrador";

    private FeedAdapter adapter;
    private RelativeLayout mLoadingView;
    private RelativeLayout mPreviewContent;
    private ImageView mPreviewContentImg;
    private FeedActivityViewModel mViewModel;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager staggeredGridLayoutManager;
    private TabLayout mTabLayout;

    private Observer<List<String>> onListChangeObservable = new Observer<List<String>>() {
        @Override
        public void onChanged(@Nullable List<String> list) {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    recyclerView.setHasFixedSize(true);
                    if (staggeredGridLayoutManager == null) {
                        if (getOrientation() == Configuration.ORIENTATION_LANDSCAPE) {
                            staggeredGridLayoutManager = new StaggeredGridLayoutManager(4,
                                    Configuration.ORIENTATION_PORTRAIT);
                        } else {
                            staggeredGridLayoutManager = new StaggeredGridLayoutManager(3,
                                    Configuration.ORIENTATION_PORTRAIT);
                        }

                    }

                    if (adapter == null) {
                        adapter = new FeedAdapter(getContext(), mViewModel.mImages.getValue());
                        adapter.setClickListener(FeedActivity.this);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(staggeredGridLayoutManager);
                    } else {
                        adapter.setDogsList(mViewModel.mImages.getValue());
                        adapter.notifyDataSetChanged();
                    }
                    mLoadingView.setVisibility(View.GONE);
                }
            });

        }
    };


    private DogsService.GetFeed mGetFeedHandler = new DogsService.GetFeed() {
        @Override
        public void onGetFeed(final GetFeedResponse response, Error error) {
            if (mViewModel.validateGetFeedResponse(response, error)) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mViewModel.mImages.setValue(response.list);
                    }
                });
            }
        }
    };

    private Context getContext() {
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        final String userToken = getIntent().getStringExtra(SignUpActivity.INTENT_USER_TOKEN);

        bindViews();

        mViewModel = ViewModelProviders
                .of(this)
                .get(FeedActivityViewModel.class);

        mViewModel.mImages.observe(this, onListChangeObservable);

        if (mViewModel.lastTabSelected != -1) {
            TabLayout.Tab tab = mTabLayout.getTabAt(mViewModel.lastTabSelected);
            if (tab != null) {
                tab.select();
            }
        }

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                String category = "";
                switch (tab.getPosition()) {
                    case 0:
                        category = HUSKY_CATEGORY;
                        mViewModel.lastTabSelected = 0;
                        break;
                    case 1:
                        category = HOUND_CATEGORY;
                        mViewModel.lastTabSelected = 1;
                        break;
                    case 2:
                        category = PUG_CATEGORY;
                        mViewModel.lastTabSelected = 2;
                        break;
                    case 3:
                        category = LABRADOR_CATEGORY;
                        mViewModel.lastTabSelected = 3;
                        break;
                }
                mLoadingView.setVisibility(View.VISIBLE);
                RetrofitImplementation.getInstance().getFeedByCategory(userToken,
                        category, mGetFeedHandler);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        findViewById(R.id.image_preview_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPreviewContent.setVisibility(View.GONE);
            }
        });

        if (mViewModel.mImages.getValue() == null || mViewModel.mImages.getValue().isEmpty()) {
            mLoadingView.setVisibility(View.VISIBLE);
            RetrofitImplementation.getInstance().getFeed(userToken, mGetFeedHandler);
        }
    }

    private int getOrientation(){
        return getResources().getConfiguration().orientation;
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.rvDogs);
        mLoadingView = findViewById(R.id.progress_view);
        mPreviewContent = findViewById(R.id.preview_content);
        mPreviewContentImg = findViewById(R.id.image_preview);
        mTabLayout = findViewById(R.id.tab_layout);
    }

    @Override
    public void onItemClick(View view, int position) {

        Picasso.get()
                .load(Objects.requireNonNull(mViewModel.mImages.getValue()).get(position))
                .into(mPreviewContentImg);

        YoYo.with(Techniques.FadeIn)
                .duration(200)
                .withListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        mPreviewContent.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {

                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                })
                .playOn(mPreviewContent);
    }
}
