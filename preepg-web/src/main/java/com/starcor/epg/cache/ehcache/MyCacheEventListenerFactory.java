package com.starcor.epg.cache.ehcache;

import net.sf.ehcache.event.CacheEventListener;
import net.sf.ehcache.event.CacheEventListenerFactory;

import java.util.Properties;

/**
 * Created by xiaoqiang on 2017/6/28.
 */
public class MyCacheEventListenerFactory extends CacheEventListenerFactory {

    private CacheEventListener myCacheEventListener;

    public CacheEventListener createCacheEventListener(Properties properties) {
        if(myCacheEventListener == null){
            myCacheEventListener = new MyCacheEventListener();
        }
        return myCacheEventListener;
    }
}
