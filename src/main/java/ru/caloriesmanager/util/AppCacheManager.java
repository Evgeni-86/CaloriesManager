package ru.caloriesmanager.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.interceptor.SimpleKey;
import org.springframework.stereotype.Component;
import ru.caloriesmanager.model.User;

import java.lang.management.ManagementFactory;
import java.util.Iterator;
import java.util.List;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheManager;
import javax.management.MBeanServer;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;


@Component
public class AppCacheManager {

    @Autowired
    public CacheManager cacheManager;

    private AppCacheManager() {
        System.out.println("Cache Initialized");
    }

    public Cache<Integer, User> getUserCache(){
        return this.cacheManager.getCache("UserCache", Integer.class, User.class);
    }

    public Cache<SimpleKey, List> getUserListCache(){
        return this.cacheManager.getCache("UserListCache",
                org.springframework.cache.interceptor.SimpleKey.class, List.class);
    }

    public String getStatistics(Cache<? extends Object, ? extends Object> cache) {
        try {
            StringBuffer b = new StringBuffer();
            ObjectName objectName = getJMXObjectName(cache);
            MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

            // printing retrieved cache statistics to console.
            for (CacheStatistics cacheStatistic : CacheStatistics.values()) {
                b.append(cacheStatistic).append("=").append(mBeanServer.getAttribute(objectName, cacheStatistic.name())).append("\n");
            }
            return b.toString();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ObjectName getJMXObjectName(Cache<? extends Object, ? extends Object> cache){
        MBeanServer mBeanServer = ManagementFactory.getPlatformMBeanServer();

        // Refer to org.ehcache.jsr107.Eh107CacheStatisticsMXBean.Eh107CacheStatisticsMXBean(String, URI, StatisticsService)
        // and org.ehcache.jsr107.Eh107MXBean.Eh107MXBean(String, URI, String)
        final String beanName = "CacheStatistics";
        String cacheManagerName = sanitize(cache.getCacheManager().getURI().toString());
        String cacheName = sanitize(cache.getName());
        ObjectName objectName = null;
        try {
            objectName = new ObjectName(
                    "javax.cache:type=" + beanName + ",CacheManager=" + cacheManagerName + ",Cache=" + cacheName);
        }
        catch (MalformedObjectNameException e) {
            throw new CacheException(e);
        }

        if(!mBeanServer.isRegistered(objectName)){
            throw new CacheException("No MBean found with ObjectName => " + objectName.getCanonicalName());
        }

        return objectName;
    }

    private String sanitize(String string) {
        return ((string == null) ? "" : string.replaceAll(",|:|=|\n", "."));
    }

    public <K extends Object, V extends Object> long getSize(Cache<K, V> cache) {
        Iterator<Cache.Entry<K, V>> itr = cache.iterator();
        long count = 0;
        while(itr.hasNext()){
            itr.next();
            count++;
        }
        return count;
    }

    public <K extends Object, V extends Object> String dump(Cache<K, V> cache) {
        Iterator<Cache.Entry<K, V>> itr = cache.iterator();
        StringBuffer b = new StringBuffer();
        while(itr.hasNext()){
            Cache.Entry<K, V> entry = itr.next();
            b.append(entry.getKey() + "=" + entry.getValue() + "\n");
        }
        return b.toString();
    }

    /**
     * Defining cache statistics parameters as constants.
     */
    private enum CacheStatistics {
        CacheHits, CacheHitPercentage,
        CacheMisses, CacheMissPercentage,
        CacheGets, CachePuts, CacheRemovals, CacheEvictions,
        AverageGetTime, AveragePutTime, AverageRemoveTime
    }

}