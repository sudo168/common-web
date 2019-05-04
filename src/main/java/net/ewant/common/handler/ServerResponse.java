package net.ewant.common.handler;

import net.ewant.common.constant.ResponseCode;
import net.ewant.common.constant.ResponseCodeMessage;
import net.ewant.common.exception.ServiceException;
import net.ewant.common.util.GlobalRequestParamHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * 统一JSON格式响应
 * Created by huangzh on 2018/10/26.
 */
public class ServerResponse {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerResponse.class);

    private static final String RESOURCE_PATH = "i18n/ResponseCode";
    private static final String BUNDLE_CHARSET = "ISO-8859-1";
    private static final String TARGET_CHARSET = "UTF8";

    public static final String REQUEST_LOCALE_PARAM = "request_locale_param";

    public static final String REQUEST_LOCALE_PARAM_NAME = "lang";

    private ResponseCodeMessage responseCode;
    /**
     * 具体响应数据
     */
    private Object data;

    public ServerResponse() {
        this(ResponseCode.OK);
    }

    public ServerResponse(ResponseCodeMessage responseCode) {
        this(responseCode, null);
    }

    public ServerResponse(ResponseCodeMessage responseCode, Object data) {
        this.responseCode = responseCode;
        this.data = data;
    }

    public Serializable getCode() {
        return responseCode.getCode();
    }

    public String getMsg() {
        Locale locale = null;
        try {
            locale = getLocale();
            ResourceBundle bundle = ResourceBundle.getBundle(RESOURCE_PATH, locale);
            return new String(bundle.getString(String.valueOf(responseCode.getCode())).getBytes(BUNDLE_CHARSET), TARGET_CHARSET);
        } catch (Exception e) {
            LOGGER.error(e.getMessage() + ", locale: " + locale, e);
        }
        return responseCode.getMessage();
    }

    public Object getData() {
        return data;
    }

    private Locale getLocale(){
        try {
            Object param = GlobalRequestParamHolder.getParam(REQUEST_LOCALE_PARAM);
            if(param instanceof Locale){
                return (Locale) param;
            }else if(param instanceof String){
                String lang = (String) param;
                String[] split = lang.split("_");
                if(split.length == 2){
                    return new Locale(split[0], split[1]);
                }else if(split.length == 3){
                    return new Locale(split[0], split[1], split[2]);
                }if(split.length == 1){
                    return new Locale(split[0]);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return Locale.getDefault();
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void putDataAsMap(String key, Object value){
        if(key == null || value == null){
            return;
        }
        if(this.data == null){
            this.data = new HashMap<String, Object>();
        }else if(!(this.data instanceof Map)){
            throw new ServiceException(ResponseCode.SYS_RESPONSE_DATA_INVALID);
        }
        ((Map)this.data).put(key, value);
    }

    /**
     * 用于反序列化
     * @param code
     */
    public void setCode(String code) {
        if(this.responseCode == null){
            this.responseCode = new DefaultResponseCodeMessage();
        }
        if(this.responseCode instanceof DefaultResponseCodeMessage){
            ((DefaultResponseCodeMessage)this.responseCode).setCode(code);
        }
    }

    /**
     * 用于反序列化
     * @param msg
     */
    public void setMsg(String msg){
        if(this.responseCode == null){
            this.responseCode = new DefaultResponseCodeMessage();
        }
        if(this.responseCode instanceof DefaultResponseCodeMessage){
            ((DefaultResponseCodeMessage)this.responseCode).setMessage(msg);
        }
    }

    static class DefaultResponseCodeMessage implements ResponseCodeMessage{

        private Serializable code;
        private String message;

        @Override
        public Serializable getCode() {
            return code;
        }

        public void setCode(Serializable code) {
            this.code = code;
        }

        @Override
        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }
}
