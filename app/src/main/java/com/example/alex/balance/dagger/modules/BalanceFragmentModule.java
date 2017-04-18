package com.example.alex.balance.dagger.modules;

import com.example.alex.balance.custom.realm.RealmHelper;
import com.example.alex.balance.dagger.presenters.BalancePresenter;
import com.example.alex.balance.views.BalanceFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class BalanceFragmentModule {
    private BalanceFragment mView;

    public BalanceFragmentModule(BalanceFragment fragment) {
        this.mView = fragment;
    }

    @Provides
    BalancePresenter providePresenter(RealmHelper helper) {
        return new BalancePresenter(mView, helper);
    }

    @Provides
    BalanceFragment provideView() {
        return mView;
    }
}
