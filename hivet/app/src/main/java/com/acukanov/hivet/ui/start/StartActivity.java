package com.acukanov.hivet.ui.start;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;

import com.acukanov.hivet.R;
import com.acukanov.hivet.ui.base.BaseActivity;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class StartActivity extends BaseActivity implements IStartView {
    private static final String LOG_TAG = LogUtils.makeLogTag(StartActivity.class);
    @Inject StartPresenter mStartPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        ButterKnife.inject(this);
        mStartPresenter.attachView(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mStartPresenter.detachView();
    }
}
