package com.example.alex.balance.dagger.components;

import com.example.alex.balance.custom.realm.RealmHelper;
import com.example.alex.balance.dagger.modules.BalanceFragmentModule;
import com.example.alex.balance.dagger.presenters.BalancePresenter;
import com.example.alex.balance.views.BalanceFragment;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {RealmHelper.class, BalanceFragmentModule.class})
public interface BalanceFragmentComponent {
    BalanceFragment inject(BalanceFragment fragment);

    BalancePresenter presenter();
}
