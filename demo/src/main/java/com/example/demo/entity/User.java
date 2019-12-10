package com.example.demo.entity;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String nickname;
    private String password;
    private String uid;
    private byte[] head;
    private int state;

    public User() {

    }

    public User(String n, String p, String id, int sta, byte[] h) {
        nickname = new String(n);
        password = new String(p);
        uid = new String(id);
        state = sta;
        head = h;
    }

    public void setState(int state) {
        this.state = state;
    }

    public void setHead(byte[] head) {
        this.head = head;
    }

    public void setUid(String uid) {
        this.uid = new String(uid);
    }

    public void setNickname(String n) {
        nickname = new String(n);
    }

    public void setPassword(String p) {
        password = new String(p);
    }

    public String getNickname() {
        return nickname;
    }

    public String getUid() {
        return uid;
    }

    public String getPassword() {

        return password;
    }

    public byte[] getHead() {
        return head;
    }

    public int getState() {
        return state;
    }
}
