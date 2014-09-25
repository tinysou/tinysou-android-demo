package com.freestorm.mymodule.tinysouAndroid.mymodule.app2;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Help.HtmlHelp;
import Help.ListHelp;
import Help.TinySouClient;
import Help.TinySouHelp;


public class TinySouSearchActivity extends Activity {

    public String engine_token = "0b732cc0ea3c11874190";
    protected String search_content = "自定义样式";
    protected int Current_page = 0;//当前显示页数
    protected int Max_page = 0;//最大页数
    protected List<String> UrlList = new ArrayList<String>();

    private ListView lt1;

    //------------------------------------处理搜索结果函数--------------------------------------------
    //处理搜索
    private Handler handler1 = new Handler() {
        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {
            lt1 = (ListView) findViewById(R.id.list1);
            String content = msg.obj.toString();
            //如果输入内容为空
            if("".equals(content)){
                return;
            }
            Gson gson = new Gson();
            TinySouHelp tinySouHelp = gson.fromJson(content, TinySouHelp.class);
            ListHelp listHelp = new ListHelp();
            listHelp.setSearch(tinySouHelp);
            List<Map<String, String>> Search = listHelp.getSearch();
            UrlList = listHelp.getUrlList();
            SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, Search,
                    R.layout.list_item, new String[] {"title", "sections", "url_sp"}, new int[] {R.id.title, R.id.sections, R.id.url_sp});
            lt1.setAdapter(adapter);
        }
    };

    //处理自动补全
    private Handler handler2 = new Handler() {
        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {
            lt1 = (ListView) findViewById(R.id.list1);
            String content = msg.obj.toString();
            //如果输入内容为空
            if("".equals(content)){
                lt1.setVisibility(8);
                return;
            }
            lt1.setVisibility(0);
            Gson gson = new Gson();
            TinySouHelp tinySouHelp = gson.fromJson(content, TinySouHelp.class);
            ListHelp listHelp = new ListHelp();
            listHelp.setAutoCompleteList(tinySouHelp);
            UrlList = listHelp.getUrlList();
            System.out.println("----------------------------"+UrlList.size());
            for(int i=0;i<UrlList.size();i++){
                System.out.println("url   "+ i + " "+ UrlList.get(i));
            }
            List<Map<String, String>> AutoCompleteList = listHelp.getAutoCompleteList();
            SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, AutoCompleteList,
                    R.layout.list_item, new String[] {"title", "sections", "url_sp"}, new int[] {R.id.title, R.id.sections, R.id.url_sp});
            lt1.setAdapter(adapter);
            lt1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    System.out.println("asdfghjkl");
                    System.out.println(arg2);
                    System.out.println(UrlList.get(arg2));
                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlList.get(arg2)));
                    startActivity(it);
                }
            });
        }
    };



    //-------------------------------------初始化函数-------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        System.out.println("111111");
        handleIntent(getIntent());
        setContentView(R.layout.activity_tiny_sou_search);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_VIEW.equals(intent.getAction())) {
            // handles a click on a search suggestion; launches activity to show word
            //Intent wordIntent = new Intent(this, WordActivity.class);
            //wordIntent.setData(intent.getData());
            //startActivity(wordIntent);
        } else if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // handles a search query
            String query = intent.getStringExtra(SearchManager.QUERY);
            search_content = query;
            Search(search_content, 0);
            Current_page = 0;//重新搜索，当前页面归零
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tiny_sou_search, menu);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB){
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(true);
            //监听输入框字符串变化
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
            {
                public boolean onQueryTextChange(String newText)
                {
                    // this is your adapter that will be filtered
                    autoComplete(newText);
                    return true;
                }
                public boolean onQueryTextSubmit(String query)
                {
                    // this is your adapter that will be filtered
                    search_content = query;
                    Search(search_content, 0);
                    Current_page = 0;//重新搜索，当前页面归零
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.search){
            onSearchRequested();
            return true;
        }else if(item.getItemId() == R.id.action_settings ){
            return true;
        }else{
            return false;
        }
    }

    //-------------------------------------执行搜索操作函数-------------------------------------

    public void Search(String query, final int page){
        search_content = query;
        new Thread(new Runnable() {
            public void run() {
                TinySouClient client = new TinySouClient(engine_token);
                client.setPage(page);
                String result = client.Search(search_content);
                Message message = Message.obtain();
                message.obj = result;
                handler1.sendMessage(message);
            }
        }).start();
    }

    public void autoComplete(final String query){
        new Thread(new Runnable() {
            public void run() {
                System.out.println(engine_token);
                TinySouClient client = new TinySouClient(engine_token);
                String result = client.AutoSearch(query);
                if("".equals(query)) {
                    System.out.println("sadasd" + query);
                }
                Message message = Message.obtain();
                message.obj = result;
                handler2.sendMessage(message);
            }
        }).start();
    }

    public void LastPage(final View view){
        System.out.println("Current_page   " + Current_page);
        if(Current_page == 0){
            return;
        }else {
            Search(search_content, Current_page-1);
            Current_page--;
        }
    }

    public void NextPage(final View view){
        System.out.println("Max_page   " + Max_page);
        System.out.println("Current_page   " + Current_page);
        if(Current_page+1 == Max_page){
            return;
        }else {
            Search(search_content, Current_page+1);
            Current_page++;
        }
    }



}

