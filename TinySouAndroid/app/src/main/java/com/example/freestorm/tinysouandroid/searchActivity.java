package com.example.freestorm.tinysouandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.freestorm.mymodule.tinysouAndroid.mymodule.app2.TinySouSearchActivity;


public class searchActivity extends TinySouSearchActivity {

    //设置你的engine key
    protected String my_engine_token = "0b732cc0ea3c11874190";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.engine_token = my_engine_token;
    }
}
