package net.ewant.common.trace;

import net.ewant.util.HttpClientUtils;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.cloud.sleuth.ErrorParser;
import org.springframework.cloud.sleuth.Tracer;
import org.springframework.cloud.sleuth.instrument.web.HttpSpanInjector;
import org.springframework.cloud.sleuth.instrument.web.HttpTraceKeysInjector;
import org.springframework.cloud.sleuth.instrument.web.client.TraceRestTemplateInterceptor;
import org.springframework.context.annotation.Configuration;

/**
 * Created by admin on 2019/1/9.
 */
@Configuration
@ConditionalOnClass(TraceRestTemplateInterceptor.class)
//@AutoConfigureAfter(TraceWebAutoConfiguration.class)
public class ZipkinBeanAutoConfiguration implements InitializingBean{

    @Autowired(required = false)
    Tracer tracer;
    @Autowired(required = false)
    HttpSpanInjector spanInjector;
    @Autowired(required = false)
    HttpTraceKeysInjector httpTraceKeysInjector;
    @Autowired(required = false)
    ErrorParser errorParser;

    public ZipkinBeanAutoConfiguration(){
    }

    private void traceHttpClientRequestInterceptor(){
        TraceStaticHttpClientRequestInterceptor interceptor = new TraceStaticHttpClientRequestInterceptor(tracer, spanInjector, httpTraceKeysInjector, errorParser);
        TraceStaticHttpClientRequestInterceptor.RequestInterceptor requestInterceptor = interceptor.getRequestInterceptor();
        TraceStaticHttpClientRequestInterceptor.ResponseInterceptor responseInterceptor = interceptor.getResponseInterceptor();
        HttpClient httpClient = HttpClientUtils.interceptor(requestInterceptor, responseInterceptor);
        requestInterceptor.setHttpClient(httpClient);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(tracer != null && spanInjector != null && httpTraceKeysInjector != null){
            traceHttpClientRequestInterceptor();
        }
    }
}
