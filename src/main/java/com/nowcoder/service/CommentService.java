package com.nowcoder.service;

import com.nowcoder.model.Comment;

import java.util.List;

/**
 * Created by tofuc on 2018/6/20.
 */
public interface CommentService {
    List<Comment> getCommentsByEntity(int entityId,int entityType);
    int getCommentCount(int entityId,int entityType);
    int addComment(Comment comment);
}
