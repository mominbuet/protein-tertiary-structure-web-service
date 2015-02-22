/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sample.whiteboardapp;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.sample.db.PDetails;
import org.sample.db.PProtein;
import org.sample.db.insertDB;
import org.sample.db.querydb;

/**
 *
 * @author shad
 */
public class utils {
    public PDetails fparsescop(String sunid, String scopid) {
        try {
            Document doc = Jsoup.connect("http://scop.berkeley.edu/sunid=" + sunid).timeout(3 * 1000).get();
            Elements descboxs = doc.getElementsByClass("descbox");
            int i = 0;
            String Class1 = "", fold = "", superf = "", family = "", protein = "", species = "";
            for (Element es : descboxs) {
                String text = es.select("a[href]").first().text().trim();
                if (i == 0) {
                    Class1 = text.substring(2, text.length()).replace(":", " ").trim();
                } else if (i == 1) {
                    fold = text.substring(4, text.length()).replace(":", " ").trim();
                } else if (i == 2) {
                    superf = text.substring(6, text.length()).replace(":", " ").trim();
                } else if (i == 3) {
                    family = text.substring(8, text.length()).replace(":", " ").trim();
                } else if (i == 4) {
                    protein = text.trim();
                } else if (i == 5) {
                    species = text.trim();
                } else {
                    continue;
                }
                
                i++;
            }
            System.out.println(Class1 + "," + fold + "," + superf + "," + family + "," + protein + "," + species);
            if (Class1 != "") {
                querydb qdb = new querydb();
                insertDB idb = new insertDB();
                PProtein pr = qdb.getProteinByScopID(scopid);
                PDetails pdetails = new PDetails();
                pdetails.setClass1(Class1);
                pdetails.setFold(fold);
                pdetails.setFamily(family);
                pdetails.setSuperfamily(superf);
                pdetails.setProtein(protein);
                pdetails.setSpecies(species);
                pdetails.setPid(pr);
                pdetails.setSunid(sunid);
                if (Class1 != "") {
                    pdetails = idb.insertDetails(pdetails);
                    return pdetails;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            fparsescop(sunid, scopid);
        }
        return null;
    }
    
    public PDetails getnewdetails(String scid,File fl) throws IOException {
        //System.out.println("On insert");
        
        Scanner sc = new Scanner(fl);
        PDetails pdet = new PDetails();
        querydb qdb = new querydb();
        while (sc.hasNext()) {
            String line = sc.nextLine();
            StringTokenizer strTok = new StringTokenizer(line, "\n\r");
            int dim = strTok.countTokens();
            for (int j = 0; j < dim; j++) {
                StringTokenizer strtokspace = new StringTokenizer(strTok.nextToken(), "\t");
                if (strtokspace.countTokens() != 0) {
                    String scopid = strtokspace.nextToken().trim();
                    String sunid = strtokspace.nextToken().trim();
                    if (scid.equals(scopid)) {
                        if (!qdb.ifDetailsExists(sunid)) {
                            pdet = fparsescop(sunid, scopid);
                        }
                    }
                    
                }
            }
        }
        return pdet;
    }
    public Map sortByComparator(Map unsortMap) {
 
		List list = new LinkedList(unsortMap.entrySet());
 
		// sort list based on comparator
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
                                       .compareTo(((Map.Entry) (o2)).getValue());
			}
		});
 
		// put sorted list into map again
                //LinkedHashMap make sure order in which keys were inserted
		Map sortedMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedMap.put(entry.getKey(), entry.getValue());
		}
		return sortedMap;
	}
}
