package net.ewant.common.handler;

import net.ewant.common.constant.ResponseCode;
import net.ewant.common.constant.ResponseCodeMessage;
import net.ewant.common.exception.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 全局异常处理
 * Created by huangzh on 2018/10/26.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    /**
     * 400 - Bad Request
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ServerResponse handleHttpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
        logger.error("参数解析失败[{}], request[{}]", e.getMessage(), request.getRequestURI());
        logger.error(e.getMessage(), e);
        return new ServerResponse(ResponseCode.SYS_UNKNOWN_ERR);
    }

    /**
     * 405 - Method Not Allowed
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ServerResponse handleHttpRequestMethodNotSupportedException(HttpServletRequest request, HttpRequestMethodNotSupportedException e) {
        logger.error("不支持当前请求方法[{}], request[{}]", e.getMessage(), request.getRequestURI());
        return new ServerResponse(ResponseCode.SYS_INVALID_METHOD);
    }

    /**
     * 415 - Unsupported Media Type
     */
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ServerResponse handleHttpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        logger.error("不支持当前媒体类型[{}], request[{}]", e.getMessage(), request.getRequestURI());
        return new ServerResponse(ResponseCode.SYS_INVALID_MEDIA);
    }

    @ExceptionHandler(ServletRequestBindingException.class)
    public ServerResponse handleMissingServletRequestParameterException(HttpServletRequest request, ServletRequestBindingException e) {
        logger.error("参数绑定异常[{}], request[{}]", e.getMessage(), request.getRequestURI());
        return new ServerResponse(ResponseCode.SYS_INVALID_PARAMS);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ServerResponse handleMissingServletRequestParameterException(HttpServletRequest request, MissingServletRequestParameterException e) {
        logger.error("必传参数不能为空[{}], request[{}]", e.getMessage(), request.getRequestURI());
        return new ServerResponse(ResponseCode.SYS_INVALID_PARAMS);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ServerResponse handleMethodArgumentTypeMismatchException(HttpServletRequest request, MethodArgumentTypeMismatchException e) {
        logger.error("参数类型不匹配[{}], request[{}]", e.getMessage(), request.getRequestURI());
        return new ServerResponse(ResponseCode.SYS_INVALID_PARAMS);
    }

    /**
     * 500 - Internal Server Error
     * 具体请求参数相关异常请参看HandlerMethodArgumentResolver
     * @see org.springframework.web.method.support.HandlerMethodArgumentResolver
     */
    @ExceptionHandler(Exception.class)
    public ServerResponse handleException(HttpServletRequest request, Exception e) {
        String message = e.getMessage();
        if(e instanceof ServiceException){
            logger.error(message + ", request[{}]", request.getRequestURI());
            if(e.getCause() != null){
                logger.error(e.getMessage(), e);
            }
            return new ServerResponse(((ServiceException) e).getCodeMessage());
        }
        if(e instanceof MultipartException){
            logger.error("文件上传处理异常[{}], request[{}]", message, request.getRequestURI());
            logger.error(e.getMessage(), e);
            if(message.contains("size of")){
                return new ServerResponse(ResponseCode.FILE_UPLOAD_SIZE_LIMITED);
            }
            return new ServerResponse(ResponseCode.FILE_UPLOAD_ERR);
        }
        logger.error("服务器异常[{}], request[{}]", message, request.getRequestURI());
        logger.error(e.getMessage(), e);
        return new ServerResponse(ResponseCode.SYS_SERVER_ERR);
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
    }
}
