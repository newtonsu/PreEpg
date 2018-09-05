package com.starcor.epg.service.impl;

import com.starcor.epg.service.MessageService;
import org.springframework.stereotype.Service;

/**
 * @desc 消息服务实现
 * @author lj
 * @date 2018/3/21 12:19
 */
@Service
public class MessageServiceImpl implements MessageService {

//    @Autowired
//    private RedisManager redisManager;
    public void sendMsg(String key,String value) throws Exception{

//        redisManager.set(key,value);
        //调用 JedisClusterClient 中的方法
//        JedisClusterClient jedisClusterClient = JedisClusterClient.getInstance();
//        try {
//            jedisClusterClient.SaveRedisCluster(key,value);
//        }catch (Exception e){
//            throw e;
//        }
    }
}
