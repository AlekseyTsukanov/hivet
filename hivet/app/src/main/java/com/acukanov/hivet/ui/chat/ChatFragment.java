package com.acukanov.hivet.ui.chat;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.acukanov.hivet.R;
import com.acukanov.hivet.data.database.model.Messages;
import com.acukanov.hivet.data.database.model.Users;
import com.acukanov.hivet.events.ChatMessageSended;
import com.acukanov.hivet.service.BotMessageService;
import com.acukanov.hivet.ui.base.BaseActivity;
import com.acukanov.hivet.ui.base.BaseFragment;
import com.acukanov.hivet.utils.DateUtils;
import com.acukanov.hivet.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatFragment extends BaseFragment implements IChatView, View.OnClickListener {
    private static final String LOG_TAG = LogUtils.makeLogTag(ChatFragment.class);
    private static final String EXTRA_BUNDLE_ARG_USER_ID = "extra_user_id";
    private Activity mActivity;
    @Inject ChatPresenter mChatPresenter;
    @InjectView(R.id.list_chat) RecyclerView mChatList;
    @InjectView(R.id.text_empty_chat) TextView mEmptyChatMessage;
    @InjectView(R.id.text_message_field) EditText mMessageField;
    @InjectView(R.id.btn_send_message) ImageButton mSendMessageButton;
    @InjectView(R.id.progress_chat) ProgressBar mProgressChat;
    private ChatAdapter mChatAdapter;
    private Messages mMessage = null;
    private long mUserId = 0;
    private Intent mMessageServiceIntent;
    private ArrayList<Messages> mMessageList;

    public ChatFragment() {
        // Need default empty constructor
    }

    public static ChatFragment newInstance(long userId) {
        ChatFragment instance = new ChatFragment();
        Bundle args = new Bundle();
        args.putLong(EXTRA_BUNDLE_ARG_USER_ID, userId);
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
        mMessageList = new ArrayList<>();
        Bundle args = getArguments();
        if (args != null) {
            mUserId = args.getLong(EXTRA_BUNDLE_ARG_USER_ID);
            mChatAdapter = new ChatAdapter(mActivity, mUserId);
        } else {
            mChatAdapter = new ChatAdapter(mActivity, mUserId);
        }
        mMessage = new Messages();
        mMessageServiceIntent =  BotMessageService.getStartIntent(mActivity);
        mActivity.startService(mMessageServiceIntent);
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
        mProgressChat.setVisibility(View.VISIBLE);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mChatPresenter.loadUsersAndMessagesData();
        //mActivity.startService(new Intent(mActivity, BotMessageService.class));
       /* mActivity.startService(BotMessageService.getStartIntent(mActivity));*/
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        mChatPresenter.detachView();
        mActivity.stopService(mMessageServiceIntent);
    }

    @Override
    @OnClick({R.id.btn_send_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send_message:
                mMessage.message = mMessageField.getText().toString();
                mMessage.dateTime = DateUtils.getDateTime();
                mMessage.userId = mUserId;
                mChatPresenter.sendMessage(mMessage);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(ChatMessageSended event) {
        mChatPresenter.loadUsersAndMessagesData();
    }

    // MVP – View methods

    @Override
    public void showProgress(boolean show) {
        if (show) {
            mProgressChat.setVisibility(View.VISIBLE);
        } else {
            mProgressChat.setVisibility(View.GONE);
        }
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
        mMessageList = messages;
        mChatAdapter.setMessages(messages);
        mChatAdapter.notifyDataSetChanged();
        mEmptyChatMessage.setVisibility(View.GONE);
    }
}
