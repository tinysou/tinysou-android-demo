package com.tinysou.module;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Help.Json.TinySouJsonHelp;
import Help.ListHelp;
import Help.TinySouClient;

/**
 * y
 * Created by tinysou on 14-9-22.
 * Author:Yeming Wang
 * Data: 2014.10.11
 * 搜索页面，实现搜索，自动补全，下拉刷新，上拉加载更多等功能
 */
public class TinySouSearchActivity extends Activity {

    protected ApplicationInfo appInfo = new ApplicationInfo();//获取当前应用
    protected String engineKey = new String();//微搜索engine_key
    protected String searchContent = new String();//默认为空
    protected int currentPage = 0;//当前显示页数
    protected int maxPage = 0;//最大页数
    protected boolean isAutoCom = true;//是否开启自动补全，默认开启
    protected List<String> urlList = new ArrayList<String>();//存储搜索结果url链接信息
    protected int position;//用于记忆和恢复listView滚动位置
    protected int lvChildTop;//用于记忆和恢复listView滚动位置
    private ListView lt1;
    private SwipeRefreshLayout swipeLayout;
    //search显示内容
    private List<Map<String, String>> searchDisplay = new ArrayList<Map<String, String>>();
    private searchThread searchThread = new searchThread(0);//搜索线程
    private autoCompleteThread autoCompleteThread = new autoCompleteThread();//自动补全线程

    /*
    1. 获取engine_key
    2. 监听下拉刷新  google官方swipeLayout集成的下拉刷新彩虹条功能
    3. 监听上拉加载更多
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tinysou_search);
        try {
            appInfo = this.getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            //System.out.println(appInfo.metaData.getString("engine_key"));
            this.engineKey = appInfo.metaData.getString("engine_key");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (!isConnected(getApplicationContext())) {
            setNetworkMethod(TinySouSearchActivity.this);
        }
        setSwipeLayoutListener();
        setListViewListener();
    }

    /*
    SearchView处理
    1. 输入框文字改变，自动搜索
    2. 提交搜索请求
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tinysou_search, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);//默认展开搜索框
            searchView.requestFocus();//默认开启焦点，打开输入法
            //监听输入框字符串变化
            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                //输入框文字改变
                public boolean onQueryTextChange(String newText) {
                    if (isAutoCom) {
                        // this is your adapter that will be filtered
                        currentPage = 0;//重新刷新，当前页面归零
                        searchContent = newText;
                        autoComplete(newText);
                        return true;
                    } else {
                        return true;
                    }
                }

                //提交搜索请求
                public boolean onQueryTextSubmit(String query) {
                    // this is your adapter that will be filtered
                    searchContent = query;
                    Search(searchContent, 0);
                    currentPage = 0;//重新搜索，当前页面归零
                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.search) {
            onSearchRequested();
            return true;
        } else if (item.getItemId() == R.id.action_settings) {
            return true;
        } else {
            return false;
        }
    }

    //设置SwipeLayout监听
    public void setSwipeLayoutListener() {
        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        //监听下拉刷新
        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if ("".equals(searchContent)) {
                    swipeLayout.setRefreshing(false);//停止刷新
                }
                // TODO Auto-generated method stub
                //System.out.println("刷新开始!!!"+search_content);
                if (swipeLayout.isRefreshing() == true) {
                    //System.out.println("刷新中!!!"+search_content);
                    currentPage = 0;//重新刷新，当前页面归零
                    Search(searchContent, 0);
                }
                //System.out.println("刷新结束!!!");
            }
        });
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    //设置ListView监听
    public void setListViewListener() {
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
                        //System.out.println("记录： "+"position "+position);
                        //System.out.println("loadmore");
                        if (searchThread.isRun()) {
                            //System.out.println("还在加载中，请稍等...");
                        } else {
                            if (currentPage + 1 < maxPage) {
                                currentPage++;
                                Search(searchContent, currentPage);
                            } else {
                                //System.out.println("当前页  " + current_page + "最大页 " + max_page + " 没有更多了！");
                            }
                        }
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    //处理搜索
    private Handler handler1 = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            String content = msg.obj.toString();
            //如果输入内容为空
            if ("".equals(content)) {
                List<Map<String, String>> searchDisplay = new ArrayList<Map<String, String>>();
                SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, searchDisplay,
                        R.layout.list_item, new String[]{"title", "sections", "url_sp"}, new int[]{R.id.title, R.id.sections, R.id.url_sp});
                lt1.setAdapter(adapter);
                swipeLayout.setRefreshing(false);
                searchThread.setStopState();
                return;
            }
            if (searchThread.isError()) {
                //new AlertDialog.Builder(TinySouSearchActivity.this).setTitle(content).
                //setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", null).show();
                Toast.makeText(TinySouSearchActivity.this, content, Toast.LENGTH_SHORT).show();
                return;
            }
            TinySouJsonHelp tinySouJsonHelp = JSON.parseObject(content, TinySouJsonHelp.class);
            ListHelp listHelp = new ListHelp();
            listHelp.setCurrentPage(currentPage);
            listHelp.setSearch(tinySouJsonHelp);
            maxPage = listHelp.getMaxPage();
            List<Map<String, String>> searchList = listHelp.getSearch();
            if (currentPage == 0) {
                urlList = new ArrayList<String>();
                List<String> urlListNew = listHelp.getUrlList();
                urlList.addAll(urlListNew);
                setContentAdapter(searchList);
                swipeLayout.setRefreshing(false);
            } else {
                List<String> urlListNew = listHelp.getUrlList();
                urlList.addAll(urlListNew);
                setContentAdapter(searchList);
                swipeLayout.setRefreshing(false);
                lt1.setSelectionFromTop(position, lvChildTop);
            }
            //设置Url链接
            lt1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(urlList.get(arg2)));
                    startActivity(it);
                }
            });
            searchThread.setStopState();
        }
    };

    //处理自动补全
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            String content = msg.obj.toString();
            //如果输入内容为空
            if ("".equals(content)) {
                List<Map<String, String>> searchDisplay = new ArrayList<Map<String, String>>();
                SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, searchDisplay,
                        R.layout.list_item, new String[]{"title", "sections", "url_sp"}, new int[]{R.id.title, R.id.sections, R.id.url_sp});
                lt1.setAdapter(adapter);
                swipeLayout.setRefreshing(false);
                autoCompleteThread.setStopState();
                return;
            }
            if (autoCompleteThread.isError()) {
                //new AlertDialog.Builder(TinySouSearchActivity.this).setTitle(content).
                //        setIcon(android.R.drawable.ic_dialog_info).setPositiveButton("确定", null).show();
                Toast.makeText(TinySouSearchActivity.this, content, Toast.LENGTH_SHORT).show();
                return;
            }
            TinySouJsonHelp tinySouJsonHelp = JSON.parseObject(content, TinySouJsonHelp.class);
            ListHelp listHelp = new ListHelp();
            listHelp.setCurrentPage(currentPage);
            listHelp.setAutoCompleteList(tinySouJsonHelp);
            maxPage = listHelp.getMaxPage();
            //Url储存
            urlList = new ArrayList<String>();
            List<String> UrlListNew = listHelp.getUrlList();
            urlList.addAll(UrlListNew);
            List<Map<String, String>> autoCompleteList = listHelp.getAutoCompleteList();
            setContentAdapter(autoCompleteList);
            //设置Url链接
            lt1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(urlList.get(arg2)));
                    startActivity(it);
                }
            });
            swipeLayout.setRefreshing(false);
            autoCompleteThread.setStopState();
        }
    };

    //设置listView adapter
    private void setContentAdapter(List<Map<String, String>> searchList) {
        searchDisplay.addAll(searchList);
        SimpleAdapter adapter = new SimpleAdapter(TinySouSearchActivity.this, searchDisplay,
                R.layout.list_item, new String[]{"title", "sections", "url_sp"}, new int[]{R.id.title, R.id.sections, R.id.url_sp});
        lt1.setAdapter(adapter);
    }

    public void Search(String query, int page) {
        if (!isConnected(getApplicationContext())) {
            setNetworkMethod(TinySouSearchActivity.this);
        } else {
            searchContent = query;
            searchThread = new searchThread(page);
            searchThread.start();
        }
    }

    public void autoComplete(String query) {
        if (!isConnected(getApplicationContext())) {
            setNetworkMethod(TinySouSearchActivity.this);
        } else {
            searchContent = query;
            autoCompleteThread = new autoCompleteThread();
            autoCompleteThread.start();
        }
    }

    //搜索线程
    class searchThread extends Thread {
        private boolean isRun = false;
        private int searchPage = 0;
        private boolean isError = false;

        public searchThread(int page) {
            isRun = false;
            searchPage = page;
        }

        public void setStopState() {
            isRun = false;
        }

        @Override
        public void run() {
            isRun = true;
            TinySouClient client = new TinySouClient(engineKey);
            client.setPage(searchPage);
            String result = client.Search(searchContent);
            this.isError = client.isError();
            Message message = Message.obtain();
            message.obj = result;
            TinySouSearchActivity.this.handler1.sendMessage(message);
        }

        public boolean isRun() {
            return this.isRun;
        }

        public boolean isError() {
            return this.isError;
        }
    }

    //自动补全线程
    class autoCompleteThread extends Thread {
        private boolean isRun = false;
        private int searchPage = 0;
        private boolean isError = false;

        public autoCompleteThread() {
            isRun = false;
        }

        public void setStopState() {
            isRun = false;
        }

        @Override
        public void run() {
            isRun = true;
            TinySouClient client = new TinySouClient(engineKey);
            String result = client.AutoSearch(searchContent);
            this.isError = client.isError();
            Message message = Message.obtain();
            message.obj = result;
            TinySouSearchActivity.this.handler2.sendMessage(message);
        }

        public boolean isRun() {
            return this.isRun;
        }

        public boolean isError() {
            return this.isError;
        }
    }

    public boolean isError(String content) {
        Pattern p = Pattern.compile("POST请求失败");
        Matcher m = p.matcher(content);
        if (m.find()) {
            return true;
        } else {
            return false;
        }
    }

    /*
     * 判断网络连接是否已开
     *true 已打开  false 未打开
     * */
    public static boolean isConnected(Context context) {
        boolean bisConnFlag = false;
        ConnectivityManager conManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = conManager.getActiveNetworkInfo();
        if (network != null) {
            bisConnFlag = conManager.getActiveNetworkInfo().isAvailable();
        }
        return bisConnFlag;
    }

    /**
     * 如何网络没有打开，提示打开网络设置界面
     */
    public void setNetworkMethod(final Context context) {
        new AlertDialog.Builder(this).setTitle("网络设置提示").setMessage("网络连接不可用,是否进行设置?")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                    }
                }).setNegativeButton("取消", null).show();
    }
}