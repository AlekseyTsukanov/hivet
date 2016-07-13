package com.acukanov.hivet;


import android.app.Application;
import android.content.Context;

import com.acukanov.hivet.data.database.DatabaseHelper;
import com.acukanov.hivet.injection.components.ApplicationComponent;
import com.acukanov.hivet.injection.components.DaggerApplicationComponent;
import com.acukanov.hivet.injection.modules.ApplicationModule;

import javax.inject.Inject;

public class HivetApplication extends Application {
    @Inject
    DatabaseHelper databaseHelper;

    private ApplicationComponent mApplicationComponent;

    public static HivetApplication get(Context context) {
        return (HivetApplication) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();

        mApplicationComponent.inject(this);
    }

    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
