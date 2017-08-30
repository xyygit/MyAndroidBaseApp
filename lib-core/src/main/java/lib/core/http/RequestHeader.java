package lib.core.http;

import android.support.v4.util.ArrayMap;

import lib.core.http.definition.Headers;

public class RequestHeader implements Headers {

    public static final String HEAD_KEY_ACCEPT = "Accept";

    public static final String HEAD_KEY_ACCEPT_CHARSET = "Accept-Charset";

    public static final String HEAD_KEY_ACCEPT_ENCODING = "Accept-Encoding";

    public static final String HEAD_KEY_ACCEPT_LANGUAGE = "Accept-Language";

    public static final String HEAD_KEY_AUTHORIZATION = "Authorization";

    public static final String HEAD_KEY_CONNECTION = "Connection";

    public static final String HEAD_KEY_COOKIE = "Cookie";

    public static final String HEAD_KEY_EXPECT = "Expect";

    public static final String HEAD_KEY_FROM = "From";

    public static final String HEAD_KEY_HOST = "Host";

    public static final String HEAD_KEY_IF_MATCH = "If-Match";

    public static final String HEAD_KEY_IF_NONE_MATCH = "If-None-Match";

    public static final String HEAD_KEY_IF_MODIFIED_SINCE = "If-Modified-Since";

    public static final String HEAD_KEY_IF_RANGE = "If-Range";

    public static final String HEAD_KEY_IF_UNMODIFIED_SINCE = "If-Unmodified-Since";

    public static final String HEAD_KEY_MAX_FORWARDS = "Max-Forwards";

    public static final String HEAD_KEY_USER_AGENT = "User-Agent";

    public static final String HEAD_KEY_RANGE = "Range";

    private ArrayMap<String, String> headers = new ArrayMap<String, String>();

    @Override
    public void addHeader(String key, String value) {
        headers.put(key,value);
    }

    @Override
    public void removeHeader(String key) {
        headers.remove(key);
    }

    @Override
    public void removeAll() {
        headers.clear();
    }

    @Override
    public String getValue(String key) {
        return headers.get(key);
    }

    @Override
    public ArrayMap<String, String> getHeaders() {
        return headers;
    }
}
