package com.msb.service.bean;

/**
 * 自定义的异常
 */
public class CustomException extends Exception {

    private String code;
    private String message;
    private String jsonStr;


    public CustomException(String msg){
        super(msg);
    }

    public CustomException(String code, String message, String jsonStr) {
        this.code = code;
        this.message = message;
        this.jsonStr = jsonStr;
    }

    public CustomException(String message, String code, String message1, String jsonStr) {
        super(message);
        this.code = code;
        this.message = message1;
        this.jsonStr = jsonStr;
    }

    public CustomException(String message, Throwable cause, String code, String message1, String jsonStr) {
        super(message, cause);
        this.code = code;
        this.message = message1;
        this.jsonStr = jsonStr;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getJsonStr() {
        return jsonStr;
    }

    public void setJsonStr(String jsonStr) {
        this.jsonStr = jsonStr;
    }
}



