package com.acukanov.hivet.injection.components;

import com.acukanov.hivet.ui.main.MainActivity;
import com.acukanov.hivet.injection.annotations.PerActivity;
import com.acukanov.hivet.injection.modules.ActivityModule;
import com.acukanov.hivet.ui.start.StartActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = {ApplicationComponent.class}, modules = {ActivityModule.class})
public interface ActivityComponent {
    // Inject Activities
    void inject(MainActivity mainActivity);
    void inject(StartActivity startActivity);

    // Inject Fragments
    // ...
}
