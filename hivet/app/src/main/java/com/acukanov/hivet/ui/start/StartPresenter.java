package com.acukanov.hivet.ui.start;


import android.content.Context;

import com.acukanov.hivet.data.DatabaseHelper;
import com.acukanov.hivet.data.model.Users;
import com.acukanov.hivet.injection.annotations.ActivityContext;
import com.acukanov.hivet.ui.base.IPresenter;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StartPresenter implements IPresenter<IStartView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(StartPresenter.class);
    private IStartView mStartView;
    private DatabaseHelper mDatabaseHelper;
    private Subscription mSubscription;

    @Inject StartPresenter(DatabaseHelper databaseHelper) {
        mDatabaseHelper = databaseHelper;
    }

    @Override
    public void attachView(IStartView IView) {
        mStartView = IView;
    }

    @Override
    public void detachView() {
        mStartView = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void openMainActivity(@ActivityContext Context context) {
        mStartView.onOpenMainActivity(context);
    }

    public void createUser(Users users) {
        mSubscription = mDatabaseHelper.createUser(users)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.debug(LOG_TAG, "Completed new user creation");
                        mStartView.onNewUserCreated();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.error(LOG_TAG, "Error " + e.getMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        LogUtils.error(LOG_TAG, "OnNext");
                    }
                });
        mSubscription = mDatabaseHelper.findAllUsers()
                .subscribe();
    }

    /*public void createMessage(Users users, Messages messages) {
        mSubscription = mDatabaseHelper.createMessage(users, messages)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Subscriber<Void>() {
                    @Override
                    public void onCompleted() {
                        LogUtils.debug(LOG_TAG, "Completed message creation");
                        mStartView.onNewUserCreated();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtils.error(LOG_TAG, "Error " + e.getMessage());
                    }

                    @Override
                    public void onNext(Void aVoid) {
                        LogUtils.error(LOG_TAG, "OnNext");
                    }
                });
        mSubscription = mDatabaseHelper.findAllMessages()
                .subscribe();
    }*/
}
