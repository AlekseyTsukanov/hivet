package com.acukanov.hivet.service;


import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.acukanov.hivet.HivetApplication;
import com.acukanov.hivet.data.database.DatabaseHelper;
import com.acukanov.hivet.data.database.model.Messages;
import com.acukanov.hivet.data.preference.SettingsPreferenceManager;
import com.acukanov.hivet.events.ChatMessageSent;
import com.acukanov.hivet.events.StopMessageService;
import com.acukanov.hivet.utils.DateUtils;
import com.acukanov.hivet.utils.LogUtils;
import com.acukanov.hivet.utils.NotificationsUtils;
import com.acukanov.hivet.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscription;
import rx.schedulers.Schedulers;

@Singleton
public class BotMessageService extends Service {
    private static final String LOG_TAG = LogUtils.makeLogTag(BotMessageService.class);
    private static final int MESSAGE_SERVICE_ID = 100;
    private SettingsPreferenceManager mPreferences = null;
    @Inject DatabaseHelper mDatabaseHelper;
    private Subscription mSubscription;
    private Handler mHandler = new Handler();
    private static Runnable sRunnable = null;
    private Messages mMessages = null;

    public static Intent getStartIntent(Context context) {
        return new Intent(context, BotMessageService.class);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        HivetApplication.get(this).getApplicationComponent().inject(this);
        EventBus.getDefault().register(this);
        mPreferences = new SettingsPreferenceManager(this);
        sRunnable = new Runnable() {
            @Override
            public void run() {
                if (mSubscription != null && !mSubscription.isUnsubscribed()) {
                    mSubscription.unsubscribe();
                }
                mMessages = new Messages();
                mMessages.message = StringUtils.generateRandomString();
                mMessages.dateTime = DateUtils.getDateTime();
                mMessages.userId = 1;
                String textMessage = mMessages.message;
                mSubscription = mDatabaseHelper.createMessage(mMessages)
                        .subscribeOn(Schedulers.io())
                        .subscribe(id -> {
                            LogUtils.error(LOG_TAG, "onNex in service message creation");
                            EventBus.getDefault().post(new ChatMessageSent(id));

                            if (mPreferences.getNotificationsValue()) {
                                NotificationsUtils.createSimpleNotification(
                                        getApplicationContext(),
                                        MESSAGE_SERVICE_ID,
                                        "Bot",
                                        textMessage);
                            }
                        }, (e) -> {
                            LogUtils.error(LOG_TAG, "onError on service message creation: " + e.getMessage());
                        }, () -> {
                            LogUtils.error(LOG_TAG, "onComplete on service message creation");
                        });
                mHandler.postDelayed(sRunnable, mPreferences.getFrequency());
            }
        };
        mHandler.post(sRunnable);
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND, sticky = true)
    public void onEvent(StopMessageService event) {
        mHandler.removeCallbacks(sRunnable);
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        stopSelf();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        mHandler.removeCallbacks(sRunnable);
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        stopSelf();
    }

    public static boolean isServiceRunning(Context context, Class aClass) {
        ActivityManager activityManager =
                (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (aClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
