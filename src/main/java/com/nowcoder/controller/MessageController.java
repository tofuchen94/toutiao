package com.nowcoder.controller;

import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tofuc on 2018/6/20.
 */
@Controller
@RequestMapping("/msg")
public class MessageController {
    private static Logger logger= LoggerFactory.getLogger(MessageController.class);
    @Autowired
    HostHolder hostHolder;
    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @RequestMapping("/detail")
    public String conversationDetail(Model model, @RequestParam("conversationId") String conversationId){
        try{
            List<ViewObject> vos=new ArrayList<>();
            List<Message> messages=messageService.getConversationDetail(conversationId,0,10);
            for(Message message:messages){
                ViewObject vo=new ViewObject();
                vo.set("message",message);
                User user=userService.getUserById(message.getFromId());
                if(user==null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userName",user.getName());
                vos.add(vo);
            }
            model.addAttribute("vos",vos);
            messageService.readMessage(hostHolder.getUser().getId(),conversationId);
        }catch(Exception e){
            logger.error("获取站内信列表失败"+e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping("/list")
    public String conversationList(Model model){
        try{
            int userId=hostHolder.getUser().getId();
            List<ViewObject> vos=new ArrayList<>();
            List<Message> messages=messageService.getConversationList(userId,0,10);
            for(Message message:messages){
                ViewObject vo=new ViewObject();
                vo.set("conversation",message);
                int targetId=message.getFromId()==userId?message.getToId():message.getFromId();
                User user=userService.getUserById(targetId);
                if(user==null){
                    continue;
                }
                vo.set("headUrl",user.getHeadUrl());
                vo.set("userName",user.getName());
                vo.set("targetId",targetId);
                vo.set("totalCount",message.getId());
                vo.set("unreadCount",messageService.getUnreadCount(userId,message.getConversationId()));
                vos.add(vo);
            }
            model.addAttribute("vos",vos);
        }catch(Exception e){
            logger.error("获取站内信列表失败"+e.getMessage());
        }
        return "letter";
    }

}
