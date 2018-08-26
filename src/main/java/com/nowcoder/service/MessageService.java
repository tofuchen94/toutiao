package com.nowcoder.service;

import com.nowcoder.model.Message;

import java.util.List;

/**
 * Created by tofuc on 2018/6/20.
 */
public interface MessageService {
    int addMessage(Message message);
    List<Message> getConversationList(int userId,int offset,int limit);
    List<Message> getConversationDetail(String conversationId,int offset,int limit);
    int getUnreadCount(int userId,String conversationId);
    void readMessage(int userId,String conversationId);

}
