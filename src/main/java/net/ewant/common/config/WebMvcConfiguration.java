package net.ewant.common.config;

import com.alibaba.fastjson.parser.ParserConfig;
import net.ewant.common.handler.EnumValueVisitor;
import net.ewant.common.handler.JacksonEnumDeserializer;
import net.ewant.common.interceptor.AccessTimeInterceptor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * spring mvc 配置
 * Created by admin on 2018/11/9.
 */
@Configuration("CommWebMvcConfiguration")
public class WebMvcConfiguration extends WebMvcConfigurerAdapter implements ApplicationListener<ContextRefreshedEvent> {

    static ObjectMapper objectMapper;

    public WebMvcConfiguration() {
        // fastjson支持String到自定义对象的转换
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        // 枚举参数注入转换
        registry.addConverterFactory(new StringToEnumConverterFactory());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new AccessTimeInterceptor());
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(objectMapper == null){
            ObjectMapper bean = null;
            try {
                bean = event.getApplicationContext().getBean(ObjectMapper.class);
            } catch (BeansException e) {
            }
            if(bean != null){
                objectMapper = bean;
                SimpleModule sm = new SimpleModule();
                sm.addDeserializer(EnumValueVisitor.class, new JacksonEnumDeserializer());
                objectMapper.registerModule(sm);
            }
        }
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
