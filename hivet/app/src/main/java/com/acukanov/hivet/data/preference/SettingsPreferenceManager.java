package com.acukanov.hivet.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.acukanov.hivet.injection.annotations.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class SettingsPreferenceManager {
    private static final String PREFERENCE_SETTINGS_FILE_NAME = "preference_settings_file_name";
    private static final String PREFERENCE_FREQUENCY_MESSAGING = "preference_frequency_messaging";
    private static final String PREFERENCE_NOTIFICATIONS = "preference_notifications";
    private static final String PREFERENCE_SOUND = "preference_sound";
    private SharedPreferences mPreference;

    @Inject
    public SettingsPreferenceManager(@ApplicationContext Context context) {
        mPreference = context.getSharedPreferences(PREFERENCE_SETTINGS_FILE_NAME, Context.MODE_PRIVATE);
    }

    public void saveFrequencyValue(int frequency) {
        int n = frequency * 1000;
        mPreference.edit().putInt(PREFERENCE_FREQUENCY_MESSAGING, n).apply();
    }

    public void saveNotificationsValue(boolean flag) {
        mPreference.edit().putBoolean(PREFERENCE_NOTIFICATIONS, flag).apply();
    }

    public void saveSoundValue(boolean flag) {
        mPreference.edit().putBoolean(PREFERENCE_SOUND, flag).apply();
    }

    public int getFrequency() {
        return mPreference.getInt(PREFERENCE_FREQUENCY_MESSAGING, (10 * 1000));
    }


    public int getFrequencyForView() {
        int frequency = mPreference.getInt(PREFERENCE_FREQUENCY_MESSAGING, (10 * 1000));
        return (frequency / 1000);
    }

    public boolean getNotificationsValue() {
        return mPreference.getBoolean(PREFERENCE_NOTIFICATIONS, true);
    }

    public boolean getSoundValue() {
        return mPreference.getBoolean(PREFERENCE_SOUND, false);
    }

}
