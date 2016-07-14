package com.acukanov.hivet.ui.settings;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.acukanov.hivet.R;
import com.acukanov.hivet.data.preference.SettingsPreferenceManager;
import com.acukanov.hivet.ui.base.BaseActivity;
import com.acukanov.hivet.ui.base.BaseFragment;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class SettingsFragment extends BaseFragment implements ISettingsView, View.OnClickListener {
    private static final String LOG_TAG = LogUtils.makeLogTag(SettingsFragment.class);
    private Activity mActivity;
    @Inject SettingsPresenter mSettingsPresenter;
    @InjectView(R.id.text_settings_frequency) EditText mMessagesFrequencyText;
    @InjectView(R.id.switch_notifications) Switch mNotificationsSwitch;
    @InjectView(R.id.switch_sound) Switch mSoundSwitch;
    @InjectView(R.id.btn_save) Button mSaveButton;
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
        mSaveButton.setOnClickListener(this);
        mNotificationsSwitch.setOnCheckedChangeListener((compoundBtn, isChecked) -> {
            notificationFlag = isChecked;
        });
        mSoundSwitch.setOnCheckedChangeListener((compoundBtn, isChecked) -> {
            soundFlag = isChecked;
        });
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

    @Override
    @OnClick({R.id.btn_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save:
                mSettingsPresenter.saveSettings(
                        Integer.parseInt(mMessagesFrequencyText.getText().toString()),
                        notificationFlag,
                        soundFlag
                );
                break;
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

    @Override
    public void onSettingsSaved() {
        Toast.makeText(mActivity, "Settings are saved", Toast.LENGTH_SHORT).show();
    }
}
