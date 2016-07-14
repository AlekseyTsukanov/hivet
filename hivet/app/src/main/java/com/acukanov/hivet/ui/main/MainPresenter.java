package com.acukanov.hivet.ui.main;


import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.acukanov.hivet.data.database.DatabaseHelper;
import com.acukanov.hivet.data.database.model.Users;
import com.acukanov.hivet.ui.base.IPresenter;
import com.acukanov.hivet.utils.LogUtils;

import java.io.File;
import java.io.IOException;

import javax.inject.Inject;

import rx.Subscription;

public class MainPresenter implements IPresenter<IMainView> {
    private static final String LOG_TAG = LogUtils.makeLogTag(MainPresenter.class);
    private IMainView mMainView;
    private DatabaseHelper mDatabaseHelper;
    private Subscription mSubscription;
    private Users mUserData;


    @Inject MainPresenter(DatabaseHelper databaseHelper) {
        mDatabaseHelper = databaseHelper;
    }

    @Override
    public void attachView(IMainView IView) {
        mMainView = IView;
    }

    @Override
    public void detachView() {
        mMainView = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }

    public void navigationItemSelected(Fragment fragment) {
        mMainView.onNavigationItemSelected(fragment);
    }

    public void logOut() {
        mMainView.onLogOut();
    }

    public String takePhoto(Activity activity, int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File file = null;
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            try {
                file = File.createTempFile(
                        System.currentTimeMillis() + "_",
                        ".jpg",
                        activity.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                );
                Uri photoUri = FileProvider.getUriForFile(
                        activity,
                        activity.getApplicationContext().getPackageName() + ".fileprovider",
                        file
                );
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            } catch (IOException e) {
                Toast.makeText(activity, "The error has been occured while photo creating", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            activity.startActivityForResult(takePictureIntent, requestCode);
        }
        return "file:" + file.getAbsolutePath();
    }

    public void saveUserAvatar(long userId, String userAvatar) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = mDatabaseHelper.updateUserAvatarByUserId(userId, userAvatar)
                .subscribe(aVoid -> {
                    LogUtils.error(LOG_TAG, "onNex on avatar updating");
                }, e -> {
                    LogUtils.error(LOG_TAG, "onError on avatar updating: " + e.getMessage());
                }, () -> {
                    LogUtils.error(LOG_TAG, "onComplete on avatar updating");
                    mMainView.onSaveAvatar();
                });
    }

    public void setUpUserData(long userId) {
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
        mSubscription = mDatabaseHelper.findUserById(userId)
                .subscribe(users -> {
                    LogUtils.error(LOG_TAG, "onNext on getting user data");
                    mUserData = users;
                }, e -> {
                    LogUtils.error(LOG_TAG, "onError on getting user data: " + e.getMessage());
                }, () -> {
                    LogUtils.error(LOG_TAG, "onComplete on getting user data");
                    mMainView.onSetUpUserData(mUserData);
                });
    }
}
