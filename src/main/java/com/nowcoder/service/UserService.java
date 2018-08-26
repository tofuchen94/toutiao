package com.nowcoder.service;

import com.nowcoder.model.User;

import java.util.Map;

/**
 * Created by tofuc on 2018/6/12.
 */
public interface UserService {

    int addUser(User user);

    User getUserById(int id);

    void updatePassword(User user);

    void deleteById(int id);

    Map<String,Object> register(String username,String password);

    Map<String,Object> login(String username,String password);

    void updateStatus(String ticket,int status);

}
