package com.nowcoder.util;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * Created by tofuc on 2018/6/22.
 */
@Service
public class JedisAdapter implements InitializingBean{
    Logger logger= LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool jedisPool=null;

    @Override
    public void afterPropertiesSet() throws Exception {
        jedisPool=new JedisPool("localhost",6379);
    }

    private Jedis getJedis(){
        return jedisPool.getResource();
    }

    public String get(String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.get(key);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return null;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public void set(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            jedis.set(key,value);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long sadd(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.sadd(key,value);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long srem(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.srem(key,value);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public boolean sismember(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.sismember(key,value);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return false;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long scard(String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.scard(key);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public void setex(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            jedis.setex(key,10,value);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public long lpush(String key,String value){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.lpush(key,value);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return 0;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout,String key){
        Jedis jedis=null;
        try{
            jedis=jedisPool.getResource();
            return jedis.brpop(timeout,key);
        }catch(Exception e){
            logger.error("发生异常"+e.getMessage());
            return null;
        }finally{
            if(jedis!=null){
                jedis.close();
            }
        }
    }

    public void setObject(String key,Object object){
        set(key, JSON.toJSONString(object));
    }

    public <T> T getObject(String key,Class<T> clazz){
        String value=get(key);
        if(value!=null){
            return JSON.parseObject(value,clazz);
        }
        return null;
    }

}
