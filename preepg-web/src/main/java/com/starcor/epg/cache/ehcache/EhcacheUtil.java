package com.starcor.epg.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.config.CacheConfiguration;

import java.net.URL;

/***
 * Ehcache缓存工具类ddd
 */
public class EhcacheUtil {

    private static final String path = "/cache/ehcache.xml";

    private URL url;

    private CacheManager manager;

    private static EhcacheUtil ehCache;

    private EhcacheUtil(String path) {
        url = getClass().getResource(path);
        manager = CacheManager.create(url);
    }

    public static EhcacheUtil getInstance() {
        if (ehCache== null) {
            ehCache= new EhcacheUtil(path);
        }
        return ehCache;
    }

    public void put(String cacheName, String key, Object value) {
        Cache cache = manager.getCache(cacheName);
        Element element = new Element(key, value);
        cache.put(element);
    }

    public Object get(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        Element element = cache.get(key);
        return element == null ? null : element.getObjectValue();
    }

    public Cache get(String cacheName) {
        return manager.getCache(cacheName);
    }

    public void remove(String cacheName, String key) {
        Cache cache = manager.getCache(cacheName);
        cache.remove(key);
    }

    public void addCache(String cacheName){
        CacheConfiguration config = new CacheConfiguration();
        config.setName(cacheName);
        config.setTimeToIdleSeconds(60);
        config.setTimeToLiveSeconds(120);
        config.setMaxEntriesLocalHeap(1000);
        config.setMaxEntriesLocalDisk(10000);
        manager.addCache(new Cache(config));
    }

    public void removeCache(String cacheName){
        manager.removeCache(cacheName);
    }
}