package com.acukanov.hivet.injection.components;

import android.app.Application;
import android.content.Context;

import com.acukanov.hivet.HivetApplication;
import com.acukanov.hivet.data.database.DatabaseHelper;
import com.acukanov.hivet.data.preference.UserPreferenceManager;
import com.acukanov.hivet.injection.annotations.ApplicationContext;
import com.acukanov.hivet.injection.modules.ApplicationModule;
import com.acukanov.hivet.service.BotMessageService;
import com.acukanov.hivet.service.GpsStateReceiver;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(HivetApplication hivetApplication);
    void inject(GpsStateReceiver gpsStateReceiver);
    void inject(BotMessageService botMessageService);

    @ApplicationContext
    Context context();
    Application application();
    DatabaseHelper databaseHelper();
    UserPreferenceManager preferenceManager();
}
