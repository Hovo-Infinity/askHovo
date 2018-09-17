package com.example.hovhannesstepanyan.askhovo;

import android.app.Application;

import Core.MGPrefsCacheManager;

public class AskApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MGPrefsCacheManager.getInstance().Initialize(getApplicationContext());
    }
}
