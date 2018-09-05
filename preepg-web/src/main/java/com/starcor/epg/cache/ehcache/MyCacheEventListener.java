package com.starcor.epg.cache.ehcache;

import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

/**
 * Created by xiaoqiang on 2017/6/28.
 */
public class MyCacheEventListener implements CacheEventListener {

    public void notifyElementRemoved(Ehcache ehcache, Element element) throws CacheException {
        System.out.println("Removed");
    }

    public void notifyElementPut(Ehcache ehcache, Element element) throws CacheException {
        System.out.println("put");
    }

    public void notifyElementUpdated(Ehcache ehcache, Element element) throws CacheException {
        System.out.println("updated");
    }

    public void notifyElementExpired(Ehcache ehcache, Element element) {
        System.out.println("Expired");
    }

    public void notifyElementEvicted(Ehcache ehcache, Element element) {
        System.out.println("Evicted");
    }

    public void notifyRemoveAll(Ehcache ehcache) {
        System.out.println("removeAll");
    }

    public Object clone() throws CloneNotSupportedException {
        return null;
    }

    public void dispose() {
        System.out.println("dispse");
    }
}
