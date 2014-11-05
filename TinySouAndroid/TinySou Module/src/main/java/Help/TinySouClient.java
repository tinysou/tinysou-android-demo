package Help;

import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by tinysou on 14-9-22.
 * Author:Yeming Wang
 * Data: 2014.10.11
 * 简介：建立微搜索客户端，调用 HttpHelp 发送搜索请求
 */
public class TinySouClient {
    //编码格式
    public final String CHARSET = HTTP.UTF_8;
    //权限验证
    protected String engineKey = new String();
    //HTTP 请求方法 get 或 post
    protected String method = "post";
    //HTTP 微搜索public 搜索url
    public final String url = "http://api.tinysou.com/v1/public/search";
    //HTTP 微搜索public 自动补全url
    public final String urlAc = "http://api.tinysou.com/v1/public/autocomplete";
    //显示的页数
    protected int page = 0;
    //是否状态正常
    protected boolean isError = false;
    //搜索请求参数
    protected JSONObject searchParams = new JSONObject();
    //自动补全参数
    protected JSONObject acParams = new JSONObject();

    public TinySouClient(String engineKey) {
        this.engineKey = engineKey;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public String getUrl() {
        return this.url;
    }

    public String getUrlAs() {
        return this.urlAc;
    }

    public boolean isError(){
        return this.isError;
    }

    public JSONObject getSearchParams(){
        return searchParams;
    }

    public JSONObject getAcParams(){
        return acParams;
    }

    public void setSearchParams(JSONObject searchParams){
        this.searchParams = searchParams;
    }

    public void setAcParams(JSONObject acParams){
        this.acParams = acParams;
    }

    //建立搜索Request
    public HttpHelp buildRequest(final String SEARCH_CONTENT) {
        final String ENGINE_TOKEN = this.engineKey;
        final int page = this.page;
        HttpHelp postRequest = new HttpHelp();
        postRequest
                .setCharset(CHARSET)
                .setConnectedTimeout(5000)
                .setSoTimeout(10000);
        postRequest.setOnHttpRequestListener(new HttpHelp.OnHttpRequestListener() {
            @Override
            public void onRequest(HttpHelp request) throws Exception {
                // 设置发送请求的 header 信息
                request.addHeader("Content-Type", "application/json");
                // 配置要 POST 的数据
                if(searchParams.length() == 0){
                    searchParams.accumulate("c", "page");
                    searchParams.accumulate("q", SEARCH_CONTENT);
                    searchParams.accumulate("engine_key", ENGINE_TOKEN);
                    searchParams.accumulate("per_page", 10);
                    searchParams.accumulate("page", page);
                }
                String body = searchParams.toString();
                //String search_params = " {\"per_page\":10,\"engine_key\":\"0b732cc0ea3c11874190\",\"page\":0,\"c\":\"page\",\"q\":\"搜索\"}";
                StringEntity entity = new StringEntity(body, CHARSET);
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
        return postRequest;
        //}
    }

    //建立自动补全Request
    public HttpHelp buildAsRequest(final String SEARCH_CONTENT) {
        final String ENGINE_TOKEN = this.engineKey;
        HttpHelp postRequest = new HttpHelp();
        postRequest
                .setCharset(CHARSET)
                .setConnectedTimeout(5000)
                .setSoTimeout(10000);
        postRequest.setOnHttpRequestListener(new HttpHelp.OnHttpRequestListener() {
            @Override
            public void onRequest(HttpHelp request) throws Exception {
                // 设置发送请求的 header 信息
                request.addHeader("Content-Type", "application/json");
                // 配置要 POST 的数据
                if(acParams.length() == 0) {
                    JSONArray fetchField = new JSONArray();
                    fetchField.put("title");
                    fetchField.put("sections");
                    fetchField.put("url");
                    fetchField.put("updated_at");
                    acParams.accumulate("c", "page");
                    acParams.accumulate("q", SEARCH_CONTENT);
                    acParams.accumulate("engine_key", ENGINE_TOKEN);
                    acParams.accumulate("fetch_fields", fetchField);
                    acParams.accumulate("per_page", "10");
                }
                String body = acParams.toString();
                StringEntity entity = new StringEntity(body, CHARSET);
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
        return postRequest;
    }

    //搜索
    public String Search(String searchContent) {
        if ("".equals(searchContent.trim())) {
            return "";
        }
        String searchUrl = this.getUrl();
        HttpHelp request = this.buildRequest(searchContent);
        String content;
        try {
            content = request.post(searchUrl);
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
    public String AutoSearch(String searchContent) {
        if ("".equals(searchContent.trim())) {
            return "";
        }
        String searchUrl = this.getUrlAs();
        HttpHelp request = this.buildAsRequest(searchContent);
        String content;
        try {
            content = request.post(searchUrl);
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
