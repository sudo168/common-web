package net.ewant.common.trace;

import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.protocol.HttpContext;
import org.springframework.cloud.sleuth.ErrorParser;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanInjector;
import org.springframework.cloud.sleuth.instrument.web.HttpTraceKeysInjector;
import org.springframework.cloud.sleuth.instrument.web.client.TraceRestTemplateInterceptor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.client.ClientHttpRequest;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 为静态方式调用 http client定制的收集器
 * Created by admin on 2019/1/9.
 */
public class TraceStaticHttpClientRequestInterceptor extends TraceRestTemplateInterceptor {

    RequestInterceptor requestInterceptor;
    ResponseInterceptor responseInterceptor;

    public TraceStaticHttpClientRequestInterceptor(Tracer tracer, HttpSpanInjector spanInjector, HttpTraceKeysInjector httpTraceKeysInjector, ErrorParser errorParser) {
        super(tracer, spanInjector, httpTraceKeysInjector, errorParser);
        this.requestInterceptor = new RequestInterceptor();
        this.responseInterceptor = new ResponseInterceptor();
    }

    public RequestInterceptor getRequestInterceptor() {
        return requestInterceptor;
    }

    public ResponseInterceptor getResponseInterceptor() {
        return responseInterceptor;
    }

    public class RequestInterceptor implements HttpRequestInterceptor{

        HttpClient httpClient;

        public void setHttpClient(HttpClient httpClient) {
            this.httpClient = httpClient;
        }

        @Override
        public void process(HttpRequest request, HttpContext context) throws HttpException, IOException {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient){
                @Override
                protected HttpContext createHttpContext(HttpMethod httpMethod, URI uri) {
                    return context;
                }

                @Override
                protected HttpUriRequest createHttpUriRequest(HttpMethod httpMethod, URI uri) {
                    return (HttpUriRequest)request;
                }
            };
            ClientHttpRequest clientHttpRequest = factory.createRequest(((HttpUriRequest) request).getURI(), HttpMethod.valueOf(((HttpUriRequest) request).getMethod()));
            publishStartEvent(clientHttpRequest);
            // 将跟踪信息传递给真正的request
            HttpHeaders headers = clientHttpRequest.getHeaders();
            Set<Map.Entry<String, List<String>>> entries = headers.entrySet();
            for(Map.Entry<String, List<String>> entry : entries){
                request.addHeader(entry.getKey(), entry.getValue().get(0));
            }
        }

    }

    public class ResponseInterceptor implements HttpResponseInterceptor{

        @Override
        public void process(HttpResponse response, HttpContext context) throws HttpException, IOException {
            finish();
        }
    }
}
