package com.example.freestorm.tinysouandroid;

import android.app.Application;
import android.test.ApplicationTestCase;

import org.apache.http.protocol.HTTP;

import Help.HttpHelp;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    public void runTest() throws Exception{
        HttpHelp httpHelp = new HttpHelp();
        urlTest(httpHelp);
        connectedTimeoutTest(httpHelp);
        soTimeoutTest(httpHelp);
        charsetTest(httpHelp);
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

    public void soTimeoutTest(HttpHelp httpHelp) {
        httpHelp.setSoTimeout(1000);
        assertEquals(1000, httpHelp.getSoTimeout());
    }

    public void charsetTest(HttpHelp httpHelp){
        httpHelp.setCharset(HTTP.UTF_8);
        assertEquals(HTTP.UTF_8, httpHelp.getCharset());
    }



}