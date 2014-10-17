package com.example.freestorm.tinysouandroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.freestorm.mymodule.tinysouAndroid.mymodule.app2.TinySouSearchActivity;


public class searchActivity extends TinySouSearchActivity {

    private Data app;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = (Data)getApplication();
        System.out.println("search "+app.getAC());
        this.isAutoCom = app.getAC();
    }
}
