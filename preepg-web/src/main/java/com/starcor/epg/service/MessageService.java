package com.starcor.epg.service;

/**
 * @desc 广告消息服务接口
 * @author lj
 * @date 2018/3/21 12:18
 */
public interface MessageService {
    /**
     * 发送消息
     * @param
     */
    void sendMsg(String key,String value ) throws Exception;

}
