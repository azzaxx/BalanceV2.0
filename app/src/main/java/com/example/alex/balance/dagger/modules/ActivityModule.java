package com.example.alex.balance.dagger.modules;

import com.example.alex.balance.custom.realm.RealmHelper;
import com.example.alex.balance.dagger.presenters.StartActivityPresenter;
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
    StartActivityPresenter providePresenter(RealmHelper helper) {
        return new StartActivityPresenter(mView, helper);
    }

    @Provides
    StartActivity provideView() {
        return this.mView;
    }
}
