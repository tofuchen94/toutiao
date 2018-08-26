package com.nowcoder.service.impl;

import com.nowcoder.dao.MessageDao;
import com.nowcoder.model.Message;
import com.nowcoder.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tofuc on 2018/6/20.
 */
@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    MessageDao messageDao;

    @Override
    public int addMessage(Message message) {
        return messageDao.addMessage(message);
    }

    @Override
    public List<Message> getConversationList(int userId, int offset, int limit) {
        return messageDao.getConversationList(userId,offset,limit);
    }

    @Override
    public List<Message> getConversationDetail(String conversationId, int offset, int limit) {
        return messageDao.getConversationDetail(conversationId,offset,limit);
    }

    @Override
    public int getUnreadCount(int userId, String conversationId) {
        return messageDao.getConversationUnReadCount(userId,conversationId);
    }

    @Override
    public void readMessage(int userId, String conversationId) {
        messageDao.readMessage(userId,conversationId);
    }
}
