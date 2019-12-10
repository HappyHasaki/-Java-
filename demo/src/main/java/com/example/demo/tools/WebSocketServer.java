package com.example.demo.tools;

import com.example.demo.entity.MesQueue;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.CopyOnWriteArrayList;
import com.alibaba.fastjson.JSONObject;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/websocket/{uid}") // 客户端URI访问的路径
@Component
public class WebSocketServer {
    /** 保存所有连接的webSocket实体
     * CopyOnWriteArrayList使用了一种叫写时复制的方法，
     * 当有新元素添加到CopyOnWriteArrayList时，
     * 先从原有的数组中拷贝一份出来，然后在新的数组做写操作，
     * 写完之后，再将原来的数组引用指向到新数组。
     * 具备线程安全，并且适用于高并发场景
     */
    private static CopyOnWriteArrayList<WebSocketServer> sWebSocketServers = new CopyOnWriteArrayList<>();
    private static HashMap<String, MesQueue> OutLineMes=new HashMap<>();
    private Session mSession; // 与客户端连接的会话，用于发送数据
    private String uid=""; // 客户端的标识(这里以机器编号)
    private Log mLog = LogFactory.getLog(WebSocketServer.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("uid") String uid){
        mSession = session;
        sWebSocketServers.add(this); // 将会话保存
        this.uid = uid;
        MesQueue queue=OutLineMes.get(uid);
        OutLineMes.remove(uid);        //用户上线，踢出离线消息队列
        if (queue!=null&&queue.getQueueSize()>0){
            Queue mq=queue.getMesQueue();
            while(!mq.isEmpty()){
                JSONObject js=(JSONObject) mq.poll();
                if (js!=null){
                    sendMessage(js.toString(),js.getString("toUser"));
                }
            }
        }
        mLog.info("离线队列用户数"+OutLineMes.size());
        mLog.info("-->onOpen new connect uid is "+uid);
    }

    @OnClose
    public void onClose(){
        sWebSocketServers.remove(this);
        MesQueue mq=new MesQueue(uid);
        OutLineMes.put(uid,mq);                //离线加入消息队列
        mLog.info("-->onClose a connect");
    }

    @OnMessage
    public void onMessage(String message, Session session){
        mLog.info("-->onMessage "+message);
        JSONObject mes=JSONObject.parseObject(message);
        mSession = session;
        sendMessage(message,mes.getString("toUser"));
    }

    /** 对外发送消息
     * @param message
     */
    public boolean sendMessage(String message){
        try {
            mSession.getBasicRemote().sendText(message);
        } catch (IOException e) {
            mLog.info(e.toString());
            return false;
        }
        return true;
    }

    /** 对某个机器发送消息
     * @param message
     * @param uid 用户id
     * @return true,返回发送的消息,false，返回failed字符串
     */
    public static String sendMessage(String message, String uid){
        boolean success = false;
        int k=0;
        for (WebSocketServer server : sWebSocketServers){
            if (server.uid.equals(uid)){
                success = server.sendMessage(message);
                k=1;
                break;
            }
        }
        if (k==0){                                  //发送的对象离线，就把消息入队列
            MesQueue queue=OutLineMes.get(uid);
            if (queue!=null){
                JSONObject mes=JSONObject.parseObject(message);
                queue.AddMesQueue(mes);
                OutLineMes.replace(uid,queue);
            }
        }
        return success ? message : "failed";
    }
    /** 全局通知
     * @param message
     */
    public int sendAllMessage(String message){
        for (WebSocketServer server : sWebSocketServers){
            server.sendMessage(message);
        }
        return 1;
    }
}