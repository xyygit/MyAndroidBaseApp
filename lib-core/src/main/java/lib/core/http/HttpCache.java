package lib.core.http;

import java.util.ArrayList;

import lib.core.cache.DiskCache;
import lib.core.cache.MemoryCache;
import lib.core.utils.ExCommonUtil;

public class HttpCache {

    private ArrayList<Integer> cacheStrategy;
    protected String key;

    public HttpCache(String url, String params) {
        key = DiskCache.getInstance().makeKey(url, params);
    }

    protected HttpCache() {}

    public String getKey() {
        return key;
    }

    public void setPersistTime(long persistTime) {
        DiskCache.getInstance().setPersistTime(persistTime);
        MemoryCache.getInstance().setPersistTime(persistTime);
    }

    public String getCache(String key) {
        String cacheResult = null;
        Object value = MemoryCache.getInstance().get(key);
        if (!ExCommonUtil.isEmpty(value)) {
            cacheResult = value.toString();
        }
        if (ExCommonUtil.isEmpty(cacheResult)) cacheResult = DiskCache.getInstance().getCache(key);
        return cacheResult;
    }

    public byte[] getImgCache(String key) {
        byte[] cacheResult = null;
        Object value = MemoryCache.getInstance().get(key);
        if (!ExCommonUtil.isEmpty(value)) {
            cacheResult = (byte[])value;
        }
        if (ExCommonUtil.isEmpty(cacheResult)) cacheResult = DiskCache.getInstance().getCacheForByte(key);
        return cacheResult;
    }

    public void saveCache(String key, String result) {
        DiskCache.getInstance().saveCache(key, result);
        MemoryCache.getInstance().remove(key);
        MemoryCache.getInstance().put(key, result);
    }

    public void saveCache(String key, byte[] result) {
        DiskCache.getInstance().saveCache(key, result);
        MemoryCache.getInstance().remove(key);
        MemoryCache.getInstance().put(key, result);
    }

    public void removeCache(String key) {
        DiskCache.getInstance().removeCache(key);
    }

    public void addCacheStrategy(int strategy) {
        if (cacheStrategy == null) {
            cacheStrategy = new ArrayList<Integer>();
        }
        cacheStrategy.add(strategy);
    }

    public boolean triggerCacheStrategy(int strategy) {
        if(cacheStrategy != null) {
            return cacheStrategy.contains(strategy);
        }
        return false;
    }

    public boolean hasCacheStrategy() {
        return !ExCommonUtil.isEmpty(cacheStrategy);
    }

    public void setCacheStrategy(ArrayList<Integer> cacheStrategy) {
        if (!ExCommonUtil.isEmpty(cacheStrategy)) {
            this.cacheStrategy = cacheStrategy;
        }
    }

}
