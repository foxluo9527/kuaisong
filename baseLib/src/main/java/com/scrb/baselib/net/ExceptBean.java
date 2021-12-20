package com.scrb.baselib.net;

public class ExceptBean {
    private int errorCode;
    private String errorMsg;


    public ExceptBean() {
    }

    public ExceptBean(int errorCode,String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }
}
