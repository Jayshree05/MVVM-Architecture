package com.example.mvvmarchitechre.view.main;

import com.example.mvvmarchitechre.view.list.RepoListFragment;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;


@Module
public abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    abstract RepoListFragment provideListFragment();

}
