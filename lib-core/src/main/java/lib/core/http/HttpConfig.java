package lib.core.http;

import java.io.InputStream;

public class HttpConfig {

    public long connectTimeout = 10;
    public long readTimeout = 10;
    public long writeTimeout = 10;
    public int maxIdleConnections = 5; //keep-alive mult connection num
    public int keepAliveDuration = 45; //keep-alive stay time

    public boolean hasNetLog;

    public InputStream[] certificates;
    public InputStream bksFile;
    public String authorityPassword;

}
