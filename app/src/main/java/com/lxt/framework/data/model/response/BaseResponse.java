package com.lxt.framework.data.model.response;


public class BaseResponse<T> {

    public T data = null;
    public int errorCode = 0;
    public String errorMsg = "";
    public boolean isSuccessful(){
        return errorCode==0;
    }
    @Override
    public String toString() {
        return "BaseRequest{" +
                "errorCode=" + errorCode +
                ", errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
