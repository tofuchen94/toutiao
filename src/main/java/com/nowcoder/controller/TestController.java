package com.nowcoder.controller;

import com.nowcoder.model.Message;
import com.nowcoder.model.News;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.NewsService;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.Random;

/**
 * Created by tofuc on 2018/6/11.
 */
@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    UserService userService;
    @Autowired
    NewsService newsService;
    @Autowired
    MessageService messageService;

    @RequestMapping(value={"/","/index"})
    public String showIndex(Model model) {
        model.addAttribute("name","tofuchen");
        return "index";
    }



    @RequestMapping("/add/{name}")
    @ResponseBody
    public String testAddUser(@PathVariable String name){
        User user=new User();
        user.setName(name);
        user.setPassword("123");
        user.setSalt("123");
        user.setHeadUrl("**");
        return String.valueOf(userService.addUser(user));
    }

    @RequestMapping("/get/{id}")
    @ResponseBody
    public User getUserById(@PathVariable int id){
        return userService.getUserById(id);
    }

    @RequestMapping("/initData")
    @ResponseBody
    public String initData(){
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userService.addUser(user);

            News news = new News();
            news.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime() + 1000*3600*5*i);
            news.setCreatedDate(date);
            news.setImage(String.format("http://images.nowcoder.com/head/%dm.png", random.nextInt(1000)));
            news.setLikeCount(i+1);
            news.setUserId(i+1);
            news.setTitle(String.format("TITLE{%d}", i));
            news.setLink(String.format("http://www.nowcoder.com/%d.html", i));
            newsService.addNews(news);
        }
        return "ok";
    }

    @RequestMapping("/addMessage")
    @ResponseBody
    public String addMessage(){
       for(int i=12;i<=22;i++){
           for(int j=0;j<3;j++){
               Message message=new Message();
               message.setFromId(i);
               message.setToId(j);
               message.setContent("user"+(i-12)+" to tofuchen:"+j);
               message.setCreatedDate(new Date());
               message.setHasRead(0);
               message.setConversationId(i+"_"+27);
               messageService.addMessage(message);
           }
       }
        return ToutiaoUtil.getJSONString(0);
    }


}
