package com.example.alex.balance.dagger.modules;

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
    BalanceFragment fragment() {
        return this.mView;
    }
}
