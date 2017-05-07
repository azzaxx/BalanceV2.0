package com.example.alex.balance.dagger.modules;

import com.example.alex.balance.views.StartActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {
    private StartActivity mView;

    public ActivityModule(StartActivity view) {
        this.mView = view;
    }

    @Provides
    StartActivity providePresenter() {
        return mView;
    }
}
