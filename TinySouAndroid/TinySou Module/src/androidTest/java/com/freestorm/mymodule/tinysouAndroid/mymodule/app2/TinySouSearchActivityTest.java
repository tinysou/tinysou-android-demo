package com.freestorm.mymodule.tinysouAndroid.mymodule.app2;

import android.content.Intent;
import android.test.ActivityUnitTestCase;

/**
 * Created by freestorm on 14-10-15.
 */
public class TinySouSearchActivityTest extends ActivityUnitTestCase<TinySouSearchActivity> {

    private Intent tinySouSearchIntent;

    public TinySouSearchActivityTest(){
        super(TinySouSearchActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        //Create an intent to launch target Activity
        tinySouSearchIntent = new Intent(getInstrumentation().getTargetContext(),
                TinySouSearchActivity.class);
    }

    public void testPreconditions() {
        startActivity(tinySouSearchIntent, null, null);
        assertNotNull("tinySouSearchActivity is not null", getActivity());
    }
}
