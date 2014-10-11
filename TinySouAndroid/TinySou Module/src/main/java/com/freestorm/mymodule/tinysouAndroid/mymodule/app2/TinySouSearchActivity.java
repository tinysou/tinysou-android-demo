package com.freestorm.mymodule.tinysouAndroid.mymodule.app2;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import Help.ListHelp;
import Help.TinySouClient;
import Help.TinySouHelp;

import android.support.v4.widget.SwipeRefreshLayout;


public class TinySouSearchActivity extends Activity {

    protected ApplicationInfo appInfo = null;
    protected String engine_token = null;
    protected String search_content = "";//默认为空
    protected int Current_page = 0;//当前显示页数
    protected int Max_page = 0;//最大页数
    protected List<String> UrlList = new ArrayList<String>();
<<<<<<< HEAD
    //protected int isSearching = 0;
    protected int position;
    protected int lvChildTop;

    private ListView lt1;
    private SwipeRefreshLayout swipeLayout;
    private List<Map<String, String>> SearchDisplay = new ArrayList<Map<String, String>>();

    private SearchThread searchThread = new SearchThread(0);
    private AutoCompleteThread autoCompleteThread =  new AutoCompleteThread();

=======
    protected int isSearching = 0;

    private ListView lt1;
    private SwipeRefreshLayout swipeLayout;
>>>>>>> remotes/origin/develop

    //------------------------------------处理搜索结果函数--------------------------------------------
    //处理搜索
    private Handler handler1 = new Handler()
    {
        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {
            String content = msg.obj.toString();
            //如果输入内容为空
            if("".equals(content)){
                List<Map<String, String>> SearchDisplay = new ArrayList<Map<String, String>>();
                SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, SearchDisplay,
                        R.layout.list_item, new String[]{"title", "sections", "url_sp"}, new int[]{R.id.title, R.id.sections, R.id.url_sp});
                lt1.setAdapter(adapter);
                //isSearching = 1;
                swipeLayout.setRefreshing(false);
                searchThread.setStopState();
                return;
            }
            Gson gson = new Gson();
            TinySouHelp tinySouHelp = gson.fromJson(content, TinySouHelp.class);
            ListHelp listHelp = new ListHelp();
            listHelp.setCurrentPage(Current_page);
            listHelp.setSearch(tinySouHelp);
            Max_page = listHelp.getMaxPage();
            List<Map<String, String>> Search = listHelp.getSearch();
<<<<<<< HEAD
            if(Current_page == 0) {
                UrlList = new ArrayList<String>();
                List<String> UrlListNew = listHelp.getUrlList();
                UrlList.addAll(UrlListNew);
                SearchDisplay = new ArrayList<Map<String, String>>();
                SearchDisplay.addAll(Search);
                SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, SearchDisplay,
                        R.layout.list_item, new String[]{"title", "sections", "url_sp"}, new int[]{R.id.title, R.id.sections, R.id.url_sp});
                lt1.setAdapter(adapter);
                //isSearching = 1;
                swipeLayout.setRefreshing(false);
            }else {
                List<String> UrlListNew = listHelp.getUrlList();
                UrlList.addAll(UrlListNew);
                SearchDisplay.addAll(Search);
                SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, SearchDisplay,
                        R.layout.list_item, new String[]{"title", "sections", "url_sp"}, new int[]{R.id.title, R.id.sections, R.id.url_sp});
                lt1.setAdapter(adapter);
                //isSearching = 1;
                swipeLayout.setRefreshing(false);
                lt1.setSelectionFromTop(position, lvChildTop);
                //lt1.setSelection(position);
                //lt1.scrollTo(scrolledX, scrolledY);
                System.out.println("记录111： "+"x "+lt1.getScrollX()+" y "+lt1.getScrollY());
                //System.out.println("x " + scrolledX + "y " + scrolledY);
            }
            lt1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlList.get(arg2)));
                    startActivity(it);
                }
            });
            //System.out.println("run? " + searchThread.isRun());
            searchThread.setStopState();
            //adapter.notifyDataSetChanged();
=======
            UrlList = listHelp.getUrlList();
            SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, Search,
                    R.layout.list_item, new String[] {"title", "sections", "url_sp"}, new int[] {R.id.title, R.id.sections, R.id.url_sp});
            lt1.setAdapter(adapter);
            isSearching = 0;
            swipeLayout.setRefreshing(false);
>>>>>>> remotes/origin/develop
        }
    };

    //处理自动补全
    private Handler handler2 = new Handler() {
        // 处理子线程给我们发送的消息。
        @Override
        public void handleMessage(android.os.Message msg) {

            String content = msg.obj.toString();
            //如果输入内容为空
            if("".equals(content)){
                List<Map<String, String>> SearchDisplay = new ArrayList<Map<String, String>>();
                SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, SearchDisplay,
                        R.layout.list_item, new String[]{"title", "sections", "url_sp"}, new int[]{R.id.title, R.id.sections, R.id.url_sp});
                lt1.setAdapter(adapter);
                //isSearching = 0;
                swipeLayout.setRefreshing(false);
                autoCompleteThread.setStopState();
                return;
            }
            Gson gson = new Gson();
            TinySouHelp tinySouHelp = gson.fromJson(content, TinySouHelp.class);
            ListHelp listHelp = new ListHelp();
            listHelp.setCurrentPage(Current_page);
            listHelp.setAutoCompleteList(tinySouHelp);
            Max_page = listHelp.getMaxPage();
            UrlList = new ArrayList<String>();
            List<String> UrlListNew = listHelp.getUrlList();
            UrlList.addAll(UrlListNew);
            System.out.println("----------------------------"+UrlList.size());
            for(int i=0;i<UrlList.size();i++){
                System.out.println("url   "+ i + " "+ UrlList.get(i));
            }
            List<Map<String, String>> AutoCompleteList = listHelp.getAutoCompleteList();
            SearchDisplay = new ArrayList<Map<String, String>>();
            SearchDisplay.addAll(AutoCompleteList);
            SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, SearchDisplay,
                    R.layout.list_item, new String[] {"title", "sections", "url_sp"}, new int[] {R.id.title, R.id.sections, R.id.url_sp});
            lt1.setAdapter(adapter);
            lt1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(UrlList.get(arg2)));
                    startActivity(it);
                }
            });
<<<<<<< HEAD
            //isSearching=0;
            swipeLayout.setRefreshing(false);
            //System.out.println("run? " + autoCompleteThread.isRun());
            autoCompleteThread.setStopState();
=======
            isSearching=0;
            swipeLayout.setRefreshing(false);
>>>>>>> remotes/origin/develop
        }
    };



    //-------------------------------------初始化函数-------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            System.out.println(appInfo.metaData.getString("engine_token"));
            this.engine_token = appInfo.metaData.getString("engine_token");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //setContentView(R.layout.activity_main);
        System.out.println("111111");
        handleIntent(getIntent());
        setContentView(R.layout.activity_tiny_sou_search);
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
<<<<<<< HEAD
        //监听下拉刷新
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if("".equals(search_content)) {
                    swipeLayout.setRefreshing(false);
                }
=======
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
>>>>>>> remotes/origin/develop
                // TODO Auto-generated method stub
                System.out.println("刷新开始!!!"+search_content);

                if (swipeLayout.isRefreshing()==true){
                    System.out.println("刷新中!!!"+search_content);
<<<<<<< HEAD
                    Current_page = 0;//重新刷新，当前页面归零
                    position = 0;
=======
>>>>>>> remotes/origin/develop
                    Search(search_content, 0);
                }
                System.out.println("刷新结束!!!");
            }
        });
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
<<<<<<< HEAD
        lt1 = (ListView) findViewById(R.id.list1);
        //监听上拉加载更多
        lt1.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                // 当不滚动时
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    //System.out.println("记录： "+"position "+lt1.getFirstVisiblePosition());
                    // 判断是否滚动到底部
                    if (view.getLastVisiblePosition() == view.getCount() - 1) {
                    //加载更多功能的代码

                        position = lt1.getFirstVisiblePosition();
                        View v = lt1.getChildAt(0);
                        lvChildTop = (v == null) ? 0 : v.getTop();
                        System.out.println("记录： "+"position "+position);
                        System.out.println("loadmore");
                        if(searchThread.isRun()){
                            System.out.println("还在加载中，请稍等...");
                        }else {
                            if (Current_page + 1 < Max_page) {
                                Current_page++;
                                Search(search_content, Current_page);
                            } else {
                                System.out.println("当前页  " + Current_page + "最大页 " + Max_page + " 没有更多了！");
                            }
                        }
                    }
                }
            }
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }

        });
=======
>>>>>>> remotes/origin/develop
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
            position = 0;
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
            searchView.setIconifiedByDefault(false);//默认展开搜索框
            searchView.requestFocus();//默认开启焦点，打开输入法
            //监听输入框字符串变化
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener()
            {
                public boolean onQueryTextChange(String newText)
                {
                    // this is your adapter that will be filtered
<<<<<<< HEAD
                    Current_page = 0;//重新刷新，当前页面归零
                    position = 0;
=======
>>>>>>> remotes/origin/develop
                    search_content = newText;
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

<<<<<<< HEAD
    public void Search(String query, int page){
        //isSearching =1;
=======
    public void Search(String query, final int page){
        isSearching =1;
>>>>>>> remotes/origin/develop
        search_content = query;
        searchThread = new SearchThread(page);
        searchThread.start();
    }

<<<<<<< HEAD
    public void autoComplete(String query){
        //isSearching=1;
        search_content = query;
        autoCompleteThread =  new AutoCompleteThread();
        autoCompleteThread.start();
=======
    public void autoComplete(final String query){
        isSearching=1;
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
>>>>>>> remotes/origin/develop
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

    //搜索线程
    class SearchThread extends Thread {
        private boolean isRun = false;
        private int searchPage = 0;
        public SearchThread(int page) {
            isRun = false;
            searchPage = page;
        }
        public void setStopState() {
            isRun = false;
        }
        @Override
        public void run() {
            isRun = true;
            TinySouClient client = new TinySouClient(engine_token);
            client.setPage(searchPage);
            String result = client.Search(search_content);
            Message message = Message.obtain();
            message.obj = result;
            TinySouSearchActivity.this.handler1.sendMessage(message);
        }
        public boolean isRun(){
            return this.isRun;
        }
    }

    //自动补全线程
    class AutoCompleteThread extends Thread {
        private boolean isRun = false;
        private int searchPage = 0;
        public AutoCompleteThread() {
            isRun = false;
        }
        public void setStopState() {
            isRun = false;
        }
        @Override
        public void run() {
            isRun = true;
            TinySouClient client = new TinySouClient(engine_token);
            client.setPage(searchPage);
            String result = client.Search(search_content);
            Message message = Message.obtain();
            message.obj = result;
            TinySouSearchActivity.this.handler2.sendMessage(message);
        }
        public boolean isRun(){
            return this.isRun;
        }
    }
}

