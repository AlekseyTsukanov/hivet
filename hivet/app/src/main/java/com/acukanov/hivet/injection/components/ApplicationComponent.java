package com.acukanov.hivet.injection.components;

import android.app.Application;
import android.content.Context;

import com.acukanov.hivet.HivetApplication;
import com.acukanov.hivet.injection.annotations.ApplicationContext;
import com.acukanov.hivet.injection.modules.ApplicationModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {
    void inject(HivetApplication hivetApplication);

    @ApplicationContext
    Context context();
    Application application();
}
