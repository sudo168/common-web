package net.ewant.common.constant;

import java.io.Serializable;

/**
 * Created by huangzh on 2018/10/26.
 */
public interface ResponseCodeMessage {
    Serializable getCode();
    String getMessage();
}
