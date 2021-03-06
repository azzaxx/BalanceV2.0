package com.example.alex.balance.dagger.components;

import com.example.alex.balance.dagger.modules.ActivityModule;
import com.example.alex.balance.dagger.modules.RelamModule;
import com.example.alex.balance.views.StartActivity;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {RelamModule.class, ActivityModule.class})
public interface StartActivityComponent {
    StartActivity inject(StartActivity view);
}
