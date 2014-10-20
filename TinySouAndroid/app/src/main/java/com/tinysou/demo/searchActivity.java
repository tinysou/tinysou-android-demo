package com.tinysou.demo;

import android.os.Bundle;

import com.freestorm.mymodule.tinysouAndroid.mymodule.module.TinySouSearchActivity;


public class searchActivity extends TinySouSearchActivity {

    private Data app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Data)getApplication();
        this.isAutoCom = app.getAC();
    }
}
