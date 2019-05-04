package net.ewant.common.util;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangzh on 2018/10/26.
 */
public class GlobalRequestParamHolder {

    private static Map<String, Object> parameterMap = new HashMap<>();

    public static Object putParam(String key, Object value){
        return parameterMap.put(key, value);
    }

    public static Object getParam(String key){
        return parameterMap.get(key);
    }

    public static Object getParamOnce(String key){
        return removeParam(key);
    }

    public static Object removeParam(String key){
        return parameterMap.remove(key);
    }
}
