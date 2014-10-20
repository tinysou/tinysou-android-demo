package com.tinysou.module;

import android.content.Intent;
import android.test.ActivityUnitTestCase;
import android.test.suitebuilder.annotation.MediumTest;

import com.tinysou.module.TinySouSearchActivity;

import org.apache.http.protocol.HTTP;

import Help.HttpHelp;

/**
 * Created by tinysou on 14-10-15.
 */
public class TinySouSearchActivityTest extends ActivityUnitTestCase<TinySouSearchActivity> {

    private Intent tinySouSearchIntent;

    private HttpHelp httpHelp = new HttpHelp();

    public TinySouSearchActivityTest() {
        super(TinySouSearchActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //Create an intent to launch target Activity
        tinySouSearchIntent = new Intent(getInstrumentation().getTargetContext(),
                TinySouSearchActivity.class);
    }

    //测试加载资源预条件
    @MediumTest
    public void testPreconditions() {
        startActivity(tinySouSearchIntent, null, null);
        assertNotNull("tinySouSearchActivity is not null", getActivity());
    }

    /*
   httlHelp测试
    */
    public void testUrl(){
        httpHelp.setUrl("http://api.tinysou.com/v1/public/search");
        assertEquals("http://api.tinysou.com/v1/public/search", httpHelp.getUrl());
    }

    public void testConnectedTimeout() {
        httpHelp.setConnectedTimeout(1000);
        assertEquals(1000, httpHelp.getConnectedTimeout());
    }

    public void testSoTimeoutTest() {
        httpHelp.setSoTimeout(1000);
        assertEquals(1000, httpHelp.getSoTimeout());
    }

    public void testCharset() {
        httpHelp.setCharset(HTTP.UTF_8);
        assertEquals(HTTP.UTF_8, httpHelp.getCharset());
    }


    public void testFirstHeader() {
        httpHelp.addHeader("Content-Type", "application/json");
        assertEquals(httpHelp.getFirstHeader("Content-Type").getValue(), "application/json");
    }

    public void testAllHeader(){
        httpHelp.addHeader("Content-Type", "application/json");
        assertEquals(1, httpHelp.getAllHeader().length);
        assertEquals(httpHelp.getAllHeader()[0].getValue(), "application/json");
    }

    public void testRequestType(){
        httpHelp.setRequestType("get");
        assertEquals("GET", httpHelp.getRequestType());
        assertTrue(httpHelp.isGet());
        httpHelp.setRequestType("post");
        assertEquals("POST", httpHelp.getRequestType());
        assertTrue(httpHelp.isPost());
        httpHelp.setRequestType("PUt");
        assertEquals("PUT", httpHelp.getRequestType());
        assertTrue(httpHelp.isPut());
        httpHelp.setRequestType("DELETE");
        assertEquals("DELETE", httpHelp.getRequestType());
        assertTrue(httpHelp.isDelete());
    }


}
