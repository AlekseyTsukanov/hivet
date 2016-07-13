package com.acukanov.hivet.data.preference;

import android.content.Context;
import android.content.SharedPreferences;

import com.acukanov.hivet.injection.annotations.ApplicationContext;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserPreferenceManager {
    public static final String PREFERENCE_USER_FILE_NAME = "preference_user_file_name";
    private static final String PREFERENCE_LOGGED_IN_USER_ID = "logged_in_user_id";
    private SharedPreferences mPreference;

    @Inject
    public UserPreferenceManager(@ApplicationContext Context context) {
        mPreference = context.getSharedPreferences(PREFERENCE_USER_FILE_NAME, Context.MODE_PRIVATE);
    }

    // Called on LogOut
    public void clearUserData() {
        mPreference.edit().clear().apply();
    }

    public void saveLoggedInUserId(long userId) {
        mPreference.edit().putLong(PREFERENCE_LOGGED_IN_USER_ID, userId).apply();
    }

    public long getLoggedInUserId() {
        return mPreference.getLong(PREFERENCE_LOGGED_IN_USER_ID, 0);
    }
}
