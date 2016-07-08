package com.acukanov.hivet.ui.start;


import com.acukanov.hivet.ui.base.IPresenter;

import javax.inject.Inject;

public class StartPresenter implements IPresenter<IStartView> {
    private IStartView mStartView;

    @Inject StartPresenter() {}

    @Override
    public void attachView(IStartView IView) {
        mStartView = IView;
    }

    @Override
    public void detachView() {
        mStartView = null;
    }
}
