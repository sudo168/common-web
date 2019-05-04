package net.ewant.common.interceptor;

import net.ewant.util.IPUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by admin on 2019/1/4.
 */
public class AccessTimeInterceptor extends HandlerInterceptorAdapter {

    private static Logger logger = LoggerFactory.getLogger(AccessTimeInterceptor.class);

    private static final String ACCESS_TIME_KEY = "_AccessTimeKey";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if(!ignore(request)){
            request.setAttribute(ACCESS_TIME_KEY, System.currentTimeMillis());
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        if(!ignore(request)){
            String appVersion = request.getParameter("appVersion");
            logger.warn("Request {} [{}] from[{}/{}] appVersion[{}] access time {}ms", request.getMethod(), request.getRequestURI(), IPUtils.getRequestIP(request), request.getRemoteAddr(), appVersion, System.currentTimeMillis() - (long)request.getAttribute(ACCESS_TIME_KEY));
        }
    }

    private boolean ignore(HttpServletRequest request){
        return HttpMethod.OPTIONS.matches(request.getMethod());
    }
}
