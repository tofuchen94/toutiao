package com.nowcoder.util;

import com.nowcoder.model.News;
import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 爬虫工具类
 * Created by tofuchen on 2018/8/29.
 */
public class CrawlerUtil {

    private SimpleDateFormat format;

    public static List<News> crawlTencentNews() {
        List<News> res=new ArrayList<News>();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String prefix = "http://new.qq.com/omn/" + format.format(new Date());
        try {
            Document doc = Jsoup.connect("https://news.qq.com").get();
            Element body = doc.body();
            Elements list = body.getElementsByClass("Q-tpList");
            for (Element element : list) {
                Element newsElement = element.getElementsByClass("Q-tpWrap").get(0);
                String newsUrl = newsElement.getElementsByClass("pic").get(0).attr("href");
                if (newsUrl.startsWith(prefix)) {
                    if (newsElement.select("img[class=picto]").size() > 0 && newsElement.select("a[class=linkto]").size() > 0) {
                        String title = newsElement.select("a[class=linkto]").get(0).text();
                        String picUrl = newsElement.select("img[class=picto]").get(0).attr("src");
                        if (!StringUtil.isBlank(title) && !StringUtil.isBlank(picUrl)) {
                            News news=new News();
                            news.setCreatedDate(new Date());
                            news.setUserId(28);
                            news.setImage(picUrl);
                            news.setLink(newsUrl);
                            news.setTitle(title);
                            res.add(news);
                        }
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
}
