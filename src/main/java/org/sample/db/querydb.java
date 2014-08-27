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
    public Comogphogfeature getFeatureByScopID(String scop) {
        Comogphogfeature res = null;
        try {
            //System.out.println("Here1");
            EntityManagerFactory emf = Persistence.createEntityManagerFactory("sample_WhiteboardApp");
            EntityManager em = emf.createEntityManager();
            //System.out.println("Here2");
            res = em.createNamedQuery("Comogphogfeature.findByScopid", Comogphogfeature.class).setParameter("scopid", scop)
                    .setMaxResults(1).getResultList().get(0);
            System.out.println("found:: "+res.getScopid());
            //res =  results;
        } catch (Exception ex) {
            
            System.out.println(ex.getMessage());
            return null;
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
}
