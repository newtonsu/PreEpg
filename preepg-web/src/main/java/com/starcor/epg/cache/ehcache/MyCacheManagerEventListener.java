package com.starcor.epg.cache.ehcache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Status;
import net.sf.ehcache.event.CacheManagerEventListener;

/**
 * Created by xiaoqiang on 2017/6/28.
 */
public class MyCacheManagerEventListener implements CacheManagerEventListener {
    public void init() throws CacheException {

    }

    public Status getStatus() {
        return null;
    }

    public void dispose() throws CacheException {

    }

    public void notifyCacheAdded(String s) {
        System.out.println("add cache: " + s);
    }

    public void notifyCacheRemoved(String s) {
        System.out.println("remove cache: " + s);
    }
}
