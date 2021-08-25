package org.zx.controller;

import org.springframework.http.HttpStatus;

public abstract class BaseController {

    class Response<T>{

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

        public Response() {

        }

        public Response(Integer code, String message, T data) {
            this.code = code;
            this.message = message;
            this.data = data;
        }
    }

    protected <T> Response<T> success(T data){
        return new Response<>(HttpStatus.OK.value(),"success",data);
    }
}
