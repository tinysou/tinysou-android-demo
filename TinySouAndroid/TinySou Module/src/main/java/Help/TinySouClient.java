package Help;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.HTTP;
import org.json.JSONStringer;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by freestorm on 14-9-22.
 * Author:Yeming Wang
 * Data: 2014.10.11
 * 简介：建立微搜索主机，调用HttpHelp接口发送微搜索请求
 */
public class TinySouClient {
    //权限验证
    protected String engine_token = null;
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

    public TinySouClient(String engine_token) {
        this.engine_token = engine_token;
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

    //建立搜索Request
    public HttpHelp buildRequest(final String SearchContent) {
        final String EngineToken = this.engine_token;
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
                System.out.println(request.getFirstHeader("Content-Type").getValue());
                // 配置要 POST 的数据
                JSONStringer search_content = new JSONStringer().object()
                        .key("q").value(SearchContent);
                System.out.print("SearchContent" + SearchContent);
                search_content.key("c").value("page");
                search_content.key("engine_key").value(EngineToken);
                search_content.key("per_page").value("10");
                search_content.key("page").value(page);
                search_content.endObject();
                String body = search_content.toString();
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
        //}
    }

    //建立自动补全Request
    public HttpHelp buildAsRequest(final String SearchContent) {
        final String EngineToken = this.engine_token;
        HttpHelp post_request = new HttpHelp();
        post_request
                .setCharset(HTTP.UTF_8)
                .setConnectedTimeout(5000)
                .setSoTimeout(10000);
        post_request.setOnHttpRequestListener(new HttpHelp.OnHttpRequestListener() {
            private String CHARSET = HTTP.UTF_8;

            @Override
            public void onRequest(HttpHelp request) throws Exception {
                String fetch_fields = "['title', 'sections', 'url','updated_at']";
                // 设置发送请求的 header 信息
                request.addHeader("Content-Type", "application/json");
                // 配置要 POST 的数据
                JSONStringer search_content = new JSONStringer().object()
                        .key("q").value(SearchContent);
                search_content.key("c").value("page");
                search_content.key("engine_key").value(EngineToken);
                search_content.key("fetch_fields").value(fetch_fields);
                search_content.key("per_page").value("10");
                search_content.endObject();
                String body = search_content.toString();
                Pattern p = Pattern.compile("\"\\['title', 'sections', 'url','updated_at']\"");
                Matcher m = p.matcher(body);
                System.out.println(body);
                body = m.replaceAll("\\[\"title\", \"sections\", \"url\",\"updated_at\"\\]");
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
            System.out.println("异常：");
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
            content = "异常：" + e.getMessage();
            this.isError = true;
        }
        return content;
    }
}
