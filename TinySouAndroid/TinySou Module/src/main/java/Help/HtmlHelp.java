package Help;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by freestorm on 14-9-22.
 * Author:Yeming Wang
 * Data: 2014.10.11
 */
public class HtmlHelp {

    //最大页数
    protected int max_page = 0;

    public int getMaxPage() {
        return this.max_page;
    }

    public void HtmlHelp() {
    }

    //搜索转html
    public String toHtml(TinySouHelp tinySouHelp) {
        List<String> Title = new ArrayList<String>();
        List<String> Sections = new ArrayList<String>();
        List<String> Url_date = new ArrayList<String>();
        int num = tinySouHelp.records.size();
        //-------------------------------------------title处理-------------------------------------------
        for (int i = 0; i < num; i++) {
            String url = tinySouHelp.records.get(i).document.url;
            String doc_title = "<h4><a href=\"" + url + "\">" + tinySouHelp.records.get(i).document.title + "</h4>";
            Title.add(doc_title);
            System.out.println(i + " " + "doc_title " + doc_title);
            try {
                int title_num = tinySouHelp.records.get(i).highlight.title.size();
                String hi_title = "";
                for (int j = 0; j < title_num; j++) {
                    hi_title = hi_title + " " + tinySouHelp.records.get(i).highlight.title.get(j);
                    if (j > 5) {
                        break;
                    }
                }
                hi_title = "<h4><a href=\"" + url + "\">" + hi_title + "</a></h4>";
                System.out.println(i + " " + "hig_title " + hi_title);
                Pattern p1 = Pattern.compile("<em>");
                Matcher m1 = p1.matcher(hi_title);
                hi_title = m1.replaceAll("<font color=\"#FF4500\">");
                Pattern p2 = Pattern.compile("</em>");
                Matcher m2 = p2.matcher(hi_title);
                hi_title = m2.replaceAll("</font>");
                Title.remove(doc_title);//删除document 的title
                Title.add(hi_title);//添加document的title
            } catch (Exception e) {
                System.out.println("<-------Exception------->");
                e.printStackTrace();
                System.out.println("<-------Exception end------->");
            }
            //都是Gson惹的祸
            //-------------------------------------------sections处理-------------------------------------------
            int doc_sections_num = tinySouHelp.records.get(i).document.sections.size();
            String doc_sections = "";
            for (int j = 0; j < doc_sections_num; j++) {
                doc_sections = doc_sections + " " + tinySouHelp.records.get(i).document.sections.get(j);
                if (j > 5) {
                    break;
                }
            }
            doc_sections = "<p>" + doc_sections + "</p>";
            System.out.println(i + " " + "doc_sextions " + doc_sections);
            Sections.add(doc_sections);
            try {
                int high_sections_num = tinySouHelp.records.get(i).highlight.sections.size();
                String hi_sections = "";
                for (int j = 0; j < high_sections_num; j++) {
                    hi_sections = hi_sections + " " + tinySouHelp.records.get(i).highlight.sections.get(j);
                    if (j > 5) {
                        break;
                    }
                }
                hi_sections = "<p>" + hi_sections + "</p>";
                Pattern p1 = Pattern.compile("<em>");
                Matcher m1 = p1.matcher(hi_sections);
                hi_sections = m1.replaceAll("<font color=\"#FF4500\">");
                Pattern p2 = Pattern.compile("</em>");
                Matcher m2 = p2.matcher(hi_sections);
                hi_sections = m2.replaceAll("</font>");
                Sections.remove(doc_sections);//删除document 的sections
                Sections.add(hi_sections);//添加document的sections
                System.out.println(i + " " + "hig_sextions " + hi_sections);
            } catch (Exception e) {
                System.out.println("<-------Exception------->");
                e.printStackTrace();
                System.out.println("<-------Exception end------->");
            }
            //获取日期
            Pattern p3 = Pattern.compile("T[A-Z0-9:.]+");
            Matcher m3 = p3.matcher(tinySouHelp.records.get(i).document.updated_at);
            String date = m3.replaceFirst("");
            System.out.println(date);
            //匹配URL的部分
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
            String url_sp = "<p>" + m5.replaceFirst("") + "..." + date + "</p>";
            Url_date.add(url_sp);
            System.out.println(i + " " + "url_sp " + url_sp);
        }
        //-------------------------------------------html生成-------------------------------------------
        String search_content = tinySouHelp.info.query;
        String html = "<html><head><title><h3><strong>关于\"" + search_content + "\"的搜索结果共有" + tinySouHelp.info.total + "条" + "</strong></h3></title></head>";
        int page = Integer.parseInt(tinySouHelp.info.page) + 1;
        int per_page = Integer.parseInt(tinySouHelp.info.per_page);
        int total = Integer.parseInt(tinySouHelp.info.total);
        int total_page = total / per_page;
        if (total % per_page != 0) {
            total_page++;
        }
        this.max_page = total_page;
        html = html + "<p>第" + page + "页   " + "共" + total_page + "页" + "</p>";
        for (int i = 0; i < num; i++) {
            html = html + Title.get(i) + Sections.get(i) + Url_date.get(i);
        }
        return html;
    }

    //自动补全转html
    public String toAsHtml(TinySouHelp tinySouHelp) {
        String html = "<html><head><title>猜你想搜</title></head>";
        int num = tinySouHelp.records.size();
        String search_content = tinySouHelp.info.query;
        String title = "";
        String sections = "";
        for (int i = 0; i < num; i++) {
            String url = tinySouHelp.records.get(i).document.url;
            int sec_num = tinySouHelp.records.get(i).document.sections.size();
            title = "<h4><a href=\"" + url + "\">" + tinySouHelp.records.get(i).document.title + "</a></h4>";
            for (int j = 0; j < sec_num; j++) {
                sections = sections + " " + tinySouHelp.records.get(i).document.sections.get(j);
                if (j > 5) {
                    break;
                }
            }
            sections = "<p>" + sections + "</p>";
            //获取日期
            Pattern p3 = Pattern.compile("T[A-Z0-9:.]+");
            Matcher m3 = p3.matcher(tinySouHelp.records.get(i).document.updated_at);
            String date = m3.replaceFirst("");
            System.out.println(date);
            //匹配URL的部分
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
            String url_sp = "<p>" + m5.replaceFirst("") + "..." + date + "</p>";
            html = html + title + sections + url_sp;
        }
        return html;
    }
}
