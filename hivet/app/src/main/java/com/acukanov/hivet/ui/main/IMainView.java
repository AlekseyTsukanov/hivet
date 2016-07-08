package com.acukanov.hivet.ui.main;


import android.support.v4.app.Fragment;

import com.acukanov.hivet.ui.base.IView;

public interface IMainView extends IView {
    void onNavigationItemSelected(Fragment fragment);
}
