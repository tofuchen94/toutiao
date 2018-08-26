package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import com.nowcoder.util.MailSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by tofuc on 2018/6/24.
 */
@Component
public class LoginExceptionHandler implements EventHandler {

    @Autowired
    MessageService messageService;

    @Autowired
    MailSender mailSender;

    @Override
    public void doHandle(EventModel eventModel) {
        Message message=new Message();
        message.setToId(eventModel.getActorId());
        message.setFromId(3);
        message.setContent("登录");
        message.setCreatedDate(new Date());
        message.setConversationId("3_"+message.getToId());
        message.setHasRead(0);
        messageService.addMessage(message);

        Map<String,Object> map=new HashMap<>();
        map.put("username", eventModel.getExt("username"));
        mailSender.sendWithHTMLTemplate(eventModel.getExt("to"),"用户登录异常",null,map);
    }

    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.LOGIN);
    }
}
