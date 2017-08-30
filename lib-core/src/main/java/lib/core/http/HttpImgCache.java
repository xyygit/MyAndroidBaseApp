package lib.core.http;


import lib.core.cache.ImageDiskCache;
import lib.core.cache.MemoryCache;
import lib.core.utils.ExCommonUtil;

public class HttpImgCache extends HttpCache {

    public HttpImgCache(String url, String params) {
        key = ImageDiskCache.getInstance().makeKey(url, params);
    }

    @Override
    public void setPersistTime(long persistTime) {
        ImageDiskCache.getInstance().setPersistTime(persistTime);
        MemoryCache.getInstance().setPersistTime(persistTime);
    }

    @Override
    public String getCache(String key) {
        String cacheResult = null;
        Object value = MemoryCache.getInstance().get(key);
        if (!ExCommonUtil.isEmpty(value)) {
            cacheResult = value.toString();
        }
        if (ExCommonUtil.isEmpty(cacheResult)) cacheResult = ImageDiskCache.getInstance().getCache(key);
        return cacheResult;
    }

    @Override
    public byte[] getImgCache(String key) {
        byte[] cacheResult = null;
        Object value = MemoryCache.getInstance().get(key);
        if (!ExCommonUtil.isEmpty(value)) {
            cacheResult = (byte[])value;
        }
        if (ExCommonUtil.isEmpty(cacheResult)) cacheResult = ImageDiskCache.getInstance().getCacheForByte(key);
        return cacheResult;
    }

    @Override
    public void saveCache(String key, String result) {
        ImageDiskCache.getInstance().saveCache(key, result);
        MemoryCache.getInstance().remove(key);
        MemoryCache.getInstance().put(key, result);
    }

    @Override
    public void saveCache(String key, byte[] result) {
        ImageDiskCache.getInstance().saveCache(key, result);
        MemoryCache.getInstance().remove(key);
        MemoryCache.getInstance().put(key, result);
    }

    public void removeCache(String key) {
        ImageDiskCache.getInstance().removeCache(key);
    }

}
