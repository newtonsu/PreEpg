package com.starcor.demo.redis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.*;
import redis.clients.util.JedisClusterCRC16;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @desc redis管理工具
 * @author lj
 * @date 2017/10/13 9:39
 */
@Data
@Repository("redisManager")
@Slf4j
public class RedisManager {

    public static final String OK="OK";//执行结果OK
    private static final int DEFAULT_EXPIRE_TIME = 300;//second

    @Autowired
    private JedisCluster jedisCluster;

    /**
     * 获取key
     * @param pattern
     * @return
     */
    public List<String> keys(String pattern){
        long s = System.currentTimeMillis();
        Set<String> keys = new TreeSet<>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for(String k : clusterNodes.keySet()){
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            try {
                connection.keys(pattern);
                keys.addAll(connection.keys(pattern));
            } catch(Exception e){
                log.error("Getting keys error: {}", e);
            } finally{
                connection.close();//用完一定要close这个链接！！！
            }
        }
        System.out.println("总耗时::: " + (System.currentTimeMillis() - s));
        return keys.stream().collect(Collectors.toList());
    }
    /**
     * 获取key
     * @param pattern
     * @return
     */
    public List<String> scan(String pattern){
        long s = System.currentTimeMillis();
        Set<String> keys = new TreeSet<>();
        Map<String, JedisPool> clusterNodes = jedisCluster.getClusterNodes();
        for(String k : clusterNodes.keySet()){
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = jp.getResource();
            String cursor = "0";
            try {
                do{
                    ScanParams params = new ScanParams().count(100).match(pattern);
                    ScanResult<String> result = connection.scan(cursor,params);
                    List<String> tempKeys = result.getResult();
                    cursor = result.getStringCursor();
                    if (!CollectionUtils.isEmpty(tempKeys)){
                        keys.addAll(tempKeys);
                    }
                }while (!"0".equals(cursor));

            } catch(Exception e){
                log.error("scan keys error: {}", e);
            } finally{
                connection.close();//用完一定要close这个链接！！！
            }
        }
        System.out.println("总耗时::: " + (System.currentTimeMillis() - s));
        return keys.stream().collect(Collectors.toList());
    }

    /**
     * 设置String类型的值
     * @param key
     * @param value
     * @return
     */
    public String set(String key, String value) {
        return jedisCluster.setex(key, DEFAULT_EXPIRE_TIME, value);
    }

    /**
     * 设置String类型的值并设置过期时间
     * @param key
     * @param seconds
     * @param value
     * @return
     */
    public String setAndExpire(String key,String value,int seconds) {
        if(seconds == -1){
            return jedisCluster.set(key,value);
        }
        return jedisCluster.setex(key, seconds, value);
    }

    /**
     * 为指定键设置过期时间
     * @param key
     * @param seconds
     * @return
     */
    public Long expire(String key, int seconds) {
        return jedisCluster.expire(key, seconds);
    }

    /**
     * 获取String类型的值
     * @param key
     * @return
     */
    public String get(String key) {
        return jedisCluster.get(key);
    }

    /**
     * 删除指定键
     * @param key
     * @return
     */
    public Long del(String key) {
        return jedisCluster.del(key);
    }

    /**
     * 批量删除key
     * @param keys
     */
    public void batchDel(List<String> keys){
        for (String key: keys) {
            this.del(key);
        }
    }

    /**
     * 获取json序列化的对象
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T getForObject(String key, Class<T> cls) {
        String obj = jedisCluster.get(key);
        if (obj != null) {
            return JSONObject.parseObject(obj, cls);
        } else {
            return null;
        }
    }

    /**
     * 存储对象
     * 序列化方式为:json
     *
     * @param key
     * @param value
     * @return
     */
    public String setForObject(String key, Object value) {
        return jedisCluster.setex(key, DEFAULT_EXPIRE_TIME, JSON.toJSONString(value));
    }
    /**
     * 存储对象并设置过期时间
     * 序列化方式为:json
     *
     * @param key
     * @param value
     * @return
     */
    public String setForObjectAndExpire(String key, Object value,int second) {
        if(-1 == second){
            return jedisCluster.set(key,JSON.toJSONString(value));
        }
        return jedisCluster.setex(key, second, JSON.toJSONString(value));
    }

    /**
     * 从对象map中获取对象
     * store mode is hash
     *
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public <T> T getForObjectMap(String key, Class<T> cls) {
        Map<String, String> map = getForValueMap(key);
        if (!CollectionUtils.isEmpty(map)) {
            try {
                T obj = cls.newInstance();
                BeanUtils.populate(obj, map);
                return obj;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        } else {
            return null;
        }
    }

    /**
     * 以map形式存储对象
     * store mode is hash
     *
     * @param key
     * @param value
     * @return
     */
    public String setForObjectMap(String key, Object value) {
        try {
            Map<String, String> valueMap = BeanUtils.describe(value);
            Set<Map.Entry<String,String>> set=valueMap.entrySet();
            Iterator<Map.Entry<String, String>> iterator = set.iterator();
            while (iterator.hasNext()){
                Map.Entry<String,String> entry=iterator.next();
                if(entry.getValue() == null){
                    iterator.remove();
                }
            }
            valueMap.remove("class");
            return setForValueMap(key, valueMap);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取对象列表
     *
     * @param key
     * @param cls
     * @param <T>
     * @return
     */
    public <T> List<T> getForObjectList(String key, Class<T> cls) {
        List<String> obj = jedisCluster.lrange(key, 0, -1);
        List<T> resultList = null;
        if (obj != null && obj.size() > 0) {
            resultList = new ArrayList<T>();
            for (String str : obj) {
                resultList.add(JSONObject.parseObject(str, cls));
            }
        }
        return resultList;
    }

    /**
     * 获取在哈希表中指定 key 的所有字段和值
     *
     * @param key
     * @return
     */
    public Map<String, String> getForValueMap(String key) {
        Map<String, String> obj = jedisCluster.hgetAll(key);
        return obj;
    }

    /**
     * 存储map,存储类型为hash
     *
     * @param key
     * @param mapValue
     */
    public String setForValueMap(String key, Map<String, String> mapValue) {
        String result = jedisCluster.hmset(key, mapValue);
        expire(key, DEFAULT_EXPIRE_TIME);
        return result;
    }

    /**
     * 存储map并设置过期时间
     *
     * @param key
     * @param mapValue
     * @return
     */
    public Long setForValueMapAndExpire(String key, Map<String, String> mapValue, int seconds) {
        jedisCluster.hmset(key, mapValue);
        if(-1 == seconds){
            return -1L;
        }
        return expire(key, seconds);
    }

    /**
     * 将 key 中储存的数字值增一
     *
     * @param key
     * @return
     */
    public Long incr(String key) {
        return jedisCluster.incr(key);
    }

    /**
     * 将 key 所储存的值加上给定的增量值(stepLength)
     *
     * @param key
     * @param stepLength
     * @return
     */
    public Long incrBy(String key, Long stepLength) {
        return jedisCluster.incrBy(key, stepLength);
    }

    /**
     * 验证key是否存在
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    /**
     * 获取key的过期时间
     *
     * @param key
     * @return
     */
    public Long ttl(String key) {
        return jedisCluster.ttl(key);
    }

    /**
     * 向有序集合添加一个，或者更新已存在成员的分数
     *
     * @param key
     * @param value
     * @return
     */
    public Long setForSortedSet(String key, Double score, Object value, int seconds) {
        Long result = jedisCluster.zadd(key, score, JSONObject.toJSONString(value));
        if(-1 == seconds){
            return result;
        }
        expire(key, seconds);
        return result;
    }

    /**
     * 获取score大于minScore的token
     * @param key
     * @param minScore
     * @return
     */
    public Set<String> getForSortedSet(String key, Double minScore) {
        return jedisCluster.zrangeByScore(key, minScore, Double.MAX_VALUE);
    }

    /**
     * 删除set中的value元素
     *
     * @param key
     * @param value
     */
    public Long removeFromSortedSet(String key, String value) {
        return jedisCluster.zrem(key, value);
    }

    /**
     * 批量删除zset中的元素
     * @param keys
     * @return
     */
    public void batchRemoveFromSortedSet(Map<String,List<String>> keys) {
        keys.forEach((key,members)->{
            String[] memberArray=new String[members.size()];
            jedisCluster.zrem(key,members.toArray(memberArray));
        });
    }

    /**
     * 批量添加zset中的元素
     * @param keys
     * @param seconds
     */
    public void batchSetForSortedSet(Map<String,Map<String, Double>> keys, int seconds) {
        keys.forEach((key,members)->{
            jedisCluster.zadd(key,members);

            if(-1 != seconds){
                expire(key, seconds);
            }
        });
    }

    /**
     * 添加set元素
     * @param key
     * @param seconds
     * @param members
     */
    public void sadd(String key,int seconds,String ... members) {
        jedisCluster.sadd(key,members);
        expire(key,seconds);
    }

    /**
     * 获取set成员
     * @param key
     * @return
     */
    public Set<String> smembers(String key) {
        return jedisCluster.smembers(key);
    }

    /**
     * 设置map中某个属性的值
     * @param key
     * @param field
     * @param value
     */
    public Long hset(String key, String field, String value) {
        Long result = jedisCluster.hset(key, field, value);
        expire(key,DEFAULT_EXPIRE_TIME);
        return result;
    }
    /**
     * 获取hash中某个属性的值
     * @param key
     * @param field
     */
    public String hget(String key, String field) {
        return jedisCluster.hget(key, field);
    }

    /**
     * 设置map中多个属性的值
     * @param key
     * @param map
     * @return
     */
    public String hmset(String key, Map<String,String> map) {
        String result = jedisCluster.hmset(key, map);
        expire(key,DEFAULT_EXPIRE_TIME);
        return result;
    }
    /**
     * 设置map中多个属性的值
     * @param key
     * @param map
     * @return
     */
    public String hmsetAndExpire(String key, Map<String,String> map,int second) {
        String result = jedisCluster.hmset(key, map);
        expire(key,second);
        return result;
    }

    /**
     * 获取列表长度
     * @param key
     * @return
     */
    public Long llen(String key) {
        return jedisCluster.llen(key);
    }

    /**
     * 将给定 key 的值设为 value ，并返回 key 的旧值(old value)
     * @param key
     * @param value
     * @return
     */
    public String getSet(String key, String value) {
        return jedisCluster.getSet(key, value);
    }

    /**
     * 在列表中添加一个或多个值
     * @param key
     * @param value
     * @return
     */
    public Long rpush(String key, String value) {
        Long result = jedisCluster.rpush(key, value);
        expire(key,DEFAULT_EXPIRE_TIME);
        return result;
    }

    /**
     * 对一个列表进行修剪(trim)，列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除
     * @param key
     * @param start
     * @param end
     * @return
     */
    public String ltrim(String key,long start,long end){
        return jedisCluster.ltrim(key,start,end);
    }

    /**
     * 获取列表数据
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key,long start,long end){
        return jedisCluster.lrange(key, start, end);
    }

    /**
     * 根据数组索引获取数据
     * @param key
     * @param index
     * @return
     */
    public String lindex(String key,long index){
        return jedisCluster.lindex(key,index);
    }


    /**
     * 批量插入键值对并设置过期时间
     * @param keyValueMap
     * @param second
     */
    public void batchSetAndExpire(Map<String,String> keyValueMap,int second){
        Map<JedisPool,List<Map.Entry<String, String>>> jedisAndData = new HashMap<>();//jedis和keys的映射
        //分配key到对应的jedis
        keyValueMap.entrySet().forEach(entry -> {
            JedisPool jedisPool = getJedisByKey(entry.getKey());
            if(jedisAndData.containsKey(jedisPool)){
                jedisAndData.get(jedisPool).add(entry);
            }else {
                List<Map.Entry<String, String>> data = new ArrayList<>();
                data.add(entry);
                jedisAndData.put(jedisPool,data);
            }
        });
        //插入数据
        jedisAndData.forEach((jedisPool,data) -> batchInsert(jedisPool,data,second));
    }

    /**
     * 批量写入
     * @param jedisPool
     * @param data
     * @param second
     */
    private void batchInsert(JedisPool jedisPool, List<Map.Entry<String, String>> data, int second){
        int listSize = data.size();
        int batchCount  = 500;
        int times = listSize % batchCount==0 ? listSize / batchCount : (listSize / batchCount + 1);//循环次数
        Jedis jedis = jedisPool.getResource();
        for (int i = 0; i < times; i++) {
            List<Map.Entry<String,String>> tempList = data.subList(i * batchCount, (i + 1) * batchCount > listSize ? listSize : (i + 1) * batchCount);
            Pipeline pipeline = jedis.pipelined();
            pipeline.clear();
            tempList.forEach(entry -> {
                if (second == -1){
                    pipeline.set(entry.getKey(),entry.getValue());
                }else {
                    pipeline.setex(entry.getKey(),second,entry.getValue());
                }
            });
            pipeline.sync();
        }
        if (jedis != null){
            jedis.close();
        }
    }

    private TreeMap<Long, String> slotHostMap;//slot和host之间的映射
    private Map<String, JedisPool> nodeMap;//节点和redis映射

    /**
     * 通过key获取jedis实例
     * @param key
     * @return
     */
    private JedisPool getJedisByKey(String key){
        int slot = JedisClusterCRC16.getSlot(key);
        Map.Entry<Long, String> longStringEntry = slotHostMap.lowerEntry(Long.valueOf(slot + 1));
        if (longStringEntry == null){
            log.error("entry为空!!!!! key为：" + key + "，slot为：" + slot);
            return null;
        }
        return nodeMap.get(longStringEntry.getValue());
    }

    /**
     * 获取slot和host之间的对应关系
     * @return
     */
    private void initSlotHostMap() {
        nodeMap = jedisCluster.getClusterNodes();
        String anyHost = nodeMap.keySet().iterator().next();
        TreeMap<Long, String> tree = new TreeMap<Long, String>();
        String parts[] = anyHost.split(":");
        HostAndPort anyHostAndPort = new HostAndPort(parts[0], Integer.parseInt(parts[1]));
        try {
            Jedis jedis = new Jedis(anyHostAndPort.getHost(), anyHostAndPort.getPort());
            List<Object> list = jedis.clusterSlots();
            for (Object object : list) {
                List<Object> list1 = (List<Object>) object;
                List<Object> master = (List<Object>) list1.get(2);
                String hostAndPort = new String((byte[]) master.get(0)) + ":" + master.get(1);
                tree.put((Long) list1.get(0), hostAndPort);
                tree.put((Long) list1.get(1), hostAndPort);
            }
            jedis.close();
        } catch (Exception e) {
            log.error("获取slot和host之间的对应关系",e);
        }
        slotHostMap = tree;
    }

    @PostConstruct
    public void afterPropertiesSet() throws Exception {
        initSlotHostMap();
    }

}
