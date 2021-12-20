package com.scrb.baselib.net;

import android.net.ParseException;
import android.util.Log;

import com.google.gson.JsonParseException;

import org.json.JSONException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

public class ExceptionHandle {

    private static final String TAG = "ExceptionHandle";

    public static ExceptBean handleException(Throwable e){
        e.printStackTrace();
        ExceptBean exceptBean = new ExceptBean(ErrorStatus.UNKNOWN_ERROR,"请求失败，请稍后重试");
        if (e instanceof SocketTimeoutException){
            Log.e(TAG, "网络连接异常: " + e.getMessage());
            exceptBean.setErrorCode(ErrorStatus.NETWORK_ERROR);
            exceptBean.setErrorMsg("网络连接异常");
        }else if (e instanceof ConnectException){
            Log.e(TAG, "网络连接异常: " + e.getMessage());
            exceptBean.setErrorCode(ErrorStatus.NETWORK_ERROR);
            exceptBean.setErrorMsg("网络连接异常");
        } else if (e instanceof UnknownHostException){
            Log.e(TAG, "网络连接异常: " + e.getMessage());
            exceptBean.setErrorCode(ErrorStatus.NETWORK_ERROR);
            exceptBean.setErrorMsg("网络连接异常");
        }else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException){
            Log.e(TAG, "数据解析异常: " + e.getMessage());
            exceptBean.setErrorCode(ErrorStatus.API_ERROR);
            exceptBean.setErrorMsg("数据解析异常");
        } else if (e instanceof IllegalArgumentException){
            Log.e(TAG, "参数错误: " + e.getMessage());
            exceptBean.setErrorCode(ErrorStatus.API_ERROR);
            exceptBean.setErrorMsg("参数错误");
        }else if (e instanceof ApiException){
            Log.e(TAG, "服务器返回的错误: " + e.getMessage());
            exceptBean.setErrorCode(ErrorStatus.SERVER_ERROR);
            exceptBean.setErrorMsg(e.getMessage());
        }else {
            Log.e(TAG, "错误: " + e.getMessage());
            exceptBean.setErrorCode(ErrorStatus.UNKNOWN_ERROR);
            exceptBean.setErrorMsg("未知错误，可能抛锚了吧~");
        }
        return exceptBean;
    }
}
