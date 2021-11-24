package com.msb.service.bean;

import java.io.Serializable;

public class ResponseEntity implements Serializable {
    private String code;
    private String message;
    private String dataType;

    public ResponseEntity() {

    }

    public ResponseEntity(String code, String message) {
        this.code = code;
        this.message = message;

    }

    public ResponseEntity(String code, String message, String dataType) {
        this.code = code;
        this.message = message;
        this.dataType = dataType;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }
}
