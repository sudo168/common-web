package net.ewant.common.filter;

import net.ewant.common.handler.ServerResponse;
import net.ewant.common.util.GlobalRequestParamHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 国际化参数
 * Created by huangzh on 2018/10/26.
 */
@Component
@WebFilter(urlPatterns = "/*", filterName = "RequestLocaleWrapFilter")
public class RequestLocaleWrapFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String lang = servletRequest.getParameter(ServerResponse.REQUEST_LOCALE_PARAM_NAME);
        if(servletRequest instanceof HttpServletRequest){
            HttpSession session = ((HttpServletRequest) servletRequest).getSession(false);
            if(session != null){
                Object attribute = session.getAttribute(ServerResponse.REQUEST_LOCALE_PARAM_NAME);
                if(attribute != null){
                    lang = (String) attribute;
                }
            }
            allowOrigin((HttpServletRequest) servletRequest, (HttpServletResponse) servletResponse);
        }
        GlobalRequestParamHolder.putParam(ServerResponse.REQUEST_LOCALE_PARAM, (lang == null || lang.trim().length() == 0) ? servletRequest.getLocale() : lang);

        filterChain.doFilter(servletRequest, servletResponse);

        GlobalRequestParamHolder.removeParam(ServerResponse.REQUEST_LOCALE_PARAM);
    }

    private void allowOrigin(HttpServletRequest request, HttpServletResponse response){
        String origin = request.getHeader(HttpHeaders.ORIGIN);
        if(origin != null && response.getHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN) == null){
            // 验证origin IP 通过后,设置跨域请求头
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, origin);
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,GET,OPTIONS,DELETE");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "Authorization,Content-Type");
        }
    }

    @Override
    public void destroy() {
    }
}
