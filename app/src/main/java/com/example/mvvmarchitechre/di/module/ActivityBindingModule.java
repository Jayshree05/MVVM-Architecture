package com.example.mvvmarchitechre.di.module;

import com.example.mvvmarchitechre.view.main.MainActivity;
import com.example.mvvmarchitechre.view.main.MainFragmentBindingModule;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract MainActivity bindMainActivity();


}
