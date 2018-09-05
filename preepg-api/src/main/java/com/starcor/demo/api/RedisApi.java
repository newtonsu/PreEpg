package com.starcor.demo.api;

import com.starcor.demo.enty.ResultDto;

public interface  RedisApi {
    ResultDto getRedis(String key,String value);
}
