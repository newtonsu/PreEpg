package com.starcor.epg.cache.ehcache;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.event.CacheManagerEventListener;
import net.sf.ehcache.event.CacheManagerEventListenerFactory;

import java.util.Properties;

/**
 * Created by xiaoqiang on 2017/6/28.
 */
public class MyCacheManagerEventListenerFactory extends CacheManagerEventListenerFactory {

    private CacheManagerEventListener myCacheManagerEventListener =null;

    public CacheManagerEventListener createCacheManagerEventListener(CacheManager cacheManager, Properties properties) {
        if(myCacheManagerEventListener == null){
            myCacheManagerEventListener = new MyCacheManagerEventListener();
        }
        return myCacheManagerEventListener;
    }
}
