package com.example.alex.balance;

import android.app.Application;

import io.realm.Realm;

/**
 * Created by alex on 10.02.17.
 */

public class MyApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

    }
}
