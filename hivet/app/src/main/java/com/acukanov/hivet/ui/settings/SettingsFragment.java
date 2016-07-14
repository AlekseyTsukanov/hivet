package com.acukanov.hivet.ui.settings;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import com.acukanov.hivet.R;
import com.acukanov.hivet.data.preference.SettingsPreferenceManager;
import com.acukanov.hivet.ui.base.BaseActivity;
import com.acukanov.hivet.ui.base.BaseFragment;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingsFragment extends BaseFragment implements ISettingsView, TextWatcher {
    private static final String LOG_TAG = LogUtils.makeLogTag(SettingsFragment.class);
    private Activity mActivity;
    @Inject SettingsPresenter mSettingsPresenter;
    @InjectView(R.id.text_settings_frequency) EditText mMessagesFrequencyText;
    @InjectView(R.id.switch_notifications) Switch mNotificationsSwitch;
    @InjectView(R.id.switch_sound) Switch mSoundSwitch;
    private SettingsPreferenceManager mPreferenceManager;
    private boolean notificationFlag = true;
    private boolean soundFlag = false;

    public SettingsFragment() {

    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((BaseActivity) mActivity).activityComponent().inject(this);
        mPreferenceManager = new SettingsPreferenceManager(mActivity);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, rootView);
        mSettingsPresenter.attachView(this);
        mSettingsPresenter.initValues();
        mNotificationsSwitch.setOnCheckedChangeListener((compoundBtn, isChecked) -> {
            notificationFlag = isChecked;
            mSettingsPresenter.saveNotificationsSettings(notificationFlag);
        });
        mSoundSwitch.setOnCheckedChangeListener((compoundBtn, isChecked) -> {
            soundFlag = isChecked;
            mSettingsPresenter.saveSoundSettings(soundFlag);
        });
        mMessagesFrequencyText.addTextChangedListener(this);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSettingsPresenter.detachView();
    }

    // TODO: Change realisation to RxBind
    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (!editable.toString().equals("")) {
            mSettingsPresenter.saveFrequencySettings(Integer.parseInt(editable.toString()));
        }
    }

    @Override
    public void onFrequencyInit(int frequency) {
        mMessagesFrequencyText.setText(String.valueOf(frequency));
    }

    @Override
    public void onNotificationsInit(boolean flag) {
        mNotificationsSwitch.setChecked(flag);
    }

    @Override
    public void onSoundSwitchInit(boolean flag) {
        mSoundSwitch.setChecked(flag);
    }
}
