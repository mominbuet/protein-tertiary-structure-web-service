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
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.sample.db.ComogphogfeatureExtended;
import org.sample.db.PDetails;
import org.sample.db.PProtein;
import org.sample.db.SiteStats;
import org.sample.db.insertDB;
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
    static FileOutputStream fos = null;
    final static String filePath = "";
    static String fileName = null;
    static File uploadedFile = null;
    int fCount = 0;
    boolean isFile = false;

    @OnMessage
    public void processUpload(ByteBuffer msg, boolean last, Session session) throws IOException {
        System.out.println("Binary Data: " + fCount + ", Capacity: " + msg.capacity());
        fCount++;

        while (msg.hasRemaining()) {
            try {
                fos.write(msg.get());
            } catch (IOException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }
        }
        session.getBasicRemote().sendText("Binary Data recieved of "+msg.capacity()+" bytes");

    }

    @OnMessage
    public void onMessage(String message, Session s) throws IOException {

//        PProtein pres = qdb.getProteinByScopID(message);
//        //s.getBasicRemote().sendText(pres.getScopsid());
//        PDetails pdet = qdb.getDetailsByScopID(pres);
//        s.getBasicRemote().sendText(pdet.getFamily());
        int loopCount = 30;

        File fl = new File("allsunid.txt");
        ComogphogfeatureExtended searchFeat = null;
        if (message.contains("filename") || message.contains("end")) {
            isFile = true;
            if (!message.equals("end")) {
                fileName = new Date().getTime() + message.substring(message.indexOf(':') + 1);
                uploadedFile = new File(filePath + fileName);

                try {
                    fos = new FileOutputStream(uploadedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    fos.flush();
                    fos.close();
                    ComogPhogFeatureExtractor extractor = new ComogPhogFeatureExtractor();
                    //File fin=new File("input\\d3rmkd_.txt");
                    String comogPhog = extractor.runFeatureExtraction(uploadedFile);
                    System.out.println("In file  " + filePath + fileName + " size " + uploadedFile.getAbsolutePath());
                    searchFeat = new ComogphogfeatureExtended();
                    searchFeat.setScopid(fileName);

                    searchFeat.setFeatureVector(comogPhog);
                    /*System.out.println("Feature " + comogPhog);
                     s.getBasicRemote().sendText("<h5 style=\"color:orange;\"><b>Feat" + comogPhog + "</b></h5>");*/
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } else {
            loopCount = Integer.parseInt(message.substring(message.length() - 2, message.length()));
            message = message.substring(0, 7);
            System.out.println("Msg:  " + message + " count " + loopCount);
            searchFeat = qdb.getFeatureByScopID(message);
        }
        if (searchFeat != null) {
            SiteStats stats = new SiteStats();
            stats = (SiteStats) qdb.getStats();
            new insertDB().incrStat(stats);
            s.getBasicRemote().sendText("<h4 style=\"color:orange;\"><b>Query no " + stats.getQueryNo() + "</b></h4>");

            int classmatch = 0, foldmatch = 0;
            DecimalFormat df = new DecimalFormat("0.000");

            //System.out.println("Here");
            s.getBasicRemote().sendText("Start time"
                    + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));
            //s.getBasicRemote().sendText("Working on redis cache...");

            Jedis jedis = new Jedis("127.0.0.1", 6379);
            Set<String> scops = jedis.keys("d*");
            //jedis.set(searchFeat.getScopid(), searchFeat.getFeatureVector());

            s.getBasicRemote().sendText("Data(" + scops.size() + ") available in cache.....");

            String[] queryFeat = searchFeat.getFeatureVector().split("-");
            s.getBasicRemote().sendText("Calculating distances of tertiary structures....");
            s.getBasicRemote().sendText("log");
            Iterator<String> it = scops.iterator();
            Map<String, Double> distances = new TreeMap<String, Double>();
            int done = 0;
            while (it.hasNext()) {
                String tmp = it.next();
                double tmpDist = 0;

                String[] allF = jedis.get(tmp).split("-");
                for (int i = 0; i < ((queryFeat.length > allF.length) ? allF.length : queryFeat.length); i++) {
                    tmpDist += Math.pow((Integer.valueOf(queryFeat[i]) - Integer.valueOf(allF[i])), 2);

                }
                tmpDist = Math.sqrt(tmpDist)/2000000;
                distances.put(tmp, tmpDist);
                //s.getBasicRemote().sendText(tmp + " distance " + tmpDist);
                done++;
                if (done % 15000 == 0) {
                    NumberFormat anotherFormat = NumberFormat
                            .getNumberInstance(Locale.US);
                    s.getBasicRemote().sendText(anotherFormat.format(done) + " done, Please wait for the next " + anotherFormat.format((scops.size() - done)));
                }
            }
            distances = new utils().sortByComparator(distances);
            s.getBasicRemote().sendText("Closest structures are:");
            s.getBasicRemote().sendText("result");
            done = 0;

            PProtein proteinOriginal = qdb.getProteinByScopID(message);
            PDetails pdetOriginal = qdb.getDetailsByScopID(proteinOriginal);
            for (Map.Entry entry : distances.entrySet()) {
                try {
                    PProtein pres = qdb.getProteinByScopID((String) entry.getKey());
                    //s.getBasicRemote().sendText(pres.getScopsid());
                    PDetails pdet = qdb.getDetailsByScopID(pres);
                    //s.getBasicRemote().sendText(pdet.getFamily());
//                    if (pdet.getSunid() == null) {
//                        pdet = new utils().getnewdetails(pres.getScopsid(), fl);
//                    }
                    if (pdet.getSunid() == null && searchFeat.getComogPhogID() != null) {
                        continue;
                    }
                    s.getBasicRemote().sendText(done + ". Scop : <a href=\"http://128.32.236.13/sunid=" + pdet.getSunid() + "\">" + entry.getKey()
                            + "</a> Distance : " + df.format(entry.getValue()));

                    //else {
                    if (searchFeat.getComogPhogID() != null) {
                        classmatch = (qdb.checkMatch(pdetOriginal.getClass1(), pdet.getClass1()))
                                ? classmatch + 1 : classmatch;
                        foldmatch = (qdb.checkMatch(pdetOriginal.getFold(), pdet.getFold()))
                                ? foldmatch + 1 : foldmatch;
                    }
                    Document doc = Jsoup.connect("http://128.32.236.13/sunid=" + pdet.getSunid())
                            .timeout(5000 * 2)
                            .get();
                    String img = doc.getElementsByClass("result").first().select("table tbody tr td img").first().attr("src");
                    String height = doc.getElementsByClass("result").first().select("table tbody tr td img").first().attr("height");
                    String width = doc.getElementsByClass("result").first().select("table tbody tr td img").first().attr("width");
                    s.getBasicRemote().sendText("<img alt='from scop.berkeley' height='" + height + "'width='" + width + "' src='http://128.32.236.13/" + img + "'/>");
                    //}
                    done++;
                    if (done == loopCount) {
                        break;
                    }

                } catch (Exception ex) {
                    //s.getBasicRemote().sendText(ex.getMessage());
                }
            }
            if (searchFeat.getComogPhogID() != null) {
                double classPerc = 100 * (((double) (loopCount - classmatch)) / loopCount);
                double foldPerc = 100 * ((double) ((loopCount - foldmatch)) / loopCount);

                s.getBasicRemote().sendText("<b>Class Matched:" + classmatch + " mismatch: " + df.format(classPerc) + "%</b>");
                s.getBasicRemote().sendText("<b>Fold Matched:" + foldmatch + " mismatch: " + df.format(foldPerc) + "%</b>");
            }
            s.getBasicRemote().sendText("End time" + new SimpleDateFormat("HH:mm:ss").format(Calendar.getInstance().getTime()));

//            for (Comogphogfeature tmp : allFeat) {
//                //if(i>50) break;
//                jedis.set(tmp.getScopid(), tmp.getFeatureVector());
//                s.getBasicRemote().sendText(tmp.getScopid());
//                //i++;
//            }
        } else {
            if (isFile) {
                s.getBasicRemote().sendText("File module working");
            } else {
                s.getBasicRemote().sendText("Scop id not found");
            }
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
