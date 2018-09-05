package com.starcor.demo.utils;

import org.springframework.beans.BeanUtils;

/**
 * @desc 对象拷贝工具
 * @author lj
 * @date 2017/12/11 16:26
 */
public class BeanCopyUtils {

    /**
     * 对象属性拷贝
     * @param source
     * @param target
     * @param <T>
     * @return
     */
    public static <T> T copyProperties(Object source,T target){
        BeanUtils.copyProperties(source,target);
        return target;
    }

}
