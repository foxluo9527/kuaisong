package com.scrb.baselib.net;

public class ApiException extends RuntimeException{
    private int code ;

    public ApiException(int code, Throwable throwable) {
        super(throwable);
        this.code = code;
    }

    public ApiException(String Message) {
        super(new Throwable(Message));
    }
}
