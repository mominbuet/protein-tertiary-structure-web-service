/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sample.whiteboardapp;

import featureExtraction.ComogPhogFeatureExtractor;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.sample.db.ComogphogfeatureExtended;
import org.sample.db.SiteStats;
import org.sample.db.insertDB;
import org.sample.db.querydb;

/**
 *
 * @author user
 */
@ServerEndpoint("/scoreEndpoint")
public class score_match {

    private static Set<Session> peers = Collections.synchronizedSet(new HashSet<Session>());
    static Map<Session, Integer> peersMap = new TreeMap<Session, Integer>();
    private querydb qdb = new querydb();
    int fCount = 0;
    static File uploadedFile1 = null;
    static File uploadedFile2 = null;
    static FileOutputStream fos1 = null;
    static FileOutputStream fos2 = null;
    static String fileName1 = null;
    static String fileName2 = null;
    final static String filePath = "";

    @OnMessage
    public void processUpload(ByteBuffer msg, boolean last, Session session) throws IOException {
        System.out.println("Binary Data: " + fCount + ", Capacity: " + msg.capacity());
        fCount++;

        while (msg.hasRemaining()) {
            try {
                if (peersMap.get(session) != null) {
                    if (peersMap.get(session) == 1 && fos1 != null) {
                        fos1.write(msg.get());
                    }
                    if (peersMap.get(session) == 2 && fos2 != null) {
                        fos2.write(msg.get());
                    }
                    session.getBasicRemote().sendText("File "+peersMap.get(session+" recieved"));
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @OnOpen
    public void onOpen(Session peer) throws IOException {
        peer.getBasicRemote().sendText("Welcome to score module of COMOGRAD");
        peers.add(peer);
    }

    @OnClose
    public void onClose(Session peer) {
        peers.remove(peer);
    }

    @OnMessage
    public void onMessage(String message, Session s) throws IOException {

        if (message.contains("filename")) {

            if (message.contains("first file")) {
                fileName1 = new Date().getTime() + message.substring(message.indexOf(':') + 1);
                uploadedFile1 = new File(filePath + fileName1);
                fos1 = new FileOutputStream(uploadedFile1);
            } else {
                fileName2 = new Date().getTime() + message.substring(message.indexOf(':') + 1);
                uploadedFile2 = new File(filePath + fileName2);
                fos2 = new FileOutputStream(uploadedFile2);
            }
             if (peersMap.get(s) == null) {
                    peersMap.put(s, 1);
                } else {
                    peersMap.put(s, 2);
                }

        }
        try {
            if (message.contains("end")) {
                if (message.equals("end1")) {
                    fos1 = new FileOutputStream(uploadedFile1);
                } else {
                    fos2 = new FileOutputStream(uploadedFile2);
                }
               
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if (fos1 != null && fos2 != null&& peersMap.get(s) != null ) {
            if (peersMap.get(s) == 2) {
                try {
                    fos1.flush();
                    fos1.close();
                    fos2.flush();
                    fos2.close();
                    ComogphogfeatureExtended searchFeat1 = null;
                    ComogphogfeatureExtended searchFeat2 = null;
                    ComogPhogFeatureExtractor extractor = new ComogPhogFeatureExtractor();
                    //File fin=new File("input\\d3rmkd_.txt");
                    String comogPhog = extractor.runFeatureExtraction(uploadedFile1);
                    System.out.println("In file  " + filePath + fileName1 + " size " + uploadedFile1.getAbsolutePath());
                    searchFeat1 = new ComogphogfeatureExtended();
                    searchFeat1.setScopid(fileName1);
                    searchFeat1.setFeatureVector(comogPhog);

                    comogPhog = extractor.runFeatureExtraction(uploadedFile2);
                    searchFeat2 = new ComogphogfeatureExtended();
                    searchFeat2.setScopid(fileName2);
                    searchFeat2.setFeatureVector(comogPhog);

                    SiteStats stats = new SiteStats();
                    stats = (SiteStats) qdb.getStats();
                    new insertDB().incrStat(stats);
                    s.getBasicRemote().sendText("<h4 style=\"color:orange;\"><b>Query no " + stats.getQueryNo() + "</b></h4>");
                    s.getBasicRemote().sendText("Start time"
                            + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
                    double tmpDist = 0;

                    String[] queryFeat = searchFeat1.getFeatureVector().split("-");
                    String[] allF = searchFeat2.getFeatureVector().split("-");
                    for (int i = 0; i < ((queryFeat.length > allF.length) ? allF.length : queryFeat.length); i++) {
                        tmpDist += Math.pow((Integer.valueOf(queryFeat[i]) - Integer.valueOf(allF[i])), 2);
                    }
                    tmpDist = Math.sqrt(tmpDist);
                    s.getBasicRemote().sendText("<h4 style=\"color:orange;\"><b>Distance " + tmpDist + "</b></h4>");
                    /*System.out.println("Feature " + comogPhog);
                     s.getBasicRemote().sendText("<h5 style=\"color:orange;\"><b>Feat" + comogPhog + "</b></h5>");*/
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
    }
}
