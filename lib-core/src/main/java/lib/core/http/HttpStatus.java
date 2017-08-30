package lib.core.http;

public class HttpStatus {

    public static final int STATUS_OK = 200; //访问成功
    public static final int STATUS_NO_CONTENT = 204; //服务器成功处理了请求，但不需要返回任何实体内容.
    public static final int STATUS_PARTIAL_CONTENT = 206; //断点请求返回

    public static final int STATUS_NOT_MODIFIED = 304; //未修改

    public static final int STATUS_BAD_REQUEST = 400; //请求参数有误
    public static final int STATUS_UNAUTHORIZED = 401; //证书校验失败
    public static final int STATUS_FORBIDDEN = 403; //请求被拒绝执行
    public static final int STATUS_NOT_FOUND = 404; //访问的资源不存在
    public static final int STATUS_REQUEST_TIMEOUT = 408; //请求超时

    public static final int STATUS_INTERNAL_SERVER_ERROR = 500; //内部服务器错误
    public static final int STATUS_SERVICE_UNAVAILABLE = 503; //服务器当前无法处理请求

    public static final int STATUS_NO = -1; //未知错误
    public static final int STATUS_CANCEL = -1000; //请求取消
    public static final int STATUS_NO_NET = -100; //无网络
    public static final int STATUS_CACHE = 800; //缓存

}
