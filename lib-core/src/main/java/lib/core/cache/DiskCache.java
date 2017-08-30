package lib.core.cache;

import android.support.v4.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lib.core.ExAppConfig;
import lib.core.security.MD5;
import lib.core.utils.ExCommonUtil;
import lib.core.utils.ExSharePreferencesUtil;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public class DiskCache {

    private static final int ENTRY_BODY = 0;
    private DiskLruCache diskLruCache;

    private static class DiskCacheHolder {
        private static final DiskCache dc = new DiskCache();
    }

    public static DiskCache getInstance() {
        DiskCache diskCache = DiskCacheHolder.dc;
        diskCache.setPersistTime(-1);
        return diskCache;
    }

    private DiskCache() {
        if (diskLruCache == null || diskLruCache.isClosed()) {
            diskLruCache = DiskLruCache.create(FileSystem.SYSTEM, new File(ExAppConfig.appHttpCachePath), BuildConfig.VERSION_CODE, 1, 10 * 1024 * 1024);
        }
    }

    public void setPersistTime(long persistTime) {
        diskLruCache.setPersistTime(persistTime);
    }

    public String getCache(String key) {
        key = key.toLowerCase();
        final DiskLruCache.Snapshot snapshot;
        BufferedSource bodySource = null;
        String cacheResult = "";
        try {
            snapshot = diskLruCache.get(key);
            if (snapshot == null) {
                return cacheResult;
            } else {
                Source source = snapshot.getSource(ENTRY_BODY);
                bodySource = Okio.buffer(new ForwardingSource(source) {
                    @Override
                    public void close() throws IOException {
                        snapshot.close();
                        super.close();
                    }
                });
                cacheResult = bodySource.readUtf8();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bodySource != null) {
                try {
                    bodySource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cacheResult;
    }

    public byte[] getCacheForByte(String key) {
        key = key.toLowerCase();
        final DiskLruCache.Snapshot snapshot;
        BufferedSource bodySource = null;
        byte[] cacheResult = null;
        try {
            snapshot = diskLruCache.get(key);
            if (snapshot == null) {
                return cacheResult;
            } else {
                Source source = snapshot.getSource(ENTRY_BODY);
                bodySource = Okio.buffer(new ForwardingSource(source) {
                    @Override
                    public void close() throws IOException {
                        snapshot.close();
                        super.close();
                    }
                });
                cacheResult = bodySource.readByteArray();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bodySource != null) {
                try {
                    bodySource.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return cacheResult;
    }

    public String makeKey(String url, String params) {
        try {
            URL uri = new URL(url);
            url = uri.getFile();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return MD5.encrypt(url + params);
    }

    public void saveCache(String key, String result) {
        key = key.toLowerCase();
        removeCache(key);
        BufferedSink bufferedSink = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = diskLruCache.edit(key);
            Sink sink = editor.newSink(ENTRY_BODY);
            bufferedSink = Okio.buffer(sink);
            bufferedSink.writeUtf8(result);
            editor.commit();
            bufferedSink.flush();
        } catch (IOException e) {
            try {
                editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (bufferedSink != null) {
                try {
                    bufferedSink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    public void saveCache(String key, String groupId, String result) {
        key = key.toLowerCase();
        saveCache(key, result);
        String group = ExSharePreferencesUtil.getInstance().getString(groupId);
        if(ExCommonUtil.isEmpty(group)) {
            ExSharePreferencesUtil.getInstance().putString(groupId, key);
        } else {
            ExSharePreferencesUtil.getInstance().putString(groupId, group + "#" + key);
        }
    }

    public void saveCache(String key, byte[] result) {
        key = key.toLowerCase();
        removeCache(key);
        BufferedSink bufferedSink = null;
        DiskLruCache.Editor editor = null;
        try {
            editor = diskLruCache.edit(key);
            Sink sink = editor.newSink(ENTRY_BODY);
            bufferedSink = Okio.buffer(sink);
            bufferedSink.write(result);
            editor.commit();
            bufferedSink.flush();
        } catch (IOException e) {
            try {
                editor.abort();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            if (bufferedSink != null) {
                try {
                    bufferedSink.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }


    public void removeCache(String key) {
        key = key.toLowerCase();
        try {
            diskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeCacheByGroup(String groupId) {
        String group = ExSharePreferencesUtil.getInstance().getString(groupId);
        if(!ExCommonUtil.isEmpty(group) && group.contains("#")) {
            String[] keys = group.split("#");
            for (String key : keys) {
                removeCache(key);
            }
        } else if(!ExCommonUtil.isEmpty(group)) {
            removeCache(group);
        }
    }

}
