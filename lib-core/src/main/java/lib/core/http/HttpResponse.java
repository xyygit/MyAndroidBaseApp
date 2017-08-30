package lib.core.http;

import lib.core.utils.ExCommonUtil;

public class HttpResponse {

    private String url;
    private byte[] responseBody;
    private int code;
    private ResponseHeader responseHeader;
    private String contentType;
    public int what;

    public HttpResponse(String url, int code, byte[] responseBody, String contentType, int what) {
        this.url = url;
        this.code = code;
        this.what = what;
        this.contentType = contentType;
        if (!ExCommonUtil.isEmpty(responseBody)) {
            this.responseBody = responseBody;
        }
    }

    public String url() {
        return url;
    }

    public byte[] getResponseBody() {
        if (ExCommonUtil.isEmpty(responseBody)) responseBody = new byte[0];
        return responseBody;
    }

    public int getCode() {
        return code;
    }

    public ResponseHeader getResponseHeader() {
        return responseHeader;
    }

    public void setResponseHeader(ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }

    public String getContentType() {
        return contentType;
    }
}
