package com.acukanov.hivet.ui.base;


public interface IPresenter<V extends IView> {
    void attachView(V IView);
    void detachView();
}
