package com.example.freestorm.tinysouandroid;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class settingActivity extends Activity {
    private CheckBox cb1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cb1 = (CheckBox) findViewById(R.id.chech_box1);
        listViewInit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void listViewInit(){
        ListView listView = (ListView) findViewById(R.id.listview1);
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("TITLE", getString(R.string.setting1));
        map1.put("CHECKBOX", cb1);
        listItem.add(map1);
        SimpleAdapter adapter = new SimpleAdapter(this, listItem,
                R.layout.check_list, new String[]{"TITLE","CHECKBOX"}, new int[]{R.id.item_title, R.id.chech_box1} );
        listView.setAdapter(adapter);
    }
}
