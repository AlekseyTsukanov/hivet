package com.acukanov.hivet.ui.chat;


import com.acukanov.hivet.data.DatabaseHelper;
import com.acukanov.hivet.data.model.Messages;
import com.acukanov.hivet.ui.base.IPresenter;
import com.acukanov.hivet.utils.LogUtils;

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

   /* public ArrayList<Users> getUsersData() {
        return mDatabaseHelper.findUsersData();
    }*/

    public void loadUsersAndMessagesData() {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = getUsersAndMessagesData()
                .subscribe(new Subscriber<ArrayList<Messages>>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.debug(LOG_TAG, "onCompleted loading users data");
                        mChatView.showProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mChatView.showProgress(false);
                        LogUtils.error(LOG_TAG, "Error on loading users data: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Messages> messages) {
                        LogUtils.debug(LOG_TAG, "onNext start point");
                        if (!messages.isEmpty()) {
                            LogUtils.debug(LOG_TAG, "onNext users data");
                            mChatView.showChatMessages(messages);
                            // show data: mChatView.showChatMessages
                        } else {
                            LogUtils.debug(LOG_TAG, "onNext empty users data");
                            //show empty message - change textview visibility: mChatView.showEmptyMessage
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
                        LogUtils.debug(LOG_TAG, "onCompleted loading users data");
                        mChatView.showProgress(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO: hide progress bar
                        mChatView.showProgress(false);
                        LogUtils.error(LOG_TAG, "Error on loading users data: " + e.getMessage());
                    }

                    @Override
                    public void onNext(ArrayList<Users> users) {
                        LogUtils.debug(LOG_TAG, "onNext start point");
                        if (!users.isEmpty()) {
                            LogUtils.debug(LOG_TAG, "onNext users data");
                            mChatView.showChatUsers(users);
                            // show data: mChatView.showChatMessages
                        } else {
                            LogUtils.debug(LOG_TAG, "onNext empty users data");
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
        return mDatabaseHelper.findUserAndMessageData()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io());
    }
}
