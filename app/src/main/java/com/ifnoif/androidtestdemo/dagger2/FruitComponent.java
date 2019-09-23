package com.ifnoif.androidtestdemo.dagger2;

import javax.annotation.ParametersAreNonnullByDefault;
import javax.inject.Singleton;

import dagger.Component;

@MyScope
@Component(modules = FruitModule.class)
public interface FruitComponent {
    Apple getApple();
    void injectDaggerActivity(DaggerActivity daggerActivity);
}
