package com.acukanov.hivet.injection.modules;

import android.app.Application;
import android.content.Context;

import com.acukanov.hivet.injection.annotations.ApplicationContext;

import dagger.Module;
import dagger.Provides;

@Module
public class ApplicationModule {
    private final Application mApplication;

    public ApplicationModule(Application application) {
        mApplication = application;
    }

    @Provides
    Application providesApplication() {
        return mApplication;
    }

    @Provides
    @ApplicationContext
    Context provideContext() {
        return mApplication;
    }
}
