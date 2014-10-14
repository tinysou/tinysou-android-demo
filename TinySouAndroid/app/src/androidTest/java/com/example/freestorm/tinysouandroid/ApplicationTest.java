package com.example.freestorm.tinysouandroid;

import android.app.Application;
import android.test.ApplicationTestCase;

import Help.HttpHelp;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void test() throws Exception{
        HttpHelp httpHelp = new HttpHelp();
        urlTest(httpHelp);
        connectedTimeoutTest(httpHelp);
    }

    /*
   httlHelp测试
    */

    public void urlTest(HttpHelp httpHelp) {
        httpHelp.setUrl("http://www.google.com");
        assertEquals("http://www.google.com", httpHelp.getUrl());
    }

    public void connectedTimeoutTest(HttpHelp httpHelp) {
        httpHelp.setConnectedTimeout(1000);
        assertEquals(1000, httpHelp.getConnectedTimeout());
    }

}