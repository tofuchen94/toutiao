package com.nowcoder.crawler;

import com.nowcoder.dao.NewsDao;
import com.nowcoder.model.News;
import com.nowcoder.util.CrawlerUtil;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 爬取腾讯新闻的爬虫
 * Created by tofuchen on 2018/8/29.
 */
@Component
public class TencentNewsCrawler implements Crawler{

    @Autowired
    NewsDao newsDao;

    @Scheduled(cron = "0 17 15 * * ?")
    public void crawlNews(){
        List<News> newsList= CrawlerUtil.crawlTencentNews();
        //过滤新闻操作
        for (News news : newsList) {
            newsDao.addNews(news);
        }
        System.out.println("****************爬取腾讯新闻完成***************");
    }
}
