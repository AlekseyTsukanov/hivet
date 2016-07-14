package com.acukanov.hivet.ui.settings;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Switch;

import com.acukanov.hivet.R;
import com.acukanov.hivet.ui.base.BaseActivity;
import com.acukanov.hivet.ui.base.BaseFragment;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SettingsFragment extends BaseFragment implements ISettingsView {
    private static final String LOG_TAG = LogUtils.makeLogTag(SettingsFragment.class);
    private Activity mActivity;
    @Inject SettingsPresenter mSettingsPresenter;
    @InjectView(R.id.text_settings_frequency) EditText mMessagesFrequencyText;
    @InjectView(R.id.switch_notifications) Switch mNotificationsSwitch;
    @InjectView(R.id.switch_sound) Switch mSoundSwitch;

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
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_settings, container, false);
        ButterKnife.inject(this, rootView);
        mSettingsPresenter.attachView(this );

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
}
