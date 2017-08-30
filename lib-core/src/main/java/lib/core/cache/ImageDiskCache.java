package lib.core.cache;

import android.support.v4.BuildConfig;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import lib.core.ExAppConfig;
import lib.core.security.MD5;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public class ImageDiskCache {

    private static final int ENTRY_BODY = 0;
    private DiskLruCache diskLruCache;

    private static class DiskCacheHolder {
        private static final ImageDiskCache dc = new ImageDiskCache();
    }

    public static ImageDiskCache getInstance() {
        return ImageDiskCache.DiskCacheHolder.dc;
    }

    private ImageDiskCache() {
        if (diskLruCache == null || diskLruCache.isClosed()) {
            diskLruCache = DiskLruCache.create(FileSystem.SYSTEM, new File(ExAppConfig.appImageCachePath), BuildConfig.VERSION_CODE, 1, 10 * 1024 * 1024);
        }
    }

    public void setPersistTime(long persistTime) {
        diskLruCache.setPersistTime(persistTime);
    }

    public String getCache(String key) {
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

    public void saveCache(String key, byte[] result) {
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
        try {
            diskLruCache.remove(key);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
