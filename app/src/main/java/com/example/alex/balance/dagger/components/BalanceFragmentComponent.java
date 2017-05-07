package com.example.alex.balance.dagger.components;

import com.example.alex.balance.dagger.modules.BalanceFragmentModule;
import com.example.alex.balance.dagger.modules.RelamModule;
import com.example.alex.balance.views.BalanceFragment;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {RelamModule.class, BalanceFragmentModule.class})
public interface BalanceFragmentComponent {
    BalanceFragment inject(BalanceFragment fragment);
}
