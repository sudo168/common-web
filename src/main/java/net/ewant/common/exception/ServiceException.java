package net.ewant.common.exception;

import net.ewant.common.constant.ResponseCodeMessage;

/**
 * Created by huangzh on 2018/10/26.
 */
public class ServiceException extends RuntimeException {

    private static final long serialVersionUID = -6804903386735623408L;

    ResponseCodeMessage codeMessage;

    public ServiceException(ResponseCodeMessage codeMessage) {
        super("[" + codeMessage.getCode() + "]" + codeMessage.getMessage());
        this.codeMessage = codeMessage;
    }

    public ServiceException(ResponseCodeMessage codeMessage, Throwable cause) {
        super(cause.getMessage() + " [" + codeMessage.getCode() + "]" + codeMessage.getMessage(), cause);
        this.codeMessage = codeMessage;
    }

    public ResponseCodeMessage getCodeMessage() {
        return codeMessage;
    }
}
