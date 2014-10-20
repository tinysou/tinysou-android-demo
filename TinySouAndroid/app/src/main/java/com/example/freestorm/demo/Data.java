package com.example.freestorm.demo;

import android.app.Application;

/**
 * Created by freestorm on 14-10-17.
 */
public class Data extends Application{
    private boolean isAC = true;  //是否开启自动补全

    public void setAC(boolean isAC){
        this.isAC = isAC;
    }

    public boolean getAC(){
        return isAC;
    }

    @Override
    public void onCreate() {
        super.onCreate();  //初始化全局变量
        setAC(true);
    }
}
