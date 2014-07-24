/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.whiteboardapp;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import static java.util.Collections.list;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
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
    private querydb qdb = new querydb();

    @OnMessage
    public void onMessage(String message, Session s) throws IOException {
        System.out.println("Msg:  " + message);
        
        Comogphogfeature searchFeat = qdb.getFeatureByScopID(message);
        if (searchFeat != null) {
            s.getBasicRemote().sendText("ScopID found in database,start time" +
                    new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
            s.getBasicRemote().sendText("Working on redis cache...");
            Jedis jedis = new Jedis("123.49.42.228");
            Set<String> scops = jedis.keys("d*");
            jedis.set(searchFeat.getScopid(), searchFeat.getFeatureVector());
            
            s.getBasicRemote().sendText("Data(" + scops.size() + ") available in cache.....");

            String[] queryFeat = searchFeat.getFeatureVector().split("-");
            s.getBasicRemote().sendText("calculating Distance of...");
            Iterator<String> it = scops.iterator();
            Map<String, Double> distances = new TreeMap<String, Double>();
            int done = 0;
            while (it.hasNext()) {
                String tmp = it.next();
                double tmpDist = 0;

                String[] allF = jedis.get(tmp).split("-");
                for (int i = 0; i < queryFeat.length; i++) {
                    tmpDist += Math.pow((Integer.valueOf(queryFeat[i]) - Integer.valueOf(allF[i])), 2);

                }
                tmpDist = Math.sqrt(tmpDist);
                distances.put(tmp, tmpDist);
                s.getBasicRemote().sendText(tmp + " distance " + tmpDist);
                done++;
                if (done% 100==0) {
                    s.getBasicRemote().sendText(done + " done, Please wait for the next " + (scops.size() - done));
                }
            }
            List list = new LinkedList(distances.entrySet());
            Collections.sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    return ((Comparable) ((Map.Entry) (o1)).getValue())
                            .compareTo(((Map.Entry) (o2)).getValue());
                }
            });
            s.getBasicRemote().sendText("sorted distances are:");
            done = 0;
            for (Map.Entry entry : distances.entrySet()) {
                done++;
                System.out.println("Scop : " + entry.getKey()
                        + " Distance : " + entry.getValue());
                if (done == 20) {
                    break;
		
                }
            }

//            for (Comogphogfeature tmp : allFeat) {
//                //if(i>50) break;
//                jedis.set(tmp.getScopid(), tmp.getFeatureVector());
//                s.getBasicRemote().sendText(tmp.getScopid());
//                //i++;
//            }
        } else {
            s.getBasicRemote().sendText("Scop id not found");
        }
    }

    @OnOpen
    public void onOpen(Session peer) {
        peers.add(peer);
    }

    @OnClose
    public void onClose(Session peer) {
        peers.remove(peer);
    }
}
