package com.acukanov.hivet.ui.settings;


import com.acukanov.hivet.ui.base.IView;

public interface ISettingsView extends IView {
    void onFrequencyInit(int frequency);
    void onNotificationsInit(boolean flag);
    void onSoundSwitchInit(boolean flag);
}
