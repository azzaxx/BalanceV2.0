package com.example.alex.balance.dagger.presenters;

public class BasePresenter<View> {
    protected View mView;

    public void bindView(View fragment) {
        this.mView = fragment;
    }
}
