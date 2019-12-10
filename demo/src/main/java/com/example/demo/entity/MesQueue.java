package com.example.demo.entity;

import com.alibaba.fastjson.*;
import java.util.LinkedList;
import java.util.Queue;

public class MesQueue {
    private String uid;
    Queue<JSONObject> mesqueue;

    public MesQueue(String uid) {
        this.uid = uid;
        mesqueue = new LinkedList<>();
    }
    public Queue getMesQueue(){
        return mesqueue;
    }
    public int AddMesQueue(JSONObject mes){
        mesqueue.add(mes);
        return 1;
    }
    public int ClearMesQueue(){
        mesqueue.clear();
        return 1;
    }
    public int getQueueSize(){
        return mesqueue.size();
    }
}