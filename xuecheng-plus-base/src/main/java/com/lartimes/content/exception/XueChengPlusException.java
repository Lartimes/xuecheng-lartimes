package com.lartimes.content.exception;

/**
 * @author Lartimes
 * @version 1.0
 * @description:
 * @since 2024/2/8 10:30
 */
public class XueChengPlusException extends RuntimeException {
    private String errMessage;

    public XueChengPlusException() {
    }

    public XueChengPlusException(String message) {
        super(message);
        this.errMessage = message;
    }
    public static  void  cast(CommonError commonError){
        throw new XueChengPlusException(commonError.getErrMessage());
    }
    public static  void  cast(String errMessage){
        throw new XueChengPlusException(errMessage);
    }
}
