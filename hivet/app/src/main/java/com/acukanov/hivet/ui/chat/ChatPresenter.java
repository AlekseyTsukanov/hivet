package com.acukanov.hivet.ui.chat;


import com.acukanov.hivet.data.DatabaseHelper;
import com.acukanov.hivet.ui.base.IPresenter;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

import rx.Subscription;

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
}
