package Help;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by freestorm on 14-9-24.
 */
public class ListHelp {
    protected List<Map<String, String>> Search = new ArrayList<Map<String, String>>();
    protected List<Map<String, String>> AutoCompleteList = new ArrayList<Map<String, String>>();
    protected List<String> UrlList = new ArrayList<String>();

    public void ListHelp(){}

    public List<Map<String, String>> getSearch(){
        return this.Search;
    }

    public List<Map<String, String>> getAutoCompleteList(){
        return this.AutoCompleteList;
    }

    public List<String> getUrlList() {return this.UrlList;}

    public void setSearch(TinySouHelp tinySouHelp){
        int num = tinySouHelp.records.size();
        for (int i = 0; i < num; i++) {
            //<title, sections, url_sp>
            Map<String,String> item = new HashMap<String, String>();
            //--------------------------------------title处理
            String title = tinySouHelp.records.get(i).document.title;
            item.put("title", title);
            //--------------------------------------sections处理
            String sections = "";
            int sec_num = tinySouHelp.records.get(i).document.sections.size();
            for (int j = 0; j < sec_num; j++) {
                sections = sections + " " + tinySouHelp.records.get(i).document.sections.get(j);
                if (j > 5) {
                    break;
                }
            }
            item.put("sections", sections);
            //--------------------------------------url处理
            //获取日期
            Pattern p3 = Pattern.compile("T[A-Z0-9:.]+");
            Matcher m3 = p3.matcher(tinySouHelp.records.get(i).document.updated_at);
            String date = m3.replaceFirst("");
            System.out.println(date);
            //匹配URL的部分
            String url = tinySouHelp.records.get(i).document.url;
            this.UrlList.add(url);
            Pattern p4 = Pattern.compile("//[A-Za-z0-9.-]+/[\\w]?");
            System.out.println(url);
            Matcher m4 = p4.matcher(url);
            if (m4.find()) {
                System.out.println(m4.group(0));
            }
            //匹配URL的‘//’
            Pattern p5 = Pattern.compile("//");
            Matcher m5 = p5.matcher(m4.group(0));
            //修改为www.baidu.com/p...2014-09-20风格
            String url_sp = m5.replaceFirst("") + "..." + date;
            item.put("url_sp", url_sp);
            this.Search.add(item);
        }
    }

    public void setAutoCompleteList(TinySouHelp tinySouHelp){
        int num = tinySouHelp.records.size();
        for (int i = 0; i < num; i++) {
            //<title, sections, url_sp>
            Map<String,String> item = new HashMap<String, String>();
            //--------------------------------------title处理
            String title = tinySouHelp.records.get(i).document.title;
            item.put("title", title);
            //--------------------------------------sections处理
            String sections = "";
            int sec_num = tinySouHelp.records.get(i).document.sections.size();
            for (int j = 0; j < sec_num; j++) {
                sections = sections + " " + tinySouHelp.records.get(i).document.sections.get(j);
                if (j > 5) {
                    break;
                }
            }
            item.put("sections", sections);
            //--------------------------------------url处理
            //获取日期
            Pattern p3 = Pattern.compile("T[A-Z0-9:.]+");
            Matcher m3 = p3.matcher(tinySouHelp.records.get(i).document.updated_at);
            String date = m3.replaceFirst("");
            System.out.println(date);
            //匹配URL的部分
            String url = tinySouHelp.records.get(i).document.url;
            this.UrlList.add(url);
            Pattern p4 = Pattern.compile("//[A-Za-z0-9.-]+/[\\w]?");
            System.out.println(url);
            Matcher m4 = p4.matcher(url);
            if (m4.find()) {
                System.out.println(m4.group(0));
            }
            //匹配URL的‘//’
            Pattern p5 = Pattern.compile("//");
            Matcher m5 = p5.matcher(m4.group(0));
            //修改为www.baidu.com/p...2014-09-20风格
            String url_sp = m5.replaceFirst("") + "..." + date;
            item.put("url_sp", url_sp);
            this.AutoCompleteList.add(item);
        }
    }
}
