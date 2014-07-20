/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sample.whiteboardapp;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.sample.db.Comogphogfeature;
import org.sample.db.querydb;
import redis.clients.jedis.Jedis;

/**
 *
 * @author user
 */
@ServerEndpoint("/whiteboardendpoint")
public class MyWhiteboard {
    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    private querydb qdb =new querydb();
    @OnMessage
    public void onMessage(String message,Session s) throws IOException{
        System.out.println("Msg:  "+message);
        Comogphogfeature searchFeat = qdb.getFeatureByScopID(message);
        Jedis jedis = new Jedis("localhost");
        
        jedis.set("foo", searchFeat.getFeatureVector());
        s.getBasicRemote().sendText("redis working: "+jedis.get("foo"));
        List<Comogphogfeature> allFeat = qdb.getFeatures();
        s.getBasicRemote().sendText("Putting all data in cache");
        int i=0;
        for(Comogphogfeature tmp :allFeat){
            //if(i>50) break;
            jedis.set(tmp.getScopid(), tmp.getFeatureVector());
            s.getBasicRemote().sendText(tmp.getScopid());
            //i++;
        }
        s.getBasicRemote().sendText("ended all data in cache:"+i);
        s.getBasicRemote().sendText("redis working: "+jedis.get(message));
    }
    @OnOpen
    public void onOpen (Session peer) {
        peers.add(peer);
    }

    @OnClose
    public void onClose (Session peer) {
        peers.remove(peer);
    }
}
