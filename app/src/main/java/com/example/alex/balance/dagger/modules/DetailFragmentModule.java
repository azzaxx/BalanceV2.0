package com.example.alex.balance.dagger.modules;

import com.example.alex.balance.custom.realm.RealmHelper;
import com.example.alex.balance.dagger.presenters.DetailFragmentPresenter;
import com.example.alex.balance.views.DetailFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class DetailFragmentModule {
    private DetailFragment mView;

    public DetailFragmentModule(DetailFragment fragment) {
        this.mView = fragment;
    }

    @Provides
    DetailFragmentPresenter providePresenter(RealmHelper helper) {
        return new DetailFragmentPresenter(mView, helper);
    }

    @Provides
    DetailFragment provideView() {
        return mView;
    }
}
