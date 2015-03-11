/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.sample.db;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
/**
 *
 * @author user
 */
public class querydb {
    
    public List<PProtein> getproteins() {
        List<PProtein> res = new ArrayList<PProtein>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample_WhiteboardApp");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("PProtein.findAll", PProtein.class).getResultList();
            //res =  results;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    public List<Comogphogfeature> getFeatures() {
        List<Comogphogfeature> res = new ArrayList<Comogphogfeature>();
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample_WhiteboardApp");
            EntityManager em = emf.createEntityManager();
            res = em.createNamedQuery("Comogphogfeature.findAll", Comogphogfeature.class).setMaxResults(50).getResultList();
            System.out.println("Count:: "+res.size());
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    public SiteStats getStats() {
        SiteStats res = null;
        try {
            //System.out.println("Here1");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample_WhiteboardApp");
            EntityManager em = emf.createEntityManager();
            //System.out.println("Here2");
            res = em.createNamedQuery("SiteStats.findAll", SiteStats.class)
                    .setMaxResults(1).getResultList().get(0);
            //System.out.println("found:: "+res.getQueryNo());
            //res =  results;
        } catch (Exception ex) {
            
            System.out.println(ex.getMessage());
            return null;
        }
        return res;
    }
    public ComogphogfeatureExtended getFeatureByScopID(String scop) {
        ComogphogfeatureExtended res = null;
        try {
            //System.out.println("Here1");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample_WhiteboardApp");
            EntityManager em = emf.createEntityManager();
            //System.out.println("Here2");
            res = em.createNamedQuery("ComogphogfeatureExtended.findByScopid", ComogphogfeatureExtended.class).setParameter("scopid", scop)
                    .setMaxResults(1).getResultList().get(0);
            System.out.println("found:: "+res.getScopid());
            //res =  results;
        } catch (Exception ex) {
            
            System.out.println(ex.getMessage());
            return null;
        }
        return res;
    }
     public boolean  ifDetailsExists(String findBySunid) {
        boolean res = false;
        try {
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("protein_insertionPU");
            EntityManager em = emf.createEntityManager();
            List<PDetails> res2 = em.createNamedQuery("PDetails.findBySunid", PDetails.class).setParameter("sunid", findBySunid).getResultList();
            res=(!res2.isEmpty())?true:false;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    public PProtein getProteinByScopID(String scop) {
        PProtein res = new PProtein();
        try {
            //System.out.println("Here1");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample_WhiteboardApp");
            EntityManager em = emf.createEntityManager();
            //System.out.println("Here2");
            res = em.createNamedQuery("PProtein.findByScopsid", PProtein.class).setParameter("scopsid", scop)
                    .setMaxResults(1).getResultList().get(0);
            //System.out.println("found:: "+res.getScopid());
            //res =  results;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    public PDetails getDetailsByScopID(PProtein scop) {
        PDetails res = new PDetails();
        try {
            //System.out.println("Here1");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample_WhiteboardApp");
            EntityManager em = emf.createEntityManager();
            //System.out.println("Here2");
            res = em.createNamedQuery("PDetails.FindByPid", PDetails.class).setParameter("pid", scop.getPid())
                    .setMaxResults(1).getResultList().get(0);
            //System.out.println("found:: "+res.getScopid());
            //res =  results;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return res;
    }
    public boolean checkMatch(String fold1, String fold2) {
        fold1=fold1.replaceAll("[0-9]","").replaceAll(":", "");
        fold2=fold2.replaceAll("[0-9]","").replaceAll(":", "");
        //System.out.println("Fold "+fold1);
        //fold2=fold1.replaceAll("[0-9]","").replaceAll(":","");
        return (fold1 == null ? fold2 == null : fold1.equals(fold2));
    }
    public PDetails fparsescop(String sunid, String scopid) {
        try {
            Document doc = Jsoup.connect("http://scop.berkeley.edu/sunid=" + sunid).timeout(10 * 1000).get();
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
                insertDB idb = new insertDB();
                PProtein pr = getProteinByScopID(scopid);
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
                    return  pdetails;
                }
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            fparsescop(sunid, scopid);
        }
        return null;
    }
}
