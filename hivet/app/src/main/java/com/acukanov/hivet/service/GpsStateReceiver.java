package com.acukanov.hivet.service;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.acukanov.hivet.HivetApplication;
import com.acukanov.hivet.events.GpsStateChanged;

import org.greenrobot.eventbus.EventBus;

public class GpsStateReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        HivetApplication.get(context).getApplicationComponent().inject(this);
        if (intent.getAction().matches("android.location.PROVIDERS_CHANGED")) {
            EventBus.getDefault().post(new GpsStateChanged());
        }
    }


}
