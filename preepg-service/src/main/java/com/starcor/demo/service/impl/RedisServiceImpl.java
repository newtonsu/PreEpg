package com.starcor.demo.service.impl;

import com.starcor.demo.enty.Result;
import com.starcor.demo.redis.RedisManager;
import com.starcor.demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @desc 消息服务实现
 * @author lj
 * @date 2018/3/21 12:19
 */
@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private RedisManager redisManager;

    public Result sendMsg(String key, String value) throws Exception{

        redisManager.set(key,value);
        Result result = new Result();
        result.setKey(key);
        result.setValue(value);
        return result;
        //调用 JedisClusterClient 中的方法
//        JedisClusterClient jedisClusterClient = JedisClusterClient.getInstance();
//        try {
//            jedisClusterClient.SaveRedisCluster(key,value);
//        }catch (Exception e){
//            throw e;
//        }
    }
}
