package com.nowcoder.service.impl;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.dao.LoginTicketDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.service.UserService;
import com.nowcoder.util.ToutiaoUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by tofuc on 2018/6/12.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Autowired
    LoginTicketDao loginTicketDao;
    @Autowired
    EventProducer eventProducer;

    @Override
    public int addUser(User user) {
        return userDao.addUser(user);
    }

    @Override
    public User getUserById(int id) {
        return userDao.selectById(id);
    }

    public void updatePassword(User user){
        userDao.updatePassword(user);
    }

    public void deleteById(int id){
        userDao.deleteById(id);
    }

    public Map<String,Object> register(String username,String password){
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","用户名不能为空");
            return map;
        }
        User user=userDao.selectByUsername(username);
        if(user!=null){
            map.put("msgname","用户名已存在");
            return map;
        }
        user=new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(ToutiaoUtil.MD5(password+user.getSalt()));
        userDao.addUser(user);

        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String,Object> login(String username,String password){
        Map<String,Object> map=new HashMap<>();
        if(StringUtils.isBlank(username)){
            map.put("msgname","用户名不能为空");
            return map;
        }
        if(StringUtils.isBlank(password)){
            map.put("msgpwd","用户名不能为空");
            return map;
        }
        User user=userDao.selectByUsername(username);
        if(user!=null){
            if(!user.getPassword().equals(ToutiaoUtil.MD5(password+user.getSalt()))){
                eventProducer.fireEvent(new EventModel().setType(EventType.LOGIN).setExt("to",user.getName()).setExt("username",user.getName()));
                map.put("msgname","用户名或密码错误");
                return map;
            }
            String ticket=addLoginTicket(user.getId());
            map.put("ticket",ticket);
            map.put("userId",user.getId());
            return map;
        }else{
            map.put("msgname","用户名或密码错误");
            return map;
        }
    }

    private String addLoginTicket(int id){
        LoginTicket loginTicket=new LoginTicket();
        loginTicket.setUserId(id);
        Date date=new Date();
        date.setTime(date.getTime()+3600*24*1000);
        loginTicket.setExpired(date);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replace("-",""));
        loginTicketDao.addLoginTicket(loginTicket);
        return loginTicket.getTicket();
    }

    @Override
    public void updateStatus(String ticket, int status) {
        loginTicketDao.updateStatus(ticket,status);
    }
}
