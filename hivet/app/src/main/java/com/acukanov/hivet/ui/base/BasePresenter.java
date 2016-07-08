package com.acukanov.hivet.ui.base;


public class BasePresenter<T extends IView> implements IPresenter<T> {
    private T mView;

    @Override
    public void attachView(T IView) {
        mView = IView;
    }

    @Override
    public void detachView() {
        mView = null;
    }

    public boolean isViewAttached() {
        return mView != null;
    }

    public T getView() {
        return mView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new ViewNotAttachedException();
    }

    public static class ViewNotAttachedException extends RuntimeException {
        public ViewNotAttachedException() {
            super("Please, call Presenter.attachView(IView)" +
                    "before requesting data to the presenter");
        }
    }
}
