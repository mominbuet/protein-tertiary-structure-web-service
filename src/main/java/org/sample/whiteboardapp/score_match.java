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
import java.text.DecimalFormat;
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
    static int endNo = 0;

    @OnMessage
    public void processUpload(ByteBuffer msg, boolean last, Session session) throws IOException {
        System.out.println("Binary Data No: " + fCount + ", Size: " + msg.capacity());
        session.getBasicRemote().sendText("Binary Data No: " + fCount + ", Size: " + msg.capacity());
        while (msg.hasRemaining()) {

            try {
//                if (peersMap.get(session) != null) {
//                if(fCount==1){
                if (fos1 != null && endNo == 0) {

                    fos1.write(msg.get());
                } else {
//                    if (peersMap.get(session) == 2 && fos2 != null) {

                    fos2.write(msg.get());
                }

//                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
    }

    @OnMessage
    public void onMessage(String message, Session s) throws IOException {
        try {
            if (message.contains("filename")) {

                if (message.contains("first file")) {
                    fileName1 = new Date().getTime() + "_" + message.substring(message.indexOf(':') + 1);
                    uploadedFile1 = new File(filePath + fileName1);
                    fos1 = new FileOutputStream(uploadedFile1);
                } else {
                    fileName2 = new Date().getTime() + "_" + message.substring(message.indexOf(':') + 1);
                    uploadedFile2 = new File(filePath + fileName2);
                    fos2 = new FileOutputStream(uploadedFile2);
                }
//                if (peersMap.get(s) == null) {
//                    peersMap.put(s, 1);
//                } else {
//                    peersMap.put(s, 2);
//                }

            }

            if (message.contains("end")) {
                if (message.equals("end1")) {
                    fos1.flush();// = new FileOutputStream(uploadedFile1);
                    fos1.close();
                    endNo++;
                    fCount++;
                    s.getBasicRemote().sendText("File received " + fCount);
                } else {
                    //fos2 = new FileOutputStream(uploadedFile2);
                    fos2.flush();// = new FileOutputStream(uploadedFile1);
                    fos2.close();
                    fCount++;
                    s.getBasicRemote().sendText("File received " + fCount);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //&& peersMap.get(s) != null
//        if (uploadedFile1 != null && uploadedFile2 != null) {
        if (fCount == 2) {
            fCount = 0;
            endNo = 0;
            try {
//                    fos1.flush();
//                    fos1.close();
//                    fos2.flush();
//                    fos2.close();

//                SiteStats stats = new SiteStats();
//                stats = (SiteStats) qdb.getStats();
//                new insertDB().incrStat(stats);
//                s.getBasicRemote().sendText("<h4 style=\"color:orange;\"><b>Query no " + stats.getQueryNo() + "</b></h4>");
                s.getBasicRemote().sendText("Start time"
                        + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));

                ComogphogfeatureExtended searchFeat1 = null;
                ComogphogfeatureExtended searchFeat2 = null;
                ComogPhogFeatureExtractor extractor = new ComogPhogFeatureExtractor();
                //File fin=new File("input\\d3rmkd_.txt");
                s.getBasicRemote().sendText("Please wait while we extract the features of your structure.....");
                s.getBasicRemote().sendText("Extracting feature of " + fileName1);
                String comogPhog = extractor.runFeatureExtraction(uploadedFile1);

                s.getBasicRemote().sendText("Extracted feature of file 1");
//                    System.out.println("In file  " + filePath + fileName1 + " size " + uploadedFile1.getAbsolutePath());
                searchFeat1 = new ComogphogfeatureExtended();
                searchFeat1.setScopid(fileName1);
                searchFeat1.setFeatureVector(comogPhog);

                s.getBasicRemote().sendText("Extracting feature of " + fileName2);
                comogPhog = extractor.runFeatureExtraction(uploadedFile2);
                s.getBasicRemote().sendText("Extracted both featured, Now calculating");
                searchFeat2 = new ComogphogfeatureExtended();
                searchFeat2.setScopid(fileName2);
                searchFeat2.setFeatureVector(comogPhog);

                double tmpDist = 0;

                String[] queryFeat = searchFeat1.getFeatureVector().split("-");
                String[] allF = searchFeat2.getFeatureVector().split("-");
                for (int i = 0; i < ((queryFeat.length > allF.length) ? allF.length : queryFeat.length); i++) {
                    tmpDist += Math.pow((Integer.valueOf(queryFeat[i]) - Integer.valueOf(allF[i])), 2);
                }
                DecimalFormat df = new DecimalFormat("0.000");
                tmpDist = Math.sqrt(tmpDist) / 2000000;
                s.getBasicRemote().sendText("<h4 style=\"color:orange;\"><b>Distance " + df.format(tmpDist) + "</b></h4>");
                /*System.out.println("Feature " + comogPhog);
                 s.getBasicRemote().sendText("<h5 style=\"color:orange;\"><b>Feat" + comogPhog + "</b></h5>");*/
                uploadedFile1.delete();
                uploadedFile2.delete();
            } catch (Exception e) {
                e.printStackTrace();
                s.getBasicRemote().sendText(e.getMessage());

            }
//            finally {
//                uploadedFile1.delete();
//                uploadedFile2.delete();
//            }
//            }
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

}
