package lib.core.http;

import android.support.v4.util.ArrayMap;

import lib.core.http.definition.Headers;

public class ResponseHeader implements Headers {

    public static final String HEAD_KEY_AGE = "Age";

    public static final String HEAD_KEY_ALLOW = "Allow";

    public static final String HEAD_KEY_CONTENT_ENCODING = "Content-Encoding";

    public static final String HEAD_KEY_CONTENT_LANGUAGE = "Content-Language";

    public static final String HEAD_KEY_CONTENT_LOCATION = "Content-Location";

    public static final String HEAD_KEY_CONTENT_MD5 = "Content-MD5";

    public static final String HEAD_KEY_CONTENT_RANGE = "Content-Range";

    public static final String HEAD_KEY_EXPIRES = "Expires";

    public static final String HEAD_KEY_E_TAG = "ETag";

    public static final String HEAD_KEY_LAST_MODIFIED = "Last-Modified";

    public static final String HEAD_KEY_LOCATION = "Location";

    public static final String HEAD_KEY_SET_COOKIE = "Set-Cookie";

    public static final String HEAD_KEY_RETRY_AFTER = "Retry-After";

    public static final String HEAD_KEY_TRANSFER_ENCODING = "Transfer-Encoding";

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
