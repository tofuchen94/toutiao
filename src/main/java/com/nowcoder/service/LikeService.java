package com.nowcoder.service;

/**
 * Created by tofuc on 2018/6/22.
 */
public interface LikeService {

    int getLikeStatus(int userId,int entityType,int entityId);
    long like(int userId,int entityType,int entityId);
    long disLike(int userId,int entityType,int entityId);
}
