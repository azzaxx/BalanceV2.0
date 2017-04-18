package com.example.alex.balance.dagger.presenters;

/**
 * Created by alex on 09.01.17.
 */

public class BasePresenter<View> {
    protected View mView;

    public void bindView(View fragment) {
        this.mView = fragment;
    }
}
