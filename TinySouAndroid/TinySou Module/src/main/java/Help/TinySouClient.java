package Help;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by tinysou on 14-9-22.
 * Author:Yeming Wang
 * Data: 2014.10.11
 * 简介：建立微搜索主机，调用HttpHelp接口发送微搜索请求
 */
public class TinySouClient {
    //权限验证
    protected String engine_key = null;
    //HTTP 请求方法 get 或 post
    protected String method = "post";
    //HTTP 微搜索public 搜索url````````
    protected String url = "http://api.tinysou.com/v1/public/search";
    //HTTP 微搜索public 自动补全url
    protected String url_as = "http://api.tinysou.com/v1/public/autocomplete";
    //显示的页数
    protected int page = 0;
    //是否状态正常
    protected boolean isError = false;
    //搜索请求参数
    protected JSONObject search_params = new JSONObject();
    //自动补全参数
    protected JSONObject ac_params = new JSONObject();

    public TinySouClient(String engine_key) {
        this.engine_key = engine_key;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String buildUrl(String SearchContent) {
        return this.url;
    }

    public String buildUrlAs(String SearchContent) {
        return this.url_as;
    }

    public boolean isError(){
        return this.isError;
    }

    public JSONObject getSearchParams(){
        return search_params;
    }

    public JSONObject getAcParams(){
        return ac_params;
    }

    public void setSearchParams(JSONObject search_params){
        this.search_params = search_params;
    }

    public void setAcParams(JSONObject ac_params){
        this.ac_params = ac_params;
    }

    //建立搜索Request
    public HttpHelp buildRequest(final String SearchContent) {
        final String EngineToken = this.engine_key;
        final int page = this.page;
        HttpHelp post_request = new HttpHelp();
        post_request
                .setCharset(HTTP.UTF_8)
                .setConnectedTimeout(5000)
                .setSoTimeout(10000);
        post_request.setOnHttpRequestListener(new HttpHelp.OnHttpRequestListener() {
            private String CHARSET = HTTP.UTF_8;

            @Override
            public void onRequest(HttpHelp request) throws Exception {
                // 设置发送请求的 header 信息
                request.addHeader("Content-Type", "application/json");
                // 配置要 POST 的数据
                if(search_params.length() == 0){
                    search_params.accumulate("c", "page");
                    search_params.accumulate("q",SearchContent);
                    search_params.accumulate("engine_key",EngineToken );
                    search_params.accumulate("per_page", 10);
                    search_params.accumulate("page", page);

                }
                String body = search_params.toString();
                System.out.println("body "+body);
                //String search_params = " {\"per_page\":10,\"engine_key\":\"0b732cc0ea3c11874190\",\"page\":0,\"c\":\"page\",\"q\":\"搜索\"}";
                StringEntity entity = new StringEntity(body, HTTP.UTF_8);
                request.buildPostEntity(entity);
            }

            @Override
            public String onSucceed(int statusCode, HttpHelp request) throws Exception {
                return request.getInputStreamJson();
            }

            @Override
            public String onFailed(int statusCode, HttpHelp request) throws Exception {
                TinySouClient.this.isError = true;
                return "POST请求失败：statusCode " + statusCode;
            }
        });
        return post_request;
        //}
    }

    //建立自动补全Request
    public HttpHelp buildAsRequest(final String SearchContent) {
        final String EngineToken = this.engine_key;
        HttpHelp post_request = new HttpHelp();
        post_request
                .setCharset(HTTP.UTF_8)
                .setConnectedTimeout(5000)
                .setSoTimeout(10000);
        post_request.setOnHttpRequestListener(new HttpHelp.OnHttpRequestListener() {
            private String CHARSET = HTTP.UTF_8;
            @Override
            public void onRequest(HttpHelp request) throws Exception {
                // 设置发送请求的 header 信息
                request.addHeader("Content-Type", "application/json");
                // 配置要 POST 的数据
                if(ac_params.length() == 0) {
                    JSONArray fetch_field = new JSONArray();
                    fetch_field.put("title");
                    fetch_field.put("sections");
                    fetch_field.put("url");
                    fetch_field.put("updated_at");
                    String fetch_field_s = fetch_field.toString();
                    ac_params.accumulate("c", "page");
                    ac_params.accumulate("q", SearchContent);
                    ac_params.accumulate("engine_key", EngineToken);
                    ac_params.accumulate("fetch_fields", fetch_field);
                    ac_params.accumulate("per_page", "10");
                }
                String body = ac_params.toString();
                System.out.println(body);
                StringEntity entity = new StringEntity(body, HTTP.UTF_8);
                request.buildPostEntity(entity);
            }

            @Override
            public String onSucceed(int statusCode, HttpHelp request) throws Exception {
                return request.getInputStreamJson();
            }

            @Override
            public String onFailed(int statusCode, HttpHelp request) throws Exception {
                TinySouClient.this.isError = true;
                return "POST请求失败：statusCode " + statusCode;
            }
        });
        return post_request;
    }

    //搜索
    public String Search(String search_content) {
        if ("".equals(search_content.trim())) {
            return "";
        }
        String SearchUrl = this.buildUrl(search_content);
        HttpHelp request = this.buildRequest(search_content);
        String content = null;
        try {
            content = request.post(SearchUrl);
        } catch (IOException e) {
            content = "IO异常：" + e.getMessage();
            this.isError = true;
        } catch (Exception e) {
            content = request.exceptionMessage;
            this.isError = true;
        }
        return content;
    }

    //自动补全
    public String AutoSearch(String search_content) {
        if ("".equals(search_content.trim())) {
            return "";
        }
        String SearchUrl = this.buildUrlAs(search_content);
        HttpHelp request = this.buildAsRequest(search_content);
        String content = null;
        try {
            content = request.post(SearchUrl);
        } catch (IOException e) {
            content = "IO异常：" + e.getMessage();
            this.isError = true;
        } catch (Exception e) {
            content = request.exceptionMessage;
            this.isError = true;
        }
        return content;
    }
}
