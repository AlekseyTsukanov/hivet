package com.acukanov.hivet.injection.components;

import android.app.Application;
import android.content.Context;

import com.acukanov.hivet.HivetApplication;
import com.acukanov.hivet.data.DatabaseHelper;
import com.acukanov.hivet.injection.annotations.ApplicationContext;
import com.acukanov.hivet.injection.modules.ApplicationModule;
import com.acukanov.hivet.service.GpsStateReceiver;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(HivetApplication hivetApplication);
    void inject(GpsStateReceiver gpsStateReceiver);

    @ApplicationContext
    Context context();
    Application application();
    DatabaseHelper databaseHelper();
}
