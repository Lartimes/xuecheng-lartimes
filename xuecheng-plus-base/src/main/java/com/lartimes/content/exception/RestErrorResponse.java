package com.lartimes.content.exception;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/8 10:33
 */

import java.io.Serializable;

/**
 * 错误响应参数包装
 */
public class RestErrorResponse implements Serializable {

    private String errMessage;

    public RestErrorResponse(String errMessage){
        this.errMessage= errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}

