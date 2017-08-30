package lib.core.http.definition;

import android.support.v4.util.ArrayMap;

public interface Headers{

    public static final String HEAD_KEY_ACCEPT_CHARSET = "Accept-Ranges";

    public static final String HEAD_KEY_CONTENT_TYPE = "Content-Type";

    public static final String HEAD_KEY_CONTENT_LENGTH = "Content-Length";

    public static final String HEAD_KEY_CACHE_CONTROL = "Cache-Control";

    public static final String HEAD_KEY_DATE = "Date";

    public static final String HEAD_KEY_PRAGMA = "Pragma";

    void addHeader(String key, String value);

    void removeHeader(String key);

    void removeAll();

    String getValue(String key);

    ArrayMap<String, String> getHeaders();

}
