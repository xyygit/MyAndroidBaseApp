package lib.core.cache;

public class MemoryCache {

    private MemoryLruCache<String, Object> mMemoryCache;
    private long persistTime = -1;

    private MemoryCache() {
        this.mMemoryCache = new MemoryLruCache(50);
    }

    private static class MemoryCacheHolder {
        private static final MemoryCache mc = new MemoryCache();
    }

    public static MemoryCache getInstance() {
        return MemoryCacheHolder.mc;
    }

    public Object get(String key) {
        return mMemoryCache.get(key);
    }

    public void put(String key, String result) {
        if(persistTime != -1) {
            mMemoryCache.put(key, result, persistTime);
        } else {
            mMemoryCache.put(key, result);
        }
    }

    public void put(String key, byte[] result) {
        if(persistTime != -1) {
            mMemoryCache.put(key, result, persistTime);
        } else {
            mMemoryCache.put(key, result);
        }
    }

    public void remove(String key) {
        mMemoryCache.remove(key);
    }

    public void setPersistTime(long persistTime) {
        this.persistTime = persistTime + System.currentTimeMillis();
    }
}
