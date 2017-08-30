package lib.core.http;

import android.support.v4.util.ArrayMap;

import com.alibaba.fastjson.JSON;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import lib.core.http.definition.HttpActuator;
import lib.core.http.definition.HttpCallback;
import lib.core.utils.ExCommonUtil;
import lib.core.utils.ExLogUtil;
import okhttp3.Call;
import okhttp3.ConnectionPool;
import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.GzipSink;
import okio.Okio;
import okio.Source;

public class OKHttpRequest implements HttpActuator {

    private static OKHttpRequest okHttpRequest;

    private OkHttpClient client;

    public OKHttpRequest(HttpConfig config) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(config.connectTimeout, TimeUnit.SECONDS);
        builder.readTimeout(config.readTimeout, TimeUnit.SECONDS);
        builder.writeTimeout(config.writeTimeout, TimeUnit.SECONDS);
        builder.connectionPool(new ConnectionPool(config.maxIdleConnections, config.keepAliveDuration, TimeUnit.SECONDS));

        SSLHelper sslHelper = SSLHelper.getSslSocketFactory(config.certificates, config.bksFile, config.authorityPassword);
        builder.sslSocketFactory(sslHelper.sSLSocketFactory, sslHelper.trustManager);
        builder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        if (config.hasNetLog) builder.addInterceptor(new LoggingInterceptor());
        client = builder.build();
    }

    public static OKHttpRequest getInstance(HttpConfig config) {
        if(okHttpRequest == null) {
            okHttpRequest = new OKHttpRequest(config);
        }
        return okHttpRequest;
    }

    @Override
    public HttpResponse performRequest(HttpRequest httpRequest) {
        Request request = null;
        try {
            request = buildRequest(httpRequest);
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(httpRequest.url(), HttpStatus.STATUS_NO, null, null, httpRequest.what);
        }

        if (httpRequest.needRebuild()) {
            rebuildClient(httpRequest);
        }
        HttpResponse httpResponse = null;
        try {
            Call call = client.newCall(request);
            httpRequest.setHttpCall(new OKHttpCall(call));

            Response response = call.execute();

            if (call.isCanceled()) {
                return new HttpResponse(httpRequest.url(), HttpStatus.STATUS_CANCEL, null, null, httpRequest.what);
            }
            if (response != null) {
                httpResponse = buildResponse(httpRequest, response);
                response.close();
            }
        } catch (SocketTimeoutException e) {
            return new HttpResponse(httpRequest.url(), HttpStatus.STATUS_REQUEST_TIMEOUT, null, null, httpRequest.what);
        } catch (IOException e) {
            if ("Canceled".equals(e.getMessage())) {
                return new HttpResponse(httpRequest.url(), HttpStatus.STATUS_CANCEL, null, null, httpRequest.what);
            } else {
                return new HttpResponse(httpRequest.url(), HttpStatus.STATUS_NO, null, null, httpRequest.what);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new HttpResponse(httpRequest.url(), HttpStatus.STATUS_NO, null, null, httpRequest.what);
        }
        return httpResponse;
    }

    private Request buildRequest(final HttpRequest httpRequest) {

        Request.Builder builder = new Request.Builder().url(httpRequest.url()).tag(httpRequest.getTag());
        /********* header *********/
        RequestHeader requestHeader = httpRequest.getRequestHeader();
        if (requestHeader != null) {
            ArrayMap<String, String> headers = requestHeader.getHeaders();
            for (String key : headers.keySet()) {
                builder.addHeader(key, headers.get(key));
            }
        }

        final Object body = httpRequest.getRequestBody();
        switch (httpRequest.getRequestMethod()) {
            case RequestMethod.GET:
                break;
            case RequestMethod.POST_STRING:
                String stringBody = ExCommonUtil.isEmpty(body) ? "" : JSON.toJSONString(body);
                builder.post(RequestBody.create(MediaType.parse(CONTENT_TYPE_NORMAL), stringBody));
                break;
            case RequestMethod.POST_STREAM:
                if (body != null) {
                    RequestBody requestBody = new RequestBody() {
                        @Override
                        public void writeTo(BufferedSink bufferedSink) throws IOException {
                            bufferedSink.writeUtf8(body.toString());
                        }

                        @Override
                        public MediaType contentType() {
                            return MediaType.parse(CONTENT_TYPE_STREAM);
                        }
                    };
                    builder.post(requestBody);
                } else {
                    builder.post(RequestBody.create(MediaType.parse(CONTENT_TYPE_NORMAL), ""));
                }
                break;
            case RequestMethod.POST_FORM:
                if (body != null) {
                    FormBody.Builder formBuilder = new FormBody.Builder();
                    ArrayMap<String, Object> bodyForm = (ArrayMap<String, Object>) body;
                    for (String key : bodyForm.keySet()) {
                        Object value = bodyForm.get(key);
                        if (value != null) {
                            formBuilder.add(key, value.toString());
                        }
                    }
                    builder.post(formBuilder.build());
                } else {
                    builder.post(RequestBody.create(MediaType.parse(CONTENT_TYPE_NORMAL), ""));
                }
                break;
            case RequestMethod.POST_MULTIPART:
                MultipartBody.Builder multipartBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
                ArrayMap<String, Object> bodyForm = (ArrayMap<String, Object>) body;
                for (String key : bodyForm.keySet()) {
                    Object value = bodyForm.get(key);
                    if (value != null) {
                        if (value instanceof File) {
                            File file = (File) value;
                            multipartBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(CONTENT_TYPE_MULTIPART_FORM_DATA), file));
                        } else if(value instanceof File[]) {
                            File[] files = (File[]) value;
                            for (File file : files) {
                                multipartBuilder.addFormDataPart(key, file.getName(), RequestBody.create(MediaType.parse(CONTENT_TYPE_MULTIPART_FORM_DATA), file));
                            }
                        } else {
                            multipartBuilder.addFormDataPart(key, value.toString());
                        }
                    }
                }
                RequestBody requestBody = multipartBuilder.build();
                builder.post(requestBody);
                break;
            case RequestMethod.PUT:
                break;
            case RequestMethod.MOVE:
                break;
            case RequestMethod.COPY:
                break;
            case RequestMethod.DELETE:
                break;
            case RequestMethod.HEAD:
                break;
            case RequestMethod.PATCH:
                break;
            case RequestMethod.OPTIONS:
                break;
            case RequestMethod.TRACE:
                break;
            case RequestMethod.CONNECT:
                break;
        }
        Request request = builder.build();
        return request;
    }

    private HttpResponse buildResponse(HttpRequest httpRequest, Response response) {
        HttpResponse httpResponse = null;
        try {
            String contentType = response.header(ResponseHeader.HEAD_KEY_CONTENT_TYPE);
            httpResponse = new HttpResponse(httpRequest.url(), response.code(), response.body().bytes(), contentType, httpRequest.what);
            ResponseHeader responseHeader = new ResponseHeader();
            Set<String> headerSet = response.headers().names();
            Iterator headers = headerSet.iterator();
            while (headers.hasNext()) {
                String headerKey = (String) headers.next();
                responseHeader.addHeader(headerKey, response.header(headerKey));
            }
            httpResponse.setResponseHeader(responseHeader);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return httpResponse;
    }

    private void rebuildClient(HttpRequest httpRequest) {
        OkHttpClient.Builder builder = client.newBuilder();

        /********* request config *********/
        RequestConfig requestConfig = httpRequest.getRequestConfig();
        if (requestConfig.isNeedProgress()) {
            HttpCallback httpCallback = requestConfig.getHttpCallback();
            if (httpCallback != null) {
                try {
                    builder.addNetworkInterceptor(new ProgressInterceptor(httpCallback));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if (requestConfig.isNeedGzipRequest()) {
            builder.addInterceptor(new GzipRequestInterceptor());
        }

        client = builder.build();
    }

    final static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request request = chain.request();

            long t1 = System.nanoTime();
            ExLogUtil.e(String.format("Sending request %s on %s%n%s",
                    request.url(), request.body().contentLength(), request.headers()));

            Response response = chain.proceed(request);

            long t2 = System.nanoTime();
            ExLogUtil.e(String.format("Received response for %s in %.1fms%n%s",
                    response.request().url(), (t2 - t1) / 1e6d, response.headers()));

            return response;
        }
    }

    final static class GzipRequestInterceptor implements Interceptor {
        @Override
        public Response intercept(Interceptor.Chain chain) throws IOException {
            Request originalRequest = chain.request();
            if (originalRequest.body() == null || originalRequest.header("Content-Encoding") != null) {
                return chain.proceed(originalRequest);
            }

            Request compressedRequest = originalRequest.newBuilder()
                    .header("Content-Encoding", "gzip")
                    .method(originalRequest.method(), gzip(originalRequest.body()))
                    .build();
            return chain.proceed(compressedRequest);
        }

        private RequestBody gzip(final RequestBody body) {
            return new RequestBody() {
                @Override
                public MediaType contentType() {
                    return body.contentType();
                }

                @Override
                public long contentLength() {
                    return -1; // We don't know the compressed length in advance!
                }

                @Override
                public void writeTo(BufferedSink sink) throws IOException {
                    BufferedSink gzipSink = Okio.buffer(new GzipSink(sink));
                    body.writeTo(gzipSink);
                    gzipSink.close();
                }
            };
        }
    }

    final static class ProgressInterceptor implements Interceptor {

        private HttpCallback httpCallback;

        public ProgressInterceptor(HttpCallback httpCallback) {
            this.httpCallback = httpCallback;
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response originalResponse = chain.proceed(chain.request());
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), httpCallback))
                    .build();
        }

    }

    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private HttpCallback httpCallback;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, HttpCallback httpCallback) {
            this.responseBody = responseBody;
            this.httpCallback = httpCallback;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    httpCallback.onProgress(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }
}
