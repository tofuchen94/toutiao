package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.util.CrawlerUtil;
import com.nowcoder.util.JedisAdapter;
import com.nowcoder.util.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tofuc on 2018/6/24.
 */
@Service
public class EventConsumer implements InitializingBean,ApplicationContextAware {

    private Logger logger= LoggerFactory.getLogger(EventConsumer.class);
    private Map<EventType,List<EventHandler>> config=new HashMap<>();
    private ApplicationContext applicationContext;

    @Autowired
    private JedisAdapter jedisAdapter;


    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext=applicationContext;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Map<String,EventHandler> beans=applicationContext.getBeansOfType(EventHandler.class);
        if(beans!=null){
            for(Map.Entry<String,EventHandler> entry:beans.entrySet()){
                List<EventType> list=entry.getValue().getSupportEventType();
                for(EventType type:list){
                    if(!config.containsKey(type)){
                        config.put(type,new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }

        Thread tempThread=new Thread(new Runnable() {
            @Override
            public void run() {
                CrawlerUtil.crawlTencentNews();
            }
        });
        tempThread.start();

        Thread thread=new Thread(new Runnable() {
            @Override
            public void run() {
                while(true){
                    String key= RedisKeyUtil.getEventQueueKey();
                    List<String> messages=jedisAdapter.brpop(0,key);
                    for(String message:messages){
                        if(message.equals(key)){
                            continue;
                        }
                        EventModel eventModel= JSON.parseObject(message,EventModel.class);
                        if(!config.containsKey(eventModel.getType())){
                            logger.error("不能识别的事件");
                            continue;
                        }
                        for(EventHandler handler:config.get(eventModel.getType())){
                            handler.doHandle(eventModel);
                        }
                    }
                }
            }
        });
        thread.start();

    }
}
