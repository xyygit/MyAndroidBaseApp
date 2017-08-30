package lib.core.http.definition;

import lib.core.http.HttpRequest;
import lib.core.http.HttpResponse;

public interface HttpActuator {

    String CONTENT_TYPE_NORMAL = "application/x-www-form-urlencoded; charset=utf-8";
    String CONTENT_TYPE_MULTIPART_FORM_DATA = "multipart/form-data; charset=utf-8";
    String CONTENT_TYPE_JSON = "application/json; charset=utf-8";
    String CONTENT_TYPE_XML = "application/xml; charset=utf-8";
    String CONTENT_TYPE_STREAM = "application/octet-stream; charset=utf-8";

    public HttpResponse performRequest(HttpRequest request);


}
