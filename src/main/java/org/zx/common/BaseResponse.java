package org.zx.common;

/**
 * @author xiang.zhang
 */
public class BaseResponse<T>{

    Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    String message;
    T data;

    public BaseResponse() {

    }

    public BaseResponse(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
