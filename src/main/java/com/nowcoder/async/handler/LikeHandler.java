package com.nowcoder.async.handler;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by tofuc on 2018/6/24.
 */
@Component
public class LikeHandler implements EventHandler {

    @Autowired
    MessageService messageService;
    @Autowired
    UserService userService;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message=new Message();
        User user=userService.getUserById(eventModel.getActorId());
        message.setToId(eventModel.getEntityOwnerId());
        message.setCreatedDate(new Date());
        message.setContent("用户" + user.getName() +
                " 赞了你的资讯,http://127.0.0.1:8080/news/"
                + String.valueOf(eventModel.getEntityId()));
        //系统账户
        message.setFromId(3);
        message.setConversationId("3_"+message.getToId());
        message.setHasRead(0);
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LIKE);
    }
}
