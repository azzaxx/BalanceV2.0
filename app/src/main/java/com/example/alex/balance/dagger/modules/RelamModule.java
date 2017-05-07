package com.example.alex.balance.dagger.modules;

import com.example.alex.balance.custom.realm.RealmHelper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class RelamModule {

    @Singleton
    @Provides
    RealmHelper realmHelper() {
        return new RealmHelper();
    }
}
