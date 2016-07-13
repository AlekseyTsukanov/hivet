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
import android.widget.TextView;

import com.acukanov.hivet.R;
import com.acukanov.hivet.data.preference.UserPreferenceManager;
import com.acukanov.hivet.ui.base.BaseActivity;
import com.acukanov.hivet.ui.chat.ChatFragment;
import com.acukanov.hivet.ui.common.ActivityCommon;
import com.acukanov.hivet.ui.start.StartActivity;
import com.acukanov.hivet.utils.LogUtils;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.Optional;
import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends BaseActivity implements IMainView {
    private static final String LOG_TAG = LogUtils.makeLogTag(MainActivity.class);
    private static final String EXTRA_USER_ID = "extra_user_id";
    @Inject MainPresenter mMainPresenter;
    @InjectView(R.id.toolbar) Toolbar mToolbar;
    @InjectView(R.id.navigation_view) NavigationView mNavigationView;
    @InjectView(R.id.drawer) DrawerLayout mDrawerLayout;
    @Optional @InjectView(R.id.profile_image) CircleImageView profileImage;
    @Optional @InjectView(R.id.username) TextView userName;
    private View mHeaderView;

    public static void startActivity(Activity activity, long userId) {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.putExtra(EXTRA_USER_ID, userId);
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

        Intent intent = getIntent();
        long userId = 0;
        if (intent != null) {
            userId = intent.getLongExtra(EXTRA_USER_ID, 1);
        }
        //getSupportFragmentManager().beginTransaction().replace(R.id.main_content, ChatFragment.newInstance(userId)).commit();
        getSupportFragmentManager().beginTransaction().replace(R.id.main_content, ChatFragment.newInstance(userId)).commit();

        long finalUserId = userId;
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
                    //fragment = ChatFragment.newInstance(finalUserId);
                    fragment = ChatFragment.newInstance(finalUserId);
                    mMainPresenter.navigationItemSelected(fragment);
                    break;
                case R.id.menu_drawer_settings:
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
        mMainPresenter.detachView();
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
}
