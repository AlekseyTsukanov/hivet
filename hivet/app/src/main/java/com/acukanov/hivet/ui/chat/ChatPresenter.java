package com.acukanov.hivet.ui.chat;


import com.acukanov.hivet.data.database.DatabaseHelper;
import com.acukanov.hivet.data.database.model.Messages;
import com.acukanov.hivet.events.ChatMessageSent;
import com.acukanov.hivet.ui.base.IPresenter;
import com.acukanov.hivet.utils.DateUtils;
import com.acukanov.hivet.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Subscription;
import rx.schedulers.Schedulers;

public class ChatPresenter implements IPresenter<IChatView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ChatPresenter.class);
    private IChatView mChatView;
    private DatabaseHelper mDatabaseHelper;
    private Subscription mSubscription;

    @Inject
    ChatPresenter(DatabaseHelper databaseHelper) {
        mDatabaseHelper = databaseHelper;
    }

    @Override
    public void attachView(IChatView IView) {
        mChatView = IView;
    }

    @Override
    public void detachView() {
        mChatView = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void loadMessages() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mChatView.showProgress(true);
        ArrayList<Messages> msgList = new ArrayList<>();
        mSubscription = mDatabaseHelper.getMessages()
                .subscribe(messages -> {
                    LogUtils.error(LOG_TAG, "on Next test");
                    if (messages != null) {
                        LogUtils.error(LOG_TAG, "on Next messages are not empty");
                        msgList.add(messages);
                        mChatView.showProgress(true);
                    } else {
                        mChatView.showEmptyMessage();
                    }
                }, e -> {
                    LogUtils.error(LOG_TAG, "on Error test: " + e.getMessage());
                    mChatView.showProgress(false);
                }, () -> {
                    LogUtils.error(LOG_TAG, "on Complete test");
                    mChatView.onMessagesLoaded(msgList);
                    mChatView.showProgress(false);
                });
    }

    public void addMessage(long id) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        final Messages[] message = {new Messages()};
        mSubscription = mDatabaseHelper.findMessageById(id)
                .subscribe(messages -> {
                    LogUtils.error(LOG_TAG, "on Next add messages are not empty");
                    message[0] = messages;
                }, (e) -> {
                    LogUtils.error(LOG_TAG, "on Error add messages: " + e.getMessage());
                }, () -> {
                    LogUtils.error(LOG_TAG, "on Complede add messages are not empty");
                    mChatView.onMessageAdded(message[0]);
                });
    }

    public void createMessage(Messages message) {
        mSubscription = mDatabaseHelper.createMessage(message)
                .subscribeOn(Schedulers.io())
                .subscribe(id -> {
                    LogUtils.error(LOG_TAG, "onNex in service message creation");
                    EventBus.getDefault().post(new ChatMessageSent(id));
                }, (e) -> {
                    LogUtils.error(LOG_TAG, "onError on service message creation: " + e.getMessage());
                }, () -> {
                    LogUtils.error(LOG_TAG, "onComplete on service message creation");
                });
    }

    public void sendLocationMassage(long userId, String location) {
        Messages message = new Messages();
        message.message = location;
        message.dateTime = DateUtils.getDateTime();
        message.userId = userId;
        mSubscription = mDatabaseHelper.createMessage(message)
                .subscribeOn(Schedulers.io())
                .subscribe(id -> {
                    LogUtils.error(LOG_TAG, "onNex in service message creation");
                    EventBus.getDefault().post(new ChatMessageSent(id));
                }, (e) -> {
                    LogUtils.error(LOG_TAG, "onError on service message creation: " + e.getMessage());
                }, () -> {
                    LogUtils.error(LOG_TAG, "onComplete on service message creation");
                });
    }
}
