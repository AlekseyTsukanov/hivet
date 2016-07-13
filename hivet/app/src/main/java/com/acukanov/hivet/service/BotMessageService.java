package com.acukanov.hivet.service;


import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.acukanov.hivet.HivetApplication;
import com.acukanov.hivet.data.database.DatabaseHelper;
import com.acukanov.hivet.data.database.model.Messages;
import com.acukanov.hivet.events.ChatMessageSended;
import com.acukanov.hivet.utils.DateUtils;
import com.acukanov.hivet.utils.LogUtils;
import com.acukanov.hivet.utils.NotificationsUtils;
import com.acukanov.hivet.utils.StringUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import rx.Subscription;
import rx.schedulers.Schedulers;

public class BotMessageService extends Service {
    private static final String LOG_TAG = LogUtils.makeLogTag(BotMessageService.class);
    private static final long INTERVAL_MESSAGE_SENDING = 10 * 1000; // 10 sec
    private static final int MESSAGE_SERVICE_ID = 100;
    @Inject DatabaseHelper mDatabaseHelper;
    private Subscription mSubscription;
    private Handler mHandler = new Handler();
    private static Runnable mRunnable = null;
    private Timer mTimer = null;
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
        if (mTimer != null) {
            mTimer.cancel();
        } else {
            mTimer = new Timer();
        }
        mTimer.scheduleAtFixedRate(new MessageTimerTask(), 0, INTERVAL_MESSAGE_SENDING);
    }

    @Override
    public void onDestroy() {
        mHandler.removeCallbacks(mRunnable);
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        stopSelf();
    }

    private class MessageTimerTask extends TimerTask {

        @Override
        public void run() {
            mHandler.post(mRunnable = () -> {
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
                        .subscribe(messages -> {
                            LogUtils.error(LOG_TAG, "onNex in service message creation");
                        }, (e) -> {
                            LogUtils.error(LOG_TAG, "onError on service message creation: " + e.getMessage());
                        }, () -> {
                            LogUtils.error(LOG_TAG, "onComplete on service message creation");
                            EventBus.getDefault().post(new ChatMessageSended());
                            NotificationsUtils.createSimpleNotification(
                                    getApplicationContext(),
                                    MESSAGE_SERVICE_ID,
                                    "Bot",
                                    textMessage);
                        });
            });
        }
    }
}
