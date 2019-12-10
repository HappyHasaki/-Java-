package com.example.demo.entity;

import java.io.Serializable;

public class BaseBean implements Serializable {
    private int code;
    private String msg;
    private Object data;

    public BaseBean() {

    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setMsg(String msg) {
        this.msg = new String(msg);
    }

    public BaseBean(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public void setBase(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public Object getData() {
        return data;
    }

    public String getMsg() {
        return msg;
    }
}
