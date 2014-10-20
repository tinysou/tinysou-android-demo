package com.example.freestorm.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class settingActivity extends Activity {
    private CheckBox cb1;
    private ListView listView;
    public final static String AC_MESSAGE = "1";
    private Intent intent;
    private Data app;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        cb1 = (CheckBox) findViewById(R.id.chech_box1);
        intent = new Intent(this, settingActivity.class);
        app = (Data)getApplication();
        //app.onCreate();
        listViewInit();
        //listViewSetListener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.setting, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, settingActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void listViewInit(){
        listView = (ListView) findViewById(R.id.listview1);
        List<Map<String, Object>> listItem = new ArrayList<Map<String, Object>>();
        Map<String, Object> map1 = new HashMap<String, Object>();
        map1.put("TITLE", getString(R.string.setting1));
        map1.put("CHECKBOX", cb1);
        listItem.add(map1);
        SimpleAdapter adapter = new SimpleAdapter(this, listItem,
                R.layout.check_list, new String[]{"TITLE","CHECKBOX"}, new int[]{R.id.item_title, R.id.chech_box1} );
        listView.setAdapter(adapter);
        listView.post(new Runnable() {
            public void run() {
                for(int i=0; i < listView.getChildCount(); i++){
                    LinearLayout itemLayout = (LinearLayout)listView.getChildAt(i);
                    CheckBox cb = (CheckBox)itemLayout.findViewById(R.id.chech_box1);
                    cbSetListener(cb);
                    if(app.getAC()){
                        cb.setChecked(true);
                    }else{
                        cb.setChecked(false);
                    }
                }
            }
        });
    }

    public void listViewSetListener(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                System.out.println("id "+id);
                System.out.println("position "+position);
            }
        });
    }

    public void cbSetListener(final CheckBox cb){
        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    app.setAC(true);
                    Toast.makeText(settingActivity.this, "即输即搜已开启", Toast.LENGTH_SHORT).show();
                } else {
                    app.setAC(false);
                    Toast.makeText(settingActivity.this, "即输即搜已关闭", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
