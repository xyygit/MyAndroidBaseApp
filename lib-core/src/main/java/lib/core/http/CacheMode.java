package lib.core.http;

public class CacheMode {

    public static final int CACHE_NO = 1000; //无缓存

    public static final int CACHE_NO_NET = 1001; //无网络取缓存

    public static final int CACHE_TIME_OUT = 1002; //超时取缓存

    public static final int CACHE_NO_DATA = 1003; //无数据或数据解析失败取缓存

    public static final int CACHE_NO_CHANGE = 1004; //数据无更新取缓存

    public static final int CACHE_LOAD_FIRST = 1005; //先查询本地缓存，如果本地没有，再查询网络数据

}
