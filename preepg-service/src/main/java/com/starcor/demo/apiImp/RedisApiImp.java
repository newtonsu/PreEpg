package com.starcor.demo.apiImp;

import com.starcor.demo.api.RedisApi;
import com.starcor.demo.enty.Result;
import com.starcor.demo.enty.ResultDto;
import com.starcor.demo.service.RedisService;
import com.starcor.demo.utils.BeanCopyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("redisApi")
public class RedisApiImp implements RedisApi {
    private Logger logger = LoggerFactory.getLogger(RedisApiImp.class);

    @Autowired
    private RedisService redisService;

    @Override
    public ResultDto getRedis(String key,String value) {
        ResultDto resultDto = new ResultDto();
        try{
            Result r = redisService.sendMsg(key,value);

            BeanCopyUtils.copyProperties(r,resultDto);
        }catch (Exception e){
            logger.info(e.getMessage());
        }
        return resultDto;
    }
}
