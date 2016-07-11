package com.acukanov.hivet.ui.chat;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.acukanov.hivet.R;
import com.acukanov.hivet.data.model.Messages;
import com.acukanov.hivet.data.model.Users;
import com.acukanov.hivet.ui.base.BaseActivity;
import com.acukanov.hivet.ui.base.BaseFragment;
import com.acukanov.hivet.utils.LogUtils;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class ChatFragment extends BaseFragment implements IChatView, View.OnClickListener {
    private static final String LOG_TAG = LogUtils.makeLogTag(ChatFragment.class);
    private Activity mActivity;
    @Inject ChatPresenter mChatPresenter;
    /*@Inject*/ ChatAdapter mChatAdapter;
    @InjectView(R.id.list_chat) RecyclerView mChatList;
    @InjectView(R.id.text_empty_chat) TextView mEmptyChatMessage;
    @InjectView(R.id.text_message_field) EditText mMessageField;
    @InjectView(R.id.btn_send_message) ImageButton mSendMessageButton;

    public ChatFragment() {}

    /*public static ChatFragment newInstance() {
        return new ChatFragment();
    }*/

    public static ChatFragment newInstance(int userId) {
        ChatFragment instance = new ChatFragment();
        Bundle args = new Bundle();
        args.putInt("user_id", userId);
        instance.setArguments(args);
        return instance;
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
        Bundle args = getArguments();
        int userId = 0;
        if (args != null) {
            userId = args.getInt("user_id");
            mChatAdapter = new ChatAdapter(mActivity, userId);
        } else {
            mChatAdapter = new ChatAdapter(mActivity, userId);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_chat, container, false);
        ButterKnife.inject(this, rootView);
        mChatPresenter.attachView(this);
        mChatList.setHasFixedSize(true);
        mChatList.setLayoutManager(new LinearLayoutManager(mActivity));
        mChatList.setAdapter(mChatAdapter);


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mChatPresenter.loadUsersAndMessagesData();
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

    @Override
    public void showProgress(boolean show) {

    }

    @Override
    public void showEmptyMessage() {
        mEmptyChatMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void showChatUsers(ArrayList<Users> users) {

    }

    @Override
    public void showChatMessages(ArrayList<Messages> messages) {
        mChatAdapter.setMessages(messages);
        mChatAdapter.notifyDataSetChanged();
        mEmptyChatMessage.setVisibility(View.GONE);
    }

}
