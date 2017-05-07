package com.example.alex.balance.dagger.modules;

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
    DetailFragment providePresenter() {
        return this.mView;
    }
}
