package com.acukanov.hivet.ui.settings;


import com.acukanov.hivet.data.database.DatabaseHelper;
import com.acukanov.hivet.ui.base.IPresenter;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

import rx.Subscription;

public class SettingsPresenter implements IPresenter<ISettingsView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(SettingsPresenter.class);
    private ISettingsView mSettingsView;
    private DatabaseHelper mDatabaseHelper;
    private Subscription mSubscription;

    @Inject
    SettingsPresenter(DatabaseHelper databaseHelper) {
        mDatabaseHelper = databaseHelper;
    }

    @Override
    public void attachView(ISettingsView IView) {
        mSettingsView = IView;
    }

    @Override
    public void detachView() {
        mSettingsView = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
