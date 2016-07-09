package com.acukanov.hivet.ui.chat;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acukanov.hivet.R;
import com.acukanov.hivet.ui.base.BaseActivity;
import com.acukanov.hivet.ui.base.BaseFragment;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;

public class ChatFragment extends BaseFragment implements IChatView, View.OnClickListener {
    private static final String LOG_TAG = LogUtils.makeLogTag(ChatFragment.class);
    private Activity mActivity;
    @Inject ChatPresenter mChatPresenter;

    public ChatFragment() {

    }

    public static ChatFragment newInstance() {
        return new ChatFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        ((BaseActivity) mActivity).activityComponent().inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.inject(this, rootView);
        mChatPresenter.attachView(this);

        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mChatPresenter.detachView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

        }
    }
}
