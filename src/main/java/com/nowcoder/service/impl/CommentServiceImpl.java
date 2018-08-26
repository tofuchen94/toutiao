package com.nowcoder.service.impl;

import com.nowcoder.dao.CommentDao;
import com.nowcoder.model.Comment;
import com.nowcoder.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by tofuc on 2018/6/20.
 */
@Service
public class CommentServiceImpl implements CommentService {
    @Autowired
    CommentDao commentDao;
    @Override
    public List<Comment> getCommentsByEntity(int entityId, int entityType) {
        return commentDao.selectByEntity(entityId,entityType);
    }

    @Override
    public int getCommentCount(int entityId, int entityType) {
        return commentDao.selectCountByEntity(entityId,entityType);
    }

    @Override
    public int addComment(Comment comment) {
        return commentDao.addComment(comment);
    }
}
