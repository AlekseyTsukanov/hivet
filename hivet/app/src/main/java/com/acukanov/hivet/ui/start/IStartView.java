package com.acukanov.hivet.ui.start;


import android.content.Context;

import com.acukanov.hivet.injection.annotations.ActivityContext;
import com.acukanov.hivet.ui.base.IView;

public interface IStartView extends IView {
    void onOpenMainActivity(@ActivityContext Context context);
    void onNewUserCreated();
}
