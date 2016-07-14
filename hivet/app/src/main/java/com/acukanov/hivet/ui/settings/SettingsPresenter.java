package com.acukanov.hivet.ui.settings;


import com.acukanov.hivet.data.database.DatabaseHelper;
import com.acukanov.hivet.data.preference.SettingsPreferenceManager;
import com.acukanov.hivet.ui.base.IPresenter;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

import rx.Subscription;

public class SettingsPresenter implements IPresenter<ISettingsView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(SettingsPresenter.class);
    private ISettingsView mSettingsView;
    private SettingsPreferenceManager mPreferenceManager;
    private DatabaseHelper mDatabaseHelper;
    private Subscription mSubscription;

    @Inject
    SettingsPresenter(DatabaseHelper databaseHelper, SettingsPreferenceManager preferenceManager) {
        mDatabaseHelper = databaseHelper;
        mPreferenceManager = preferenceManager;
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

    public void initValues() {
        mSettingsView.onFrequencyInit(mPreferenceManager.getFrequencyForView());
        mSettingsView.onNotificationsInit(mPreferenceManager.getNotificationsValue());
        mSettingsView.onSoundSwitchInit(mPreferenceManager.getSoundValue());
    }

    public void saveSettings(int frequency, boolean notifications, boolean sound) {
        mPreferenceManager.saveFrequencyValue(frequency);
        mPreferenceManager.saveNotificationsValue(notifications);
        mPreferenceManager.saveSoundValue(sound);
    }

    public void saveNotificationsSettings(boolean notifications) {
        mPreferenceManager.saveNotificationsValue(notifications);
    }

    public void saveSoundSettings(boolean sound) {
        mPreferenceManager.saveSoundValue(sound);
    }

    public void saveFrequencySettings(int frequency) {
        mPreferenceManager.saveFrequencyValue(frequency);
    }
}
