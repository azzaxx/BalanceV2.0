package com.example.alex.balance.dagger.components;

import com.example.alex.balance.custom.realm.RealmHelper;
import com.example.alex.balance.dagger.modules.DetailFragmentModule;
import com.example.alex.balance.views.DetailFragment;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {RealmHelper.class, DetailFragmentModule.class})
public interface DetailFragmentComponent {
    DetailFragment inject(DetailFragment fragment);
}
