package lib.core.http;

import java.util.LinkedList;

/**
 * Created by lightning on 17/4/28.
 */

public class HttpTrack {

    private static LinkedList<HttpRequest> httpRequestList = new LinkedList<HttpRequest>();

    private HttpTrack() {
    }

    private static class HttpTrackHolder {
        private static final HttpTrack hth = new HttpTrack();
    }

    public static final HttpTrack getInstance() {
        return HttpTrackHolder.hth;
    }

    public void addHttpRequest(HttpRequest httpRequest) {
        if (httpRequestList.size() < 10) httpRequestList.add(httpRequest);
    }

    public void removeRequest(String url) {
        for (HttpRequest httpRequest : httpRequestList) {
            if (url.equals(httpRequest.url())) {
                httpRequestList.remove(httpRequest);
                return;
            }
        }
    }

    public void removeRequest(HttpRequest httpRequest) {
        httpRequestList.remove(httpRequest);
    }

    public LinkedList<HttpRequest> getHttpRequestList() {
        return httpRequestList;
    }
}
