package com.acukanov.hivet.ui.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.acukanov.hivet.R;
import com.acukanov.hivet.data.database.model.Users;
import com.acukanov.hivet.data.preference.UserPreferenceManager;
import com.acukanov.hivet.events.StopMessageService;
import com.acukanov.hivet.ui.base.BaseActivity;
import com.acukanov.hivet.ui.chat.ChatFragment;
import com.acukanov.hivet.ui.common.ActivityCommon;
import com.acukanov.hivet.ui.settings.SettingsFragment;
import com.acukanov.hivet.ui.start.StartActivity;
import com.acukanov.hivet.utils.LogUtils;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements IMainView, View.OnClickListener {
    private static final String LOG_TAG = LogUtils.makeLogTag(MainActivity.class);
    private static final String EXTRA_USER_ID = "extra_user_id";
    private static final int REQUEST_TAKE_PHOTO = 0;
    @Inject MainPresenter mMainPresenter;
    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.navigation_view) NavigationView mNavigationView;
    @InjectView(R.id.drawer) DrawerLayout mDrawerLayout;
    private CircleImageView mProfileImage;
    private TextView mUserName;
    private ImageButton mTakeAvatarButton;
    private View mHeaderView;
    private long mUserId;
    private String mTakenPhotoPath = null;

    public static void startActivity(Activity activity, long userId) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityComponent().inject(this);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        mMainPresenter.attachView(this);

        setSupportActionBar(mToolbar);
        ActivityCommon.setHomeAsUp(this);

        mHeaderView = LayoutInflater.from(this).inflate(R.layout.partial_drawer_header, null);
        mNavigationView.addHeaderView(mHeaderView);
        mTakeAvatarButton = (ImageButton) mHeaderView.findViewById(R.id.btn_take_avatar);
        mTakeAvatarButton.setOnClickListener(this);
        mProfileImage = (CircleImageView) mHeaderView.findViewById(R.id.profile_image);
        mUserName = (TextView) mHeaderView.findViewById(R.id.username);

        Intent intent = getIntent();
        if (intent != null) {
            mUserId = intent.getLongExtra(EXTRA_USER_ID, 1);
        }
        mMainPresenter.setUpUserData(mUserId);
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, ChatFragment.newInstance(mUserId)).commit();

        mNavigationView.setNavigationItemSelectedListener((menuItem) -> {
            if (menuItem.isChecked()) {
                menuItem.setChecked(false);
            } else {
                menuItem.setChecked(true);
            }
            mDrawerLayout.closeDrawers();
            Fragment fragment = null;
            switch (menuItem.getItemId()){
                case R.id.menu_drawer_chat:
                    fragment = ChatFragment.newInstance(mUserId);
                    mMainPresenter.navigationItemSelected(fragment);
                    break;
                case R.id.menu_drawer_settings:
                    fragment = SettingsFragment.newInstance();
                    mMainPresenter.navigationItemSelected(fragment);
                    break;
                case R.id.menu_drawer_logout:
                    mMainPresenter.logOut();
                    break;
            }
            return true;
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                mToolbar,
                R.string.res_menu_drawer_open,
                R.string.res_menu_drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        mDrawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().postSticky(new StopMessageService());
        mMainPresenter.detachView();
    }

    @Override
    @Optional @OnClick({R.id.btn_take_avatar})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_avatar:
                mTakenPhotoPath = mMainPresenter.takePhoto(this, REQUEST_TAKE_PHOTO);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                mMainPresenter.saveUserAvatar(mUserId, mTakenPhotoPath);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /* MVP - View methods */

    @Override
    public void onNavigationItemSelected(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content, fragment).commit();
        }
    }

    @Override
    public void onLogOut() {
        UserPreferenceManager preferenceManager = new UserPreferenceManager(this);
        preferenceManager.clearUserData();
        StartActivity.startActivity(this);
    }

    @Override
    public void onSaveAvatar() {
        mMainPresenter.setUpUserData(mUserId);
    }

    @Override
    public void onSetUpUserData(Users user) {
        if (user != null) {
            if (user.userAvatar != null) {
                Glide.with(this)
                        .load(user.userAvatar)
                        .into(mProfileImage);
            }
            mUserName.setText(user.userName);
        }
    }
}
