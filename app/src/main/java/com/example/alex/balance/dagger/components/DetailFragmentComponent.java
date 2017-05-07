package com.example.alex.balance.dagger.components;

import com.example.alex.balance.dagger.modules.DetailFragmentModule;
import com.example.alex.balance.dagger.modules.RelamModule;
import com.example.alex.balance.views.DetailFragment;

import javax.inject.Singleton;

@Singleton
@dagger.Component(modules = {RelamModule.class, DetailFragmentModule.class})
public interface DetailFragmentComponent {
    DetailFragment inject(DetailFragment fragment);
}
