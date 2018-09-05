package com.starcor.demo.service;

import com.starcor.demo.enty.Result;

/**
 * @desc 广告消息服务接口
 * @author lj
 * @date 2018/3/21 12:18
 */
public interface RedisService {
    /**
     * 发送消息
     * @param
     */
    Result sendMsg(String key, String value) throws Exception;

}
