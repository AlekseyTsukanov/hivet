package com.acukanov.hivet.ui.chat;


import com.acukanov.hivet.data.database.DatabaseHelper;
import com.acukanov.hivet.data.database.model.Messages;
import com.acukanov.hivet.events.ChatMessageSended;
import com.acukanov.hivet.ui.base.IPresenter;
import com.acukanov.hivet.utils.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ChatPresenter implements IPresenter<IChatView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(ChatPresenter.class);
    private IChatView mChatView;
    private DatabaseHelper mDatabaseHelper;
    private Subscription mSubscription;

    @Inject ChatPresenter(DatabaseHelper databaseHelper) {
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

    public void loadUsersAndMessagesData() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mChatView.showProgress(true);
        mSubscription = getUsersAndMessagesData()
                .subscribe(new Subscriber<ArrayList<Messages>>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.debug(LOG_TAG, "onCompleted loading user data");
                        mChatView.showProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mChatView.showProgress(false);
                        LogUtils.error(LOG_TAG, "Error on loading user data: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Messages> messages) {
                        LogUtils.debug(LOG_TAG, "onNext start point");
                        if (!messages.isEmpty()) {
                            LogUtils.debug(LOG_TAG, "onNext user data");
                            mChatView.showChatMessages(messages);
                        } else {
                            LogUtils.debug(LOG_TAG, "onNext empty user data");
                            mChatView.showEmptyMessage();
                        }
                    }
                });
    }

    /*public void loadUsersDataTest() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = getUsersDataTest()
                .subscribe(new Subscriber<ArrayList<Users>>() {
                    @Override
                    public void onCompleted() {
                        // TODO: hide progress bar
                        LogUtils.debug(LOG_TAG, "onCompleted loading user_id data");
                        mChatView.showProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO: hide progress bar
                        mChatView.showProgress(false);
                        LogUtils.error(LOG_TAG, "Error on loading user_id data: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Users> user_id) {
                        LogUtils.debug(LOG_TAG, "onNext start point");
                        if (!user_id.isEmpty()) {
                            LogUtils.debug(LOG_TAG, "onNext user_id data");
                            mChatView.showChatUsers(user_id);
                            // show data: mChatView.showChatMessages
                        } else {
                            LogUtils.debug(LOG_TAG, "onNext empty user_id data");
                            //show empty message - change textview visibility: mChatView.showEmptyMessage
                        }
                    }
                });
    }*/

    /*private Observable<ArrayList<Users>> getUsersDataTest() {
        return mDatabaseHelper.findUsersDataTest()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }*/

    private Observable<ArrayList<Messages>> getUsersAndMessagesData() {
        return mDatabaseHelper.findAllMessages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }

    public void sendMessage(Messages message) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = mDatabaseHelper.createMessage(message)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Messages>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.error(LOG_TAG, "onCompleted message sending");
                        EventBus.getDefault().post(new ChatMessageSended());
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.error(LOG_TAG, "onError sending: " + e.getMessage());
                    }

                    @Override
                    public void onNext(Messages aMessage) {

                    }
                });
    }
}
